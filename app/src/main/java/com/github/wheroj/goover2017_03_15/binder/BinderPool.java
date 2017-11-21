package com.github.wheroj.goover2017_03_15.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

/**
 * Created by shopping on 2017/5/4 15:15.
 *
 * @description
 */

public class BinderPool {

    private static BinderPool mBinderPool = null;
    private CountDownLatch countDownLatch;

    private IBindPool mIBindPool;

    private Context mContext;
    private BinderPool(Context mContext){
        this.mContext = mContext;
        connectBinderService(mContext);
    }

    private void connectBinderService(Context mContext) {
        countDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, serviceConnection, mContext.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mIBindPool.asBinder().unlinkToDeath(deathRecipient, 0);
            mIBindPool = null;
            connectBinderService(mContext);
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBindPool = IBindPool.Stub.asInterface(service);
            countDownLatch.countDown();
            try {
                mIBindPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static BinderPool getInstance(Context context){
        if (mBinderPool == null){
            synchronized (BinderPool.class){
                if (mBinderPool == null){
                    return new BinderPool(context);
                }
            }
        }
        return mBinderPool;
    }

    /**
     * 查询
     * @param code
     * @return
     */
    public IBinder queryBinder(int code){
        if (mIBindPool != null){
            try {
                return mIBindPool.queryBinder(code);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
