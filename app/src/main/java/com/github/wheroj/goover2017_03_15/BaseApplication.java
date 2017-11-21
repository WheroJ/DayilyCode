package com.github.wheroj.goover2017_03_15;

import android.app.Application;

/**
 * Created by Administrator on 2017/3/15 14:30.
 *
 * @description
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CommonUtils.init(this);

        SPUtils.init(this);

        UIUtils.init(this);
    }
}
