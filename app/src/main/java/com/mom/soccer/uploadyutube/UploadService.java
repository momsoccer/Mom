package com.mom.soccer.uploadyutube;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.common.collect.Lists;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.mission.MissionCommon;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sungbo on 2016-06-13.
 */
public class UploadService extends IntentService {


    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = new GsonFactory();
    GoogleAccountCredential credential;


    private int mUploadAttemptCount;
    private static final int MAX_RETRY = 3;
    private static final int UPLOAD_REATTEMPT_DELAY_SEC = 60;
    private static final String TAG = "UploadService";
    private Mission mission;

    private YoutubeUploadVo youtubeUploadVo;

    String videoId = null;

    public UploadService() {
        super("UploadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "IntentService onCreate  ========================================");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "IntentService onStartCommand  ========================================");
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "IntentService onHandleIntent ========================================");

        Uri fileUri = intent.getData();
        mission = (Mission) intent.getSerializableExtra(MissionCommon.OBJECT);

        String chosenAccountName = Auth.accountName;

        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Lists.newArrayList(Auth.SCOPES));
        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), YouTubeScopes.all());
        credential.setSelectedAccountName(chosenAccountName);
        credential.setBackOff(new ExponentialBackOff());

        String appName = "몸 싸커 업로드";

        Log.d(TAG, "====================업로드 Intent 시작합니다.");
        final YouTube youtube =  new YouTube.Builder(transport, jsonFactory, credential).setApplicationName(appName).build();

        try {
            tryUploadAndShowSelectableNotification(fileUri, youtube);
        } catch (InterruptedException e) {
            // ignore
        }

        Log.d(TAG, "====================업로드 종료");

    }


    private String tryUpload(Uri mFileUri, YouTube youtube) {

        long fileSize;
        InputStream fileInputStream = null;
        try {

            fileSize = getContentResolver().openFileDescriptor(mFileUri, "r").getStatSize();
            fileInputStream = getContentResolver().openInputStream(mFileUri);
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(mFileUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            videoId = ResumableUpload.upload(youtube, fileInputStream, fileSize, mFileUri, cursor.getString(column_index), getApplicationContext());

        } catch (FileNotFoundException e) {
            Log.d(TAG,"여기서 에러가 나는가요 1");
            Log.e(getApplicationContext().toString(), e.getMessage());
        } finally {
            try {
                Log.d(TAG,"여기서 에러가 나는가요2");
                fileInputStream.close();
            } catch (IOException e) {
                Log.d(TAG,"여기서 에러가 나는가요3");
            }
        }
        return videoId;
    }

    private static void zzz(int duration) throws InterruptedException {
        Thread.sleep(duration);
    }


    private void tryUploadAndShowSelectableNotification(final Uri fileUri, final YouTube youtube) throws InterruptedException {

        Log.d(TAG,"여기였어 여기..tryUploadAndShowSelectableNotification");

        while (true) {
            Log.i(TAG, String.format("Uploading [%s] to YouTube", fileUri.toString()));

            String videoId = tryUpload(fileUri, youtube);

            if (videoId != null) {

                Log.i(TAG, String.format("Uploaded video with ID: %s", videoId));
                //tryShowSelectableNotification(videoId, youtube);
                return;

            } else {
                Log.e(TAG, String.format("Failed to upload %s", fileUri.toString()));
                Log.d(TAG,"여기였어 여기..");
                if (mUploadAttemptCount++ < MAX_RETRY) {
                    Log.i(TAG, String.format("Will retry to upload the video ([%d] out of [%d] reattempts)",
                            mUploadAttemptCount, MAX_RETRY));
                    zzz(UPLOAD_REATTEMPT_DELAY_SEC * 1000);
                } else {
                    Log.e(TAG, String.format("Giving up on trying to upload %s after %d attempts",
                            fileUri.toString(), mUploadAttemptCount));
                    return;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "IntentService onDestroy  ========================================");

        final Intent intent = new Intent("uploadReceiver");
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        intent.putExtra("uploadMessage","업로드가 종료 되었습니다");
        intent.putExtra("upflag",videoId);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("업로드 알림")
                .setContentText("업로드가 정상적으로 이루어 졌습니다")
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                ;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        broadcastManager.sendBroadcast(intent);

    }

}

