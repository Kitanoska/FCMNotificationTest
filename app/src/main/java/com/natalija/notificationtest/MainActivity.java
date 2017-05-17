package com.natalija.notificationtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    private  Button subscribeGroup1Btn, subscribeGroup2Btn, subscribeGroup3Btn;
    private TextView msgTextView;
    MessageBroadcastReciver broadCastReciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("TOKEN", FirebaseInstanceId.getInstance().getToken());

        msgTextView = (TextView) findViewById(R.id.msgRecievedtextView);

        //handle notification message if any
        if (getIntent().getExtras() != null) {
            String data = (String) getIntent().getExtras().get("messageText");
            if(data != null)
                refreshTextMessage(data);
        }

        subscribeGroup1Btn = (Button) findViewById(R.id.group1Btn);
        subscribeGroup1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.group1));

                String msg = getString(R.string.msg_subscribed) +" "+ getString(R.string.group1);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                //TODO set unsubscribe
            }
        });

        subscribeGroup2Btn = (Button) findViewById(R.id.group2Btn);
        subscribeGroup2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.group2));

                String msg = getString(R.string.msg_subscribed)+" "+ getString(R.string.group2);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                //TODO set unsubscribe
            }
        });

        subscribeGroup3Btn = (Button) findViewById(R.id.testSendMsgBtn);
        subscribeGroup3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                AtomicInteger msgId = new AtomicInteger();
                fm.send(new RemoteMessage.Builder(R.string.senderId + "@gcm.googleapis.com")
                        .setMessageId(Integer.toString(msgId.incrementAndGet()))
                        .addData("my_message", "Hello from notification test app")
                        .addData("my_action","SAY_HELLO")
                        .build());

                String msg = "Message sent to FCM with Id: "+msgId.toString();
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class MessageBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshTextMessage(intent.getStringExtra("msgRecieved") );
        }
    }

    private void refreshTextMessage(String msgRecieved) {
        msgTextView.setText(msgRecieved);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(broadCastReciver!=null)
            unregisterReceiver(broadCastReciver);
    }

    protected void onResume() {
        super.onResume();
        broadCastReciver = new MessageBroadcastReciver();
        registerReceiver(broadCastReciver, new IntentFilter("sendData"));
    }
}
