package com.natalija.notificationtest.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.natalija.notificationtest.MainActivity;
import com.natalija.notificationtest.R;

import java.util.Map;

/**
 * Created by kitanoskan on 12/05/2017.
 */

public class MessageService extends FirebaseMessagingService {

    String TAG = "MessageService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String wholeMessage = "";

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            wholeMessage += remoteMessage.getNotification().getBody()+"\n";
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            wholeMessage += remoteMessage.getData().get("messageText")+" ";
           /* if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow(remoteMessage.getData().get("data"));
            }*/

            //sendNotification(remoteMessage);
        }

        handleNow(wholeMessage); //do broadcast
    }

    public void handleNow(String msg){
        //TODO handle message immediatly
        //display message to user
        Intent intent = new Intent("sendData");
        intent.putExtra("msgRecieved",msg);
        sendBroadcast(intent);

    }

//    private void sendNotification(RemoteMessage remoteMessage) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle(remoteMessage.getData().get("title"))
//                .setContentText(remoteMessage.getData().get("body"))
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

}
