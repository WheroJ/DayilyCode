package com.github.wheroj.goover2017_03_15.motionevent;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * Created by shopping on 2017/6/9 16:36.
 *  可改造为下拉刷新
 * @description
 */

public class StickyLayout extends LinearLayout {

    private float lastX;
    private float lastY;

    private double lastTouchX;
    private float lastTouchY;

    public StickyLayout(Context context) {
        super(context);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getRawX();
        float y = ev.getRawY();
        int headerHeight = getHeaderHeight();

        boolean intercept = false;
        ListView listView = getListView();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getRawX();
                lastY = ev.getRawY();
                lastTouchX = ev.getRawX();
                lastTouchY = ev.getRawY();
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float desty = y - lastY;
                float destx = x - lastX;
                int scrollY = getScrollY();

                if (Math.abs(destx) >= Math.abs(desty)) {
                    intercept = false;
                } else if (y < headerHeight) {
                    intercept = true;
                } else {
                    if (desty < 0) {
                        //向上滑
                        if (scrollY >= headerHeight) {
                            //header 完全隐藏
                            intercept = false;
                        } else {
                            if (listView == null){
                                intercept = true;
                            } else {
                                if (listView.getFirstVisiblePosition() == 0) {
                                    intercept = true;
                                } else {
                                    intercept = false;
                                }
                            }
                        }
                    } else {
                        if (scrollY > 0) {
                            //header 有隐藏部分
                            if (listView == null){
                                intercept = true;
                            } else {
                                if (listView.getFirstVisiblePosition() == 0) {
                                    intercept = true;
                                } else {
                                    intercept = false;
                                }
                            }
                        } else {
                            intercept = false;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                intercept = false;
                break;
        }
        lastX = (int) ev.getRawX();
        lastY = (int) ev.getRawY();
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = ev.getRawX();
        float y = ev.getRawY();
        int headerHeight = getHeaderHeight();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //因为没有拦截 down事件，所以这儿是不会执行的，所以需要将获取坐标放到onInterceptTouchEvent方法中
//                lastTouchX = ev.getRawX();
//                lastTouchY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float desty = y - lastTouchY;
                int scrollY = getScrollY();//当scrollY大于零时，内容向上滑动了scrollY
                if (scrollY > 0 && scrollY >= headerHeight) {
                    if (desty < 0) {

                    } else {
                        scrollBy(0, -(int) desty);
                    }
                } else if (scrollY <= 0 && desty > 0) {

                } else {
                    scrollBy(0, -(int) desty);
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollYActual = getScrollY();
                if (scrollYActual > headerHeight / 2) {
                    smoothScroll(headerHeight - scrollYActual, 0);
                } else {
                    smoothScroll(-scrollYActual, 0);
                }
                break;
        }

        lastTouchX = ev.getRawX();
        lastTouchY = ev.getRawY();
        return true;
    }

    private int getHeaderHeight() {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            if (childAt != null) {
                int height = childAt.getHeight();
                return height;
            } else {
                return 0;
            }
        }
        return 0;
    }

    private ListView getListView() {
        if (getChildCount() > 1) {
            View childAt = getChildAt(1);
            if (childAt != null) {
                if (childAt instanceof ListView) return (ListView) childAt;
                else return null;
            } else {
                return null;
            }
        }
        return null;
    }

    private int getMeasureSize(View view) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(makeMeasureSpec, makeMeasureSpec);
        return view.getMeasuredHeight();
    }

    Scroller scroller = new Scroller(getContext());

    private void smoothScroll(float destY, float destX) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        scroller.startScroll(scrollX, scrollY, (int) destX, (int) destY, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
