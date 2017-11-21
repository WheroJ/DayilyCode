package com.github.wheroj.goover2017_03_15.chapter3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.wheroj.goover2017_03_15.R;
import com.github.wheroj.goover2017_03_15.UIUtils;

import java.util.ArrayList;

/**
 * Created by shopping on 2017/7/4 16:37.
 *
 * @description
 */

public class SignInView extends LinearLayout {

    private int circleCount = 7;
    private int circleWidth = getSize(40);
    private int circleHeight = getSize(40);
    private int lineWidth = getSize(10);
    private int lineHeight = getSize(1);

    /**
     * 中间 连线颜色
     */
    private int lineColor = getResources().getColor(R.color.gray_3);

    /**
     * 已经签到 圆颜色
     */
    private int signedColor = getResources().getColor(R.color.yellow);

    /**
     * 未签到  圆颜色
     */
    private int notSignColor = getResources().getColor(R.color.white);

    /**
     * 中心字体颜色
     */
    private int levelColor = getResources().getColor(R.color.green);

    private String level1 = "+5";
    private String level2 = "+10";

    /**
     * 签到天数
     */
    private int signDay = 0;

    /**
     * 是否画日历
     */
    private boolean drawDate = false;

    private ArrayList<CircleTextView> children;

    public SignInView(Context context) {
        this(context, null);
    }

    public SignInView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SignInView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SignInView);
        circleWidth = (int) typedArray.getDimension(R.styleable.SignInView_circlewidth, circleWidth);
        circleHeight = (int) typedArray.getDimension(R.styleable.SignInView_circleHeight, circleHeight);
        lineWidth = (int) typedArray.getDimension(R.styleable.SignInView_lineWidth, lineWidth);
        lineColor = typedArray.getColor(R.styleable.SignInView_lineColor, lineColor);
        signedColor = typedArray.getColor(R.styleable.SignInView_signColor, signedColor);
        notSignColor = typedArray.getColor(R.styleable.SignInView_notSignColor, notSignColor);
        drawDate = typedArray.getBoolean(R.styleable.SignInView_drawDate, drawDate);
        String attrLevel1 = typedArray.getString(R.styleable.SignInView_level1);
        level1 = TextUtils.isEmpty(attrLevel1) ? level1 : attrLevel1;
        String attrLevel2 = typedArray.getString(R.styleable.SignInView_level2);
        level2 = TextUtils.isEmpty(attrLevel2) ? level2 : attrLevel2;
        typedArray.recycle();
        init();
    }

    private void init() {
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setOrientation(LinearLayout.HORIZONTAL);
        children = new ArrayList<>();
        for (int i = 0; i < circleCount; i++) {
            LayoutParams layoutParams = new LayoutParams(circleWidth, circleHeight);
            CircleTextView circleTextView = new CircleTextView(getContext());
            circleTextView.setBackgroundColor(getResources().getColor(R.color.transparent));
            circleTextView.setGravity(Gravity.CENTER);
            if (i < signDay) {
                circleTextView.setPaintColor(signedColor);
            } else {
                circleTextView.setPaintColor(notSignColor);
            }

            if (i == circleCount - 1) {
                SpannableStringBuilder builder = new SpannableStringBuilder(level2);
                ForegroundColorSpan span = new ForegroundColorSpan(levelColor);
                builder.setSpan(span, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                circleTextView.setText(builder);
                addView(circleTextView, layoutParams);
            } else {
                SpannableStringBuilder builder = new SpannableStringBuilder(level1);
                ForegroundColorSpan span = new ForegroundColorSpan(levelColor);
                builder.setSpan(span, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                circleTextView.setText(builder);
                addView(circleTextView, layoutParams);

                LayoutParams lineParams = new LayoutParams(lineWidth, lineHeight);
                View line = new View(getContext());
                line.setBackgroundColor(lineColor);
                addView(line, lineParams);
            }
            children.add(circleTextView);
        }

        if (drawDate) {
            ImageView date = new ImageView(getContext());
            LayoutParams dateParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dateParam.leftMargin = UIUtils.dip2px(10);
            date.setBackgroundResource(R.drawable.date);
            date.setScaleType(ImageView.ScaleType.CENTER);
            date.setPadding(10, 10, 10, 10);
            addView(date, dateParam);

            date.setClickable(true);
            date.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onDateImageClick(v);
                    }
                }
            });
        }
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
        invalidate();
    }

    public void setLevel2(String level2) {
        this.level2 = level2;
        invalidate();
    }

    public void setSignDay(int signDay) {
        this.signDay = signDay;
        for (int i = 0; i < circleCount; i++) {
            CircleTextView circleTextView = children.get(i);
            if (i < signDay) {
                circleTextView.setPaintColor(signedColor);
            } else {
                circleTextView.setPaintColor(notSignColor);
            }
        }
    }

    private int getSize(int size) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density * size + 0.5);
    }

    public interface OnInnerItemClickListener {
        void onDateImageClick(View view);
    }

    private OnInnerItemClickListener mListener;

    public void setOnInnerItemClickListener(OnInnerItemClickListener onInnerItemClickListener) {
        mListener = onInnerItemClickListener;
    }
}
