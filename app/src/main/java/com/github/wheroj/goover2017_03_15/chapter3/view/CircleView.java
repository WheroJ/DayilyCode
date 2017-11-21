package com.github.wheroj.goover2017_03_15.chapter3.view;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.CycleInterpolator;

import com.github.wheroj.goover2017_03_15.R;


/**
 * Created by shopping on 2017/6/23 15:41.
 *
 * @description
 */

public class CircleView extends View {

    private int mHeight = 200;
    private int mWidth = 200;
    private Paint mPaint;
    private float radius;
    private int bgColor;

    /**
     * 旋转的角度
     */
    private int angle = 0;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        bgColor = typedArray.getColor(R.styleable.CircleView_bgcolor, Color.RED);
        radius = typedArray.getDimension(R.styleable.CircleView_radius, 0);//已经处理了 dp转换px
        typedArray.recycle();
        init();
    }

    private int getSize(int size){
        float density = getResources().getDisplayMetrics().density;
        return (int) (size * density + 0.5);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(bgColor);
        mPaint.setAntiAlias(true);
        mWidth = getSize(200);
        mHeight = getSize(200);

        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
        new HandlerThread("").start();
//        IntentService
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        final int width = getWidth() - paddingLeft - paddingRight;
        final int height = getHeight() - paddingTop - paddingBottom;
        mPaint.setColor(bgColor);
        canvas.drawCircle(paddingLeft + width/2, paddingTop + height/2, radius, mPaint);

        /**
         * 图片旋转：采用位图，使用矩阵旋转，之后画到画布上，缩放和平移类似
         */
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.cursorbitmap);
        final Bitmap bitmap = drawable.getBitmap();
        final Matrix matrix = new Matrix();
        canvas.translate(paddingLeft + width/2 - bitmap.getWidth()/2, paddingTop + height/2 - radius);
        matrix.postRotate(angle, bitmap.getWidth()/2, radius);
        canvas.drawBitmap(bitmap, matrix, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawCircle(bitmap.getWidth()/2, radius, 10, mPaint);

//        Camera camera = new Camera();
//        camera.getMatrix(matrix);
//        camera.rotateZ(360);

//        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        canvas.drawBitmap(bitmap1, paddingLeft + width/2 - bitmap1.getWidth()/2,  paddingTop + height/2 - radius , mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int defaultWidthSize = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int defaultHeightSize = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, mHeight);
        } else {
            if (widthMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(mWidth, defaultHeightSize);
            } else if (heightMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(defaultWidthSize, mHeight);
            }
        }
    }

    /**
     * 麻蛋，在onDraw 里面调用一直没成功，原来是需要view绘制完成了才能调用
     */
    public void cursorAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1);
        valueAnimator.setInterpolator(new CycleInterpolator(0.25f));
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                IntEvaluator intEvaluator = new IntEvaluator();
                angle = intEvaluator.evaluate(animation.getAnimatedFraction(), 0, 150);

                invalidate();
            }
        };
        valueAnimator.addUpdateListener(listener);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }
}
