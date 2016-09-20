package com.mom.soccer.mission;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.GoogleAuth;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.uploadyutube.UploadService;
import com.mom.soccer.widget.VeteranToast;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ReviewActivity";

    private Activity activity;
    private VideoView mVideoView;
    private Uri mFileUri;
    private MediaController mc;
    private Mission mission;
    private CastRemoteDisplayLocalService.Callbacks mCallbacks;

    private User user;
    private PrefUtil prefUtil;
    private UserMission userMission;

    private UploadBroadcastReceiver broadcastReceiver;

    //유투브 업로드를 사용하기 위한 필요 객체들
    GoogleAccountCredential credential;
    private GoogleApiClient mGoogleApiClient;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = new GsonFactory();

    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    private static final int REQUEST_ACCOUNT_PICKER = 2;
    private static final int REQUEST_AUTHORIZATION = 3;
    private static final int REQUEST_RESOLVE_ERROR = 1001;

    public static final String REQUEST_AUTHORIZATION_INTENT = "com.mom.soccer.RequestAuth";
    public static final String REQUEST_AUTHORIZATION_INTENT_PARAM = "com.mom.soccer.RequestAuth.param";

    public static boolean uploadPossibility = false;
    public boolean uploadFlag = false;
    private String upset = "N";

    //화면의 정보들
    @Bind(R.id.upload_mission_content)
    EditText et_content;

    @Bind(R.id.upload_mission_subject)
    EditText et_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_review_layout);
        ButterKnife.bind(this);
        activity = this;

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //getSupportActionBar().setTitle(getString(R.string.mission_upload_start));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();
        upset = prefUtil.getUploadFlag();

        Log.i(TAG,"업로드 플래그는 : " + upset);

        Intent intent = getIntent();
        mFileUri = intent.getData();
        mission = (Mission) intent.getSerializableExtra(MissionCommon.OBJECT);


        mVideoView = (VideoView) findViewById(R.id.videoView);
        reviewVideo(mFileUri);

        Auth.accountName = user.getGoogleemail();

        GoogleAuth googleAuth = new GoogleAuth(this,credential, Auth.accountName);
        credential = googleAuth.setYutubeCredential();
        googleAPI();

        mGoogleApiClient.connect();

        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(Auth.SCOPES));

        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), YouTubeScopes.all());
        credential.setBackOff(new ExponentialBackOff());

        credential.setSelectedAccountName(Auth.accountName);

        //Plus.AccountApi.getAccountName(mGoogleApiClient);

        new Async(getApplicationContext()).execute();



    }

    @Override
    protected void onStart() {
        super.onStart();

        //네트워크 검사
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobile;
        NetworkInfo wifi;
        mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        if(!wifi.isConnected()) {
            new MaterialDialog.Builder(activity)
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.networkcheck1)
                    .content(R.string.networkcheck2)
                    .contentColor(activity.getResources().getColor(R.color.color6))
                    .positiveText(R.string.mom_diaalog_confirm_y)
                    .negativeText(R.string.mom_diaalog_cancel_n)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            uploadFlag = true;
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            uploadFlag = false;
                        }
                    })
                    .show();
        }else{
            uploadFlag = true;
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

    @OnClick(R.id.btn_upload)
    public void uploadVideo(){
        if(!uploadFlag){
            VeteranToast.makeToast(getApplicationContext(),getResources().getString(R.string.networkcheck3),Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (mFileUri != null) {

            final Intent uploadIntent = new Intent(this, UploadService.class);
            uploadIntent.putExtra(MissionCommon.OBJECT,mission);
            uploadIntent.setData(mFileUri);

            userMission = new UserMission();
            userMission.setSubject(et_subject.getText().toString());
            userMission.setMissionid(mission.getMissionid());
            userMission.setUid(user.getUid());
            userMission.setDescription(et_content.getText().toString());
            userMission.setUploadflag("Y");
            userMission.setPassflag("N");
            userMission.setFilename(getFileName(mFileUri));
            userMission.setGrade(mission.getGrade());

            uploadIntent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMission);
            uploadIntent.putExtra(MissionCommon.USER_OBJECT,user);

            //서버 검증 작업을 한 후에 자료를 업로드 하게 해준다
            UserMissionService service = ServiceGenerator.createService(UserMissionService.class,getApplicationContext(),user);
            Call<ServerResult> call = service.videoValidate(userMission);

            call.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        ServerResult result = response.body();

                        if(result.getResult().equals("F")){
                            VeteranToast.makeToast(getApplicationContext(),getString(R.string.upload_duplicate_validate),Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            VeteranToast.makeToast(getApplicationContext(),getString(R.string.upload_disp),Toast.LENGTH_SHORT).show();
                            startService(uploadIntent);
                            finish();
                        }

                    }else{
                        Log.d(TAG,"오류");
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    t.printStackTrace();
                }
            });

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

    private void reviewVideo(Uri mFileUri) {
        try {
            mVideoView = (VideoView) findViewById(R.id.videoView);
            mc = new MediaController(this);
            mVideoView.setMediaController(mc);
            mVideoView.setVideoURI(mFileUri);
            mc.show();
            mVideoView.start();
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), e.toString());
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }


    ///인증문제.....

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
            broadcastReceiver = new UploadBroadcastReceiver();IntentFilter intentFilter = new IntentFilter(REQUEST_AUTHORIZATION_INTENT);
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private class UploadBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(REQUEST_AUTHORIZATION_INTENT)) {
                Intent toRun = intent.getParcelableExtra(REQUEST_AUTHORIZATION_INTENT_PARAM);
                startActivityForResult(toRun, REQUEST_AUTHORIZATION);
            }
        }
    }

}
