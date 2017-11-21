package com.github.wheroj.goover2017_03_15;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/3/15 17:48.
 *
 * @description
 */

public class SPUtils {
    private static SharedPreferences sharedPreferences;

    private static Context mContext;
    public static void init(Context context) {
        mContext = context;
        if (sharedPreferences == null){
            sharedPreferences = mContext.getSharedPreferences("test", Context.MODE_PRIVATE);
        }
    }

    public static int getInt(String key, int defValue){
        return sharedPreferences.getInt(key, defValue);
    }

    public static void setInt(String key, int value){
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static String getPackgeName(){
        return mContext.getPackageName();
    }
}
