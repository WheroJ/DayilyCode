package com.github.wheroj.goover2017_03_15.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by shopping on 2017/5/4 15:18.
 *
 * @description
 */

public class BinderPoolService extends Service {

    public static final int SECURITY_CODE = 2;
    public static final int COMPUTE = 3;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBindPool;
    }

    private IBinder iBindPool = new IBindPool.Stub(){
        @Override
        public IBinder queryBinder(int code) throws RemoteException {
            IBinder iBinder = null;
            switch (code){
                case COMPUTE:
                    iBinder = iCompute;
                    break;
                case SECURITY_CODE:
                    iBinder = iSecurityCenter;
                    break;
            }
            return iBinder;
        }
    };

    private IBinder iCompute = new ICompute.Stub() {
        @Override
        public double add(double a, double b) throws RemoteException {
            return a + b;
        }

        @Override
        public double subtraction(double a, double b) throws RemoteException {
            return a - b;
        }
    };

    private IBinder iSecurityCenter = new ISecurityCenter.Stub() {
        private static final char SECRET_CODE = '^';
        @Override
        public String encrypt(String pass) throws RemoteException {
            if (TextUtils.isEmpty(pass)) return null;
            char[] chars = pass.toCharArray();
            for (int i = 0; i<chars.length; i++) {
                chars[i] ^= SECRET_CODE;
            }
            return chars.toString();
        }

        @Override
        public String decrypt(String pass) throws RemoteException {
            return encrypt(pass);
        }
    };
}
