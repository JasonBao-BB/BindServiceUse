package com.example.administrator.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static android.app.Service.START_NOT_STICKY;

public class MainActivity extends Activity {

    Button startService;
    Button stopService;
    Button bindService;
    Button unbindService;
    Button sysBtn;
    Intent intent;
    EditText editext;
    TextView textView;
    private MyService.MyBinder binder = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService = (Button) findViewById(R.id.start);
        stopService = (Button) findViewById(R.id.stop);
        bindService = (Button) findViewById(R.id.bind);
        unbindService = (Button) findViewById(R.id.unbind);
        editext = (EditText) findViewById(R.id.editText);
        sysBtn = (Button) findViewById(R.id.sysBtn);
        textView = (TextView) findViewById(R.id.textView);

        startService.setOnClickListener(onClickListener);
        stopService.setOnClickListener(onClickListener);
        bindService.setOnClickListener(onClickListener);
        unbindService.setOnClickListener(onClickListener);
        sysBtn.setOnClickListener(onClickListener);

        intent = new Intent(MainActivity.this, MyService.class);



    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start:
                    intent.putExtra("data",editext.getText().toString());
                    startService(intent);
                    break;

                case R.id.stop:
                    stopService(intent);
                    break;

                case R.id.bind:
                    bindService(intent,connection,Context.BIND_AUTO_CREATE);
                    break;

                case R.id.unbind:
                    unbindService(connection);
                    break;

                case R.id.sysBtn:
                    if (binder != null) {
                        binder.setData(editext.getText().toString());
                    }
                    break;
            }
        }
    };

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.MyBinder) service;
            binder.getService().setCallBack(new MyService.CallBack() {
                @Override
                public void dataChange(String data) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("data",data);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            textView.setText(msg.getData().getString("data"));
        }
    };



}
