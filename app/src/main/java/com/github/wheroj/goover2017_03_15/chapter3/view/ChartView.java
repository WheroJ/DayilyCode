package com.github.wheroj.goover2017_03_15.chapter3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import com.github.wheroj.goover2017_03_15.R;


/**
 * Created by shopping on 2017/7/26 15:44.
 *
 * @description
 */

public class ChartView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.line_light));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(getScaleSize(1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawCircle(canvas);
        drawvLine(canvas);
    }

    private void drawLine(Canvas canvas){
        float startX = getScaleSize(20), startY = getScaleSize(20);

        mPaint.setColor(getResources().getColor(R.color.text_yellow));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);//画线必须是这个style
        mPaint.setStrokeWidth(getScaleSize(1));

        Path path = new Path();
        DashPathEffect pathEffect = new DashPathEffect(new float[]{5, 8}, 1);
        mPaint.setPathEffect(pathEffect);

        path.moveTo(startX, startY);
        path.lineTo(startX + getScaleSize(200), startY);

        canvas.drawPath(path, mPaint);
        path.moveTo(startX , startY + getScaleSize(100));
        path.lineTo(startX + getScaleSize(200), startY + getScaleSize(100));
        canvas.drawPath(path, mPaint);
        path.close();
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.text_orange));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(getScaleSize(2));
        DashPathEffect pathEffect = new DashPathEffect(new float[]{5, 8}, 1);  //画出来还挺好看的
        mPaint.setPathEffect(null);
        canvas.drawCircle(getScaleSize(50), getScaleSize(60), 10, mPaint);
        canvas.drawCircle(getScaleSize(150), getScaleSize(60), 10, mPaint);

        mPaint.setColor(getResources().getColor(R.color.green_3CD395));
        canvas.drawCircle(getScaleSize(200), getScaleSize(100), 10, mPaint);

        mPaint.setPathEffect(pathEffect);
        mPaint.setStrokeWidth(getScaleSize(4));
        canvas.drawCircle(getScaleSize(100), getScaleSize(90), 10, mPaint);

        mPaint.setStrokeWidth(2);
        mPaint.setPathEffect(pathEffect);
        Path path = new Path();
        path.moveTo(getScaleSize(50) + 10, getScaleSize(60));
        path.lineTo(getScaleSize(200) - 10, getScaleSize(100));
        canvas.drawPath(path, mPaint);
    }

    private void drawvLine(Canvas canvas){
        Path path = new Path();
        mPaint.setStrokeWidth(getScaleSize(3));
        mPaint.setPathEffect(null);

        path.moveTo(getScaleSize(60), getScaleSize(120));
        path.lineTo(getScaleSize(60), getScaleSize(90));
        mPaint.setColor(getResources().getColor(R.color.blue_6f));
        canvas.drawPath(path, mPaint);
        path.close();

        path = new Path();
        path.moveTo(getScaleSize(70), getScaleSize(120));
        path.lineTo(getScaleSize(70), getScaleSize(70));
        mPaint.setColor(getResources().getColor(R.color.blue_4a));
        canvas.drawPath(path, mPaint);
        path.close();
    }

    private int getScaleSize(int size){
        float density = getResources().getDisplayMetrics().density;
        return (int) (density*size + 0.5);
    }

}
