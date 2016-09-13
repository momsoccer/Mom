package com.mom.soccer.ins;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.mom.soccer.R;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Constants;
import com.mom.soccer.dto.FeedbackLine;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.uploadyutube.UploadService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.CancellationException;


public class ResumableFeedUpload {

    private static String VIDEO_FILE_FORMAT = "video/*";
    private static final String SUCCEEDED = "succeeded";
    private static final String TAG = "ResumableUpload";
    private static int UPLOAD_NOTIFICATION_ID = 1001;
    private static int PLAYBACK_NOTIFICATION_ID = 1002;
    private static String videoId;

    public static String upload(YouTube youtube, final InputStream fileInputStream,
                                final long fileSize, final Uri mFileUri, final String path, final Context context, final FeedbackLine feedbackLine){

        final NotificationManager notifyManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent notificationIntent = new Intent(context, InsDashboardActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra(Param.FRAGMENT_COUNT,0);
        notificationIntent.setAction(Intent.ACTION_VIEW);

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        final Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);


        builder.setContentTitle(context.getString(R.string.upload_pre_title))
                .setContentText( context.getString(R.string.upload_pre_title_content)+" : "+ feedbackLine.getSubject())
                .setSmallIcon(R.drawable.ic_diarog_mom).setContentIntent(contentIntent).setStyle(
                new NotificationCompat.BigPictureStyle().bigPicture(thumbnail).setSummaryText(feedbackLine.getContent()));
        ;

        notifyManager.notify(UPLOAD_NOTIFICATION_ID, builder.build());
        videoId = null;

        try{
            Video videoObjectDefiningMetadata = new Video();
            VideoStatus status = new VideoStatus();
            status.setPrivacyStatus("public");
            videoObjectDefiningMetadata.setStatus(status);

            VideoSnippet snippet = new VideoSnippet();

            snippet.setTitle(feedbackLine.getUsername()+" Mom Soccer Feedback : "+feedbackLine.getFeedbackid());
            snippet.setDescription(feedbackLine.getContent()+" : " + feedbackLine.getProfileimgurl());

            //태그를 입력합니다.
            snippet.setTags(Arrays.asList(Constants.DEFAULT_KEYWORD
                    ,"MomSoccerMission","몸싸커","몸싸커미션챌린지","축구","셀프축구","selftraining","soccerselftraining","soccerfreestyle","freestylesoccer"
                    ,"피드백답변","FeedBack Reply"
                    )
            );
            // Set completed snippet to the video object.
            videoObjectDefiningMetadata.setSnippet(snippet);

            InputStreamContent mediaContent =
                    new InputStreamContent(VIDEO_FILE_FORMAT, new BufferedInputStream(fileInputStream));
            mediaContent.setLength(fileSize);

            YouTube.Videos.Insert videoInsert = youtube.videos().insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);
            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

            uploader.setDirectUploadEnabled(false);

            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {

                public void progressChanged(MediaHttpUploader uploader) throws IOException {

                    if (Common.isUpflag()) {
                        //This was the only way I found to abort the download

                        Log.d(TAG,"업로드를 취소합니다");
                        throw new CancellationException(context.getString(R.string.upload_msg2));
                    }

                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            Log.d(TAG,"INITIATION_STARTED  ========================");

                            builder.setContentText(context.getString(R.string.upload_start)).setProgress((int) fileSize,
                                    (int) uploader.getNumBytesUploaded(), false);
                            notifyManager.notify(UPLOAD_NOTIFICATION_ID, builder.build());

                            break;
                        case INITIATION_COMPLETE:
                            Log.d(TAG,"INITIATION_COMPLETE ========================");
                            builder.setContentText(context.getString(R.string.upload_pre))
                                    .setProgress((int) fileSize, (int) uploader.getNumBytesUploaded(), false)
                            //.setSound(defaultSoundUri)
                            ;
                            notifyManager.notify(UPLOAD_NOTIFICATION_ID, builder.build());
                            break;
                        case MEDIA_IN_PROGRESS:
                            Log.d(TAG,"MEDIA_IN_PROGRESS  ========================" + uploader.getProgress() * 100 + "%");

                            builder.setContentTitle(context.getString(R.string.file_upload_progress_title) + (int) (uploader.getProgress() * 100) + "%")
                                    .setContentText(context.getString(R.string.file_upload_progress_content))
                                    .setProgress((int) fileSize, (int) uploader.getNumBytesUploaded(), false);
                            notifyManager.notify(UPLOAD_NOTIFICATION_ID, builder.build());

                            break;
                        case MEDIA_COMPLETE:
                            Log.d(TAG,"MEDIA_COMPLETE ========================");

                            builder.setContentTitle(context.getString(R.string.upload_complate))
                                    .setContentText(context.getString(R.string.upload_msg1) + feedbackLine.getFilename())
                                    .setProgress(0, 0, false)
                                    .setSound(defaultSoundUri)
                            ;
                            notifyManager.notify(UPLOAD_NOTIFICATION_ID, builder.build());

                        case NOT_STARTED:
                            Log.d(TAG,"NOT_STARTED ========================");
                            break;
                    }
                }
            };

            uploader.setProgressListener(progressListener);
            Video returnedVideo = videoInsert.execute();
            videoId = returnedVideo.getId();

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            Log.e(TAG, "GooglePlayServicesAvailabilityIOException", availabilityException);
            notifyFailedUpload(context,"구글 서비스를 이용할 수 없습니다", notifyManager, builder);

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            Log.i(TAG, String.format("UserRecoverableAuthIOException: %s",
                    userRecoverableException.getMessage()));
            requestAuth(context, userRecoverableException);
        } catch (IOException e) {
            UploadService.uploadExecuteFlag = "fail";
            Log.i(TAG,"업로드 실패 : " + e.getMessage());
            e.printStackTrace();
            notifyFailedUpload(context,"유투브 채널 설정이 필요합니다", notifyManager, builder);

        } catch (CancellationException e){
            Log.e(TAG, "사용자 에러를 발생 시켰습니다");
            return "uploadCancel";
        }
        return videoId;
    }


    //구글 계정에 문제가 있을 예외시
    private static void requestAuth(Context context,
                                    UserRecoverableAuthIOException userRecoverableException) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);

        Intent authIntent = userRecoverableException.getIntent();

        Intent runReqAuthIntent = new Intent(FeedBackWrite.REQUEST_AUTHORIZATION_INTENT);
        runReqAuthIntent.putExtra(FeedBackWrite.REQUEST_AUTHORIZATION_INTENT_PARAM, authIntent);
        manager.sendBroadcast(runReqAuthIntent);
    }

    //업로드 실패시 노티로 알려준다
    private static void notifyFailedUpload(Context context, String message, NotificationManager notifyManager,
                                           NotificationCompat.Builder builder) {
        builder.setContentTitle(context.getString(R.string.upload_error))
                .setContentText(message);
        notifyManager.notify(UPLOAD_NOTIFICATION_ID, builder.build());
    }
}
