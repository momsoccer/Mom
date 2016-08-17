package com.mom.soccer.bottommenu;

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
import com.mom.soccer.dto.FriendApply;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.UserMissionActivity;
import com.mom.soccer.retrofitdao.FollowService;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Bind(R.id.btnfollow)
    Button btnfollow;  //팔로워

    @Bind(R.id.btnfollowing)
    Button btnfollowing; //팔로잉

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
                    for(int i=0 ; i < mapVo.size();i++){
                        if(mapVo.get(i).getType().equals("M")){
                            btnfollow.setText(getString(R.string.follow_toolbar_page_text)+"("+String.valueOf(mapVo.get(i).getCount())+")");
                        }else{
                            btnfollowing.setText(getString(R.string.follow_view_pager)+"("+String.valueOf(mapVo.get(i).getCount())+")");
                        }
                    }
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
        if(pageFlag.equals("me")){
            Intent intent = new Intent(MyPageActivity.this,FollowActivity.class);
            startActivity(intent);
        }else{
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.f_layout_not_see),Toast.LENGTH_SHORT).show();
        }
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

}
