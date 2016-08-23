package com.mom.soccer.mission;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.mom.soccer.adapter.FeedBackAdapter;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.cardview.FeedBackCard;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.ExpandableHeightGridView;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.InsInfoVo;
import com.mom.soccer.dto.FavoriteMission;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.Mission;
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
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionMainActivity extends AppCompatActivity {

    private static final String TAG = "MissionMainActivity";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final int REQUEST_FEED_BACK_CODE = 201;

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

    @Bind(R.id.li_mymittion)
    LinearLayout linearLayout;

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

    @Bind(R.id.li_no_data_found)
    LinearLayout li_no_data_found;

    @Bind(R.id.li_feedback_history)
    LinearLayout li_feedback_history;

    @Bind(R.id.btnReqEval)
    Button btnReqEval;

    private InsInfoVo insInfoVo;
    private View positiveAction;
    /**************************************************
     * google uplaod define
     **********************/
    private Uri mFileURI = null;

    private static final int RESULT_PICK_IMAGE_CROP = 4;
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

    String viewTitle = null;
    int videoPoint = 0;
    int wordPoint  = 0;

    private FeedbackHeader feedbackHeader;
    String pubStauts = "Y";

    private static int USER_TEAM_ID = 0;
    private int myPoint = 0;

    /*feedback*/
    private List<FeedbackHeader> feedbackHeaders;

    @Bind(R.id.feedback_recyclerview)
    CardRecyclerView cardRecyclerView;

    //@Bind(R.id.feedback_recyclerview)
    //RecyclerView recyclerView;

    private FeedBackAdapter   recyclerAdapter;
    Activity activity;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_OK);
        finish();
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

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume() =====================================");
        intent = getIntent();
        String uploadflag = intent.getStringExtra("uploadflag");
        Log.i(TAG,"받은 값은 : " + uploadflag);

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
                    Log.i(TAG," USER_TEAM_ID ********************** : " +  USER_TEAM_ID);

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
                    Log.i(TAG,"get point error 1");
                }
            }

            @Override
            public void onFailure(Call<SpBalanceHeader> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                Log.i(TAG,"get point error 1");
                t.printStackTrace();
            }
        });


    }


    /*
    BroadcastReceiver uploadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String uploadMessage = intent.getStringExtra("uploadMessage");
            String upflag = intent.getStringExtra("upflag");

        }
    };
    */

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
                        li_video_list.setVisibility(View.GONE);
                        li_no_data_found.setVisibility(View.VISIBLE);
                    }else{
                        li_video_list.setVisibility(View.VISIBLE);
                        li_no_data_found.setVisibility(View.GONE);
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

                    Log.i(TAG," 받아온 값은  : " + userMission.toString() );
                    //좋아요 카운트
                    usermission_tx_hart.setText(String.valueOf(userMission.getBookmarkcount()));
                    usermission_tx_comment.setText(String.valueOf(userMission.getBoardcount()));

                    if(userMission.getUid()==0){
                        img_missiontab.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                    }else{

                        Log.i(TAG," Test View Start ######" + userMission.toString());

                        if(userMission.getPassflag().equals("Y")){
                            Log.i(TAG,"===1" + userMission.getPassflag());
                            img_missiontab.setVisibility(View.VISIBLE);
                            view_l1.setVisibility(View.GONE);
                            view_l2.setVisibility(View.GONE);
                            im_clear_marck.setVisibility(View.VISIBLE);
                            btnReqEval.setText(R.string.user_mission_y);
                        }else if(userMission.getPassflag().equals("P")){

                            Log.i(TAG,"===2" + userMission.getPassflag());
                            img_missiontab.setVisibility(View.GONE);
                            tx_main_usermission.setText(R.string.user_mission_p);
                            btn_mission_upload.setText(R.string.user_mission_p);

                            btn_mission_upload.setBackgroundResource(R.color.color8);
                            im_clear_marck.setVisibility(View.GONE);

                            btnReqEval.setText(R.string.user_mission_p);

                        }else if(userMission.getPassflag().equals("N")){
                            Log.i(TAG,"===3" + userMission.getPassflag());
                            img_missiontab.setVisibility(View.GONE);
                            tx_main_usermission.setText(R.string.user_mission_n);
                            im_clear_marck.setVisibility(View.GONE);
                            btnReqEval.setText(R.string.mission_eval_btn);
                        }
                        Log.i(TAG," Test View End #####");


                        //내가 좋아요를 했다면
                        if(userMission.getMycheck()==0){
                            usermission_iv_hart.setImageResource(R.drawable.ic_white_hart);
                        }else{
                            usermission_iv_hart.setImageResource(R.drawable.ic_hart_red);
                        }

                        linearLayout.setVisibility(View.VISIBLE);
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

                    //피드백이 있는지 검사.
                    getFeedBack();

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


    //버튼 컨트롤
    @OnClick(R.id.btn_mission_upload)
    public void missionVideoUpload(){

        if(userMission.getUid()!=0){

            if(userMission.getPassflag().equals("P")) {
                VeteranToast.makeToast(getApplicationContext(), getString(R.string.user_mission_progress), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, RESULT_PICK_IMAGE_CROP);
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

        if(USER_TEAM_ID==0){
            strings = new String[]{
                    getString(R.string.user_mission_team_feedback_another_request)
            };
        }else{
            strings = new String[]{getString(R.string.user_mission_team_feedback_request),
                    getString(R.string.user_mission_team_feedback_another_request)
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
                                    startActivityForResult(intent,REQUEST_FEED_BACK_CODE);
                                }else{
                                    if(which==0){
                                        popUpFeedBack("myins");
                                    }else if(which==1){
                                        Intent intent = new Intent(MissionMainActivity.this,FeedBackInsListActivity.class);
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
                            .content(R.string.user_mission_pass_msg)
                            .positiveText(R.string.mom_diaalog_confirm)
                            .widgetColor(getResources().getColor(R.color.enabled_red))
                            .show();
                }else if(userMission.getPassflag().equals("P")){
                    new MaterialDialog.Builder(this)
                            .content(R.string.user_mission_progress_msg)
                            .positiveText(R.string.mom_diaalog_confirm)
                            .widgetColor(getResources().getColor(R.color.enabled_red))
                            .show();
                }else if(userMission.getPassflag().equals("N")){



                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_PICK_IMAGE_CROP:
                if (resultCode == RESULT_OK) {
                    mFileURI = data.getData();
                    if (mFileURI != null) {
                        Intent intent = new Intent(this, ReviewActivity.class);
                        intent.setData(mFileURI);
                        intent.putExtra(MissionCommon.OBJECT,mission);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }
                }
                break;
            case REQUEST_FEED_BACK_CODE:
                if (resultCode == RESULT_OK) {
                    insInfoVo = (InsInfoVo) data.getSerializableExtra(MissionCommon.INS_OBJECT);
                    Log.i(TAG,"insInfoVo 값은 : " + insInfoVo.toString());
                    popUpFeedBack("pubins");
                }
                break;
        }
    }


    public void popUpFeedBack(final String reqType){

        if(reqType.equals("myins")) {
            viewTitle = getString(R.string.feedback_title1) + insInfoVo.getName();
            videoPoint = insInfoVo.getTeamvideopoint();
            wordPoint = insInfoVo.getTeamwordpoint();
        }else if(reqType.equals("pubins")){
            viewTitle = getString(R.string.feedback_title1) + insInfoVo.getName();
            videoPoint = insInfoVo.getPubvideopoint();
            wordPoint = insInfoVo.getPubwordpoint();
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
                        feedbackHeader.setInstructorid(insInfoVo.getInstructorid());

                        //피드백 포인트
                        if(feed_video.isChecked()){
                            feedbackHeader.setFeedbacktype("video");
                            //강사가 지정인지 아닌지
                            if(reqType.equals("pubins")){
                                feedbackHeader.setCashpoint(insInfoVo.getPubvideopoint());
                            }else{
                                feedbackHeader.setCashpoint(insInfoVo.getTeamvideopoint());
                            }
                        }else{
                            feedbackHeader.setFeedbacktype("word");
                            //강사가 지정인지 아닌지
                            if(reqType.equals("pubins")){
                                feedbackHeader.setCashpoint(insInfoVo.getPubwordpoint());
                            }else{
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
                        String title = "To." + insInfoVo.getName();
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
                    Log.i(TAG,"********************** : " +  insInfoVo.toString());

                }else{
                    Log.i(TAG,"getInsInfo error1");
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<InsInfoVo> call, Throwable t) {
                Log.i(TAG,"getInsInfo error2");
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

/*    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        final ScrollView scroll_layout;
        scroll_layout = (ScrollView) findViewById(R.id.scroll_layout);
        scroll_layout.post(new Runnable(){
            public void run() {
                scroll_layout.scrollTo(1, 1);
            }
        });
    }*/


    //피드백 리스트
    public void getFeedBack(){
        Log.i(TAG,"feedbackHeaders() 피드백이 있는지 검사합니다");
        WaitingDialog.showWaitingDialog(MissionMainActivity.this,false);

        FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,getApplicationContext(),user);
        FeedbackHeader header = new FeedbackHeader();

        header.setUid(user.getUid());
        header.setUsermissionid(userMission.getUsermissionid());

        Call<List<FeedbackHeader>> c = feedBackService.getFeedHeaderList(header);
        c.enqueue(new Callback<List<FeedbackHeader>>() {
            @Override
            public void onResponse(Call<List<FeedbackHeader>> call, Response<List<FeedbackHeader>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    feedbackHeaders = response.body();
                    Log.i(TAG,"feedbackHeaders() " + feedbackHeaders.size());

                    if(feedbackHeaders.size()==0){
                        li_feedback_history.setVisibility(View.GONE);
                    }else{
                        li_feedback_history.setVisibility(View.VISIBLE);


                        ArrayList<Card> cards = new ArrayList<Card>();

                        for(int i=0 ; i < feedbackHeaders.size();i++){
                            Card card = new FeedBackCard(activity,feedbackHeaders.get(i),user,mission);
                            cards.add(card);
                        }


                        CardArrayRecyclerViewAdapter feedbackAdapter = new CardArrayRecyclerViewAdapter(getApplicationContext(), cards);

                        cardRecyclerView.setHasFixedSize(false);
                        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        cardRecyclerView.setAdapter(feedbackAdapter);


                        //defalut recyclerView
                        /*
                        recyclerAdapter = new FeedBackAdapter(getApplicationContext(),feedbackHeaders);
                        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        recyclerView.getItemAnimator().setAddDuration(300);
                        recyclerView.getItemAnimator().setRemoveDuration(300);
                        recyclerView.getItemAnimator().setMoveDuration(300);
                        recyclerView.getItemAnimator().setChangeDuration(300);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MissionMainActivity.this));
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(recyclerAdapter);
                        alphaAdapter.setDuration(500);
                        recyclerView.setAdapter(alphaAdapter);
                        */
                    }

                }else{
                    Log.i(TAG,"feedbackHeaders() error1");
                }
            }

            @Override
            public void onFailure(Call<List<FeedbackHeader>> call, Throwable t) {
                Log.i(TAG,"feedbackHeaders() error2");
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });

    }
}
