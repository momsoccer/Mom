package com.mom.soccer.bottommenu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.common.BlurTransformation;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.ExpandableHeightGridView;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.FollowManage;
import com.mom.soccer.dto.FriendApply;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.UserMissionActivity;
import com.mom.soccer.retrofitdao.FriendService;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.DialogBuilder;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

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

    @Bind(R.id.mypage_coachname)
    TextView tx_coachname;

    @Bind(R.id.mypage_teamname)
    TextView tx_teamname;

    @Bind(R.id.mypage_total_ranking)
    TextView tx_total_ranking;

    @Bind(R.id.mypage_total_count)
    TextView tx_total_count;

    @Bind(R.id.mypage_friend_ranking)
    TextView tx_friend_ranking;

    @Bind(R.id.mypage_friend_count)
    TextView tx_friend_count;

    @Bind(R.id.mypage_team_ranking)
    TextView tx_team_ranking;

    @Bind(R.id.mypage_team_count)
    TextView tx_team_count;

    @Bind(R.id.mypage_fllower)
    TextView tx_fllowerCount;

    @Bind(R.id.mypage_fllowing)
    TextView tx_fllowingCount;

    //종목별 점수 및 레벨
    @Bind(R.id.mypage_lifting_level)  // 1.리프팅
            TextView tx_lifting_level;

    @Bind(R.id.mypage_lifting_score)
    TextView tx_lifting_score;

    @Bind(R.id.mypage_around_level)  // 2.어라운드
            TextView tx_around_level;

    @Bind(R.id.mypage_around_score)
    TextView tx_around_score;

    @Bind(R.id.mypage_trapping_level) // 3.트래핑
            TextView tx_trapping_level;

    @Bind(R.id.mypage_trapping_socre)
    TextView tx_trapping_socre;

    @Bind(R.id.mypage_flib_level) // 4.플립
            TextView tx_flib_level;

    @Bind(R.id.mypage_flib_score)
    TextView tx_flib_socre;

    @Bind(R.id.mypage_drible_level) // 5.드리블
            TextView tx_drible_level;

    @Bind(R.id.mypage_drible_score)
    TextView tx_drible_socre;

    @Bind(R.id.mypage_crosba_level) // 6.크로스바 대체 예정 complex
            TextView tx_crosbal_level;

    @Bind(R.id.mypage_crosba_score)
    TextView tx_crosbal_socre;

    //친구요청 관련
    @Bind(R.id.friend_btn_follow)
    Button friend_btn_follow;

    @Bind(R.id.friend_btn)
    Button friend_btn;

    @Bind(R.id.li_friend)
    LinearLayout li_friend;

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart() 사용자 프로필 조회 =================================");

        if(pageFlag.equals("me")){
            findUser = user;
            li_friend.setVisibility(View.GONE);


        }else{
            youUid = getParamInt.getExtras().getInt("frienduid");
            Log.i(TAG,"youUid : " + youUid );
            findUser.setUid(youUid);
            li_friend.setVisibility(View.VISIBLE);

            //친구요청 중인지 아닌지 로직
            getFriendStatus(friend_btn);

            //팔로우 버튼 설정
            getFollowTransaction(user.getUid(),youUid,"getCount");

        }

        getProfileUser(findUser.getUid());
        qUserMission.setUid(findUser.getUid());
        userGrid_MissionList(qUserMission);


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
        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);
        UserService service = ServiceGenerator.createService(UserService.class,getApplicationContext(),user);

        Call<User> userCall = service.getProfileUser(uid);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    findUser = response.body();
                    dialog.dismiss();
                    Log.i(TAG,"1.조회 유저정보는 : " + findUser.toString());

                    if(youUid!=0){
                        mypage_title.setText(findUser.getUsername());
                    }

                    if(!Compare.isEmpty(findUser.getProfileimgurl())) {
                        Glide.with(MyPageActivity.this)
                                .load(findUser.getProfileimgurl())
                                .into(mypageImage);


                        //리니어 레이아웃에 블러드 효과 주기
                        Glide.with(MyPageActivity.this)
                                .load(findUser.getProfileimgurl())
                                .asBitmap().transform(new BlurTransformation(MyPageActivity.this, 10))
                                .into(mypageBackImage);
                    }

                    /*************************************************************************************
                     //====>  액티비티 모든 뷰에 정보를 매핑하는 작업(TextView 등등)
                     **************************************************************************************/

                    tx_user_name.setText(findUser.getUsername());

                    if(findUser.getTeamname()==null){
                        tx_teamname.setText(R.string.user_team_yet_join);
                    }else{
                        tx_teamname.setText(findUser.getTeamname());
                    }

                    /*
                    TextView tx_coachname;
                    TextView tx_total_ranking;
                    TextView tx_total_count;
                    TextView tx_friend_ranking;
                    TextView tx_friend_count;
                    TextView tx_team_ranking;
                    TextView tx_team_count;
                    TextView tx_fllowerCount;
                    TextView tx_fllowingCount;
                    TextView tx_lifting_level;
                    TextView tx_lifting_score;
                    TextView tx_around_level;
                    TextView tx_around_score;
                    TextView tx_trapping_level;
                    TextView tx_trapping_socre;
                    TextView tx_flib_level;
                    TextView tx_flib_socre;
                    TextView tx_drible_level;
                    TextView tx_drible_socre;
                    TextView tx_crosbal_level;
                    TextView tx_crosbal_socre;
                    */

                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });

    }


    //영상 목록
    public void userGrid_MissionList(final UserMission userMission){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);

        /*
        final AlertDialog dialog = new SpotsDialog(this, R.style.Custom);
        dialog.show();
        */

        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,getApplicationContext(),user);
        Call<List<UserMission>> call = userMissionService.getUserMissionList(userMission);
        call.enqueue(new Callback<List<UserMission>>() {
            @Override
            public void onResponse(Call<List<UserMission>> call, Response<List<UserMission>> response) {

                if(response.isSuccessful()){
                    userMissions = response.body();
                    gridMissionAdapter = new GridMissionAdapter(getApplicationContext(),R.layout.adapter_user_mission_grid_item,userMissions,"ME");
                    videoGridView.setAdapter(gridMissionAdapter);
                    dialog.dismiss();

                    if(userMissions.size()==0){
                        noData_textView.setVisibility(View.VISIBLE);
                        noData_textView.setText("등록된 영상이 없습니다");
                    }else{
                        noData_textView.setVisibility(View.INVISIBLE);
                    }

                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<UserMission>> call, Throwable t) {
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.friend_btn_follow)
    public void friend_btn_follow(){
        //VeteranToast.makeToast(getApplicationContext(),"값은 : " + FollowCount,Toast.LENGTH_SHORT).show();
        if(FollowCount==0){
            getFollowTransaction(user.getUid(),youUid,"saveFollow");
        }else{
            getFollowTransaction(user.getUid(),youUid,"cancelFollow");
        }
    }


    @OnClick(R.id.mypage_bell)
    public void uprofile_bell(){
        VeteranToast.makeToast(getApplicationContext(),"준비중",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume() ===================================");
    }


    //팔러우를 했는지 알아보기 위한 사전 작업
    public void getFollowTransaction(int uid, int followuid,String trFlag){

        FollowManage followManage = new FollowManage();
        followManage.setUid(uid);
        followManage.setFollowuid(followuid);

        FriendService friendService = ServiceGenerator.createService(FriendService.class,this,user);

        //final ProgressDialog dialog;
        //dialog = ProgressDialog.show(this, "",getString(R.string.network_get_list), true);
        //dialog.show();

        if(trFlag.equals("getCount")){

            Call<ServerResult> call = friendService.getFollowUserCount(followManage);
            call.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        //dialog.dismiss();
                        ServerResult result = response.body();
                        if(result.getCount()==0){
                            friend_btn_follow.setText(getString(R.string.friedn_follow));
                            FollowCount=0;
                        }else{
                            friend_btn_follow.setText(getString(R.string.friedn_follow_cancel));
                            FollowCount=1;
                        };


                    }else{
                        //dialog.dismiss();
                        //VeteranToast.makeToast(getApplicationContext(),"Follow Info Error(1) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                        FollowCount=0;
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //dialog.dismiss();
                    //VeteranToast.makeToast(getApplicationContext(),"Follow Info Error(2) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }else if(trFlag.equals("saveFollow")){
            Call<ServerResult> call = friendService.saveFollow(followManage);
            call.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        //dialog.dismiss();
                        ServerResult result = response.body();
                        friend_btn_follow.setText(getString(R.string.friedn_follow_cancel));
                        FollowCount=1;
                        //VeteranToast.makeToast(getApplicationContext(), findUser.getUsername() + getString(R.string.follow_start),Toast.LENGTH_SHORT).show();
                    }else{
                        //dialog.dismiss();
                        //VeteranToast.makeToast(getApplicationContext(),"Follow Info Error(3) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //dialog.dismiss();
                    //VeteranToast.makeToast(getApplicationContext(),"Follow Info Error(4) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }else if(trFlag.equals("cancelFollow")){
            Call<ServerResult> call = friendService.deleteFollow(followManage);
            call.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        //dialog.dismiss();
                        ServerResult result = response.body();
                        friend_btn_follow.setText(getString(R.string.friedn_follow));
                        FollowCount=0;
                        //VeteranToast.makeToast(getApplicationContext(), findUser.getUsername() + getString(R.string.follow_end),Toast.LENGTH_SHORT).show();
                    }else{
                        //dialog.dismiss();
                        //VeteranToast.makeToast(getApplicationContext(),"Follow Info Error(5) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //dialog.dismiss();
                    //VeteranToast.makeToast(getApplicationContext(),"Follow Info Error(6) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
    }

    /***************************************************************
     *  친구 요청 관련 기능
     * */

    //DB 상태 조회
    public void getFriendStatus(Button button){

        Log.i(TAG,"친구 요청 이력을 가져 옵니다");

        FriendApply qfriendApply = new FriendApply();
        qfriendApply.setRequid(user.getUid());
        qfriendApply.setResuid(youUid);

        FriendService friendService = ServiceGenerator.createService(FriendService.class,this,user);
        Call<FriendApply> call = friendService.getFriendApply(qfriendApply);

        call.enqueue(new Callback<FriendApply>() {
            @Override
            public void onResponse(Call<FriendApply> call, Response<FriendApply> response) {
                if(response.isSuccessful()){
                    friendApply = response.body();
                    Log.i(TAG,"친구 요청 이력을 가져 옵니다" + friendApply.toString());
                    if(friendApply.getApplyid()==0){
                        friend_btn.setText(getString(R.string.friedn_friend_request));
                    }else{
                        if(friendApply.getFlag().equals("REQUEST")){
                            friend_btn.setText(getString(R.string.friedn_friend_request_process));
                        }else if(friendApply.getFlag().equals("REJECT")){
                            friend_btn.setText(getString(R.string.friedn_friend_reject));
                        }else if(friendApply.getFlag().equals("FRIEND")){
                            friend_btn.setText(getString(R.string.friedn_friend_cancel));
                        }
                    }

                }else{
                    Log.i(TAG,"에러 입니다");
                }
            }

            @Override
            public void onFailure(Call<FriendApply> call, Throwable t) {
                Log.i(TAG,"에러 입니다");
                t.printStackTrace();
            }
        });

    }

    // 친구요청하기
    @OnClick(R.id.friend_btn)
    public void friendBtn(){

        new DialogBuilder(MyPageActivity.this)
                //.setTitle("Title")
                .setMessage(getString(R.string.preparation))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
        return;

/*        if(friendApply.getApplyid()==0){

            new DialogBuilder(MyPageActivity.this)
                    //.setTitle("Title")
                    .setMessage(getString(R.string.friedn_req))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestFriend();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else if(friendApply.getFlag().equals("REJECT")){ //친구요청을 거절한 상태
            new DialogBuilder(MyPageActivity.this)
                    //.setTitle("Title")
                    .setMessage(getString(R.string.friedn_reject_req))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestFriend();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else if(friendApply.getFlag().equals("FRIEND")){ //이미친구 친구 끈기
            new DialogBuilder(MyPageActivity.this)
                    //.setTitle("Title")
                    .setMessage(getString(R.string.friedn_stop_req))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else if(friendApply.getFlag().equals("REQUEST")){ //친구요청 한 상태
            new DialogBuilder(MyPageActivity.this)
                    //.setTitle("Title")
                    .setMessage(getString(R.string.friedn_alrady_req))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
            return;
        }*/
    }

    public void requestFriend(){
        WaitingDialog.showWaitingDialog(this,false);
        FriendApply friendApply = new FriendApply();
        friendApply.setRequid(user.getUid());
        friendApply.setResuid(youUid);
        friendApply.setFlag("REQUEST"); // "REJECT","FRIEND"

        FriendService friendService = ServiceGenerator.createService(FriendService.class,this,user);
        Call<ServerResult> c = friendService.reqFriend(friendApply);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    REQ_FRIEND = result.getResult();
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

    public void requestFriendStop(){
        WaitingDialog.showWaitingDialog(this,false);
        FriendApply friendApply = new FriendApply();
        friendApply.setRequid(user.getUid());
        friendApply.setResuid(youUid);

        FriendService friendService = ServiceGenerator.createService(FriendService.class,this,user);
        Call<ServerResult> c = friendService.reqFriend(friendApply);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    REQ_FRIEND = result.getResult();
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
