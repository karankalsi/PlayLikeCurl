package karacken.curl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by karacken on 18/11/16.
 */
public class PageRight extends Page {


	private  static  final  float depth =-0.003f;

	public PageRight(int screen_width)
	{
		super(screen_width);

	}
	@Override
	public void calculateVerticesCoords(){
super.calculateVerticesCoords();
		float angle = 1.0f/((float)GRID*RADIUS);
		for(int row=0;row<=GRID;row++)
			for(int col=0;col<=GRID;col++){
				int pos = 3*(row*(GRID+1)+col);

				if(!isactive())
				vertices[pos+2]=depth;

					vertices[pos]=((float)col/(float)GRID);


				vertices[pos+1]=((float)row/(float)GRID*h_w_ratio)-h_w_correction;
			}

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

	}

}