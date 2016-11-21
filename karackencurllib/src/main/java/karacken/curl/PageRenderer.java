package karacken.curl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.view.Display;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by karacken on 18/11/16.
 */
public class PageRenderer implements Renderer {
	
	public Page leftPage;
	public Page frontPage;
	public Page rightPage;
	private Context context;


	public enum PAGE{LEFT,RIGHT,CURRENT};
public PAGE active_page = null;

	public static final int PAGE_LEFT=100;
	public static final int PAGE_RGHT=-5;
	public PageRenderer(Context context) {
		this.context = context;
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		leftPage = new PageLeft(width);
		frontPage = new PageFront(width);
		rightPage = new PageRight(width);


		togglePageActive(PAGE.CURRENT);
		leftPage.curlCirclePosition=Page.GRID*((float)PAGE_RGHT/100f);
		rightPage.curlCirclePosition=Page.GRID;


	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		frontPage.loadGLTexture(gl, this.context);
		rightPage.loadGLTexture(gl, this.context);
		leftPage.loadGLTexture(gl, this.context);


		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST); 		
		gl.glDepthFunc(GL10.GL_LEQUAL); 	
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}

	
	public void onDrawFrame(GL10 gl) {

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -2.0f);
		gl.glTranslatef(-0.5f, -0.5f, 0.0f);

		leftPage.draw(gl,context);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -2.0f);
		gl.glTranslatef(-0.5f, -0.5f, 0.0f);

		frontPage.draw(gl, context);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -2.0f);
		gl.glTranslatef(-0.5f, -0.5f, 0.0f);
		rightPage.draw(gl,context);

		gl.glPopMatrix();

	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						
			height = 1;
		}


		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
if(height>width)
	GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		else
	GLU.gluPerspective(gl, 45.0f, (float) height / (float) width, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	
		gl.glLoadIdentity(); 					
	}
	public void updatePageRes(String lef_res,String front_res,String right_res)
	{
		leftPage.setRes_id(lef_res);
		frontPage.setRes_id(front_res);
		rightPage.setRes_id(right_res);

	}

public void togglePageActive(PAGE page)
{

	if(active_page==null || active_page!=page) {
		active_page = page;

		switch (active_page)
		{
			case LEFT:leftPage.setIsactive(true);
				frontPage.setIsactive(false);
				rightPage.setIsactive(false);
				break;
			case RIGHT:
				leftPage.setIsactive(false);
				frontPage.setIsactive(false);
				rightPage.setIsactive(true);
				break;
			case CURRENT:
				leftPage.setIsactive(false);
				frontPage.setIsactive(true);
				rightPage.setIsactive(false);
				break;
		}

	}

}

	public void updateCurlPosition(float value)
	{


switch (active_page)
{
	case LEFT: leftPage.curlCirclePosition= value;break;
	case RIGHT:rightPage.curlCirclePosition= value;break;
	case CURRENT:frontPage.curlCirclePosition= value;break;
}
	}

public  int getCurrentPagePerc()
{


	switch (active_page)
	{
		case LEFT: return  (int)(leftPage.curlCirclePosition/Page.GRID*100);
		case RIGHT:return  (int)(rightPage.curlCirclePosition/Page.GRID*100);
		case CURRENT:return  (int)(frontPage.curlCirclePosition/Page.GRID*100);
		default:return  (int)(frontPage.curlCirclePosition/Page.GRID*100);
	}



}public  float getCurrentPageValue()
{


	switch (active_page)
	{
		case LEFT: return  leftPage.curlCirclePosition;
		case RIGHT:return  rightPage.curlCirclePosition;
		case CURRENT:return  frontPage.curlCirclePosition;
		default:return  frontPage.curlCirclePosition;
	}



}

	public void resetPages()
	{

		leftPage.curlCirclePosition=Page.GRID*((float)PAGE_RGHT/100f);
		rightPage.curlCirclePosition=Page.GRID;
		frontPage.curlCirclePosition=Page.GRID;
		togglePageActive(PAGE.CURRENT);

	}


}