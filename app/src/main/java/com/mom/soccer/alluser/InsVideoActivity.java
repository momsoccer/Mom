package com.mom.soccer.alluser;

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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
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
import com.mom.soccer.alluser.adapter.InsVideoLineAdapter;
import com.mom.soccer.alluser.youtubeupload.VideoUploadService;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.EventBus;
import com.mom.soccer.common.GoogleAuth;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.common.RecyclerItemClickListener;
import com.mom.soccer.common.internal.BusObject;
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.InsVideoVo;
import com.mom.soccer.dto.InsVideoVoLine;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.fragment.YoutubeSeedMissionFragment;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.pubretropit.PubReport;
import com.mom.soccer.retrofitdao.InsVideoService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsVideoActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "InsVideoActivity";

    private Instructor instructor;
    private Intent intent;

    @Bind(R.id.subject)
    EditText subject;

    @Bind(R.id.content)
    EditText content;

    @Bind(R.id.layout_youtubeaddr)
    TextInputLayout layout_youtubeaddr;

    @Bind(R.id.layout_subject)
    TextInputLayout layout_subject;

    @Bind(R.id.youtubeaddr)
    EditText youtubeaddr;

    @Bind(R.id.ins_video_btn)
    Button ins_video_btn;

    @Bind(R.id.upload_video)
    Button upload_video;

    @Bind(R.id.video_check)
    Button video_check;

    @Bind(R.id.li_layout)
    LinearLayout li_layout;

    @Bind(R.id.li_youtube_addr)
    LinearLayout li_youtube_addr;

    @Bind(R.id.li_reply)
    LinearLayout li_reply;

    @Bind(R.id.comment)
    EditText comment;

    @Bind(R.id.sendBtn)
    Button sendBtn;

    @Bind(R.id.replyRecview)
    RecyclerView replyRecview;

    //댓글
    @Bind(R.id.textImageCount)
    TextView textImageCount;

    private int lineCount=0;
    private InsVideoLineAdapter insVideoLineAdapter;

    private String youtubeAdress;
    private String viewtype;
    private InsVideoVo insVideoVo;
    private User user;
    private Activity activity;

    private List<InsVideoVoLine> insVideoVoLines;

    //uplaod
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 3;
    private Uri mFileUri;
    private static final String EXTRA_LOCAL_ONLY = "android.intent.extra.LOCAL_ONLY";

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

    private UploadBroadcastReceiver broadcastReceiver;
    private LinearLayoutManager linearLayoutManager;

    @Bind(R.id.li_uploadview)
    LinearLayout li_uploadview;

    @Bind(R.id.videoView)
    VideoView mVideoView;
    private MediaController mc;

    private boolean uploadFlag = false;
    private boolean copyyoutube = false;
    private int posintion = 0;
    View positiveAction;
    EditText report_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_video);
        ButterKnife.bind(this);

        intent = getIntent();
        instructor = (Instructor) intent.getSerializableExtra(MissionCommon.INS_OBJECT);
        user = (User) intent.getSerializableExtra(MissionCommon.USER_OBJECT);
        viewtype = intent.getExtras().getString("viewtype");

        activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        li_uploadview.setVisibility(View.GONE);
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

        if(viewtype.equals("new")){

            li_layout.setVisibility(View.GONE);
            video_check.setVisibility(View.GONE);
            li_reply.setVisibility(View.GONE);
            youtubeaddr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().length() > 0 ){
                        upload_video.setEnabled(false);
                        video_check.setVisibility(View.VISIBLE);
                    }else{
                        video_check.setVisibility(View.GONE);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }else{

            posintion = intent.getExtras().getInt("position");

            Log.i(TAG,"posintion : " + posintion);

            li_reply.setVisibility(View.VISIBLE);
            comment.requestFocus();

            insVideoVo = (InsVideoVo) intent.getSerializableExtra(MissionCommon.OBJECT);

            lineCount = insVideoVo.getCommentcount();
            textImageCount.setText(getResources().getString(R.string.momboard_comment_msg) + "("+lineCount+")");

            upload_video.setVisibility(View.GONE);
            video_check.setVisibility(View.GONE);
            ins_video_btn.setVisibility(View.GONE);

            subject.setText(insVideoVo.getSubject());
            subject.setEnabled(false);

            if(!Compare.isEmpty(insVideoVo.getContent())){
                content.setText(insVideoVo.getContent());
            }
            content.setEnabled(false);
            li_youtube_addr.setVisibility(View.GONE);

            li_layout.setVisibility(View.VISIBLE);
            YoutubeSeedMissionFragment youtubeFragment = new YoutubeSeedMissionFragment(InsVideoActivity.this,insVideoVo.getYoutubeaddr());
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction tc = fm.beginTransaction();
            tc.add(R.id.youtube_seed_frame_layout,youtubeFragment,"");
            tc.commit();

            comment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    sendBtn.setEnabled(s.toString().trim().length() > 0);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            sendBtn.setEnabled(false);


            replyRecview.addOnItemTouchListener(
                    new RecyclerItemClickListener(getApplicationContext(), replyRecview ,new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            // do whatever
                        }

                        //삭제하기
                        @Override public void onLongItemClick(View view, final int position) {
                            final InsVideoVoLine vo = insVideoLineAdapter.getBoardLine(position);

                            if(user.getUid()==vo.getUid()){
                                new MaterialDialog.Builder(activity)
                                        .content(R.string.board_main_coment_delete)
                                        .contentColor(activity.getResources().getColor(R.color.color6))
                                        .positiveText(R.string.mom_diaalog_confirm)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                insVideoLineAdapter.deleteLine(position,vo.getLineid());
                                                lineCount = lineCount - 1;
                                                textImageCount.setText(getResources().getString(R.string.momboard_comment_msg) + "("+lineCount+")");

                                            }
                                        })
                                        .show();
                            }else{
                                MaterialDialog dialog = new MaterialDialog.Builder(activity)
                                        .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                        .title(R.string.mom_diaalog_board_report)
                                        .titleColor(getResources().getColor(R.color.color6))
                                        .customView(R.layout.dialog_report_view, true)
                                        .positiveText(R.string.momboard_edit_send)
                                        .negativeText(R.string.cancel)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Report report = new Report();
                                                report.setType(PubReport.REPORTTYPE_INS_VIDEO_COMMENT);
                                                report.setUid(user.getUid());
                                                report.setReason(report_content.getText().toString());
                                                report.setContent(vo.getComment());
                                                report.setPublisherid(vo.getLineid());
                                                PubReport.doReport(activity,report,user);
                                            }
                                        })
                                        .build();

                                report_content = (EditText) dialog.findViewById(R.id.report_content);
                                positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

                                report_content.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                                        positiveAction.setEnabled(s.toString().trim().length() > 0);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });

                                dialog.show();
                                positiveAction.setEnabled(false);
                            }

                        }
                    })
            );

        }


    }

    @OnClick(R.id.ins_video_btn)
    public void ins_video_btn(){

        layout_subject.setError(null);
        layout_youtubeaddr.setError(null);

        if(Compare.isEmpty(subject.getText().toString())) {
            layout_subject.setError(getString(R.string.ins_video_subject_empty));
            return;
        }


        //업로드가 아니고 붙여 넣기 일 경우에는 꼭 영상체크를 누르게 한다
       if(!uploadFlag){
            if(Compare.isEmpty(youtubeaddr.getText().toString())) {
                layout_youtubeaddr.setError(getString(R.string.ins_video_youtube_empty));
                return;
            }

            if(!copyyoutube){
                new MaterialDialog.Builder(InsVideoActivity.this)
                        .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                        .title(R.string.mom_diaalog_alert)
                        .titleColor(getResources().getColor(R.color.color6))
                        .content(R.string.ins_video_check_mag)
                        .contentColor(getResources().getColor(R.color.color6))
                        .positiveText(R.string.mom_diaalog_confirm)
                        .show();
                return;
            }

        }

        saveInsVideoContent();

    }

    //youtube upload
    @OnClick(R.id.upload_video)
    public void uploadBtn(){

        changeImage();
    }

    @OnClick(R.id.video_check)
    public void video_check(){

        copyyoutube = true;

        int lastString = youtubeaddr.getText().toString().length();
        youtubeAdress = youtubeaddr.getText().toString().substring(17,lastString);
        li_layout.setVisibility(View.VISIBLE);
        YoutubeSeedMissionFragment youtubeFragment = new YoutubeSeedMissionFragment(InsVideoActivity.this,youtubeAdress);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.youtube_seed_frame_layout,youtubeFragment,"");
        tc.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //network
    public void saveInsVideoContent(){

        InsVideoVo insVideoVo = new InsVideoVo();
        insVideoVo.setInstructorid(instructor.getInstructorid());
        insVideoVo.setSubject(subject.getText().toString());
        insVideoVo.setContent(content.getText().toString());
        insVideoVo.setTeamid(instructor.getTeamid());

        if(!uploadFlag){
            insVideoVo.setYoutubeaddr(youtubeAdress);

            Log.i(TAG,"주소는 " + youtubeAdress);

            InsVideoService insVideoService = ServiceGenerator.createService(InsVideoService.class,getApplicationContext(),instructor);

            WaitingDialog.showWaitingDialog(InsVideoActivity.this,false);
            Call<ServerResult> c = insVideoService.saveVideo(insVideoVo);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        new MaterialDialog.Builder(InsVideoActivity.this)
                                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                .title(R.string.mom_diaalog_alert)
                                .titleColor(getResources().getColor(R.color.color6))
                                .content(R.string.ins_video_save)
                                .contentColor(getResources().getColor(R.color.color6))
                                .positiveText(R.string.mom_diaalog_confirm)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(InsVideoActivity.this, AllUserMainActivity.class);
                                        intent.putExtra(Param.FRAGMENT_COUNT,1);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }else{
            //업로드일 경우는
            String filename = getFileName(mFileUri);
            insVideoVo.setFilename(filename);

            final Intent uploadIntent = new Intent(this, VideoUploadService.class);
            uploadIntent.putExtra(MissionCommon.VIDEO_OBJECT,insVideoVo);
            uploadIntent.putExtra(MissionCommon.INS_OBJECT,instructor);
            uploadIntent.setData(mFileUri);
            startService(uploadIntent);

            new MaterialDialog.Builder(InsVideoActivity.this)
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.mom_diaalog_alert)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(R.string.ins_video_upload_start)
                    .contentColor(getResources().getColor(R.color.color6))
                    .positiveText(R.string.mom_diaalog_confirm)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(InsVideoActivity.this, AllUserMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Param.FRAGMENT_COUNT,1);
                            startActivity(intent);
                        }
                    })
                    .show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!viewtype.equals("new")){
            InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(subject.getWindowToken(), 0);
            mInputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);
            mInputMethodManager.hideSoftInputFromWindow(youtubeaddr.getWindowToken(), 0);

            if(insVideoVo.getCommentcount() > 0){
                getLineList();
            }
        }
    }

    @OnClick(R.id.sendBtn)
    public void sendBtn(){

        InsVideoVoLine insVideoVoLine = new InsVideoVoLine();
        insVideoVoLine.setVideoid(insVideoVo.getVideoid());
        insVideoVoLine.setComment(comment.getText().toString());
        insVideoVoLine.setUid(user.getUid());

        WaitingDialog.showWaitingDialog(activity,false);

        InsVideoService videoService = ServiceGenerator.createService(InsVideoService.class,getApplicationContext(),user);
        Call<ServerResult> c = videoService.saveLine(insVideoVoLine);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    MomSnakBar.show(getWindow().getDecorView().getRootView(),activity,getResources().getString(R.string.ins_video_comment));
                    getLineList();
                    //코멘트 바꿔 주기
                    lineCount = lineCount + 1;
                    textImageCount.setText(getResources().getString(R.string.momboard_comment_msg) + "("+lineCount+")");
                    comment.setText(null);

                    //posintion
                    BusObject busObject = new BusObject();
                    busObject.setPosition(posintion);
                    busObject.setLineCount(lineCount);
                    EventBus.getInstance().post(busObject);
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void getLineList(){
        WaitingDialog.showWaitingDialog(activity,false);
        InsVideoService videoService = ServiceGenerator.createService(InsVideoService.class,getApplicationContext(),user);
        Call<List<InsVideoVoLine>> c = videoService.getLineList(insVideoVo.getVideoid());
        c.enqueue(new Callback<List<InsVideoVoLine>>() {
            @Override
            public void onResponse(Call<List<InsVideoVoLine>> call, Response<List<InsVideoVoLine>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    insVideoVoLines = response.body();

                    replyRecview.setHasFixedSize(true);
                    replyRecview.setLayoutManager(linearLayoutManager);
                    insVideoLineAdapter = new InsVideoLineAdapter(activity,insVideoVoLines,user);

                    replyRecview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    replyRecview.getItemAnimator().setAddDuration(300);
                    replyRecview.getItemAnimator().setRemoveDuration(300);
                    replyRecview.getItemAnimator().setMoveDuration(300);
                    replyRecview.getItemAnimator().setChangeDuration(300);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(insVideoLineAdapter);
                    alphaAdapter.setDuration(500);
                    replyRecview.setAdapter(insVideoLineAdapter);
                    replyRecview.scrollToPosition(insVideoVoLines.size()-1);
                }
            }

            @Override
            public void onFailure(Call<List<InsVideoVoLine>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void changeImage(){
        new MaterialDialog.Builder(activity)
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

    //선택한 사진 업로드 또는 첨부하기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    mFileUri = data.getData();
                    if (mFileUri != null) {
                        li_uploadview.setVisibility(View.VISIBLE);
                        reviewVideo(mFileUri);
                        li_youtube_addr.setVisibility(View.GONE);
                        uploadFlag = true;
                    }
                }
                break;
            case PICK_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    mFileUri = data.getData();
                    if (mFileUri != null) {
                        li_uploadview.setVisibility(View.VISIBLE);
                        reviewVideo(mFileUri);
                        li_youtube_addr.setVisibility(View.GONE);
                        uploadFlag = true;
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

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
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

    private void reviewVideo(Uri mFileUri) {
        try {
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
}
