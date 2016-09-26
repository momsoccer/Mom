package com.mom.soccer.bottommenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.ball.PlayerMainActivity;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.ExpandableHeightGridView;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.TeamMapVo;
import com.mom.soccer.dataDto.UserLevelDataVo;
import com.mom.soccer.dto.FollowManage;
import com.mom.soccer.dto.FriendApply;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.UserMissionActivity;
import com.mom.soccer.point.PointMainActivity;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.pubactivity.PubActivity;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.FollowService;
import com.mom.soccer.retrofitdao.FriendService;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.trservice.UserTRService;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity {

    private static final String TAG = "MyPageActivity";


    @Bind(R.id.mypage_image_user_image)
    ImageView mypageImage;

    @Bind(R.id.mypage_back_image)
    ImageView mypageBackImage;

    @Bind(R.id.mypage_no_data_found)
    TextView noData_textView;

    //page view mapping
    @Bind(R.id.mypage_title)
    TextView mypage_title;

    @Bind(R.id.mypage_user_name)
    TextView tx_user_name;
/*
    @Bind(R.id.mypage_total_ranking)
    TextView mypage_total_ranking;

    @Bind(R.id.mypage_total_count)
    TextView mypage_total_count;

    @Bind(R.id.mypage_friend_ranking)
    TextView mypage_friend_ranking;

    @Bind(R.id.mypage_friend_count)
    TextView mypage_friend_count;

    @Bind(R.id.mypage_team_ranking)
    TextView mypage_team_ranking;

    @Bind(R.id.mypage_team_count)
    TextView mypage_team_count;*/

    //종목별 점수 및 레벨
    @Bind(R.id.mypage_lifting_level)  // 1.리프팅
            TextView mypage_lifting_level;

    @Bind(R.id.mypage_lifting_score)
    TextView mypage_lifting_score;

    @Bind(R.id.mypage_around_level)  // 2.어라운드
            TextView mypage_around_level;

    @Bind(R.id.mypage_around_score)
    TextView mypage_around_score;

    @Bind(R.id.mypage_trapping_level) // 3.트래핑
            TextView mypage_trapping_level;

    @Bind(R.id.mypage_trapping_socre)
    TextView mypage_trapping_socre;

    @Bind(R.id.mypage_flib_level) // 4.플립
            TextView mypage_flib_level;

    @Bind(R.id.mypage_flib_score)
    TextView mypage_flib_score;

    @Bind(R.id.mypage_drible_level) // 5.드리블
            TextView mypage_drible_level;

    @Bind(R.id.mypage_drible_score)
    TextView mypage_drible_score;

    @Bind(R.id.mypage_crosba_level) // 6.크로스바 대체 예정 complex
            TextView mypage_crosba_level;

    @Bind(R.id.mypage_crosba_score)
    TextView mypage_crosba_score;

    @Bind(R.id.tx_follower)
    TextView tx_follower;

    @Bind(R.id.add_friend)
    Button add_friend;

    private User user = new User();
    private User findUser = new User();
    private PrefUtil prefUtil;

    GridMissionAdapter gridMissionAdapter;
    ExpandableHeightGridView videoGridView;
    UserMission qUserMission = new UserMission();
    List<UserMission> userMissions;

    String pageFlag=null;
    int youUid=0;
    private Intent getParamInt;
    int FollowCount = 0;

    //친구요청 관련 변수
    private String REQ_FRIEND = "N";
    private FriendApply friendApply;

    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Bind(R.id.im_point)
    ImageView im_point;

    @Bind(R.id.tx_level)
    TextView tx_level;

    @Bind(R.id.text_title)
    TextView text_title;

    @Bind(R.id.mybtnLyout)
    LinearLayout mybtnLyout;

    private int followingCount = 0;
    @Bind(R.id.add_follwing)
    Button add_follwing;

    @Bind(R.id.backImageCamera)
    ImageButton backImageCamera;

    private String friendStatus = "N";

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private Uri mImageCaptureUri;
    private String absoultePath;
    private String RealFilePath;
    private String fileName;

    private String imgtype;
    private String uploadflag="N";

    @OnClick(R.id.im_batch)
    public void im_batch(){

        PubActivity pubActivity = new PubActivity(this,user,findUser.getUid());
        pubActivity.showDialog();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bottom_mypage_layout);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.uprofile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        //자신의 프로필? 다른 사람의 프로필
        getParamInt = getIntent();
        pageFlag = getParamInt.getExtras().getString("pageflag");

        Log.i(TAG,"pageFlag : " + pageFlag);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        videoGridView = (ExpandableHeightGridView) findViewById(R.id.mypage_gridview);
        videoGridView.setExpanded(true);


        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent userMissionListIntent = new Intent(getApplicationContext(),UserMissionActivity.class);
                userMissionListIntent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMissions.get(position));
                startActivity(userMissionListIntent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        if(pageFlag.equals("me")){
            im_point.setVisibility(View.VISIBLE);
            mybtnLyout.setVisibility(View.GONE);
        }else{
            im_point.setVisibility(View.GONE);
            mybtnLyout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(pageFlag.equals("me")){
            findUser = user;
        }else{

            youUid = getParamInt.getExtras().getInt("frienduid");
            Log.i(TAG,"youUid : " + youUid );
            findUser.setUid(youUid);
            followMethod("getcount");

            friendTransaction("QUERY");
        }

        getProfileUser(findUser.getUid());
        qUserMission.setUid(findUser.getUid());
        userGrid_MissionList(qUserMission);

        //팔러잉 버튼 텍스트
        getFollowCountBtnSet();
        getUserLevel();
    }

    public void getFollowCountBtnSet(){

        FollowService f = ServiceGenerator.createService(FollowService.class,getApplicationContext(),user);
        int queryUid = 0;

        if(pageFlag.equals("me")){
            queryUid=user.getUid();
        }else{
            queryUid=findUser.getUid();
        }

        Call<List<TeamMapVo>> call = f.getFollowerCount(queryUid);
        call.enqueue(new Callback<List<TeamMapVo>>() {
            @Override
            public void onResponse(Call<List<TeamMapVo>> call, Response<List<TeamMapVo>> response) {
                if(response.isSuccessful()){
                    List<TeamMapVo> mapVo = response.body();

                    int follower  = 0;
                    int following = 0;
                    String value = null;
                    for(int i=0 ; i < mapVo.size();i++){

                        if(mapVo.get(i).getType().equals("M")){
                            follower = mapVo.get(i).getCount();
                        }else{
                            following = mapVo.get(i).getCount();
                        }
                    }
                    value = getString(R.string.follow_toolbar_page_text)+"("+follower+")   |   "+getString(R.string.follow_view_pager)+"("+following+")";
                    tx_follower.setText(value);

                }else{

                }
            }

            @Override
            public void onFailure(Call<List<TeamMapVo>> call, Throwable t) {

            }
        });
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

    //유저 정보
    public void getProfileUser(int uid){
        WaitingDialog.showWaitingDialog(this,false);
        UserService service = ServiceGenerator.createService(UserService.class,getApplicationContext(),user);

        Call<User> userCall = service.getProfileUser(uid);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    findUser = response.body();
                    WaitingDialog.cancelWaitingDialog();


                    if(youUid!=0){
                        mypage_title.setText(findUser.getUsername());
                        text_title.setText(findUser.getUsername()+" "+ getString(R.string.app_myvideo_view_title));
                    }

                    if(uploadflag.equals("N")){ //업로드시 시간차에 의해 업데이트보다 먼저 onstart 메소드가 실행되버린다
                        if(!Compare.isEmpty(findUser.getProfileimgurl())) {
                            Glide.with(MyPageActivity.this)
                                    .load(findUser.getProfileimgurl())
                                    .asBitmap().transform(new RoundedCornersTransformation(MyPageActivity.this,10,5))
                                    .into(mypageImage);
                        }

                        if(!Compare.isEmpty(findUser.getBackimage())){

                            Glide.with(MyPageActivity.this)
                                    .load(findUser.getBackimage())
                                    .into(mypageBackImage);
                        }
                    }

                    /*************************************************************************************
                     //====>  액티비티 모든 뷰에 정보를 매핑하는 작업(TextView 등등)
                     **************************************************************************************/

                    tx_user_name.setText(findUser.getUsername());
                }else{
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();

            }
        });

    }


    //영상 목록
    public void userGrid_MissionList(final UserMission userMission){
        WaitingDialog.showWaitingDialog(this,false);
        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,getApplicationContext(),user);
        Call<List<UserMission>> call = userMissionService.getUserMissionList(userMission);
        call.enqueue(new Callback<List<UserMission>>() {
            @Override
            public void onResponse(Call<List<UserMission>> call, Response<List<UserMission>> response) {

                if(response.isSuccessful()){
                    WaitingDialog.cancelWaitingDialog();
                    userMissions = response.body();

                    //adapter_book_mark_layout
                    //adapter_user_mission_grid_item

                    gridMissionAdapter = new GridMissionAdapter(getApplicationContext(),R.layout.adapter_book_mark_layout,userMissions,"ME");
                    videoGridView.setAdapter(gridMissionAdapter);

                    if(userMissions.size()==0){
                        noData_textView.setVisibility(View.VISIBLE);
                        noData_textView.setText("등록된 영상이 없습니다");
                    }else{
                        noData_textView.setVisibility(View.INVISIBLE);
                    }

                }else{
                    WaitingDialog.cancelWaitingDialog();
                }

            }

            @Override
            public void onFailure(Call<List<UserMission>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() ===================================");
    }

    public void followClick(View v){
        Intent intent = new Intent(MyPageActivity.this,FollowActivity.class);
        intent.putExtra("queryuid",findUser.getUid());
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        scrollView.post(new Runnable(){
            public void run() {
                scrollView.scrollTo(1, 1);
            }
        });
    }


    @OnClick(R.id.im_point)
    public void point(){
        Intent intent = new Intent(this, PointMainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void getUserLevel(){
        WaitingDialog.showWaitingDialog(MyPageActivity.this,false);
        DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);
        Call<List<UserLevelDataVo>> c = dataService.getUserLevelDataList(findUser.getUid());
        c.enqueue(new Callback<List<UserLevelDataVo>>() {
            @Override
            public void onResponse(Call<List<UserLevelDataVo>> call, Response<List<UserLevelDataVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){

                    List<UserLevelDataVo> dataVos = response.body();

                    for(int i=0; i < dataVos.size(); i++){
                        UserLevelDataVo levelDataVo = dataVos.get(i);

                        Log.i(TAG,"levelDataVo : " + levelDataVo.toString());

                        if(levelDataVo.getMittiontype().equals("DRIBLE")){
                            mypage_drible_level.setText("Lv."+String.valueOf(levelDataVo.getLevel()));
                            mypage_drible_score.setText(String.valueOf(levelDataVo.getScore()));
                        }else if(levelDataVo.getMittiontype().equals("LIFTING")){
                            mypage_lifting_level.setText("Lv."+String.valueOf(levelDataVo.getLevel()));
                            mypage_lifting_score.setText(String.valueOf(levelDataVo.getScore()));
                        }else if(levelDataVo.getMittiontype().equals("TRAPING")){
                            mypage_trapping_level.setText("Lv."+String.valueOf(levelDataVo.getLevel()));
                            mypage_trapping_socre.setText(String.valueOf(levelDataVo.getScore()));
                        }else if(levelDataVo.getMittiontype().equals("AROUND")){
                            mypage_around_level.setText("Lv."+String.valueOf(levelDataVo.getLevel()));
                            mypage_around_score.setText(String.valueOf(levelDataVo.getScore()));
                        }else if(levelDataVo.getMittiontype().equals("FLICK")){
                            mypage_flib_level.setText("Lv."+String.valueOf(levelDataVo.getLevel()));
                            mypage_flib_score.setText(String.valueOf(levelDataVo.getScore()));
                        }else if(levelDataVo.getMittiontype().equals("COMPLEX")){
                            mypage_crosba_level.setText("Lv."+String.valueOf(levelDataVo.getLevel()));
                            mypage_crosba_score.setText(String.valueOf(levelDataVo.getScore()));
                        }else if(levelDataVo.getMittiontype().equals("TOTAL")){
                            tx_level.setText(String.valueOf(levelDataVo.getLevel()));
                        }

                    }

                }else{

                }
            }

            @Override
            public void onFailure(Call<List<UserLevelDataVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    //팔로잉 관련 트랜잭션
    @OnClick(R.id.add_follwing)
    public void followBtn(){

        Log.i(TAG,"팔로우 상태는 : " + followingCount);

        if(followingCount==0){
            followMethod("save");
        }else{
            followMethod("cancel");
        }
    }

    //팔로우 팔로잉
    public void followMethod(String followType){

        WaitingDialog.showWaitingDialog(this,false);
        final FollowManage manage = new FollowManage();
        manage.setUid(user.getUid());
        manage.setFollowuid(findUser.getUid());

        FollowService service = ServiceGenerator.createService(FollowService.class,this,user);

        if(followType.equals("save")){
            Call<ServerResult> c = service.saveFollow(manage);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        WaitingDialog.cancelWaitingDialog();
                        add_follwing.setText(getString(R.string.app_fllow_cancel));
                        VeteranToast.makeToast(getApplicationContext(),findUser.getUsername()+getString(R.string.follow_pickup),Toast.LENGTH_SHORT).show();
                        followingCount = 1;
                    }else{
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                }
            });
        }else if(followType.equals("cancel")){
            Call<ServerResult> c = service.deleteFollow(manage);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        WaitingDialog.cancelWaitingDialog();
                        add_follwing.setText(getString(R.string.app_fllow));
                        VeteranToast.makeToast(getApplicationContext(),findUser.getUsername()+getString(R.string.follow_pickup_cancel),Toast.LENGTH_SHORT).show();
                        followingCount = 0;
                    }else{
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                }
            });
        }else if(followType.equals("getcount")){
            Call<ServerResult> c = service.getFollowUserCount(manage);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        followingCount = result.getCount();

                        if(followingCount==0){
                            //팔로우 유저가 아닐경우
                            add_follwing.setText(getString(R.string.app_fllow));
                        }else{
                            //팔로우 유저일 경우
                            add_follwing.setText(getString(R.string.app_fllow_cancel));
                        }

                        WaitingDialog.cancelWaitingDialog();
                    }else{
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                }
            });
        }
    }

    //친구 트랜잭션
    @OnClick(R.id.add_friend)
    public void add_friend(){
        if(friendStatus.equals("P")){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.friend_progressing),Toast.LENGTH_SHORT).show();
        }else if(friendStatus.equals("ME")){ //친구가 나에게 신청을 했을 경우 수락 할것인지 아닌지?
            //친구수락은 메인 볼 메뉴에서한다
            new MaterialDialog.Builder(this)
                    .content(R.string.friend_re_msg)
                    .contentColor(getResources().getColor(R.color.color6))
                    .positiveText(R.string.mom_diaalog_confirm)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(getApplicationContext(),PlayerMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Param.FRAGMENT_COUNT,2);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                        }
                    })
                    .negativeText(R.string.mom_diaalog_cancel)
                    .widgetColor(getResources().getColor(R.color.enabled_red))
                    .show();

        }else if(friendStatus.equals("N")){
            friendTransaction("REQUEST");
        }else if(friendStatus.equals("Y")){
            friendTransaction("DELETE");
        }

    }

    public void friendTransaction(String transactionType){
        if(transactionType.equals("REQUEST")){

            WaitingDialog.showWaitingDialog(MyPageActivity.this,false);
            FriendService service = ServiceGenerator.createService(FriendService.class,getApplicationContext(),user);

            FriendApply apply = new FriendApply();
            apply.setRequsername(user.getUsername());
            apply.setRequid(user.getUid());
            apply.setResuid(findUser.getUid());
            apply.setFlag("REQUEST");

            Call<ServerResult> c = service.reqFriend(apply);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        VeteranToast.makeToast(getApplicationContext(),findUser.getUsername()+" "+getString(R.string.friedn_req),Toast.LENGTH_SHORT).show();
                        add_friend.setText(getString(R.string.friedn_friend_request_process));

                        Intent intent = new Intent(MyPageActivity.this,MyPageActivity.class);
                        intent.putExtra("pageflag","friend");
                        intent.putExtra("frienduid",findUser.getUid());
                        finish();
                        startActivity(intent);

                    }else{

                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog(); t.printStackTrace();
                }
            });
        }else if(transactionType.equals("QUERY")){
            WaitingDialog.showWaitingDialog(MyPageActivity.this,false);
            FriendService service = ServiceGenerator.createService(FriendService.class,getApplicationContext(),user);
            Call<ServerResult> c = service.getFriendStatus(user.getUid(),findUser.getUid());

            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        ServerResult result = response.body();

                        if(result.getResult().equals("P")){
                            add_friend.setText(getString(R.string.friedn_friend_request_process));
                            friendStatus = "P";
                        }else if(result.getResult().equals("ME")){ //나에게 친구 요청을 한 상태일 경우
                            add_friend.setText(getString(R.string.friedn_friend_request_me));
                            friendStatus = "ME";
                        }else if(result.getResult().equals("Y")){
                            add_friend.setText(getString(R.string.friedn_friend_cancel));
                            friendStatus = "Y";
                        }

                    }else{

                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog(); t.printStackTrace();
                }
            });
        }else if(transactionType.equals("DELETE")) {
            WaitingDialog.showWaitingDialog(MyPageActivity.this,false);
            FriendService service = ServiceGenerator.createService(FriendService.class,getApplicationContext(),user);

            FriendApply friendApply = new FriendApply();
            friendApply.setRequid(user.getUid());
            friendApply.setResuid(findUser.getUid());
            Call<ServerResult> c = service.deleteFriend(friendApply);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.friend_delete),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyPageActivity.this,MyPageActivity.class);
                        intent.putExtra("pageflag","friend");
                        intent.putExtra("frienduid",findUser.getUid());
                        finish();
                        startActivity(intent);
                    }else{

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
    //사진변경
    @OnClick(R.id.backImageCamera)
    public void backImageCamera(){

        if(pageFlag.equals("me")){
            imgtype="background";
            changeImage();
        }

    }

    public void changeImage(){

        new MaterialDialog.Builder(MyPageActivity.this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(R.string.mom_diaalog_photo_title)
                .titleColor(getResources().getColor(R.color.color6))
                .content(R.string.mom_diaalog_photo_contnet)
                .contentColor(getResources().getColor(R.color.color6))
                .positiveText(R.string.mom_diaalog_photo_gallery)
                .neutralText(R.string.mom_diaalog_photo_camera)
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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //카메라에서 사진촬영
    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String uri = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),uri));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent,PICK_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){
            case PICK_FROM_ALBUM:

                mImageCaptureUri = data.getData();
                Intent intenti = new Intent("com.android.camera.action.CROP");
                intenti.setDataAndType(mImageCaptureUri, "image/*");

                //서버에가기전 적용해주기
                if(imgtype.equals("front")){
                    intenti.putExtra("outputX",100);
                    intenti.putExtra("outputY",100);
                    intenti.putExtra("aspectX",100);
                    intenti.putExtra("aspectY",100);
                }else{
                    intenti.putExtra("outputX",200);
                    intenti.putExtra("outputY",150);
                    intenti.putExtra("aspectX",200);
                    intenti.putExtra("aspectY",150);
                }
                intenti.putExtra("scale",true);
                intenti.putExtra("return-data", true);
                startActivityForResult(intenti,CROP_FROM_IMAGE);

                break;

            case PICK_FROM_CAMERA:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");


                //서버에가기전 적용해주기
                if(imgtype.equals("front")){
                    intent.putExtra("outputX",100);
                    intent.putExtra("outputY",100);
                    intent.putExtra("aspectX",100);
                    intent.putExtra("aspectY",100);
                }else{
                    intent.putExtra("outputX",200);
                    intent.putExtra("outputY",150);
                    intent.putExtra("aspectX",200);
                    intent.putExtra("aspectY",150);
                }

                intent.putExtra("scale",true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent,CROP_FROM_IMAGE);
                break;

            case CROP_FROM_IMAGE:
                if(resultCode != RESULT_OK){
                    return;
                }

                final Bundle extras = data.getExtras();

                //크롭된 이미지를 저장하기 위한 file 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ Common.IMAGE_MOM_PATH+System.currentTimeMillis()+".jpg";

                RealFilePath = filePath;
                fileName    = System.currentTimeMillis()+".jpg";

                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");

                    //서버에가기전 적용해주기
                    if(imgtype.equals("front")){
                        mypageImage.setImageBitmap(photo);
                    }else{
                        mypageBackImage.setImageBitmap(photo);
                    }

                    storeCropImage(photo, filePath);
                    absoultePath = filePath;
                    break;
                }
                File file = new File(mImageCaptureUri.getPath());
                if(file.exists()){
                    file.delete();
                }
        }
    }

    private void storeCropImage(Bitmap bitmap,String filePath){

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+Common.IMAGE_MOM_PATH;
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        //서버에 파일을 업로드 합니다
        String profileimgurl = Common.SERVER_USER_IMGFILEADRESS + fileName;
        String StrUid = String.valueOf(user.getUid());

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(copyFile)));
            out.flush();
            out.close();

            //이미지 업로드
            UserTRService userTRService = new UserTRService(this,user);
            userTRService.updateUserImage(StrUid,fileName,RealFilePath,imgtype);

            uploadflag = "Y";

            //유저사진을 쉐어퍼런스에 저장해준다(업데이트)
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor pre = sp.edit();

            if(imgtype.equals("front")){
                pre.putString("profileImgUrl", profileimgurl);
            }else{
                pre.putString("backimage", profileimgurl);
            }
            pre.commit();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.mypage_image_user_image)
    public void mypage_image_user_image(){
        if(pageFlag.equals("me")){
            imgtype="front";
            changeImage();
        }
    }
}
