package com.mom.soccer.alluser.youtubeupload;

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
import com.mom.soccer.dto.InsVideoVo;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.exception.UploadExceptionActivity;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.uploadyutube.UploadService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class VideoUploadService extends IntentService {

    private static final String TAG = "UploadFeedService";
    private static final String UloadStep = "UploadFeedService FeedBack step Check";

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = new GsonFactory();
    GoogleAccountCredential credential;

    private int mUploadAttemptCount;
    private static final int MAX_RETRY = 3;
    private static final int UPLOAD_REATTEMPT_DELAY_SEC = 60;

    private InsVideoVo insVideoVo;
    private Instructor instructor;
    private String videoId = null;

    public VideoUploadService() {
        super("UploadFeedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Uri fileUri = intent.getData();
        Bundle extras = intent.getExtras();
        if(extras != null) {

            insVideoVo = (InsVideoVo) extras.getSerializable(MissionCommon.VIDEO_OBJECT);
            instructor  =  (Instructor) extras.getSerializable(MissionCommon.INS_OBJECT);
        }

        Auth.accountName = instructor.getEmail();
        String chosenAccountName = Auth.accountName;

        String appName = "몸 싸커 영상피드백 업로드";

        Log.i(TAG,"피드백 영상 업로드 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 1");

        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Lists.newArrayList(Auth.SCOPES));
        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), YouTubeScopes.all());
        credential.setSelectedAccountName(chosenAccountName);
        credential.setBackOff(new ExponentialBackOff());

        final YouTube youtube =  new YouTube.Builder(transport, jsonFactory, credential).setApplicationName(appName).build();
        try {
            tryUploadAndShowSelectableNotification(fileUri, youtube);
        } catch (InterruptedException e) {
            // ignore
        }

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

            videoId = ResumableVideoUpload.upload(youtube, fileInputStream, fileSize, mFileUri, cursor.getString(column_index), getApplicationContext(),insVideoVo);

        } catch (FileNotFoundException e) {
            Log.d(TAG,"");
            Log.e(getApplicationContext().toString(), e.getMessage());
        } finally {
            try {
                Log.d(TAG,UloadStep + 1);
                fileInputStream.close();
            } catch (IOException e) {
                Log.d(TAG,UloadStep + 2);
            }
        }
        return videoId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if(UploadService.uploadExecuteFlag.equals("fail")){
            Intent intent = new Intent(getApplicationContext(),UploadExceptionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else {

        }


    }

}
