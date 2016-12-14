package com.mom.soccer.fcmtoken;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mom.soccer.MainActivity;
import com.mom.soccer.R;
import com.mom.soccer.alluser.AllUserMainActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.pubactivity.Param;

/**
 * Created by sungbo on 2016-06-29.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessaging";
    private String sfName = "momSoccerSetup";
    private String setupValue= null;

    /*

    노티를 받으면 해당 화면으로 갈 수 있게 설계가 필요함.

     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

            SharedPreferences sf = getSharedPreferences(sfName,0);
            setupValue = sf.getString("push","");

            if(Compare.isEmpty(setupValue)){
                sf = getSharedPreferences(sfName, 0);
                SharedPreferences.Editor editor = sf.edit();
                editor.putString("push", "Y"); // 입력
                editor.commit(); // 파일에 최종 반영함
                setupValue="Y";
            }

            if(setupValue.equals("Y")){
                sendNotification(remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("content"),
                        remoteMessage.getData().get("message"),
                        remoteMessage.getData().get("key1"),
                        remoteMessage.getData().get("key2")
                );
            }

    }

    private void sendNotification(String title, String content, String message,String key1,String key2) {
        Intent intent;
        if(key1.equals("lesson")){
            intent = new Intent(this, AllUserMainActivity.class);
            intent.putExtra(Param.FRAGMENT_COUNT,1);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        }else {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_diarog_mom)
                .setContentTitle(title)
                .setContentText(content)
                .setSubText(message)
                .setColor(getResources().getColor(R.color.color8))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setLights(0xff00ff00, 300, 100)
                ;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());


        /*
        if(key1.equals("lesson")){
            Log.i(TAG,"콜 했습니다 해당 화면으로 이동합니다");
            intent = new Intent(this, AllUserMainActivity.class);
            intent.putExtra(Param.FRAGMENT_COUNT,1);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        */

    }
}
