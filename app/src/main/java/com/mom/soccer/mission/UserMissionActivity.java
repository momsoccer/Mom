package com.mom.soccer.mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.board.BoardMainActivity;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.FollowManage;
import com.mom.soccer.dto.MyBookMark;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.fragment.UserMissionFragment;
import com.mom.soccer.retrofitdao.FollowService;
import com.mom.soccer.retrofitdao.PickService;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMissionActivity extends AppCompatActivity {

    private static final String TAG = "UserMissionActivity";
    private UserMission userMission;

    @Bind(R.id.ac_user_mission_image)
    ImageView image_user;

    @Bind(R.id.ac_user_mission_name)
    TextView text_userName;

    @Bind(R.id.ac_user_mission_team)
    TextView text_teamName;

    @Bind(R.id.btn_fllow)
    Button btn_fllow;

    @Bind(R.id.ac_user_mission_video_pickup)
    ImageButton imageButton_pickup;

    @Bind(R.id.ac_user_mission_video_comment)
    ImageButton imageButton_comment;

    @Bind(R.id.ac_user_mission_video_pickcount)
    TextView textView_pickupCount;

    @Bind(R.id.ac_user_mission_video_commentcount)
    TextView textView_commentCount;

    User user;
    PrefUtil prefUtil;
    MyBookMark myBookMark = new MyBookMark();
    PickService pickService;

    private String pickUpFlag = "N";

    @Bind(R.id.user_missionview_title)
    TextView user_missionview_title;

    String pageTitle;

    private static int followingCount = 0;

    @Bind(R.id.level)
    TextView level;

    @Bind(R.id.tx_score)
    TextView tx_score;

    @Bind(R.id.tx_date)
    TextView tx_date;

    //view...sliding
    ViewPager viewPager;
    PagerSlidingTabStrip tabsStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate() ============================================ ");

        setContentView(R.layout.ac_user_mission_layout);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Intent intent = getIntent();
        userMission = (UserMission) intent.getSerializableExtra(MissionCommon.USER_MISSTION_OBJECT);

        Log.i(TAG,"userMission : " + userMission.toString());

/*        if(userMission.getPassflag().equals("Y")){
            missionClear.setVisibility(View.VISIBLE);
        }else{
            missionClear.setVisibility(View.GONE);
        }*/


        Toolbar toolbar = (Toolbar) findViewById(R.id.user_missionview_title_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        if (!Compare.isEmpty(user.getUsername())) {
            pageTitle = "(" + user.getUsername() + ") " + getString(R.string.toolbar_user_missionview_page);
        } else {
            pageTitle = getString(R.string.toolbar_user_missionview_page);
        }
        user_missionview_title.setText(pageTitle);
        level.setText(String.valueOf(userMission.getLevel()));

        //정보 적용
        text_userName.setText(userMission.getUsername());
        if (userMission.getTeamname() == null) {
            text_teamName.setText(getString(R.string.user_team_yet_join));
        } else {
            text_teamName.setText(userMission.getTeamname());
        }

        textView_pickupCount.setText(String.valueOf(userMission.getBookmarkcount()));
        textView_commentCount.setText(userMission.getBoardcount());
        tx_score.setText(getString(R.string.user_mission_detail_tx3) +":"+userMission.getTotalscore());
        tx_date.setText(userMission.getChange_creationdate());

        //YoutubeFragment 유투브 플래그먼트
/*        YoutubeFragment youtubeFragment = new YoutubeFragment(this, userMission.getYoutubeaddr());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.youtube_frame_layout, youtubeFragment, "");
        tc.commit();*/

        if (!Compare.isEmpty(userMission.getProfileimgurl())) {
            Glide.with(this)
                    .load(userMission.getProfileimgurl())
                    .into(image_user);
        }

        if (user.getUid() == userMission.getUid()) {
            btn_fllow.setVisibility(View.GONE);
        } else {
            btn_fllow.setVisibility(View.VISIBLE);
        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        UserMissionPagerAdapter pagerAdapter = new UserMissionPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.pagertabs);

        //viewPager.setCurrentItem(pageCall);

        //setting
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.enabled_red));
        tabsStrip.setTextColor(getResources().getColor(R.color.color6));
        tabsStrip.setViewPager(viewPager);
    }

    public class UserMissionPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 2;

        private String tabTitles[] = new String[] {
            "미션관련",getString(R.string.user_mission_d_title2)
        };

        public UserMissionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return UserMissionFragment.newInstance(position + 1,user,userMission);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //홈버튼클릭시
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //좋아요 클릭
    @OnClick(R.id.ac_user_mission_video_pickup)
    public void imageButton_pickup() {

        myBookMark.setUid(user.getUid());
        myBookMark.setUsermissionid(userMission.getUsermissionid());

        //관심영상 체크
        if (pickUpFlag == "Y") {
            pickService = ServiceGenerator.createService(PickService.class, this, user);
            //플래그가 Y일 경우 좋아요 한 상태입니다.
            Call<ServerResult> call = pickService.pickCancel(myBookMark);
            call.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if (response.isSuccessful()) {
                        VeteranToast.makeToast(getApplicationContext(), getString(R.string.user_video_like_cancel), Toast.LENGTH_LONG).show();
                        ServerResult serverResult = response.body();
                        imageButton_pickup.setImageResource(R.drawable.ic_white_hart);
                        pickUpFlag = "N";

                        int likeCount = Integer.parseInt(textView_pickupCount.getText().toString());
                        likeCount = likeCount - 1;
                        textView_pickupCount.setText(String.valueOf(likeCount));

                    } else {
                        //VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });

        } else {
            //플래그가 N일 경우 좋아요 안한 상태입.
            pickService = ServiceGenerator.createService(PickService.class, this, user);
            Log.d(TAG, "보낸 값 : " + myBookMark.toString());
            Call<ServerResult> call = pickService.pickVideo(myBookMark);
            call.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if (response.isSuccessful()) {
                        VeteranToast.makeToast(getApplicationContext(), getString(R.string.user_video_like_ok), Toast.LENGTH_LONG).show();
                        ServerResult serverResult = response.body();
                        imageButton_pickup.setImageResource(R.drawable.ic_hart_red);
                        pickUpFlag = "Y";

                        int likeCount = Integer.parseInt(textView_pickupCount.getText().toString());
                        likeCount = likeCount + 1;
                        textView_pickupCount.setText(String.valueOf(likeCount));

                    } else {
                        //VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }

            });
        }
    }

    //댓글 남긴 정보 가져오기
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() ============================================ ");

        WaitingDialog.showWaitingDialog(this, false);

        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class, this, user);
        UserMission quserMission = new UserMission();
        quserMission.setUsermissionid(userMission.getUsermissionid());
        Call<UserMission> call = userMissionService.getUserMission(quserMission);
        call.enqueue(new Callback<UserMission>() {
            @Override
            public void onResponse(Call<UserMission> call, Response<UserMission> response) {
                if (response.isSuccessful()) {
                    WaitingDialog.cancelWaitingDialog();
                    userMission = response.body();
                    textView_commentCount.setText(userMission.getBoardcount());

                } else {
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<UserMission> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                t.printStackTrace();
            }
        });

        //팔로우
        followMethod("getcount");

        //좋아요체크
        initialPickCheck();


        /*
        WaitingDialog.showWaitingDialog(this,false);
        BoardService boardService = ServiceGenerator.createService(BoardService.class,this,user);

        Board board = new Board();
        board.setUsermissionid(userMission.getUsermissionid());
        board.setUid(userMission.getUid());
        Call<List<Board>> c = boardService.getboardlist(board);

        c.enqueue(new Callback<List<Board>>() {
            @Override
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    List<Board> boardList = response.body();

                    Log.i(TAG,"보더 자료는 : " + boardList.size());

                    boardListAdapter = new BoardItemAdapter(UserMissionActivity.this,boardList);
                    rvSample.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    rvSample.getItemAnimator().setAddDuration(300);
                    rvSample.getItemAnimator().setRemoveDuration(300);
                    rvSample.getItemAnimator().setMoveDuration(300);
                    rvSample.getItemAnimator().setChangeDuration(300);

                    rvSample.setHasFixedSize(true);
                    rvSample.setLayoutManager(new LinearLayoutManager(UserMissionActivity.this));

                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(boardListAdapter);
                    alphaAdapter.setDuration(500);
                    rvSample.setAdapter(alphaAdapter);
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<List<Board>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
        */


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() ============================================ ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() ============================================ ");
    }

    //관심 영상 체크 상태 확인
    public void initialPickCheck() {

        WaitingDialog.showWaitingDialog(this, false);

        pickService = ServiceGenerator.createService(PickService.class, this, user);
        Call<ServerResult> call = pickService.getPickCount(user.getUid(), userMission.getUsermissionid());
        call.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if (response.isSuccessful()) {
                    ServerResult serverResult = response.body();
                    if (serverResult.getCount() == 0) {
                        pickUpFlag = "N";
                        imageButton_pickup.setImageResource(R.drawable.ic_white_hart);
                    } else {
                        pickUpFlag = "Y";
                        imageButton_pickup.setImageResource(R.drawable.ic_hart_red);
                    }
                    WaitingDialog.cancelWaitingDialog();
                } else {
                    //VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_LONG).show();
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                //VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    //댓글쓰기
    @OnClick(R.id.ac_user_mission_video_comment)
    public void boardMain() {
        Intent intent = new Intent(this, BoardMainActivity.class);
        intent.putExtra("usermissionid", userMission.getUsermissionid());
        intent.putExtra("missionuid", userMission.getUid());
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    //팔로잉 관련 트랜잭션
    @OnClick(R.id.btn_fllow)
    public void followBtn() {

        Log.i(TAG, "팔로우 상태는 : " + followingCount);

        if (followingCount == 0) {
            followMethod("save");
        } else {
            followMethod("cancel");
        }
    }

    public void followMethod(String followType) {

        WaitingDialog.showWaitingDialog(this, false);
        final FollowManage manage = new FollowManage();
        manage.setUid(user.getUid());
        manage.setFollowuid(userMission.getUid());

        FollowService service = ServiceGenerator.createService(FollowService.class, this, user);

        if (followType.equals("save")) {
            Call<ServerResult> c = service.saveFollow(manage);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if (response.isSuccessful()) {
                        WaitingDialog.cancelWaitingDialog();
                        btn_fllow.setText(getString(R.string.app_fllow_cancel));
                        VeteranToast.makeToast(getApplicationContext(), userMission.getUsername() + getString(R.string.follow_pickup), Toast.LENGTH_SHORT).show();
                        followingCount = 1;
                    } else {
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                }
            });
        } else if (followType.equals("cancel")) {
            Call<ServerResult> c = service.deleteFollow(manage);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if (response.isSuccessful()) {
                        WaitingDialog.cancelWaitingDialog();
                        btn_fllow.setText(getString(R.string.app_fllow));
                        VeteranToast.makeToast(getApplicationContext(), userMission.getUsername() + getString(R.string.follow_pickup_cancel), Toast.LENGTH_SHORT).show();
                        followingCount = 0;
                    } else {
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                }
            });
        } else if (followType.equals("getcount")) {
            Call<ServerResult> c = service.getFollowUserCount(manage);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if (response.isSuccessful()) {
                        ServerResult result = response.body();
                        followingCount = result.getCount();

                        if (followingCount == 0) {
                            //팔로우 유저가 아닐경우
                            btn_fllow.setText(getString(R.string.app_fllow));
                        } else {
                            //팔로우 유저일 경우
                            btn_fllow.setText(getString(R.string.app_fllow_cancel));
                        }

                        WaitingDialog.cancelWaitingDialog();
                    } else {
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

    //유저 정보
    @OnClick(R.id.ac_user_mission_image)
    public void ac_user_mission_image(){
        Intent intent = new Intent(UserMissionActivity.this,MyPageActivity.class);

        if(user.getUid() == userMission.getUid()){
            intent.putExtra("pageflag","me");
        }else{
            intent.putExtra("pageflag","friend");
            intent.putExtra("frienduid",userMission.getUid());
        }
        finish();
        startActivity(intent);
    }

    //shrareBtn 카카오 공유

}
