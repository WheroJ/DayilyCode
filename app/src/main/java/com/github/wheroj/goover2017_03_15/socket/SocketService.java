package com.github.wheroj.goover2017_03_15.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by shopping on 2017/5/2 16:27.
 *
 * @description
 */

public class SocketService extends Service {

    private Socket socket;

    private boolean mIsServiceDestoryed = false;

    private String[] reply = new String[]{"收到了", "哈哈哈", "嘻嘻嘻", "黑恶黑", "呵呵呵"};
    private ServerSocket serverSocket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (socket != null){
            try {
                socket.shutdownInput();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mIsServiceDestoryed = true;
        super.onDestroy();
    }

    class TcpServer implements Runnable{

        private BufferedReader reader;
        private PrintWriter writer;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(45000);
            } catch (IOException e) {
                System.err.println("server 错误");
                e.printStackTrace();
                return;
            }

            try {
                socket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                writer.println("欢迎来到聊天室");
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (!mIsServiceDestoryed) {
                try {
                    Log.i("SocketService", "client:" + reader.readLine());
                    int i = new Random().nextInt(reply.length);

                    writer.println(reply[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
