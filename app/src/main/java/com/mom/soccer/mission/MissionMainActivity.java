package com.mom.soccer.mission;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.adapter.FeedBackAllListAdapter;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.adapter.PassListAdapter;
import com.mom.soccer.besideactivity.FavoriteMissionActivity;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.ExpandableHeightGridView;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.InsInfoVo;
import com.mom.soccer.dto.FavoriteMission;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.SpBalanceHeader;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.fragment.YoutubeSeedMissionFragment;
import com.mom.soccer.ins.FeedBackInsListActivity;
import com.mom.soccer.point.PointMainActivity;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.FavoriteMissionService;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retrofitdao.MissionPassService;
import com.mom.soccer.retrofitdao.PointService;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionMainActivity extends AppCompatActivity {


    private static final String TAG = "MissionMainActivity";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final int REQUEST_FEED_BACK_CODE = 201;
    private static final int REQUEST_USERMISSION_PASS_CODE = 301;

    private int seedMissionId = 0;

    private User user = new User();
    private PrefUtil prefUtil;
    private YouTubeThumbnailView thumbnailView;
    int favoriteCount = 0;

    private Mission mission = new Mission();

    private Boolean setNet = false;

    @Bind(R.id.main_mission_rlayout)
    RelativeLayout relativeLayout;

    @Bind(R.id.main_mission_start)
    ImageButton btnStart;

    @Bind(R.id.tx_main_mission_level)
    TextView tx_level;

    @Bind(R.id.tx_main_mission_name)
    TextView tx_missionName;

    @Bind(R.id.tx_main_mission_disp)
    TextView tx_missionDisp;

    @Bind(R.id.tx_main_mission_precon)
    TextView tx_missionPreCondition;

    @Bind(R.id.tx_main_mission_potin)
    TextView tx_missionPoint;

    @Bind(R.id.img_missiontab)
    ImageView img_missiontab;

    @Bind(R.id.li_mymission)
    LinearLayout li_mymission;

    @Bind(R.id.tx_main_usermission)
    TextView tx_main_usermission;

    @Bind(R.id.view_l1)
    View view_l1;
    @Bind(R.id.view_l2)
    View view_l2;

    @Bind(R.id.usermission_iv_hart)
    ImageView usermission_iv_hart;

    @Bind(R.id.usermission_tx_hart)
    TextView usermission_tx_hart;

    @Bind(R.id.usermission_iv_comment)
    ImageView usermission_iv_comment;

    @Bind(R.id.usermission_tx_comment)
    TextView usermission_tx_comment;

    @Bind(R.id.btn_mission_upload)
    Button btn_mission_upload;

    @Bind(R.id.im_clear_marck)
    ImageView im_clear_marck;

    ExpandableHeightGridView videoGridView;
    UserMission userMission;

    @Bind(R.id.li_video_list)
    LinearLayout li_video_list;


    @Bind(R.id.li_feedback_history)
    LinearLayout li_feedback_history;

    @Bind(R.id.btnReqEval)
    Button btnReqEval;

    private InsInfoVo insInfoVo;
    private InsInfoVo feedbackInfo;
    private View positiveAction;
    /**************************************************
     * google uplaod define
     **********************/
    private Uri mFileURI = null;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 3;
    private static final String EXTRA_LOCAL_ONLY = "android.intent.extra.LOCAL_ONLY";

    private GridMissionAdapter gridMissionAdapter;

    List<UserMission> userMissionList;
    UserMission qUserMission = new UserMission();
    Intent intent;
    private static int UPLOAD_NOTIFICATION_ID = 1001;

    private TextInputLayout layout_Content;
    private EditText        feedback_content;
    private RadioGroup      radioGroup;
    private RadioButton feed_video, feed_word;
    private TextView feed_video_point,feed_word_point,text_mypoint;
    private CheckBox pub_status;
    private String actName = "NOL";

    String viewTitle = null;
    int videoPoint = 0;
    int wordPoint  = 0;

    private FeedbackHeader feedbackHeader;
    String pubStauts = "Y";

    private static int USER_TEAM_ID = 0;
    private int myPoint = 0;

    /*feedback*/
    private List<FeedbackHeader> feedbackHeaders;
    @Bind(R.id.feedbackRecylerView)
    RecyclerView feedbackRecylerView;
    FeedBackAllListAdapter feedBackAllListAdapter;

    Activity activity;

    //mitsstion pass function
    private String passType;
    RecyclerView passrecyclerview;
    PassListAdapter passListAdapter;
    List<MissionPass> missionPasses = new ArrayList<>();
    private String missionPassFlag;

    @Bind(R.id.li_pass_history)
    LinearLayout li_pass_history;

    private String missionType;

    // 1. No Data Found
    @Bind(R.id.li_mymission_back)
    LinearLayout li_mymission_back;

    @Bind(R.id.li_mymission_no_data_back)
    LinearLayout li_mymission_no_data_back;

    // 2. pass mission
    @Bind(R.id.li_mission_no_pass)
    LinearLayout li_mission_no_pass;

    // 3. feed back
    @Bind(R.id.li_feedback_no_data_back)
    LinearLayout li_feedback_no_data_back;

    // 4.another vidio data
    @Bind(R.id.li_another_no_data_back)
    LinearLayout li_another_no_data_back;

/*    @Bind(R.id.scaleUp)
    ImageButton scaleUp;*/

    @OnClick(R.id.btn_shre)
    public void btn_shre(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, user.getUsername() +" : "+userMission.getSubject());
        intent.putExtra(Intent.EXTRA_TEXT,"https://youtu.be/"+userMission.getYoutubeaddr());
        intent.putExtra(Intent.EXTRA_TITLE, userMission.getDescription());
        startActivity(Intent.createChooser(intent, "Mom Soccer"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_mission_main_layout);
        ButterKnife.bind(this);
        activity = this;
        Log.d(TAG,"onCreate() =====================================");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        intent = getIntent();
        mission = (Mission) intent.getSerializableExtra(MissionCommon.OBJECT);
        missionType = intent.getExtras().getString(MissionCommon.MISSIONTYPE);
        actName = intent.getExtras().getString("actName");

        Log.i(TAG,"미션 타입은 :: " + missionType);

        thumbnailView = (YouTubeThumbnailView) findViewById(R.id.youtybe_Thumbnail);

        if (mission.getYoutubeaddr()==null){
            setNet = true;
        }

        //미션 타입에 따라 백그라운드를 다르게 해준다
        if(mission.getTypename().equals("DRIBLE")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.drible_back));
        }else if(mission.getTypename().equals("LIFTING")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.lifting_back));
        }else if(mission.getTypename().equals("TRAPING")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.trappring_back));
        }else if(mission.getTypename().equals("AROUND")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.around_back));
        }else if(mission.getTypename().equals("FLICK")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.flick_back));
        }else if(mission.getTypename().equals("COMPLEX")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.complex_back));
        }

        tx_level.setText(String.valueOf("Lv." + mission.getSequence()));
        tx_missionName.setText(mission.getMissionname());
        tx_missionDisp.setText(mission.getDescription());
        tx_missionPreCondition.setText(mission.getPrecon());

        String advance = getString(R.string.point_upload)+" "+mission.getGrade()+getString(R.string.point_get)+", "+
                getString(R.string.point_pre_word)+" "+mission.getPassgrade()+getString(R.string.point_get);

        tx_missionPoint.setText(advance);

        //LocalBroadcastManager.getInstance(this).registerReceiver(uploadReceiver, new IntentFilter("uploadReceiver"));

        videoGridView = (ExpandableHeightGridView) findViewById(R.id.main_mission_gridview);
        videoGridView.setExpanded(true);


        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent userMissionListIntent = new Intent(getApplicationContext(),UserMissionActivity.class);
                userMissionListIntent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMissionList.get(position));
                startActivity(userMissionListIntent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent = new Intent(getApplicationContext(),UserMissionActivity.class);
                miIntent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMission);
                startActivity(miIntent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });


        favoriteTransaction(user.getUid(),mission.getMissionid(),"getCount",btnStart);
        //youTubeView.initialize(Auth.KEY,MissionMainActivity.this);

        //업로드후 노티를 지워준다
        NotificationManager nm =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(UPLOAD_NOTIFICATION_ID);
        passrecyclerview = (RecyclerView)findViewById(R.id.passrecyclerview);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG,"onResume() =====================================");
        intent = getIntent();
        String uploadflag = intent.getStringExtra("uploadflag");


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() =====================================");


        //나의 미션 영상
        getMyVideo();
        //조회 조건 <나를 제외한 같은 미션 영상을 업로드한 리스트>
        qUserMission.setMissionid(mission.getMissionid());
        qUserMission.setUid(user.getUid());
        userGrid_MissionList(qUserMission);

        //YoutubeFragment 유투브 플래그먼트
        YoutubeSeedMissionFragment youtubeFragment = new YoutubeSeedMissionFragment(this,mission.getYoutubeaddr());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.youtube_seed_frame_layout,youtubeFragment,"");
        tc.commit();

        WaitingDialog.showWaitingDialog(this,false);
        UserService service = ServiceGenerator.createService(UserService.class,getApplicationContext(),user);
        Call<User> userCall = service.getProfileUser(user.getUid());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    WaitingDialog.cancelWaitingDialog();
                    User user = response.body();
                    USER_TEAM_ID = user.getTeamid();

                    getInsInfo();

                }else {
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
            }
        });

        WaitingDialog.showWaitingDialog(MissionMainActivity.this,false);

        //현재 내포인트
        PointService pointService = ServiceGenerator.createService(PointService.class,getApplicationContext(),user);
        Call<SpBalanceHeader> c = pointService.getSelfAmt(user.getUid());
        c.enqueue(new Callback<SpBalanceHeader>() {
            @Override
            public void onResponse(Call<SpBalanceHeader> call, Response<SpBalanceHeader> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    SpBalanceHeader spBalanceHeader = response.body();
                    myPoint = spBalanceHeader.getAmount();
                }else{
                }
            }

            @Override
            public void onFailure(Call<SpBalanceHeader> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });

        /*//initial youtube screen
        if(Common.YOUTUBESCREEN_STATUS.equals("LANDSCAPE")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
    }

    //다른 사람들의 영상 목록
    public void userGrid_MissionList(final UserMission userMission){

        WaitingDialog.showWaitingDialog(MissionMainActivity.this,false);
        userMission.setQueryRow(20);

        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,getApplicationContext(),user);
        Call<List<UserMission>> call = userMissionService.uMissionList(userMission);

        call.enqueue(new Callback<List<UserMission>>() {
            @Override
            public void onResponse(Call<List<UserMission>> call, Response<List<UserMission>> response) {

                if(response.isSuccessful()){
                    WaitingDialog.cancelWaitingDialog();
                    userMissionList = response.body();
                    gridMissionAdapter = new GridMissionAdapter(getApplicationContext(),R.layout.adapter_book_mark_layout,userMissionList,"YOU");
                    videoGridView.setAdapter(gridMissionAdapter);

                    if(userMissionList.size()==0){
                        li_another_no_data_back.setVisibility(View.VISIBLE);
                    }else{
                        li_another_no_data_back.setVisibility(View.GONE);
                    }

                }else{
                    WaitingDialog.cancelWaitingDialog();

                }
            }
            @Override
            public void onFailure(Call<List<UserMission>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    //내가 수행한 미션
    public void getMyVideo(){
        WaitingDialog.showWaitingDialog(MissionMainActivity.this,false);
        UserMission u = new UserMission();
        u.setMissionid(mission.getMissionid());
        u.setUid(user.getUid());
        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,this,user);
        Call<UserMission> c = userMissionService.getUserMission(u);

        c.enqueue(new Callback<UserMission>() {
            @Override
            public void onResponse(Call<UserMission> call, Response<UserMission> response) {
                if(response.isSuccessful()){
                    WaitingDialog.cancelWaitingDialog();
                    userMission = response.body();

                    getFeedBack(userMission);
                    passHistory();


                    usermission_tx_hart.setText(String.valueOf(userMission.getBookmarkcount()));
                    usermission_tx_comment.setText(String.valueOf(userMission.getBoardcount()));

                    if(userMission.getUid()==0){
                        img_missiontab.setVisibility(View.GONE);
                        li_mymission_back.setVisibility(View.GONE);
                        li_mymission_no_data_back.setVisibility(View.VISIBLE);
                    }else{
                        li_mymission_back.setVisibility(View.VISIBLE);
                        li_mymission_no_data_back.setVisibility(View.GONE);

                        if(userMission.getPassflag().equals("Y")){
                            img_missiontab.setVisibility(View.VISIBLE);
                            view_l1.setVisibility(View.GONE);
                            view_l2.setVisibility(View.GONE);
                            im_clear_marck.setVisibility(View.VISIBLE);
                            btnReqEval.setText(R.string.user_mission_y);
                            missionPassFlag = "Y";
                        }else if(userMission.getPassflag().equals("P")){

                            img_missiontab.setVisibility(View.GONE);
                            tx_main_usermission.setText(R.string.user_mission_p);
                            btn_mission_upload.setText(R.string.user_mission_p);

                            btn_mission_upload.setBackgroundResource(R.color.color8);
                            im_clear_marck.setVisibility(View.GONE);

                            btnReqEval.setText(R.string.user_mission_p);
                            missionPassFlag = "P";

                        }else if(userMission.getPassflag().equals("N")){
                            img_missiontab.setVisibility(View.GONE);
                            tx_main_usermission.setText(R.string.user_mission_n);
                            im_clear_marck.setVisibility(View.GONE);
                            btnReqEval.setText(R.string.mission_eval_btn);
                            missionPassFlag = "N";
                        }



                        //내가 좋아요를 했다면
                        if(userMission.getMycheck()==0){
                            usermission_iv_hart.setImageResource(R.drawable.ic_white_hart);
                        }else{
                            usermission_iv_hart.setImageResource(R.drawable.ic_hart_red);
                        }


                        thumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                                youTubeThumbnailLoader.setVideo(userMission.getYoutubeaddr());
                            }

                            @Override
                            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });
                    }


                }else{
                    WaitingDialog.cancelWaitingDialog();

                }
            }

            @Override
            public void onFailure(Call<UserMission> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });

    }


    //업로드
    @OnClick(R.id.btn_mission_upload)
    public void missionVideoUpload(){

        if(userMission.getUid()!=0){

            if(userMission.getPassflag().equals("P")) {
                //VeteranToast.makeToast(getApplicationContext(), getString(R.string.user_mission_progress), Toast.LENGTH_SHORT).show();

                return;
            }
        }
        changeImage();

    }

    public void changeImage(){
        new MaterialDialog.Builder(MissionMainActivity.this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title("영상선택")
                .titleColor(getResources().getColor(R.color.color6))
                .content("영상 업로드 유의 사항")
                .contentColor(getResources().getColor(R.color.color6))
                .positiveText("영상 갤러리")
                .neutralText("촬영하기")
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

        /*
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, RESULT_PICK_IMAGE_CROP);
        */
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

    //즐겨찾기 컨트롤
    public void favoriteTransaction(int uId, int missionId, String typeMethod, final ImageButton imageButton){

        FavoriteMissionService service = ServiceGenerator.createService(FavoriteMissionService.class,this,user);
        FavoriteMission favoriteMission = new FavoriteMission();
        favoriteMission.setUid(uId);
        favoriteMission.setMissionid(missionId);

        if(typeMethod.equals("getCount")){
            Call<ServerResult> callBack = service.getCountFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        favoriteCount = result.getCount();

                        if(favoriteCount != 0){
                            imageButton.setImageResource(R.drawable.star);
                        }else{
                            imageButton.setImageResource(R.drawable.star_enabled);
                        }

                    }else{
                        //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(1) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(2) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }else if(typeMethod.equals("create")){

            Call<ServerResult> callBack = service.saveFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        VeteranToast.makeToast(getApplicationContext(),"즐겨찾기에 추가했습니다", Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star);
                        favoriteCount = 1;
                    }else{
                        //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(3) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star_enabled);
                        favoriteCount = 0;
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(4) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }else if(typeMethod.equals("delete")){

            Call<ServerResult> callBack = service.deleteFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        VeteranToast.makeToast(getApplicationContext(),"즐겨찾기에서 제외 했습니다", Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star_enabled);
                        favoriteCount = 0;
                    }else{
                        //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(3) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star);
                        favoriteCount = 1;
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(6) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }
    }

    @OnClick(R.id.main_mission_start)
    public void btnStart(){
        if(favoriteCount==0){
            favoriteTransaction(user.getUid(),mission.getMissionid(),"create",btnStart);
            favoriteCount=1;
        }else{
            favoriteTransaction(user.getUid(),mission.getMissionid(),"delete",btnStart);
            favoriteCount=0;
        }
    }

    @OnClick(R.id.btnMainVideo)
    public void userVideoList(){
        Intent intent = new Intent(this,UserMissionListActivity.class);
        startActivity(intent);
    }


    public void reqBtnClick(View v){

        String[] strings;
        String[] passStrings;

        if(USER_TEAM_ID==0){
            strings = new String[]{
                    getString(R.string.user_mission_team_feedback_another_request)
            };
        }else{
            strings = new String[]{getString(R.string.user_mission_team_feedback_request),
                    getString(R.string.user_mission_team_feedback_another_request)
            };
        }

        //
        if(USER_TEAM_ID==0){
            passStrings = new String[]{
                    getString(R.string.user_mission_team_feedback_another_request) //0
                    //getString(R.string.user_mission_team_feedback_mom_request)  //1
            };
        }else{
            passStrings = new String[]{getString(R.string.user_mission_team_feedback_request), //0
                    getString(R.string.user_mission_team_feedback_another_request) //1
                    //getString(R.string.user_mission_team_feedback_mom_request) //2
            };
        }

        switch (v.getId()){
            case R.id.btnReqfeed:
                new MaterialDialog.Builder(this)
                        .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                        .title(R.string.mom_diaalog_feedback_title)
                        .titleColor(getResources().getColor(R.color.color6))
                        .items(strings)
                        .itemsColor(getResources().getColor(R.color.color6))
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() //첫번째 인수는 기본 선택
                        {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                if(USER_TEAM_ID==0){
                                    Intent intent = new Intent(MissionMainActivity.this,FeedBackInsListActivity.class);
                                    intent.putExtra(MissionCommon.MISSIONTYPE,missionType);
                                    startActivityForResult(intent,REQUEST_FEED_BACK_CODE);
                                }else{
                                    if(which==0){
                                        popUpFeedBack("myins");
                                    }else if(which==1){
                                        Intent intent = new Intent(MissionMainActivity.this,FeedBackInsListActivity.class);
                                        intent.putExtra(MissionCommon.MISSIONTYPE,missionType);
                                        startActivityForResult(intent,REQUEST_FEED_BACK_CODE);
                                    }
                                }

                                return true;
                            }
                        })
                        .positiveText(R.string.mom_diaalog_confirm)
                        .negativeText(R.string.mom_diaalog_cancel)
                        .widgetColor(getResources().getColor(R.color.enabled_red))
                        .show();
                break;
            case R.id.btnReqEval:

                if(userMission.getPassflag().equals("Y")){
                    new MaterialDialog.Builder(this)
                            .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                            .title(R.string.mom_diaalog_alert)
                            .content(R.string.user_mission_pass_msg)
                            .positiveText(R.string.mom_diaalog_confirm)
                            .widgetColor(getResources().getColor(R.color.enabled_red))
                            .show();
                }else if(userMission.getPassflag().equals("P")){
                    new MaterialDialog.Builder(this)
                            .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                            .title(R.string.mom_diaalog_pass_title_ing)
                            .content(R.string.user_mission_progress_msg)
                            .positiveText(R.string.mom_diaalog_confirm)
                            .widgetColor(getResources().getColor(R.color.enabled_red))
                            .show();
                }else if(userMission.getPassflag().equals("N")){
                    new MaterialDialog.Builder(this)
                            .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                            .title(R.string.mom_diaalog_pass_title)
                            .titleColor(getResources().getColor(R.color.color6))
                            .items(passStrings)
                            .itemsColor(getResources().getColor(R.color.color6))
                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() //첫번째 인수는 기본 선택
                            {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                    if(USER_TEAM_ID==0){
                                            Intent mintent = new Intent(MissionMainActivity.this,FeedBackInsListActivity.class);
                                            mintent.putExtra(MissionCommon.MISSIONTYPE,missionType);
                                            startActivityForResult(mintent,REQUEST_USERMISSION_PASS_CODE);
                                    }else{
                                        if(which==0){  //자기 팀 강사
                                            missionPass("myteam","save",feedbackInfo);
                                        }else if(which==1){  //다른팀 강사
                                            Intent mintent = new Intent(MissionMainActivity.this,FeedBackInsListActivity.class);
                                            mintent.putExtra(MissionCommon.MISSIONTYPE,missionType);
                                            startActivityForResult(mintent,REQUEST_USERMISSION_PASS_CODE);
                                        }
                                    }

                                    return true;
                                }
                            })
                            .positiveText(R.string.mom_diaalog_confirm)
                            .negativeText(R.string.mom_diaalog_cancel)
                            .widgetColor(getResources().getColor(R.color.enabled_red))
                            .show();
                    break;

                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    mFileURI = data.getData();
                    if (mFileURI != null) {
                        Intent intent = new Intent(this, ReviewActivity.class);
                        intent.setData(mFileURI);
                        intent.putExtra(MissionCommon.OBJECT,mission);
                        startActivity(intent);
                    }
                }
                break;
            case PICK_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    mFileURI = data.getData();
                    if (mFileURI != null) {
                        Intent intent = new Intent(this, ReviewActivity.class);
                        intent.setData(mFileURI);
                        intent.putExtra(MissionCommon.OBJECT,mission);
                        startActivity(intent);
                    }
                }
                break;
            case REQUEST_FEED_BACK_CODE:
                if (resultCode == RESULT_OK) {
                    feedbackInfo = (InsInfoVo) data.getSerializableExtra(MissionCommon.INS_OBJECT);
                    missionType = data.getExtras().getString(MissionCommon.MISSIONTYPE);
                    Log.i(TAG,"**********************************");
                    Log.i(TAG,"받은 미션 타입1 " + missionType);
                    popUpFeedBack("pubins");
                }
                break;
            case REQUEST_USERMISSION_PASS_CODE:
                if(resultCode == RESULT_OK){
                    feedbackInfo = (InsInfoVo) data.getSerializableExtra(MissionCommon.INS_OBJECT);
                    missionType = data.getExtras().getString(MissionCommon.MISSIONTYPE);
                    Log.i(TAG,"**********************************");
                    Log.i(TAG,"받은 미션 타입2 " + missionType);

                    String question = getString(R.string.mom_diaalog_pass_apply_msg2)+feedbackInfo.getPubpasspoint()+"P"
                            +"\n"+getString(R.string.mom_diaalog_pass_apply_msg3);

                    new MaterialDialog.Builder(this)
                            .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                            .title(R.string.mom_diaalog_pass_apply_title)
                            .titleColor(getResources().getColor(R.color.color6))
                            .content(question)
                            .positiveText(R.string.mom_diaalog_confirm)
                            .negativeText(R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    missionPass("another","save",feedbackInfo);
                                }
                            })
                            .show();

                }
        }
    }

    public void missionPass(String passType,String trtype,InsInfoVo insinfor){

        MissionPass query = new MissionPass();
        query.setUid(user.getUid());
        query.setMissionid(userMission.getMissionid());
        query.setUsermissionid(userMission.getUsermissionid());
        query.setStatus("REQUEST");

        if(passType.equals("another")){
            query.setEvaltype("N");
            query.setInstructorid(insinfor.getInstructorid());
        }else{
            query.setEvaltype("Y");
            query.setInstructorid(insInfoVo.getInstructorid());
        }

        if(trtype.equals("save")){

            WaitingDialog.showWaitingDialog(MissionMainActivity.this,false);

            MissionPassService service = ServiceGenerator.createService(MissionPassService.class,getApplicationContext(),user);

            Call<ServerResult> c = service.saveUserMissionPass(query);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){

                        ServerResult result = response.body();
                        if(result.getCount()==0){
                            new MaterialDialog.Builder(MissionMainActivity.this)
                                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                    .title(R.string.feedback_lack)
                                    .titleColor(getResources().getColor(R.color.color6))
                                    .content(R.string.feedback_lack_msg)
                                    .contentColor(getResources().getColor(R.color.color6))
                                    .positiveText(R.string.mom_diaalog_confirm)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Intent intent = new Intent(MissionMainActivity.this, PointMainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .negativeText(R.string.mom_diaalog_cancel)
                                    .show();
                            return;
                        }else{
                            applyMissionPassMessage("S");
                        }

                    }else{
                        applyMissionPassMessage("N");
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                }
            });
        }
    }

    public void applyMissionPassMessage(String type){
        if(type.equals("S")){

            String content = getString(R.string.mom_diaalog_pass_apply_msg) + "\n" +getString(R.string.mom_diaalog_pass_apply_msg1) + mission.getPassgrade();

            new MaterialDialog.Builder(this)
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.mom_diaalog_pass_apply_title)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(content)
                    .positiveText(R.string.mom_diaalog_confirm)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(MissionMainActivity.this, MissionMainActivity.class);
                            finish();
                            intent.putExtra(MissionCommon.OBJECT,mission);
                            intent.putExtra(MissionCommon.MISSIONTYPE,missionType);
                            startActivity(intent);
                        }
                    })
                    .show();
        }else{
            new MaterialDialog.Builder(this)
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.mom_diaalog_pass_apply_title)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(R.string.mom_diaalog_pass_apply_f_msg)
                    .positiveText(R.string.mom_diaalog_confirm)
                    .show();
        }

    }


    public void popUpFeedBack(final String reqType){

        if(reqType.equals("myins")) {
            viewTitle = getString(R.string.feedback_title1) + insInfoVo.getName();
            videoPoint = insInfoVo.getTeamvideopoint();
            wordPoint = insInfoVo.getTeamwordpoint();
        }else if(reqType.equals("pubins")){
            viewTitle = getString(R.string.feedback_title1) + feedbackInfo.getName();
            videoPoint = feedbackInfo.getPubvideopoint();
            wordPoint = feedbackInfo.getPubwordpoint();
        }

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(viewTitle)
                .titleColor(getResources().getColor(R.color.color6))
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.feedback_req_btn)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {

                        feedbackHeader = new FeedbackHeader();
                        feedbackHeader.setUid(user.getUid());


                        //피드백 포인트
                        if(feed_video.isChecked()){
                            feedbackHeader.setFeedbacktype("video");
                            //강사가 지정인지 아닌지
                            if(reqType.equals("pubins")){
                                feedbackHeader.setInstructorid(feedbackInfo.getInstructorid());
                                feedbackHeader.setCashpoint(feedbackInfo.getPubvideopoint());
                            }else{
                                feedbackHeader.setInstructorid(insInfoVo.getInstructorid());
                                feedbackHeader.setCashpoint(insInfoVo.getTeamvideopoint());
                            }
                        }else{
                            feedbackHeader.setFeedbacktype("word");
                            //강사가 지정인지 아닌지
                            if(reqType.equals("pubins")){
                                feedbackHeader.setInstructorid(feedbackInfo.getInstructorid());
                                feedbackHeader.setCashpoint(feedbackInfo.getPubwordpoint());
                            }else{
                                feedbackHeader.setInstructorid(insInfoVo.getInstructorid());
                                feedbackHeader.setCashpoint(insInfoVo.getTeamwordpoint());
                            }
                        }

                        //공개여부 체크
                        if(pub_status.isChecked()){
                            feedbackHeader.setPubstatus("Y");
                        }else{
                            feedbackHeader.setPubstatus("N");
                        }

                        feedbackHeader.setFrequency(1);

                        //강사가 지정이라면 아니면
                        if(reqType.equals("pubins")){
                            feedbackHeader.setInstype("N");
                        }else{
                            feedbackHeader.setInstype("Y");
                        }

                        feedbackHeader.setType("user");
                        feedbackHeader.setVideoaddr(userMission.getYoutubeaddr());
                        feedbackHeader.setSubject(user.getUsername()+" : "+getString(R.string.feedback_feedback_request_subject));
                        feedbackHeader.setContent(feedback_content.getText().toString());
                        feedbackHeader.setUsermissionid(userMission.getUsermissionid());
                        feedbackHeader.setMissionid(userMission.getMissionid());

                        if(myPoint < feedbackHeader.getCashpoint() ){
                            //포인트 부족시 포인트 구매 페이지로 이동
                            new MaterialDialog.Builder(MissionMainActivity.this)
                                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                    .title(R.string.feedback_lack)
                                    .titleColor(getResources().getColor(R.color.color6))
                                    .content(R.string.feedback_lack_msg)
                                    .contentColor(getResources().getColor(R.color.color6))
                                    .positiveText(R.string.mom_diaalog_confirm)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Intent intent = new Intent(MissionMainActivity.this, PointMainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .negativeText(R.string.mom_diaalog_cancel)
                                    .show();
                            return;
                        }
                        String title = "";
                        if(reqType.equals("pubins")){
                            title = "To." + feedbackInfo.getName();
                        }else{
                            title = "To." + insInfoVo.getName();
                        }
                        String content = getResources().getString(R.string.feedback_req_content);

                        new MaterialDialog.Builder(MissionMainActivity.this)
                                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                .title(title)
                                .titleColor(getResources().getColor(R.color.color6))
                                .content(content)
                                .contentColor(getResources().getColor(R.color.color6))
                                .positiveText(R.string.mom_diaalog_confirm)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        WaitingDialog.showWaitingDialog(MissionMainActivity.this,false);
                                        FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,getApplicationContext(),user);
                                        Call<ServerResult> call = feedBackService.saveFeedHeader(feedbackHeader);
                                        call.enqueue(new Callback<ServerResult>() {
                                            @Override
                                            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                                                WaitingDialog.cancelWaitingDialog();
                                                if(response.isSuccessful()){
                                                    ServerResult result = response.body();

                                                    if(result.getCount()==0){
                                                        //포인트 부족
                                                        new MaterialDialog.Builder(MissionMainActivity.this)
                                                                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                                                .title(R.string.feedback_lack)
                                                                .titleColor(getResources().getColor(R.color.color6))
                                                                .content(R.string.feedback_lack_msg)
                                                                .contentColor(getResources().getColor(R.color.color6))
                                                                .positiveText(R.string.mom_diaalog_confirm)
                                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                                    @Override
                                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                        Intent intent = new Intent(MissionMainActivity.this, PointMainActivity.class);
                                                                        startActivity(intent);
                                                                    }
                                                                })
                                                                .negativeText(R.string.mom_diaalog_cancel)
                                                                .show();
                                                    }else{
                                                        //미션 페이지 재실행
                                                        Intent intent = new Intent(MissionMainActivity.this, MissionMainActivity.class);
                                                        finish();
                                                        intent.putExtra(MissionCommon.OBJECT,mission);
                                                        intent.putExtra(MissionCommon.MISSIONTYPE,missionType);
                                                        startActivity(intent);

                                                    }

                                                }else{
                                                    WaitingDialog.cancelWaitingDialog();
                                                    Log.i(TAG,"피드백 신청 에러 발생1");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ServerResult> call, Throwable t) {
                                                WaitingDialog.cancelWaitingDialog();
                                                Log.i(TAG,"피드백 신청 에러 발생2");
                                                t.printStackTrace();
                                            }
                                        });
                                    }
                                })
                                .negativeText(R.string.mom_diaalog_cancel)
                                .widgetColor(getResources().getColor(R.color.enabled_red))
                                .show();
                    }
                }).build();

        layout_Content = (TextInputLayout) dialog.getCustomView().findViewById(R.id.layout_feedback_content);
        feedback_content = (EditText) dialog.getCustomView().findViewById(R.id.feedback_content);
        feed_video_point = (TextView) dialog.getCustomView().findViewById(R.id.feed_video_point);
        feed_word_point = (TextView) dialog.getCustomView().findViewById(R.id.feed_word_point);
        pub_status = (CheckBox) dialog.getCustomView().findViewById(R.id.pub_status);
        text_mypoint = (TextView) dialog.getCustomView().findViewById(R.id.text_mypoint);

        feed_video_point.setText(getString(R.string.feedback_video_point)+" "+videoPoint+"P");
        feed_word_point.setText(getString(R.string.feedback_word_point)+" "+wordPoint+"P");

        NumberFormat numberFormat = NumberFormat.getInstance();
        text_mypoint.setText(numberFormat.format(myPoint)+"P");

        feedback_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        feed_video = (RadioButton) dialog.getCustomView().findViewById(R.id.feed_Video);
        feed_word = (RadioButton) dialog.getCustomView().findViewById(R.id.feed_word);
        radioGroup = (RadioGroup) dialog.getCustomView().findViewById(R.id.feed_RadioGroup);
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }

    //피드빽 요청
    public void getInsInfo(){
        WaitingDialog.showWaitingDialog(this,false);
        DataService service = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);
        InsInfoVo vo = new InsInfoVo();
        vo.setTeamid(USER_TEAM_ID);
        Call<InsInfoVo> voCall = service.getInsInfo(vo);
        voCall.enqueue(new Callback<InsInfoVo>() {
            @Override
            public void onResponse(Call<InsInfoVo> call, Response<InsInfoVo> response) {
                if(response.isSuccessful()){
                    WaitingDialog.cancelWaitingDialog();
                    insInfoVo = response.body();

                }else{
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<InsInfoVo> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    //피드백 리스트
    public void getFeedBack(UserMission userMission){

        WaitingDialog.showWaitingDialog(MissionMainActivity.this,false);
        FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,getApplicationContext(),user);
        FeedbackHeader header = new FeedbackHeader();

        header.setUid(user.getUid());
        header.setUsermissionid(userMission.getUsermissionid());

        if(userMission.getUsermissionid()==0){
            li_feedback_no_data_back.setVisibility(View.VISIBLE);
        }else {

            Call<List<FeedbackHeader>> c = feedBackService.getFeedAllList(header);
            c.enqueue(new Callback<List<FeedbackHeader>>() {
                @Override
                public void onResponse(Call<List<FeedbackHeader>> call, Response<List<FeedbackHeader>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if (response.isSuccessful()) {

                        feedbackHeaders = response.body();

                        if (feedbackHeaders.size() == 0) {
                            li_feedback_no_data_back.setVisibility(View.VISIBLE);
                        } else {
                            li_feedback_no_data_back.setVisibility(View.GONE);
                        }

                        feedBackAllListAdapter = new FeedBackAllListAdapter(activity, feedbackHeaders, user, mission);
                        feedbackRecylerView.setHasFixedSize(true);
                        feedbackRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        feedbackRecylerView.setAdapter(feedBackAllListAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<FeedbackHeader>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }
    }


    public void passHistory(){
        if(!Compare.isEmpty(userMission.getPassflag())){

                WaitingDialog.showWaitingDialog(MissionMainActivity.this,false);
                MissionPassService service = ServiceGenerator.createService(MissionPassService.class,getApplicationContext(),user);

                MissionPass query = new MissionPass();
                query.setUid(user.getUid());
                query.setUsermissionid(userMission.getUsermissionid());
                query.setMissionid(userMission.getMissionid());

                Call<List<MissionPass>> c = service.getPassList(query);
                c.enqueue(new Callback<List<MissionPass>>() {
                    @Override
                    public void onResponse(Call<List<MissionPass>> call, Response<List<MissionPass>> response) {
                        WaitingDialog.cancelWaitingDialog();
                        if(response.isSuccessful()){
                            missionPasses = response.body();

                            if(missionPasses.size()==0){
                                li_mission_no_pass.setVisibility(View.VISIBLE);
                            }else{
                                li_mission_no_pass.setVisibility(View.GONE);
                            }

                            passListAdapter = new PassListAdapter(activity,missionPasses,user,missionPassFlag,mission);
                            passrecyclerview.setHasFixedSize(true);
                            passrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            passrecyclerview.setAdapter(passListAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MissionPass>> call, Throwable t) {
                        WaitingDialog.cancelWaitingDialog();
                        t.printStackTrace();
                    }
                });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.i(TAG,"onBackPressed() ==================================");

        if(Common.YOUTUBESCREEN_STATUS .equals("LANDSCAPE")){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        if(actName == null ){
            actName = "NOL";
        }

        if(actName.equals("NOL")){
            Intent intent = new Intent(MissionMainActivity.this,MissionActivity.class);
            intent.putExtra(MissionCommon.MISSIONTYPE,missionType);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            Intent intent = new Intent(MissionMainActivity.this,FavoriteMissionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MissionCommon.MISSIONTYPE,missionType);
            startActivity(intent);
        }


    }
}
