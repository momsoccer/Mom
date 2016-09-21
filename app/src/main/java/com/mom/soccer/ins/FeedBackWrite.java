package com.mom.soccer.ins;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.GoogleAuth;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.FeedbackLine;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackWrite extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "FeedBackWrite";

    private UploadBroadcastReceiver broadcastReceiver;

    @Bind(R.id.feedback_content)
    EditText feedback_content;

    @Bind(R.id.li_video_layout)
    LinearLayout li_video_layout;

    @Bind(R.id.li_video_title)
    LinearLayout li_video_title;

    @Bind(R.id.feedvideoBtn)
    Button feedvideoBtn;

    @Bind(R.id.title)
    TextView title;

    Intent paramIntent;

    private PrefUtil prefUtil;
    private User user;
    private Instructor instructor;

    //video
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 3;

    private FeedbackHeader feedbackHeader;
    private String videoFlag = "N";
    private Uri mFileUri;
    private static final String EXTRA_LOCAL_ONLY = "android.intent.extra.LOCAL_ONLY";

    private VideoView mVideoView;
    private MediaController mc;

    //업로드 사전 준비
    GoogleAccountCredential credential;
    private GoogleApiClient mGoogleApiClient;

    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    private static final int REQUEST_ACCOUNT_PICKER = 5;
    private static final int REQUEST_AUTHORIZATION = 6;
    private static final int REQUEST_RESOLVE_ERROR = 1001;

    public static final String REQUEST_AUTHORIZATION_INTENT = "com.mom.soccer.RequestAuth";
    public static final String REQUEST_AUTHORIZATION_INTENT_PARAM = "com.mom.soccer.RequestAuth.param";

    public static boolean uploadPossibility = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_feed_back_write_layout);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();
        instructor = prefUtil.getIns();

        Toolbar toolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        paramIntent = getIntent();
        feedbackHeader = (FeedbackHeader) paramIntent.getSerializableExtra(MissionCommon.FEEDBACKHEADER);

        Log.i(TAG,"feedbackHeader : $$$$$ : "+feedbackHeader.toString());

        if(feedbackHeader.getFeedbacktype().equals("video")){
            title.setText(getString(R.string.feedback_write_title_video));
            li_video_layout.setVisibility(View.VISIBLE);
            li_video_title.setVisibility(View.VISIBLE);
            feedvideoBtn.setVisibility(View.VISIBLE);
        }else{
            title.setText(getString(R.string.feedback_write_title_word));
            li_video_layout.setVisibility(View.GONE);
            li_video_title.setVisibility(View.GONE);
            feedvideoBtn.setVisibility(View.GONE);
        }
        mVideoView = (VideoView) findViewById(R.id.videoView);

        //preset upload
        Auth.accountName = instructor.getEmail();
        GoogleAuth googleAuth = new GoogleAuth(this,credential, Auth.accountName);
        credential = googleAuth.setYutubeCredential();

        googleAPI();

        mGoogleApiClient.connect();

        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(Auth.SCOPES));

        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), YouTubeScopes.all());
        credential.setBackOff(new ExponentialBackOff());

        credential.setSelectedAccountName(Auth.accountName);

        new Async(getApplicationContext()).execute();

    }

    //비디오 및 영상 어태치
    public void videoAttach(View view){
        changeImage();
    }

    @OnClick(R.id.feedBtn)
    public void feedWrite(){
        if(Compare.isEmpty(feedback_content.getText().toString())){
            new MaterialDialog.Builder(FeedBackWrite.this)
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.mom_diaalog_alert)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(R.string.feedback_write_msg1)
                    .contentColor(getResources().getColor(R.color.color6))
                    .positiveText(R.string.mom_diaalog_confirm)
                    .show();
            return;
        }

        if(Compare.isEmpty(feedback_content.getText().length()<=11)){
            new MaterialDialog.Builder(FeedBackWrite.this)
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.mom_diaalog_alert)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(R.string.feedback_write_msg2)
                    .contentColor(getResources().getColor(R.color.color6))
                    .positiveText(R.string.mom_diaalog_confirm)
                    .show();
            return;
        }

        if(feedbackHeader.getFeedbacktype().equals("video")){
            if(videoFlag.equals("N")){
                new MaterialDialog.Builder(FeedBackWrite.this)
                        .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                        .title(R.string.mom_diaalog_alert)
                        .titleColor(getResources().getColor(R.color.color6))
                        .content(R.string.feedback_write_msg3)
                        .contentColor(getResources().getColor(R.color.color6))
                        .positiveText(R.string.mom_diaalog_confirm)
                        .show();
                return;
            }else{
                if (mFileUri != null) {
                    WaitingDialog.showWaitingDialog(this,false);
                    String filename = getFileName(mFileUri);
                    DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),instructor);
                    Call<ServerResult> c = dataService.getInsFileCount(instructor.getUid(),instructor.getInstructorid(),filename);
                    c.enqueue(new Callback<ServerResult>() {
                        @Override
                        public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                            WaitingDialog.cancelWaitingDialog();
                            if(response.isSuccessful()){
                                ServerResult result = response.body();
                                if(result.getCount()==0){

                                    new MaterialDialog.Builder(FeedBackWrite.this)
                                            .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                            .title(R.string.mom_diaalog_alert)
                                            .titleColor(getResources().getColor(R.color.color6))
                                            .content(R.string.ins_feedback_upload)
                                            .contentColor(getResources().getColor(R.color.color6))
                                            .positiveText(R.string.mom_diaalog_confirm)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    FeedbackLine feedbackLine = new FeedbackLine();
                                                    feedbackLine.setFeedbackid(feedbackHeader.getFeedbackid());
                                                    feedbackLine.setType("ins");
                                                    feedbackLine.setContent(feedback_content.getText().toString());
                                                    feedbackLine.setSubject(feedbackHeader.getSubject());
                                                    feedbackLine.setUsername(feedbackHeader.getUsername());
                                                    feedbackLine.setFilename(getFileName(mFileUri));
                                                    feedbackLine.setMissionname(feedbackHeader.getMissionname());
                                                    feedbackLine.setDescription(feedbackHeader.getDescription());
                                                    feedbackLine.setProfileimgurl(feedbackHeader.getProfileimgurl());
                                                    final Intent uploadIntent = new Intent(FeedBackWrite.this, UploadFeedService.class);
                                                    uploadIntent.putExtra(MissionCommon.FEEDBACKHEADER,feedbackLine);
                                                    uploadIntent.putExtra(MissionCommon.INS_OBJECT,instructor);
                                                    uploadIntent.setData(mFileUri);
                                                    startService(uploadIntent);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }else{
                                    new MaterialDialog.Builder(FeedBackWrite.this)
                                            .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                            .title(R.string.mom_diaalog_alert)
                                            .titleColor(getResources().getColor(R.color.color6))
                                            .content(R.string.ins_feedback_upload_duplicate)
                                            .contentColor(getResources().getColor(R.color.color6))
                                            .positiveText(R.string.mom_diaalog_confirm)
                                            .show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResult> call, Throwable t) {
                            WaitingDialog.cancelWaitingDialog();
                            t.printStackTrace();
                        }
                    });


                }
            }
        }else {
            //디비 실행 및 전 화면으로 이동
            WaitingDialog.showWaitingDialog(FeedBackWrite.this, false);
            FeedbackLine line = new FeedbackLine();

            line.setFeedbackid(feedbackHeader.getFeedbackid());
            line.setType("ins");
            line.setContent(feedback_content.getText().toString());

            FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class, getApplicationContext(), user);
            Call<ServerResult> c = feedBackService.saveLine(line);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if (response.isSuccessful()) {
                        ServerResult result = response.body();
                        if (result.getCount() == 1) {
                            //강사 화면으로 다시 이동해줍니다.
                            Intent intent = new Intent(FeedBackWrite.this, InsDashboardActivity.class);
                            intent.putExtra(Param.FRAGMENT_COUNT, 0);
                            //finish();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //홈버튼클릭시
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeImage(){
        new MaterialDialog.Builder(FeedBackWrite.this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(R.string.ins_feedback_msg1)
                .titleColor(getResources().getColor(R.color.color6))
                .content(R.string.ins_feedback_msg2)
                .contentColor(getResources().getColor(R.color.color6))
                .positiveText(R.string.ins_feedback_msg3)
                .neutralText(R.string.ins_feedback_msg4)
                .negativeText(R.string.mom_diaalog_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakeAlbumAction();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakePhotoAction();
                    }
                })
                .show();
    }

    //앨범에서 이미지 가져오기
    public void doTakeAlbumAction(){
        //앨범호출
        Intent intent = new Intent(Intent.ACTION_PICK, null).setType("video/*");
        intent.putExtra(EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    //카메라에서 영상촬영
    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

        try {
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PICK_FROM_CAMERA);

        } catch (ActivityNotFoundException e) {

        }
    }

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    //선택한 사진 업로드 또는 첨부하기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    mFileUri = data.getData();
                    if (mFileUri != null) {
                        reviewVideo(mFileUri);
                    }
                }
                break;
            case PICK_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    mFileUri = data.getData();
                    if (mFileUri != null) {
                        reviewVideo(mFileUri);
                    }
                }
                break;
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG,"Google Play Services User chose......");
                    mGoogleApiClient.connect();
                } else {
                    VeteranToast.makeToast(getApplicationContext(),"업로드를 하시려면 구글 계정이 필요합니다", Toast.LENGTH_LONG).show();
                }
                break;

            case REQUEST_AUTHORIZATION:
                //재인증 필요
                if (resultCode != Activity.RESULT_OK) {
                    chooseAccount();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    Log.d(TAG,"유저가 OAuth 동의를 했습니다");
                    Auth.accountName = user.getGoogleemail();
                    Log.d(TAG,"인증 유저 아이디는 : " + Auth.accountName);
                    uploadPossibility=true;
                }else{
                    Log.d(TAG,"유저가 OAuth 동의를 하지 않았습니다");
                    uploadPossibility=false;
                }
                break;
        }
    }

    private void reviewVideo(Uri mFileUri) {
        try {
            videoFlag = "Y";
            mc = new MediaController(this);
            mVideoView.setMediaController(mc);
            mVideoView.setVideoURI(mFileUri);
            mc.show();
            mVideoView.start();
        } catch (Exception e) {
            Log.i(TAG,"================== Video Error =====================" + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getFileName(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void googleAPI(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .setAccountName(Auth.accountName)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "구글 플레이 연결이 되었습니다.");

        if (!mGoogleApiClient.isConnected() || Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null) {
            Log.d(TAG, "onConnected 연결 실패");
        } else {
            Log.d(TAG, "onConnected 연결 성공");
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if (currentPerson.hasImage()) {

                Log.d(TAG, "이미지 경로는 : " + currentPerson.getImage().getUrl());

            }
            if (currentPerson.hasDisplayName()) {
                Log.d(TAG,"디스플레이 이름 : "+ currentPerson.getDisplayName());
                Log.d(TAG, "디스플레이 아이디는 : " + currentPerson.getId());
            }

        }
    }

    class Async extends AsyncTask<Void, Void, Void> {

        Context credential = null;

        public Async(Context credential) {
            this.credential = credential;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG,"구글 토큰 가져오기가 실행이 되었습니다");
            getAccessToken(credential);
            return null;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "구글 계정 Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "연결 에러 " + connectionResult);
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
        if (connectionResult.hasResolution()) {

            Log.e(TAG,
                    String.format(
                            "Connection to Play Services Failed, error: %d, reason: %s",
                            connectionResult.getErrorCode(),
                            connectionResult.toString()));
            try {

                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);

            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
                Log.i(TAG,"구글 에러로 다시 접속을 한다");
                Log.e(TAG, e.toString(), e);
            }
        }else{
            Log.d(TAG,"이미 로그인 중입니다");
        }
    }

    //구글 토큰값 확인해보기
    public void getAccessToken(Context mContext) {

        try {
            String token = credential.getToken();
            Log.d(TAG, "구글 토큰값은 : " + token);
            uploadPossibility = true;

            //토큰을 저장해놓는다.
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor pre = sp.edit();
            pre.putString("googletoken", token);
            pre.commit();

        } catch (UserRecoverableAuthException e) {
            Log.d(TAG, "업로드가 가능하지 않습니다. 토큰이 없습니다");
            startActivityForResult(e.getIntent(), REQUEST_ACCOUNT_PICKER);
            uploadPossibility = false;
        }catch (IllegalArgumentException e){
            Log.d(TAG, "IllegalArgumentException 에러 발생......");
            Auth.accountName = user.getGoogleemail();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();

        if (broadcastReceiver == null)
            broadcastReceiver = new UploadBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(
                REQUEST_AUTHORIZATION_INTENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, intentFilter);
    }

    private class UploadBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(REQUEST_AUTHORIZATION_INTENT)) {
                Log.d(TAG, "Request auth received - executing the intent");
                Intent toRun = intent
                        .getParcelableExtra(REQUEST_AUTHORIZATION_INTENT_PARAM);
                startActivityForResult(toRun, REQUEST_AUTHORIZATION);
            }
        }
    }

}
