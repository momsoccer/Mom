package com.mom.soccer.bottommenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.common.BlurTransformation;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.ExpandableHeightGridView;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.TeamMapVo;
import com.mom.soccer.dataDto.UserLevelDataVo;
import com.mom.soccer.dto.FriendApply;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.UserMissionActivity;
import com.mom.soccer.point.PointMainActivity;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.FollowService;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
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
    TextView mypage_team_count;

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
        }else{
            im_point.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart() 사용자 프로필 조회 =================================");

        if(pageFlag.equals("me")){
            findUser = user;
        }else{
            youUid = getParamInt.getExtras().getInt("frienduid");
            Log.i(TAG,"youUid : " + youUid );
            findUser.setUid(youUid);
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

}
