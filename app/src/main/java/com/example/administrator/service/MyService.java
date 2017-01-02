package com.example.administrator.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import static java.lang.Thread.sleep;

public class MyService extends Service {
    private boolean statue = false;
    private String data = "This is the default data";
    String textData;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    public class MyBinder extends Binder {

        public void setData(String data) {
            //Outside class data
            MyService.this.data = data;
        }

        public MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        data = intent.getStringExtra("data");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        statue = true;
        new Thread() {

            @Override
            public void run() {

                int i = 0;
                super.run();
                while (statue) {
                    i++;
                    textData = i+data;

                    if (callBack != null) {
                        callBack.dataChange(textData);
                    }

                    System.out.println(i+data);

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        statue = false;
    }

    private CallBack callBack = null;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    //Interface used for callback operation
    public static interface CallBack {
        public void dataChange(String data);
    }

}
