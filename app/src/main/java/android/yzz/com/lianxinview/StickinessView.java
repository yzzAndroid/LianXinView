package android.yzz.com.lianxinview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * Created by yzz on 2017/3/3 0003.
 * 通过外部可滑动的监听传递到粘性View中进行滑动
 */
public class StickinessView extends LinearLayout implements AbsListView.OnScrollListener {

    private View mHeadFiest;
    private View mHeadSecond;
    private ListView mListView;
    //当前head的位置
    public static final int TOP = 0;
    public static final int CENTER = 1;
    public static final int BOTTOM = 2;
    private int mHeadPosition = BOTTOM;
    private boolean isIntercept = false;
    private boolean isListViewTop = true;
    private boolean isListViewBottom = false;
    private float touchY;
    private int MAXY = -1;
    private Scroller mScroll;

    public StickinessView(Context context) {
        super(context);
    }

    public StickinessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickinessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        //本粘性布局只支持ListView
        if (count == 3 && getChildAt(2) instanceof ListView)
            init();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (MAXY == -1)
            MAXY = mHeadSecond.getMeasuredHeight();
    }

    /**
     * 初始化
     */
    private void init() {
        //获得子元素
        mHeadFiest = getChildAt(0);
        mHeadSecond = getChildAt(1);
        mListView = (ListView) getChildAt(2);
        mListView.setOnScrollListener(this);
        mScroll = new Scroller(getContext());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroll.computeScrollOffset()){
            scrollTo(mScroll.getCurrX(),mScroll.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        View v = mListView.getChildAt(0);
        if (mListView.getChildCount() > 0 && firstVisibleItem == 0) {
            //滑动到顶部
            Log.e("=============","=======到了==="+v.getTop()+mListView.getTop());
            if (v.getTop() == 0) {
                //滑动到顶部了
                isListViewTop = true;
            } else {
                isListViewBottom = false;
            }
        }else if (mListView.getChildCount()>0&&firstVisibleItem+visibleItemCount==totalItemCount){
            final View bottomChildView = mListView.getChildAt(mListView.getChildCount()-1);
            if (mListView.getHeight()>=bottomChildView.getBottom()){
                isListViewBottom = true;
            }else {
                isListViewBottom = false;
            }
        }else {
            isListViewBottom = false;
            isListViewTop = false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchY = ev.getRawY();
                isIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float distant = ev.getRawY() - touchY;
                if (isListViewTop) {
                    switch (mHeadPosition) {
                        case TOP:
                            if (distant > 0) isIntercept = true;
                            break;
                        case CENTER:
                            isIntercept = true;
                            break;
                    }
                }
                if (isListViewBottom){
                    switch (mHeadPosition) {
                        case CENTER:
                            isIntercept = true;
                            break;
                        case BOTTOM:
                            if (distant < 0) isIntercept = true;
                            break;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                isIntercept = true;
                break;
        }
        return isIntercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取不到的
                break;
            case MotionEvent.ACTION_MOVE:
                int distant = (int) (touchY - event.getRawY());
                Log.e("===","==="+(getScrollY() + distant)+"=="+getScrollY()+"=="+distant);
                if (getScrollY() +  distant-1 < MAXY && getScrollY() +  distant > 0) {
                    scrollTo(0, getScrollY() +  distant);
                } else {
                    mListView.dispatchTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollY() == 0) mHeadPosition = BOTTOM;
                if (getScrollY() == MAXY) mHeadPosition = TOP;
                if (getScrollY() > 0 && getScrollY() < MAXY) mHeadPosition = CENTER;
                if (getScrollY() > MAXY / 2) {
                    mScroll.startScroll(0, getScrollY(), 0, MAXY-getScrollY(),100);
                    invalidate();
                    mHeadPosition = TOP;
                }
                if (getScrollY() < MAXY / 2) {
                    mScroll.startScroll(0, getScrollY(),0,-getScrollY(),100);
                    invalidate();
                    mHeadPosition = BOTTOM;
                }
                break;
        }
        return true;
    }


}
