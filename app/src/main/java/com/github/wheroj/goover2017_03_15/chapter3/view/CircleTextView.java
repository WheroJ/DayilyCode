package com.github.wheroj.goover2017_03_15.chapter3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.wheroj.goover2017_03_15.R;

/**
 * Created by shopping on 2017/7/5 10:15.
 *
 * @description
 */

public class CircleTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;
    private int ovalBorderColor = getResources().getColor(R.color.gray_6);
    private int ovalWidth = 6;
    private Paint ovalPaint;

    public CircleTextView(Context context) {
        this(context, null);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.white));
        mPaint.setAntiAlias(true);

        ovalPaint = new Paint();
        ovalPaint.setAntiAlias(true);
        ovalPaint.setColor(ovalBorderColor);
        ovalPaint.setStrokeWidth(ovalWidth);
        ovalPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 修改 画布 圆背景色
     * @param color
     */
    public void setPaintColor(int color){
        mPaint.setColor(color);
        invalidate();
    }

    /**
     * 修改边线颜色和宽度
     * @param color  -1为不修改
     * @param ovalWidth   -1为不修改
     */
    public void setOvalBorderColor(int color, int ovalWidth){
        if (color != -1) {
            mPaint.setColor(color);
        }
        if (ovalWidth != -1) {
            mPaint.setStrokeWidth(ovalWidth);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        canvas.drawCircle(width/2, height/2, Math.min(width/2 - ovalWidth, height/2 - ovalWidth), mPaint);

        RectF rectF = new RectF();
        rectF.left = ovalWidth;
        rectF.top = ovalWidth;
        rectF.bottom = height - ovalWidth;
        rectF.right = width - ovalWidth;
        canvas.drawArc(rectF, 0, 360, true, ovalPaint);
        super.onDraw(canvas);
    }
}
