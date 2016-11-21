package karacken.curl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by karacken on 18/11/16.
 */
public class PageLeft extends Page {


	private  static  final  float depth =-0.001f;

	public PageLeft(int screen_width)
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

				 float perc = 1.0f-curlCirclePosition/(float)GRID;
				perc *=0.75f;

				float dx=(GRID-curlCirclePosition);
				float calc_r = perc*RADIUS;
				if(calc_r>RADIUS)
					calc_r=RADIUS;

				calc_r=RADIUS*1;
				float mov_x=0;
				if(perc<0.20f)
					calc_r=RADIUS*perc*5;

				mov_x=perc;



				if(isactive())
					vertices[pos+2]=(float) (calc_r*(Math.sin(3.14/(GRID*0.50f)*(col-(dx))))+(calc_r*1.1f)); //Asin(2pi/wav*x)
				float w_h_ratio = 1-calc_r;
				vertices[pos]=((float)col/(float)GRID*w_h_ratio)-mov_x;


				vertices[pos+1]=((float)row/(float)GRID*h_w_ratio)-h_w_correction;
			}

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

	}

}