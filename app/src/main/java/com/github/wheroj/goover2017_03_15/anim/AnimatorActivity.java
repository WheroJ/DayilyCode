package com.github.wheroj.goover2017_03_15.anim;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.github.wheroj.goover2017_03_15.R;

public class AnimatorActivity extends AppCompatActivity {

    View targetView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);

        ObjectAnimator.ofFloat(targetView, "translationX", 0, 100);

        ValueAnimator animator = ValueAnimator.ofInt(0, 1);
        animator.setDuration(1000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
            }
        });
        animator.setInterpolator(new LinearInterpolator());

        View decorView = getWindow().getDecorView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getWindow().superDispatchTouchEvent(ev)){

        }
        return super.dispatchTouchEvent(ev);
    }
}
