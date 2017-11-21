package com.github.wheroj.goover2017_03_15.chapter3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by shopping on 2017/5/5 17:09.
 *
 * @description
 */

public class MoveButton extends android.support.v7.widget.AppCompatButton {

    private float mLastX = -1;
    private float mLastY = -1;

    public MoveButton(Context context) {
        this(context, null);
    }

    public MoveButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setText("move");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = rawY;
                mLastX = rawX;
                Log.i("MoveButton", rawX + ", rawY = " + rawY);
                break;
            case MotionEvent.ACTION_MOVE:
                //移动整个控件
//                int transitionX = (int) (rawX - mLastX + getTranslationX());
//                int transitionY = (int) (rawY - mLastY + getTranslationY());
//                setTranslationX(transitionX);
//                setTranslationY(transitionY);

                //使得view变大变小，但是位置不变
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)this.getLayoutParams();
                layoutParams.leftMargin = layoutParams.leftMargin + (int) (rawX - mLastX);
                layoutParams.topMargin = layoutParams.topMargin + (int) (rawY - mLastY);
                postInvalidate();
//                setLeft(getLeft() + (int) (rawX - mLastX));
//                setTop(getTop() + (int) (rawY - mLastY));

                //移动内容
//                scrollBy(-(int) (rawX - mLastX), -(int) (rawY - mLastY));
//                Log.i("MoveButton", transitionX + ", transitionY = " + transitionY);
                break;
        }

        mLastX = rawX;
        mLastY = rawY;
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
