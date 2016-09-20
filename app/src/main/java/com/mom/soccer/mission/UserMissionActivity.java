package com.mom.soccer.mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.adapter.BoardListAdapter;
import com.mom.soccer.adapter.FeedBackAllListAdapter;
import com.mom.soccer.adapter.PassListAdapter;
import com.mom.soccer.board.BoardMainActivity;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.ExpandableHeightListView;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.Board;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.FollowManage;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.dto.MyBookMark;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.fragment.UserMissionFragment;
import com.mom.soccer.retrofitdao.BoardService;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retrofitdao.FollowService;
import com.mom.soccer.retrofitdao.MissionPassService;
import com.mom.soccer.retrofitdao.MissionService;
import com.mom.soccer.retrofitdao.PickService;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;
import com.mom.soccer.youtubeplayer.YoutubePlayerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMissionActivity extends AppCompatActivity
{

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

    Mission mission;

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


    @Bind(R.id.li_mission_info)
    LinearLayout li_mission_info;

    @Bind(R.id.li_board_tab_layout)
    LinearLayout li_board_tab_layout;

    @Bind(R.id.board_list_view)
    ExpandableHeightListView board_list_view;

    BoardListAdapter boardListAdapter;
    List<Board> boardList;


    @Bind(R.id.mission_message)
    TextView mission_message;

    //색갈조정
    LinearLayout li_back_layout,li_bottom_layout,li_icon_layout;

    RecyclerView passrecyclerview,feedbackRecylerView;
    PassListAdapter passListAdapter;
    List<MissionPass> missionPasses = new ArrayList<>();

    FeedBackAllListAdapter feedBackAllListAdapter;
    private List<FeedbackHeader> feedbackHeaders;


    @Bind(R.id.li_pass_history)
    LinearLayout li_pass_history;

    //심사없을때
    @Bind(R.id.li_pass_no_found)
    LinearLayout li_pass_no_found;

    //피드백 없을때
    @Bind(R.id.li_feedback_no_found)
    LinearLayout li_feedback_no_found;

    @Bind(R.id.youtube_YouTubeThumbnailView)
    YouTubeThumbnailView youtube_YouTubeThumbnailView;

    @Bind(R.id.missionprecon)
    TextView missionprecon;

    @Bind(R.id.missionname)
    TextView missionname;

    @Bind(R.id.missiondisp)
    TextView missiondisp;

    @Bind(R.id.li_board_no_found)
    LinearLayout li_board_no_found;

    //화면에서 댓글입력
    //@Bind(R.id.et_board_contnet)
    //EditText et_board_contnet;

    //통과관련
    @Bind(R.id.mission_pass_li)
    LinearLayout mission_pass_li;

    @Bind(R.id.mission_sequence)
    TextView mission_sequence;

    @Bind(R.id.mission_medal)
    ImageView mission_medal;

    @Bind(R.id.ic_board_write)
    ImageButton ic_board_write;

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


        if(userMission.getPassflag().equals("Y")){
            mission_pass_li.setVisibility(View.VISIBLE);
            mission_sequence.setText("Level : "+userMission.getSequence());
            mission_message.setText(getString(R.string.user_mission_d_title8));

            if(userMission.getMissiontype().equals("DRIBLE")){
                mission_medal.setImageDrawable(getResources().getDrawable(R.drawable.batch_drible_a));
            }else if(userMission.getMissiontype().equals("LIFTING")){
                mission_medal.setImageDrawable(getResources().getDrawable(R.drawable.batch_lifting_a));
            }else if(userMission.getMissiontype().equals("TRAPING")){
                mission_medal.setImageDrawable(getResources().getDrawable(R.drawable.batch_traping_a));
            }else if(userMission.getMissiontype().equals("AROUND")){
                mission_medal.setImageDrawable(getResources().getDrawable(R.drawable.batch_around_a));
            }else if(userMission.getMissiontype().equals("FLICK")){
                mission_medal.setImageDrawable(getResources().getDrawable(R.drawable.batch_flick_a));
            }else if(userMission.getMissiontype().equals("COMPLEX")){
                mission_medal.setImageDrawable(getResources().getDrawable(R.drawable.batch_complex_a));
            }

        }else{
            mission_pass_li.setVisibility(View.GONE);
        }

        li_back_layout = (LinearLayout) findViewById(R.id.li_back_layout);   //바탕 이미지
        li_bottom_layout = (LinearLayout) findViewById(R.id.li_bottom_layout);  //바탕 선
        li_icon_layout = (LinearLayout) findViewById(R.id.li_icon_layout);  //바탕 색
        //바탕색


        Log.i(TAG,"미션 타입은요 .... " + userMission.getMissiontype());

        if(userMission.getMissiontype().equals("DRIBLE")){

            li_back_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_drible));
            li_bottom_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_drible));


        }else if(userMission.getMissiontype().equals("LIFTING")){

            li_back_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_lifting));
            li_bottom_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_lifting));


        }else if(userMission.getMissiontype().equals("TRAPING")){

            li_back_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_traping));
            li_bottom_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_traping));


        }else if(userMission.getMissiontype().equals("AROUND")){

            li_back_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_around));
            li_bottom_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_around));

        }else if(userMission.getMissiontype().equals("FLICK")){

            li_back_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_flick));
            li_bottom_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_flick));


        }else if(userMission.getMissiontype().equals("COMPLEX")){

            li_back_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_complex));
            li_bottom_layout.setBackground(getResources().getDrawable(R.drawable.xml_back_complex));


        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_missionview_title_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

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


        //어댑터 설정
        passrecyclerview = (RecyclerView)findViewById(R.id.passrecyclerview);

        //탭1,탭2
        li_board_tab_layout.setVisibility(View.VISIBLE);
        li_mission_info.setVisibility(View.GONE);

        passHistory();
        youtube_YouTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMissionActivity.this,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,mission.getYoutubeaddr());
                startActivity(intent);
            }
        });

        //floating button
        /*
        final ImageView fabIconNew = new ImageView(this);

        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_color_white_24dp));
        int redActionButtonSize = 70;
        int redActionButtonMargin = 10;

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconNew.setLayoutParams(starParams);

        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_RIGHT_CENTER)
                .build();

        rightLowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(UserMissionActivity.this, BoardMainActivity.class);
                intent1.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMission);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        });
        */
        ic_board_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(UserMissionActivity.this, BoardMainActivity.class);
                intent1.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMission);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        });
    }

    public void tabChangeClick(View v){
        switch (v.getId()){
            case R.id.tabBtn1:
                li_board_tab_layout.setVisibility(View.VISIBLE);
                li_mission_info.setVisibility(View.GONE);
            break;

            case R.id.tabBtn2:
                li_board_tab_layout.setVisibility(View.GONE);
                li_mission_info.setVisibility(View.VISIBLE);
                break;


        }
    }

    public void getBoarderList(){
        WaitingDialog.showWaitingDialog(UserMissionActivity.this,false);
        BoardService boardService = ServiceGenerator.createService(BoardService.class,this,user);

        Board board = new Board();
        board.setUsermissionid(userMission.getUsermissionid());
        board.setUid(userMission.getUid());
        Call<List<Board>> call = boardService.getboardlist(board);

        call.enqueue(new Callback<List<Board>>() {
            @Override
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){

                    List<Board> boardList = response.body();

                    if(boardList.size()==0){
                        li_board_no_found.setVisibility(View.VISIBLE);
                    }else{
                        li_board_no_found.setVisibility(View.GONE);
                    }
                    boardListAdapter = new BoardListAdapter(UserMissionActivity.this,boardList,user.getUid(),user,userMission);
                    board_list_view.setExpanded(true);
                    board_list_view.setAdapter(boardListAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Board>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
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

        //댓글리스트
        getBoarderList();

        //시드미션
        getMission();

        //피드백 리스트
        getFeedBack();

        //UserMissionFragment 유투브 플래그먼트
        UserMissionFragment youtubeFragment = new UserMissionFragment(this,userMission.getYoutubeaddr());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.youtube_seed_frame_layout,youtubeFragment,"");
        tc.commit();

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
    @OnClick(R.id.shrareBtn)
    public void shrareBtn(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, user.getUsername() +" : "+userMission.getSubject());
        intent.putExtra(Intent.EXTRA_TEXT,"https://youtu.be/"+userMission.getYoutubeaddr());
        intent.putExtra(Intent.EXTRA_TITLE, userMission.getDescription());
        startActivity(Intent.createChooser(intent, "Mom Soccer"));
    }


    public void passHistory(){
        if(!Compare.isEmpty(userMission.getPassflag())){

            WaitingDialog.showWaitingDialog(UserMissionActivity.this,false);
            MissionPassService service = ServiceGenerator.createService(MissionPassService.class,getApplicationContext(),user);

            MissionPass query = new MissionPass();
            query.setUid(userMission.getUid());
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
                            li_pass_no_found.setVisibility(View.VISIBLE);
                        }else{
                            li_pass_no_found.setVisibility(View.GONE);
                        }

                        passListAdapter = new PassListAdapter(UserMissionActivity.this,missionPasses,user,userMission.getPassflag(),"Y");
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

    public void getMission(){
        WaitingDialog.showWaitingDialog(UserMissionActivity.this,false);
        MissionService missionService = ServiceGenerator.createService(MissionService.class,getApplicationContext(),user);
        Mission query = new Mission();
        query.setMissionid(userMission.getMissionid());
        Call<Mission> c = missionService.getMission(query);
        c.enqueue(new Callback<Mission>() {
            @Override
            public void onResponse(Call<Mission> call, Response<Mission> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    mission = response.body();

                    missionname.setText(mission.getMissionname());
                    missionprecon.setText(mission.getPrecon());
                    missiondisp.setText(mission.getDescription());

                    youtube_YouTubeThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                            youTubeThumbnailLoader.setVideo(mission.getYoutubeaddr());
                        }

                        @Override
                        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Mission> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void getFeedBack(){

        WaitingDialog.showWaitingDialog(UserMissionActivity.this,false);
        FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,getApplicationContext(),user);
        FeedbackHeader header = new FeedbackHeader();

        header.setUid(userMission.getUid());
        header.setUsermissionid(userMission.getUsermissionid());

        Call<List<FeedbackHeader>> c = feedBackService.getFeedAllList(header);
        c.enqueue(new Callback<List<FeedbackHeader>>() {
            @Override
            public void onResponse(Call<List<FeedbackHeader>> call, Response<List<FeedbackHeader>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){

                    feedbackHeaders = response.body();

                    if(feedbackHeaders.size()==0){
                        li_feedback_no_found.setVisibility(View.VISIBLE);
                    }else{
                        li_feedback_no_found.setVisibility(View.GONE);
                    }

                    feedbackRecylerView = (RecyclerView)findViewById(R.id.feedbackRecylerView);
                    feedBackAllListAdapter = new FeedBackAllListAdapter(UserMissionActivity.this,feedbackHeaders,user,"Y");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();


            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
