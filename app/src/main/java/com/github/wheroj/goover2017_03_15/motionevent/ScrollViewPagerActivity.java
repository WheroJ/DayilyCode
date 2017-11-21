package com.github.wheroj.goover2017_03_15.motionevent;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.wheroj.goover2017_03_15.LocationUtils;
import com.github.wheroj.goover2017_03_15.LocationUtils2;
import com.github.wheroj.goover2017_03_15.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shopping on 2017/6/5 15:13.
 *
 * @description
 */

public class ScrollViewPagerActivity extends FragmentActivity {

    private ViewPager viewPager;

    private int[] imgs = new int[]{R.drawable.card_huxi, R.drawable.card_niaoliang, R.drawable.card_pinggu
            , R.drawable.card_suifang, R.drawable.card_tiwen, R.drawable.card_tizhong, R.drawable.card_xeuyang, R.drawable.card_xinlv
            , R.drawable.card_xinqing, R.drawable.card_xueya, R.drawable.card_xuezhi, R.drawable.card_yinshi, R.drawable.card_yongyoa
            , R.drawable.card_yundong};
    private final int ADD = 100;
    private final int CITY = 200;
    private Timer timer;
    private TimerTask timerTask;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        textView = (TextView)findViewById(R.id.textview);

        LocationUtils2.getCNBylocation(this);

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(LocationUtils2.cityName)){
                    timer.cancel();
                    mHandler.sendEmptyMessage(CITY);
                }
            }
        };
        timer.schedule(timerTask, 3*1000, 3*1000);

        viewPager.setAdapter(new MyPagerAdapter(this));

        RelativeLayout decorView = (RelativeLayout) findViewById(R.id.root);

        LinearLayout linearLayout = new LinearLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getSize(20));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(layoutParams);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.rightMargin = getSize(15);
        layoutParams.bottomMargin = getSize(15);
        decorView.addView(linearLayout);

        int rem = ((Integer.MAX_VALUE/2)/imgs.length)*imgs.length;
        viewPager.setCurrentItem(rem);
        final List<ImageView> pots = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++){
            ImageView pot = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(10), getSize(10));
            if (i == imgs.length - 1){
                params.rightMargin = getSize(10);
            }
            params.leftMargin = getSize(10);

            if (i == 0) pot.setBackgroundColor(Color.RED);
            else pot.setBackgroundColor(Color.GRAY);
            pot.setLayoutParams(params);

            pots.add(pot);
            linearLayout.addView(pot);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = position % imgs.length;
                for (int i = 0; i < pots.size(); i++){
                    ImageView view = pots.get(i);
                    if (index == i)
                        view.setBackgroundColor(Color.RED);
                    else view.setBackgroundColor(Color.GRAY);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    stopScroll();
                    viewPager.getParent().requestDisallowInterceptTouchEvent(false);
                } else if (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL){
                    viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                    startScroll();
                }
                return false;
            }
        });

        startScroll();
    }

    private void stopScroll() {
        if (timer != null) timer.cancel();
        timer = null;
        timerTask = null;
    }

    private void startScroll() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(ADD);
            }
        };
        timer.schedule(timerTask, 1000*3, 1000*3);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CITY:
                    textView.setText(LocationUtils2.cityName);
                    break;
                case ADD:
                    int currentItem = viewPager.getCurrentItem();
                    viewPager.setCurrentItem(++currentItem);
                    break;
            }
        }
    };

    private int getSize(int size){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (size * scale + 0.5f);
    }

    class MyPagerAdapter extends PagerAdapter {

        private Context mContext;
        public MyPagerAdapter(Context context){
            mContext = context;
        }
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = position % imgs.length;
            ImageView imageView = new ImageView(mContext);
            ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
            layoutParams.width = ViewPager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewPager.LayoutParams.WRAP_CONTENT;
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(imgs[index]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "哈哈";
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
