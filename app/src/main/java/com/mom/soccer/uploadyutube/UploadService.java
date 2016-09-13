package com.mom.soccer.uploadyutube;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.exception.UploadExceptionActivity;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.trservice.UserMissionTRService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sungbo on 2016-06-13.
 */
public class UploadService extends IntentService {

    private static final String TAG = "UploadService";

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = new GsonFactory();
    GoogleAccountCredential credential;

    private int mUploadAttemptCount;
    private static final int MAX_RETRY = 3;
    private static final int UPLOAD_REATTEMPT_DELAY_SEC = 60;
    private Mission mission;
    private UserMission userMission;

    private String videoId = null;
    private User user;

    public static String uploadExecuteFlag="no";

    public UploadService() {
        super("UploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "IntentService onHandleIntent ========================================");

        Uri fileUri = intent.getData();

        //inetnt service 파라미터 변수는 번들로 받아야 한다
        Bundle extras = intent.getExtras();
        if(extras != null) {
            mission = (Mission) extras.getSerializable(MissionCommon.OBJECT);
            userMission = (UserMission) extras.getSerializable(MissionCommon.USER_MISSTION_OBJECT);
            user = (User) extras.getSerializable(MissionCommon.USER_OBJECT);
        }

        String chosenAccountName = Auth.accountName;

        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Lists.newArrayList(Auth.SCOPES));
        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), YouTubeScopes.all());
        credential.setSelectedAccountName(chosenAccountName);
        credential.setBackOff(new ExponentialBackOff());

        String appName = "몸 싸커 업로드";

        Log.d(TAG, "====================업로드 Intent 시작합니다.");
        Log.d(TAG, "====================업로드 자료는 : "  + userMission.toString());

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

            videoId = ResumableUpload.upload(youtube, fileInputStream, fileSize, mFileUri, cursor.getString(column_index), getApplicationContext(),mission,userMission);

        } catch (FileNotFoundException e) {
            Log.e(getApplicationContext().toString(), e.getMessage());
        } finally {
            try {

                fileInputStream.close();
            } catch (IOException e) {
            }
        }
        return videoId;
    }

    private static void zzz(int duration) throws InterruptedException {
        Thread.sleep(duration);
    }


    private void tryUploadAndShowSelectableNotification(final Uri fileUri, final YouTube youtube) throws InterruptedException {


        while (true) {
            Log.i(TAG, String.format("Uploading [%s] to YouTube", fileUri.toString()));

            String videoId = tryUpload(fileUri, youtube);

            if (videoId != null) {

                Log.i(TAG, String.format("Uploaded video with ID: %s", videoId));
                //tryShowSelectableNotification(videoId, youtube);
                return;

            } else {

                Log.d(TAG,"업로드 재시도 중");

                if (mUploadAttemptCount++ < MAX_RETRY) {
                    Log.i(TAG, String.format("Will retry to upload the video ([%d] out of [%d] reattempts)", mUploadAttemptCount, MAX_RETRY));
                    zzz(UPLOAD_REATTEMPT_DELAY_SEC * 100);
                } else {
                    Log.e(TAG, String.format("Giving up on trying to upload %s after %d attempts",
                            fileUri.toString(), mUploadAttemptCount));
                    return;
                }
            }
        }
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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "IntentService onDestroy  ========================================");

        if(uploadExecuteFlag.equals("fail")){
            Intent intent = new Intent(getApplicationContext(),UploadExceptionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else {
            userMission.setYoutubeaddr(videoId);
            userMission.setVideoaddr(Common.YOUTUBE_ADDR + videoId);
            userMission.setPassgrade(mission.getPassgrade());

            UserMissionTRService trService = new UserMissionTRService(getApplicationContext(), userMission, user);
            trService.createUserMission();
        }
    }
}

