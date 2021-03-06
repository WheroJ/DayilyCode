package com.github.wheroj.goover2017_03_15;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by shopping on 2017/6/19 16:56.
 *
 * @description
 */

public class LocationUtils2 {

    public static String cityName;  //城市名

    private static Geocoder geocoder;   //此对象能通过经纬度来获取相应的城市等信息

    /**
     * 通过地理坐标获取城市名  其中CN分别是city和name的首字母缩写
     *
     * @param context
     */
    public static void getCNBylocation(Activity context) {

        geocoder = new Geocoder(context);

        //用于获取Location对象, 实例化一个LocationManager对象
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //provider的类型
        String provider = LocationManager.NETWORK_PROVIDER;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   //高精度
        criteria.setAltitudeRequired(false);    //不要求海拔
        criteria.setBearingRequired(false); //不要求方位
        criteria.setCostAllowed(false); //不允许有话费
        criteria.setPowerRequirement(Criteria.POWER_LOW);   //低功耗

//        AndPermission.with(context)
//                .requestCode(300)
//                .permission(android.Manifest.permission.ACCESS_FINE_LOCATION,
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                .callback(new PermissionListener() {
//                    @Override
//                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
        Location location = locationManager.getLastKnownLocation(provider);

        String queryed_name = updateWithNewLocation(location);
        if ((queryed_name != null) && (0 != queryed_name.length())) {
            cityName = queryed_name;
        }

        /*
         * 第二个参数表示更新的周期，单位为毫秒；第三个参数的含义表示最小距离间隔，单位是米
         * 设定每30秒进行一次自动定位
         */
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 30000, 50,
                locationListener);
        //移除监听器，在只有一个widget的时候，这个还是适用的
        locationManager.removeUpdates(locationListener);
//                    }
//
//                    @Override
//                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//
//                    }
//                }).start();
    }

    /**
     * 方位改变时触发，进行调用
     */
    private final static LocationListener locationListener = new LocationListener() {
        String tempCityName;

        public void onLocationChanged(Location location) {

            tempCityName = updateWithNewLocation(location);
            if ((tempCityName != null) && (tempCityName.length() != 0)) {
                cityName = tempCityName;
            }
        }

        public void onProviderDisabled(String provider) {
            tempCityName = updateWithNewLocation(null);
            if ((tempCityName != null) && (tempCityName.length() != 0)) {
                cityName = tempCityName;
            }
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    /**
     * 更新location
     *
     * @param location
     * @return cityName
     */
    private static String updateWithNewLocation(Location location) {
        String mcityName = "";
        double lat = 0;
        double lng = 0;
        List<Address> addList = null;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            System.out.println("无法获取地理信息");
        }

        try {
            addList = geocoder.getFromLocation(lat, lng, 1);    //解析经纬度
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address add = addList.get(i);
                mcityName += add.getLocality();
            }
        }
        if (mcityName.length() != 0) {
            return mcityName.substring(0, (mcityName.length() - 1));
        } else {
            return mcityName;
        }
    }

    /**
     * 通过经纬度获取地址信息的另一种方法
     *
     * @param latitude
     * @param longitude
     * @return 城市名
     */
    public static String GetAddr(String latitude, String longitude) {
        String addr = "";

        /*
         * 也可以是http://maps.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s，不过解析出来的是英文地址
         * 密钥可以随便写一个key=abc
         * output=csv,也可以是xml或json，不过使用csv返回的数据最简洁方便解析
         */
        String url = String.format(
                "http://ditu.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s",
                latitude, longitude);
        URL myURL;
        URLConnection httpsConn;
        try {

            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        try {

            httpsConn = myURL.openConnection();

            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null) {
                    String[] retList = data.split(",");
                    if (retList.length > 2 && ("200".equals(retList[0]))) {
                        addr = retList[2];
                    } else {
                        addr = "";
                    }
                }
                insr.close();
            }
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
        return addr;
    }

}
