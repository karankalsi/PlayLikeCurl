package karacken.curl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by karacken on 18/11/16.
 */
public class PageSurfaceView extends GLSurfaceView implements GestureDetector.OnGestureListener {

    private GestureDetector mGesturedDetector;

    private OnPageChangeListener onPageChangeListener=null;

    int currentPosition=0;
    static final int SWIPE_MIN_DISTANCE = 120;
    static final int SWIPE_MAX_OFF_PATH = 250;
    static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private boolean canSwipeLeft=false;
    private boolean canSwipeRight=false;

    private PageCurlAdapter pageCurlAdapter;
    float x1=0;
    float pos =0;
    private PageRenderer renderer;

    public PageSurfaceView(Context context) {
        super(context);
        mGesturedDetector=new GestureDetector(context,this);

        renderer = new PageRenderer(context);
         setRenderer(renderer);
    }

    public boolean onPageTouchEvent(MotionEvent event) {

        if(mGesturedDetector.onTouchEvent(event))
            return true;

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                renderer.togglePageActive(PageRenderer.PAGE.CURRENT);
                pos = renderer.getCurrentPageValue();

                break;
            case MotionEvent.ACTION_MOVE:
                float perc_move = (event.getX()-x1)/(float)getWidth();
                if((event.getX()-x1)>0)
                { // page_left
                    if(pos>=Page.GRID && canSwipeLeft) {
                        renderer.togglePageActive(PageRenderer.PAGE.LEFT);
                        pos = renderer.getCurrentPageValue();
                    }
                    float value= (pos)+(perc_move)*Page.GRID;



                    if(value<=Page.GRID)
                        renderer.updateCurlPosition(value);



                }
                else
                if((event.getX()-x1)<0)
                {//  page_right


                    float value= (1-Math.abs(perc_move))*Page.GRID- (Page.GRID-pos);
                    if(canSwipeRight)
                    renderer.updateCurlPosition(value);

                }
                //  x1=event.getX();
                break;
            case MotionEvent.ACTION_UP:

                if(renderer.active_page.equals(PageRenderer.PAGE.CURRENT))
                animatePagetoDefault(PageRenderer.PAGE_LEFT,false,new AccelerateDecelerateInterpolator());
                else
                   animatePagetoDefault(PageRenderer.PAGE_RGHT,false,new AccelerateDecelerateInterpolator());


                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {







        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {return false;
        } else {
            if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                return false;
            }
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && canSwipeRight

                    ) {

                animatePagetoDefault(PageRenderer.PAGE_RGHT,true,new DecelerateInterpolator());
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && canSwipeLeft) {

                animatePagetoDefault(PageRenderer.PAGE_LEFT,true,new DecelerateInterpolator());
                return true;
            }
        }

        return false;
    }

    private void animatePagetoDefault(final int end_perc,final  boolean ispagechanged,Interpolator interpolator)
    {


        int start_per =  renderer.getCurrentPagePerc();



        if(start_per==end_perc)
        {
            renderer.resetPages();
            if(ispagechanged)
            {
               processPageChange(end_perc);
            }

            return;
        }

        AnimateCounter animateCounter = new AnimateCounter.Builder()
                .setCount( start_per,end_perc)
                .setDuration(300)
                .setInterpolator(interpolator)
                .build();
        animateCounter.setAnimateCounterListener(new AnimateCounter.AnimateCounterListener() {


            @Override
            public void onAnimateCounterEnd() {
                renderer.resetPages();
                if(ispagechanged)
                {
                    processPageChange(end_perc);
                }
            }

            @Override
            public void onValueUpdate(float value) {
                renderer.updateCurlPosition(renderer.frontPage.GRID * value / 100f);
            }
        });


        animateCounter.execute();


    }

    private void processPageChange(int page_type) {
        if(page_type==PageRenderer.PAGE_LEFT)
        {
            currentPosition--;
        }
        else
        {
            currentPosition++;
        }

        if(onPageChangeListener!=null)onPageChangeListener.onPageChanged(currentPosition);
        processPage();

    }

    private void processPage() {

        if(currentPosition==0)
        {
            renderer.updatePageRes(pageCurlAdapter.getItemResource(currentPosition),
                    pageCurlAdapter.getItemResource(currentPosition),
                    pageCurlAdapter.getItemResource(currentPosition+1));
            canSwipeLeft=false;
            canSwipeRight=true;
        }
        else
        if (currentPosition==pageCurlAdapter.getCount()-1)
        {
            renderer.updatePageRes(pageCurlAdapter.getItemResource(currentPosition-1),
                    pageCurlAdapter.getItemResource(currentPosition),
                    pageCurlAdapter.getItemResource(currentPosition));
            canSwipeLeft=true;
            canSwipeRight=false;
        }
        else
        {
            renderer.updatePageRes(pageCurlAdapter.getItemResource(currentPosition-1),
                    pageCurlAdapter.getItemResource(currentPosition),
                    pageCurlAdapter.getItemResource(currentPosition+1));

            canSwipeLeft=true;
            canSwipeRight=true;
        }


    }


    public PageCurlAdapter getPageCurlAdapter() {
        return pageCurlAdapter;
    }

    public void setPageCurlAdapter(PageCurlAdapter pageCurlAdapter) {
        this.pageCurlAdapter = pageCurlAdapter;
        if(pageCurlAdapter.getCount()>0)
            processPage();

    }

public void setCurrentPosition(int position)
{
    if(position>=0 && position<pageCurlAdapter.getCount()) {
        currentPosition = position;
        processPage();
    }
}

 public int getCurrentPosition()
 {
     return currentPosition;
 }




   public interface OnPageChangeListener
    {
    public void  onPageChanged(int position);

    }



    public OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

}
