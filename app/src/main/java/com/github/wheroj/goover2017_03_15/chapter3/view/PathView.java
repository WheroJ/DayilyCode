package com.github.wheroj.goover2017_03_15.chapter3.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;

/**
 * Created by shopping on 2017/7/6 10:21.
 *
 * @description
 */

public class PathView extends View {

    private Paint mPaint;
    private Path mPath;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
        initPath();
    }

    private void initPath() {
        mPath = new Path();
        mPath.moveTo(50, 50);
        int count = 10;
        int step = getSize(50);
        for (int i = 0; i < count; i++){
            int randow = new Random().nextInt(100);
            mPath.lineTo(50 + (i + 1) * step, (float) (randow *1.0));
        }
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = getSize(widthMeasureSpec);
        int heightSize = getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMeasureSpec == MeasureSpec.AT_MOST){
            setMeasuredDimension(getWindowWidth() - 100,  getWindowHeight());
        } else {
            if (widthMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(getWindowWidth() - 100, heightSize);
            } else if (heightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(widthSize, getWindowHeight());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setPathEffect(null);
        canvas.drawPath(mPath, mPaint);

        //CornerPathEffect则可以将路径的转角变得圆滑，CornerPathEffect的构造方法只接受一个参数radius，意思就是转角处的圆滑程度
        canvas.translate(0, getSize(20));
        mPaint.setPathEffect(new CornerPathEffect(50));
        canvas.drawPath(mPath, mPaint);


        /*
            （离散路径效果）相对来说则稍微复杂点，其会在路径上绘制很多“杂点”的突出来模拟一种类似生锈铁丝的效果。其构造方法有两个参数：
            第一个呢指定这些突出的“杂点”的密度，值越小杂点越密集；
            第二个参数呢则是“杂点”突出的大小，值越大突出的距离越大反之反之。
         */
        canvas.translate(0, getSize(20));
        mPaint.setPathEffect(new DiscretePathEffect(2.0f, 5.0f));
        canvas.drawPath(mPath, mPaint);

        /*
            偶数参数5（注意数组下标是从0开始哦）定义了我们第一条实线的长度，而奇数参数8则表示第一条虚线的长度，如果此时数组后面不再有数据则重复第一个数以此往复循环
            第二个参数（phase）我称之为偏移值，动态改变其值会让路径产生动画的效果
         */
        canvas.translate(0, getSize(20));
        mPaint.setPathEffect(new DashPathEffect(new float[]{5, 8}, 1));
        canvas.drawPath(mPath, mPaint);

        canvas.translate(0, getSize(20));
        Path path = new Path();
        path.addCircle(0, 0, 3, Path.Direction.CCW);
        mPaint.setPathEffect(new PathDashPathEffect(path, 10, 1, PathDashPathEffect.Style.ROTATE));
        canvas.drawPath(mPath, mPaint);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private int getWindowWidth(){
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        return size.x;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private int getWindowHeight(){
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        return size.y;
    }

    private int getSize(int size) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density * size + 0.5);
    }
}
