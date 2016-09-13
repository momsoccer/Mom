package com.mom.soccer.fcmtoken;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mom.soccer.MainActivity;
import com.mom.soccer.R;

/**
 * Created by sungbo on 2016-06-29.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessaging";

    // 호출시 (푸시 리시버에서 호출)
    //Intent intent_ = new Intent(context, SOSPopupActivity.class);
    //intent_.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);   // 이거 안해주면 안됨
    //context.startActivity(intent_);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());  //819984969479 발신자 ID
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        Log.d(TAG,"타이틀은 : "+remoteMessage.getData().get("title"));
        Log.d(TAG,"콘텐츠는 : "+remoteMessage.getData().get("content"));
        Log.d(TAG,"메세지 : "+remoteMessage.getData().get("message"));

        sendNotification(remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("content"),
                        remoteMessage.getData().get("message")
        );
    }

    private void sendNotification(String title, String content, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_diarog_mom)
                .setContentTitle(title)
                .setContentText(content)
                .setSubText(message)
                .setColor(getResources().getColor(R.color.color6)) //빽그라운드 컬러
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                ;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }
}
