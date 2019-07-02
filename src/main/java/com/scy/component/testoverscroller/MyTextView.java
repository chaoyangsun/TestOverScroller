package com.scy.component.testoverscroller;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MyTextView extends android.support.v7.widget.AppCompatTextView {

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int top;//上边界坐标
    private int bottom;//下边界坐标

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public void setBoundary(int top, int bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private float mLastX;
    private float mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;
                mScroller.startScroll(0, mScroller.getFinalY(), 0, (int) dy);//mCurrY = oldScrollY + dy*scale;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) mVelocityTracker.getYVelocity();
                System.out.println("else: " + initialVelocity + " -- " + mMinimumVelocity);
                if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                    fling(initialVelocity);
                }else if (mScroller.springBack(0, (int) getTranslationY(), 0, 0, top - getTop(),
                        bottom - getBottom())) {
                    postInvalidateOnAnimation();
                }
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(event);
    }

    public void fling(int velocityY) {
        System.out.println(top + "--" + bottom + " -- " + getTranslationY() + "--" + (top - getTop()) + "--" + (bottom - getBottom()));
        int minY = top - getTop();//上滑允许滑动的范围 = minY - overY
        int maxY = bottom - getBottom();//下滑允许滑动的范围 = maxY + overY
        mScroller.fling(0, (int) getTranslationY(), 0, velocityY, 0, 0, minY,
                maxY, 0, 20);

        postInvalidateOnAnimation();
    }
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            setTranslationY(mScroller.getCurrY());
            postInvalidate();
        }
    }

}
