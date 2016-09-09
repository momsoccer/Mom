package com.mom.soccer.ball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.adapter.BoardLineItemAdapter;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.Team;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamBoardReply extends AppCompatActivity {

    private static final String TAG = "TeamBoardReply";
    private Activity activity;
    private PrefUtil prefUtil;
    private User user;
    private Team team;

    @Bind(R.id.action_bar_title)
    TextView action_bar_title;

    @Bind(R.id.content)
    TextView content;

    @Bind(R.id.txt_username)
    TextView txt_username;

    ImageView userimg;

    @Bind(R.id.txt_date)
    TextView txt_date;

    @Bind(R.id.boardlineCount)
    TextView boardlineCount;

    @Bind(R.id.replyRecview)
    RecyclerView replyRecview;

    @Bind(R.id.comment)
    EditText comment;

    @Bind(R.id.sendBtn)
    Button sendBtn;

    private Intent intent;
    private int boardid = 0;
    private MomBoard momBoard;
    private BoardLineItemAdapter boardLineItemAdapter;
    private List<MomBoard> momBoardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_board_reply);
        ButterKnife.bind(this);
        activity = this;
        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        intent = getIntent();
        boardid = intent.getExtras().getInt("boardid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        userimg = (ImageView) findViewById(R.id.userimg);

        if(!Compare.isEmpty(user.getProfileimgurl())){
            Glide.with(getApplicationContext())
                    .load(user.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(userimg);
        }

        if(!Compare.isEmpty(user.getUsername())){
            txt_username.setText(user.getUsername());
        }

        WaitingDialog.showWaitingDialog(activity,false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getApplicationContext(),user);

        MomBoard query = new MomBoard();
        query.setBoardid(boardid);
        Call<MomBoard> c = momBoardService.getBoardHeader(query);
        c.enqueue(new Callback<MomBoard>() {
            @Override
            public void onResponse(Call<MomBoard> call, Response<MomBoard> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    momBoard = response.body();

                    action_bar_title.setText(momBoard.getTeamname());
                    txt_date.setText(momBoard.getChange_creationdate());
                    content.setText(momBoard.getContent());

                    String replayString = getString(R.string.momboard_comment_msg) +" "+ String.valueOf(momBoard.getLikecount())+getString(R.string.momboard_comment_ea);
                    boardlineCount.setText(replayString);
                }
            }

            @Override
            public void onFailure(Call<MomBoard> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });


        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendBtn.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendBtn.setEnabled(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart ---------------------------------------------------------------------");

        WaitingDialog.showWaitingDialog(activity,false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getApplicationContext(),user);
        MomBoard query = new MomBoard();
        query.setBoardid(boardid);
        Call<List<MomBoard>> c = momBoardService.getBoardLineList(query);
        c.enqueue(new Callback<List<MomBoard>>() {
            @Override
            public void onResponse(Call<List<MomBoard>> call, Response<List<MomBoard>> response) {
                if(response.isSuccessful()){
                    momBoardList = response.body();
                    replyRecview.setHasFixedSize(true);
                    replyRecview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    boardLineItemAdapter = new BoardLineItemAdapter(activity,momBoardList,user);

                    replyRecview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    replyRecview.getItemAnimator().setAddDuration(300);
                    replyRecview.getItemAnimator().setRemoveDuration(300);
                    replyRecview.getItemAnimator().setMoveDuration(300);
                    replyRecview.getItemAnimator().setChangeDuration(300);
                    replyRecview.setHasFixedSize(true);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(boardLineItemAdapter);
                    alphaAdapter.setDuration(500);
                    replyRecview.setAdapter(boardLineItemAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<MomBoard>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.sendBtn)
    public void sendBtn(){
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getApplicationContext(),user);
        MomBoard momBoard = new MomBoard();
    }


}
