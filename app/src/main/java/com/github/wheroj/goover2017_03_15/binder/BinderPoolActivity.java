package com.github.wheroj.goover2017_03_15.binder;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;
import android.widget.TextView;

import com.github.wheroj.goover2017_03_15.R;

/**
 * Created by shopping on 2017/5/4 15:31.
 *
 * @description
 */

public class BinderPoolActivity extends Activity {

    private TextView textView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    textView.setText(textView.getText() + (String)msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_socket);

        textView = (TextView) findViewById(R.id.textview);

        textView.requestLayout();
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        boolean touchEvent = gestureDetector.onTouchEvent(event);
        return touchEvent;
    }

    private void doWork(){
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder iBinder = binderPool.queryBinder(BinderPoolService.COMPUTE);
        IBinder iBinder2 = binderPool.queryBinder(BinderPoolService.SECURITY_CODE);
        ICompute iCompute = ICompute.Stub.asInterface(iBinder);
        ISecurityCenter iSecurityCenter = ISecurityCenter.Stub.asInterface(iBinder2);
        try {
            handler.obtainMessage(0, iCompute.add(3, 5) + "\r\n").sendToTarget();
            handler.obtainMessage(0, iSecurityCenter.encrypt("123456") + "\r\n").sendToTarget();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
