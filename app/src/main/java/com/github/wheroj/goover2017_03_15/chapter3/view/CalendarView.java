package com.github.wheroj.goover2017_03_15.chapter3.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.wheroj.goover2017_03_15.R;
import com.github.wheroj.goover2017_03_15.UIUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * Created by shopping on 2017/7/6 16:38.
 *
 * @description
 */

public class CalendarView extends LinearLayout {

    private Paint mPaint;

    private String[] weekStrs = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        initPaint();
        createTitle();
        createWeek();
        createDay();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
    }

    private void createTitle() {
        View title = View.inflate(getContext(), R.layout.calendar_title, null);
        View calendarLeft = title.findViewById(R.id.calendar_left);
        View calendarRight = title.findViewById(R.id.calendar_right);
        View calendarTvCenter = title.findViewById(R.id.calendar_tv_center);
        LayoutParams titleLayoutParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(45));
        titleLayoutParam.leftMargin = UIUtils.dip2px(5);
        titleLayoutParam.rightMargin = UIUtils.dip2px(5);
        title.setPadding(UIUtils.dip2px(5),UIUtils.dip2px(5),UIUtils.dip2px(5),UIUtils.dip2px(5));
        addView(title, titleLayoutParam);
    }

    private void createWeek() {
        LinearLayout llWeek = new LinearLayout(getContext());
        LayoutParams weekParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(45));
        llWeek.setBackgroundColor(getResources().getColor(R.color.bgColor));
        setWeightSum(7);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        for (int i = 0; i < 7; i++){
            TextView view = new TextView(getContext());
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(COMPLEX_UNIT_DIP, 16);
            view.setTextColor(getResources().getColor(R.color.week));
            view.setText(weekStrs[i]);
            llWeek.addView(view, layoutParams);
        }
        addView(llWeek, weekParams);
    }


    private void createDay() {
        RecyclerView dayList = new RecyclerView(getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7, VERTICAL, true);
        dayList.setLayoutManager(layoutManager);
        SimpleDateFormat yearMon = new SimpleDateFormat("yyyy-MM");
        try {
            int rows = caculateDayRows(yearMon.parse("2017-07"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算列数
     * @return
     * @param date
     */
    private int caculateDayRows(Date date) {
        Calendar calendar = Calendar.getInstance();
        int start = date.getDay();
        return 0;
    }

    private int dayNum(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        if (year % 4 == 0 && ((year%100)!=0)||(year%400==0)){

        }
        return 0;
    }
}
