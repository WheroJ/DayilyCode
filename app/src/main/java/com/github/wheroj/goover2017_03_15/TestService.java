package com.github.wheroj.goover2017_03_15;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shopping on 2017/4/14 16:38.
 *
 * @description
 */

public class TestService extends Service {

    AtomicBoolean isAddDestory = new AtomicBoolean(false);

    CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    /**
     * 防止Binder通信中反序列化过程导致对象变化而无法识别
     */
    RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.github.wheroj.goover2017_03_15.permission.ACCESS_BOOK_SERVICE");
        Log.d("TestService", "onbind check=" + check);
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return iBookManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            iBookManager.addBook(new Book(mBookList.size() + 1 + "", "我是#" + (mBookList.size() + 1)));
            iBookManager.addBook(new Book(mBookList.size() + 1 + "", "我是#" + (mBookList.size() + 1)));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        new Thread(new AddBookThread()).start();
    }

    IBookManager.Stub iBookManager = new IBookManager.Stub(){

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws RemoteException {
            // 权限验证
            int check = checkCallingOrSelfPermission("com.github.wheroj.goover2017_03_15.permission.ACCESS_BOOK_SERVICE");
            Log.d("IBookManager", "check:"+check);
            if(check== PackageManager.PERMISSION_DENIED){
                Log.d("IBookManager", "Binder 权限验证失败");
                return false;
            }
            // 包名验证
            String packageName=null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if(packages!=null && packages.length>0){
                packageName = packages[0];
            }
            if(!packageName.startsWith("com.github.wheroj.goover2017_03_15")){
                Log.d("IBookManager", "包名验证失败");
                return false;

            }
            return super.onTransact(code, data, reply, flags);
        }


        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            int N = listeners.beginBroadcast();
            for (int i = 0; i < N; i++) {
                IOnNewBookArrivedListener broadcastItem = listeners.getBroadcastItem(i);
                if (broadcastItem != null) {
                    broadcastItem.onNewBookArrived(book);
                }
            }
            listeners.finishBroadcast();
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.register(listener);
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.unregister(listener);
        }
    };

    class AddBookThread implements Runnable {

        @Override
        public void run() {
            while (!isAddDestory.get()){
                try {
                    Thread.sleep(5000);
                    iBookManager.addBook(new Book(mBookList.size() + 1 + "", "我是#" + (mBookList.size() + 1)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        isAddDestory.set(true);
        super.onDestroy();
    }
}
