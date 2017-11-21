package com.github.wheroj.goover2017_03_15;


import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ServiceConnection serviceConnection;
    private IBookManager remoteBookManager;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent("com.github.wheroj.goover2017_03_15.TestService");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage(SPUtils.getPackgeName());


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Messenger replyTo = msg.replyTo;
                Message message = Message.obtain();
                try {
                    replyTo.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        Messenger messenger = new Messenger(mHandler);
        Message message = Message.obtain();
        message.replyTo = messenger;
        message.what = 0;
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IBookManager iBookManager = IBookManager.Stub.asInterface(service);
                remoteBookManager = iBookManager;
                try {
                    iBookManager.registerListener(listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                remoteBookManager = null;
            }
        };
//        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
//        getHistory();

        String host = "content://com.github.wheroj.goover2017_03_15.provider.BookProvider";
        ContentValues values = new ContentValues();
        values.put("_id", 4);
        values.put("name", "程序员开发艺术");
        getContentResolver().insert(Uri.parse(host + "/book"), values);

        Cursor cursor = getContentResolver().query(Uri.parse(host + "/book"), null, "select book(_id, name)", null, null);
        while (cursor.moveToNext()){
            Log.i("MainActivity", "_id:" + cursor.getInt(0) + ", name:" + cursor.getString(1));
        }
        cursor.close();

    }

    IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Log.d("MainActivity", "又收到一本新书:" + book.bookName);
        }
    };


    public void getHistory() {
        int count = SPUtils.getInt("count", -1);
        if (count > 0){
            Toast.makeText(this, "这是您第" + count + "次打开该应用", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "这是您首次打开该应用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (remoteBookManager != null && remoteBookManager.asBinder().isBinderAlive()){
            try {
                remoteBookManager.unRegisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
