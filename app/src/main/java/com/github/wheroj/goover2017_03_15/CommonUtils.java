package com.github.wheroj.goover2017_03_15;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/3/15 14:25.
 *
 * @description
 */

public class CommonUtils {

    private static Context mContext;
    private CommonUtils(){}

    public static String getPackageName(){
//        return mContext.getPackageName();
        return null;
    }

    public static void init(Context context) {
        mContext = context;
    }
}
