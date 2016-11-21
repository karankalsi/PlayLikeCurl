package karacken.curl;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by karacken on 18/11/16.
 */
public class Page {

	public static  final int GRID = 25;

	public final float RADIUS = 0.18f;
	public float curlCirclePosition = 25;
	float bitmap_ratio =1.0f;
	int screen_width=0;
	public boolean isactive() {
		return isactive;
	}


	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	private boolean isactive=false;
	public FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private ShortBuffer indexBuffer;

	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {

		this.res_id = res_id;
		needtextureupdate=true;
	}

	private String res_id="";
	private boolean needtextureupdate=false;

	private int[] textures = new int[1];
	public float vertices[] = new float[(GRID+1)*(GRID+1)*3];
	private float texture[] = new float[(GRID+1)*(GRID+1)*2];
	private short indices[] = new short[GRID*GRID*6];

	float h_w_ratio;
	float h_w_correction;
	public void calculateVerticesCoords(){
		 h_w_ratio = bitmap_ratio;
		 h_w_correction = (h_w_ratio-1f)/2.0f;


	}

	private void calculateFacesCoords(){

		for(int row=0;row<GRID;row++)
			for(int col=0;col<GRID;col++){
				int pos = 6*(row*GRID+col);

				indices[pos] = (short) (row*(GRID+1)+col);
				indices[pos+1]=(short) (row*(GRID+1)+col+1);
				indices[pos+2]=(short) ((row+1)*(GRID+1)+col);

				indices[pos+3]=(short) (row*(GRID+1)+col+1);
				indices[pos+4]=(short) ((row+1)*(GRID+1)+col+1);
				indices[pos+5]=(short) ((row+1)*(GRID+1)+col);

			}
	}

	private void calculateTextureCoords(){

		for(int row=0;row<=GRID;row++)
			for(int col=0;col<=GRID;col++){
				int pos = 2*(row*(GRID+1)+col);

				texture[pos]=col/(float)GRID;
				texture[pos+1]=1-row/(float)GRID;
			}
	}

	public Page(int screen_width) {

		this.screen_width=screen_width;
		calculateVerticesCoords();
		calculateFacesCoords();
		calculateTextureCoords();

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(indices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		indexBuffer = byteBuf.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	public void draw(GL10 gl,Context context) {

		if(needtextureupdate) {

			needtextureupdate=false;
			loadGLTexture(gl, context);
		}
		calculateVerticesCoords();


		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glFrontFace(GL10.GL_CCW);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	private void updateTexture() {

	}

	public void loadGLTexture(GL10 gl, Context context) {

		if(res_id.isEmpty())return;

		Bitmap bitmap = null;
		InputStream is=null;
		try {
		 is = context.getAssets().open(res_id);
			bitmap = BitmapFactory.decodeStream(is);

		}
		catch (IOException e)
		{
			
		}
		finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}
		if(context.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
		bitmap_ratio=(float)bitmap.getHeight()/(float)bitmap.getWidth();
		else
			bitmap_ratio=(float)bitmap.getWidth()/(float)bitmap.getHeight();


		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		bitmap.recycle();
	}
}