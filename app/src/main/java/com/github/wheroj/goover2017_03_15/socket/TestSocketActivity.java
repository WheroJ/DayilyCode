package com.github.wheroj.goover2017_03_15.socket;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.github.wheroj.goover2017_03_15.R;
import com.github.wheroj.goover2017_03_15.baidusdk.BaiDuLocationUtils;
import com.github.wheroj.goover2017_03_15.chapter3.view.CircleView;
import com.github.wheroj.goover2017_03_15.chapter3.view.SignInView;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

public class TestSocketActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader bufferedReader;
    private TextView textview;

    private final int NEW_MSG = 3;
    private final int CONNECTED = 4;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NEW_MSG:
                    textview.setText(textview.getText() + "\r\n" + msg.obj);
                    break;
                case CONNECTED:
                    Toast.makeText(TestSocketActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Intent service;
    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_socket);


        editText = (EditText) findViewById(R.id.edittext);
        ViewTreeObserver viewTreeObserver = editText.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                editText.getMeasuredHeight();
            }
        });
        button = (Button) findViewById(R.id.button);
        textview = (TextView) findViewById(R.id.textview);
        circleView = (CircleView) findViewById(R.id.circleView);
        circleView.cursorAnimation();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int date2 = calendar.get(Calendar.DATE);
        int mon = calendar.get(Calendar.MONTH);
        textview.setText(year + "-" + mon + "-" + date2 + "-" + week);
        BaiDuLocationUtils.getInstance(getApplicationContext()).start();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.setlist);

//        textview.startAnimation(animation);
//        textview.getBackground().setLevel(100);
//        ((TransitionDrawable)textview.getBackground()).startTransition(1000);
        ((AnimationDrawable)textview.getBackground()).start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString().trim();
                if (writer != null){
                    writer.println(text);
                    mHandler.sendMessage(mHandler.obtainMessage(NEW_MSG, text));
                    editText.setText("");
                }
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textview, "translationX", 500);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());
//        objectAnimator.start();

        service = new Intent(this, SocketService.class);
        startService(service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectServer();
            }
        }).start();

        SignInView signInView = (SignInView) findViewById(R.id.signinview);
        signInView.setSignDay(4);
        signInView.setOnInnerItemClickListener(new SignInView.OnInnerItemClickListener() {
            @Override
            public void onDateImageClick(View view) {
                Toast.makeText(getApplication(), "hah", Toast.LENGTH_SHORT).show();
            }
        });

        setTiming();
    }

    private void connectServer() {

        while (socket == null) {
            try {
                socket = new Socket("localhost", 45000);
//                socket.connect(new InetSocketAddress("localhost", 45000));//这个居然导致了连接失败？
                mHandler.sendEmptyMessage(CONNECTED);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("connect fail");
            }
        }

        try {
            String content;
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!TestSocketActivity.this.isFinishing()){
                content = bufferedReader.readLine();
                mHandler.sendMessage(mHandler.obtainMessage(NEW_MSG, content));
                Log.i("TestSocketActivity", "server:  " + content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 设置定时发布
    private void setTiming() {
        final Calendar calendar = Calendar.getInstance();


        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

                if (checkTiming(year, month, day, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    Calendar c = Calendar.getInstance();
                    if (month == c.get(Calendar.MONTH) && day == c.get(Calendar.DAY_OF_MONTH)) {
                    } else {
                    }
                }

            }
        };
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                if (checkTiming(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute)) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                }
            }
        };

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(onDateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

        View.OnClickListener timingOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 设置日期
                if (v.getId() == R.id.textview) {
                    datePickerDialog.setVibrate(false);
                    datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
                    datePickerDialog.setCloseOnSingleTapDay(false);
                    datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
                }
                // 设置时间
                else if (v.getId() == R.id.button) {
                    timePickerDialog.setVibrate(false);
                    timePickerDialog.setStartTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    timePickerDialog.setCloseOnSingleTapMinute(false);
                    timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
                }
            }
        };
        textview.setOnClickListener(timingOnClickListener);
        button.setOnClickListener(timingOnClickListener);

    }

    private boolean checkTiming(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            System.out.println("erro");

            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {

        if (socket != null){
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stopService(service);
        super.onDestroy();
    }

    public void clickImage(View view){
        Toast.makeText(getApplication(), "hah", Toast.LENGTH_SHORT).show();
    }
}
