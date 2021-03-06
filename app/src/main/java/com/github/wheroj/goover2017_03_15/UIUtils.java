package com.github.wheroj.goover2017_03_15;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

public class UIUtils {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getWinWidth() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕高度
     */
    public static int getWinHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px转换dip
     */

    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取当前版本名
     *
     * @return
     */
    public static String getVersionName() {
        PackageManager pmManager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = pmManager.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断是否连接网络
     *
     * @return
     */
    public static boolean isConnected() {
        ConnectivityManager conn = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * 获取一个保存缓存的目录
     *
     * @return
     */
    public static String getSavePath() {
        File path = null;
        if (!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            path = mContext.getFilesDir().getAbsoluteFile();
        } else {
            path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mContext.getPackageName());
        }
        if(!path.exists())
            path.mkdirs();
        return path.getAbsolutePath();
    }

    /**
     * 获取一个缓存目录
     *
     * @return
     */
    public static String getCachePath() {
        File dirFile = null;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // getCacheDir()方法用于获取/data/data//cache目录
            // getFilesDir()方法用于获取/data/data//files目录
            dirFile = UIUtils.getContext().getCacheDir();
        } else {
            dirFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mContext.getPackageName() + File.separator + "cache");
        }
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile.getAbsolutePath();
    }

    /**
     * 获取存放下载图片的地址
     * @return
     */
    public static String getImageCachePath() {
        File dirFile = new File(getCachePath() + File.separator + "image");
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile.getAbsolutePath();
    }

    /**
     * @return 当存在SD卡的时候返回绝对路径，当不存在时返回null
     */
    public static String getExtraStoragePath(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * 关闭软键盘
     * @param content
     */
    public static void closeEditor(Activity content){
        InputMethodManager inputMethodManager = (InputMethodManager) content.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(content.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 打开软键盘
     * @param controller
     */
    public static void openEditor(Activity content, View controller){
        InputMethodManager inputMethodManager = (InputMethodManager) content.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(controller, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getWindowHeight(){
        WindowManager windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }
}