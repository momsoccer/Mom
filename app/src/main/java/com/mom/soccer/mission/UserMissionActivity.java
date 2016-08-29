package com.mom.soccer.mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.board.BoardMainActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.FollowManage;
import com.mom.soccer.dto.MyBookMark;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.fragment.YoutubeFragment;
import com.mom.soccer.retrofitdao.FollowService;
import com.mom.soccer.retrofitdao.PickService;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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

    //RecyclerView rvSample;
    //BoardItemAdapter adapter;
    //List<Board> boardList = new ArrayList<>();

    private SlidingUpPanelLayout mLayout;

    private static int followingCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG,"onCreate() ============================================ ");

        setContentView(R.layout.ac_user_mission_layout);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Intent intent = getIntent();
        userMission = (UserMission) intent.getSerializableExtra(MissionCommon.USER_MISSTION_OBJECT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_missionview_title_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        if(!Compare.isEmpty(user.getUsername())){
            pageTitle = "(" + user.getUsername() +") " + getString(R.string.toolbar_user_missionview_page);
        }else{
            pageTitle = getString(R.string.toolbar_user_missionview_page);
        }
        user_missionview_title.setText(pageTitle);

        //정보 적용
        text_userName.setText(userMission.getUsername());
        if(userMission.getTeamname() == null){
            text_teamName.setText(getString(R.string.user_team_yet_join));
        }else{
            text_teamName.setText(userMission.getTeamname());
        }
        textView_pickupCount.setText(String.valueOf(userMission.getBookmarkcount()));
        textView_commentCount.setText(userMission.getBoardcount());

        //YoutubeFragment 유투브 플래그먼트
        YoutubeFragment youtubeFragment = new YoutubeFragment(this,userMission.getYoutubeaddr());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.youtube_frame_layout,youtubeFragment,"");
        tc.commit();

        if(!Compare.isEmpty(userMission.getProfileimgurl())) {
            Glide.with(this)
                    .load(userMission.getProfileimgurl())
                    .into(image_user);
        }

        if(user.getUid() == userMission.getUid()){
            btn_fllow.setVisibility(View.GONE);
        }else{
            btn_fllow.setVisibility(View.VISIBLE);
        }

   /*      //RecyclerView apply
        rvSample = (RecyclerView) findViewById(R.id.rvSample);
       rvSample.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), rvSample ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Toast.makeText(getApplicationContext(),"하이하이",Toast.LENGTH_SHORT).show();
                        adapter.removeItem(0);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });


        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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

    //좋아요 클릭
    @OnClick(R.id.ac_user_mission_video_pickup)
    public void imageButton_pickup(){

        myBookMark.setUid(user.getUid());
        myBookMark.setUsermissionid(userMission.getUsermissionid());

        //관심영상 체크
        if(pickUpFlag=="Y"){
            pickService = ServiceGenerator.createService(PickService.class, this, user);
            //플래그가 Y일 경우 좋아요 한 상태입니다.
            Call<ServerResult> call = pickService.pickCancel(myBookMark);
            call.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if (response.isSuccessful()) {
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.user_video_like_cancel),Toast.LENGTH_LONG).show();
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

        }else {
            //플래그가 N일 경우 좋아요 안한 상태입.
            pickService = ServiceGenerator.createService(PickService.class, this, user);
            Log.d(TAG, "보낸 값 : " + myBookMark.toString());
            Call<ServerResult> call = pickService.pickVideo(myBookMark);
            call.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if (response.isSuccessful()) {
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.user_video_like_ok),Toast.LENGTH_LONG).show();
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
        Log.d(TAG,"onStart() ============================================ ");

        WaitingDialog.showWaitingDialog(this,false);

        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,this,user);
        UserMission quserMission = new UserMission();
        quserMission.setUsermissionid(userMission.getUsermissionid());
        Call<UserMission> call = userMissionService.getUserMission(quserMission);
        call.enqueue(new Callback<UserMission>() {
            @Override
            public void onResponse(Call<UserMission> call, Response<UserMission> response) {
                if(response.isSuccessful()){
                    WaitingDialog.cancelWaitingDialog();
                    userMission = response.body();
                    textView_commentCount.setText(userMission.getBoardcount());

                }else{
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

        //댓글 리스트
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
                if(response.isSuccessful()){
                    boardList = response.body();

                    Log.i(TAG,"RecyclerView===========================================================");
                    Log.i(TAG,"받은 값은 : " +boardList.size());

                    adapter = new BoardItemAdapter(getApplicationContext(),boardList);

                    rvSample.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    rvSample.getItemAnimator().setAddDuration(300);
                    rvSample.getItemAnimator().setRemoveDuration(300);
                    rvSample.getItemAnimator().setMoveDuration(300);
                    rvSample.getItemAnimator().setChangeDuration(300);

                    rvSample.setHasFixedSize(true);
                    rvSample.setLayoutManager(new LinearLayoutManager(UserMissionActivity.this));

                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                    alphaAdapter.setDuration(500);
                    rvSample.setAdapter(alphaAdapter);

                    WaitingDialog.cancelWaitingDialog();
                }else{
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
        Log.d(TAG,"onResume() ============================================ ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() ============================================ ");
    }

    //관심 영상 체크 상태 확인
    public void initialPickCheck(){

        WaitingDialog.showWaitingDialog(this,false);

        pickService = ServiceGenerator.createService(PickService.class, this, user);
        Call<ServerResult> call = pickService.getPickCount(user.getUid(),userMission.getUsermissionid());
        call.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    ServerResult serverResult = response.body();
                    if(serverResult.getCount()==0){
                        pickUpFlag="N";
                        imageButton_pickup.setImageResource(R.drawable.ic_white_hart);
                    }else{
                        pickUpFlag="Y";
                        imageButton_pickup.setImageResource(R.drawable.ic_hart_red);
                    }
                    WaitingDialog.cancelWaitingDialog();
                }else{
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
    public void boardMain(){
        Intent intent = new Intent(this, BoardMainActivity.class);
        intent.putExtra("usermissionid",userMission.getUsermissionid());
        intent.putExtra("missionuid",userMission.getUid());
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    //팔로잉 관련 트랜잭션
    @OnClick(R.id.btn_fllow)
    public void followBtn(){

        Log.i(TAG,"팔로우 상태는 : " + followingCount);

        if(followingCount==0){
            followMethod("save");
        }else{
            followMethod("cancel");
        }
    }

    public void followMethod(String followType){

        WaitingDialog.showWaitingDialog(this,false);
        final FollowManage manage = new FollowManage();
        manage.setUid(user.getUid());
        manage.setFollowuid(userMission.getUid());

        FollowService service = ServiceGenerator.createService(FollowService.class,this,user);

        if(followType.equals("save")){
            Call<ServerResult> c = service.saveFollow(manage);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        WaitingDialog.cancelWaitingDialog();
                        btn_fllow.setText(getString(R.string.app_fllow_cancel));
                        VeteranToast.makeToast(getApplicationContext(),userMission.getUsername()+getString(R.string.follow_pickup),Toast.LENGTH_SHORT).show();
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
                        btn_fllow.setText(getString(R.string.app_fllow));
                        VeteranToast.makeToast(getApplicationContext(),userMission.getUsername()+getString(R.string.follow_pickup_cancel),Toast.LENGTH_SHORT).show();
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
                            btn_fllow.setText(getString(R.string.app_fllow));
                        }else{
                            //팔로우 유저일 경우
                            btn_fllow.setText(getString(R.string.app_fllow_cancel));
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

}
