package com.mom.soccer.ball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.adapter.BoardLineItemAdapter;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.RecyclerItemClickListener;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.Team;
import com.mom.soccer.dto.User;
import com.mom.soccer.pubretropit.PubReport;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.ArrayList;
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
    private int posintion=0;

    View positiveAction;
    EditText report_content;

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

    @Bind(R.id.level)
    TextView level;

    private Intent intent;
    private int boardid = 0;
    private MomBoard momBoard;
    private BoardLineItemAdapter boardLineItemAdapter;
    private List<MomBoard> momBoardList = new ArrayList<>();

    private int lineCount = 0;

    @Bind(R.id.li_attach_image_group)
    LinearLayout li_attach_image_group;

    ImageView attchimageFile1,attchimageFile2,attchimageFile3;
    private boolean sendWrite = false;

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
        posintion = intent.getExtras().getInt("posintion");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        userimg = (ImageView) findViewById(R.id.userimg);

        attchimageFile1 = (ImageView) findViewById(R.id.attchimageFile1);
        attchimageFile2 = (ImageView) findViewById(R.id.attchimageFile2);
        attchimageFile3 = (ImageView) findViewById(R.id.attchimageFile3);

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

                    if(!Compare.isEmpty(momBoard.getUserimg())){
                        Glide.with(getApplicationContext())
                                .load(momBoard.getUserimg())
                                .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                                .into(userimg);
                    }

                    if(!Compare.isEmpty(momBoard.getUsername())){
                        txt_username.setText(momBoard.getUsername());
                    }

                    action_bar_title.setText(momBoard.getTeamname());
                    txt_date.setText(momBoard.getChange_creationdate());
                    content.setText(momBoard.getContent());
                    level.setText(String.valueOf(momBoard.getLevel()));
                    lineCount = momBoard.getCommentcount();
                    getLineCountText(lineCount);


                    if(momBoard.getFilecount()!=0){

                        li_attach_image_group.setVisibility(View.VISIBLE);

                        for(int i=0; i < momBoard.getBoardFiles().size() ; i++){

                            if(i==0){
                                Glide.with(activity)
                                        .load(momBoard.getBoardFiles().get(i).getFileaddr())
                                        .fitCenter()
                                        .crossFade()
                                        .into(attchimageFile1);
                            }else if(i==1){
                                Glide.with(activity)
                                        .load(momBoard.getBoardFiles().get(i).getFileaddr())
                                        .fitCenter()
                                        .crossFade()
                                        .into(attchimageFile2);
                            }else if(i==2){
                                Glide.with(activity)
                                        .load(momBoard.getBoardFiles().get(i).getFileaddr())
                                        .fitCenter()
                                        .crossFade()
                                        .into(attchimageFile3);
                            }

                        }

                    }else{
                        li_attach_image_group.setVisibility(View.GONE);
                    }

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

        replyRecview.addOnItemTouchListener(

                new RecyclerItemClickListener(getApplicationContext(), replyRecview ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                    }

                    //삭제하기
                    @Override public void onLongItemClick(View view, final int position) {
                        final MomBoard vo = boardLineItemAdapter.getBoardLine(position);

                        if(user.getUid()==vo.getUid()){
                            new MaterialDialog.Builder(activity)
                                    .content(R.string.board_main_coment_delete)
                                    .contentColor(activity.getResources().getColor(R.color.color6))
                                    .positiveText(R.string.mom_diaalog_confirm)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            boardLineItemAdapter.deleteLine(position);
                                            lineCount = lineCount - 1;
                                            getLineCountText(lineCount);
                                        }
                                    })
                                    .show();
                        }else{
                            MaterialDialog dialog = new MaterialDialog.Builder(activity)
                                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                    .title(R.string.mom_diaalog_board_report)
                                    .titleColor(getResources().getColor(R.color.color6))
                                    .customView(R.layout.dialog_report_view, true)
                                    .positiveText(R.string.momboard_edit_send)
                                    .negativeText(R.string.cancel)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Report report = new Report();
                                            report.setType(PubReport.REPORTTYPE_MOMBOARD_COMMENT);
                                            report.setUid(user.getUid());
                                            report.setReason(report_content.getText().toString());
                                            report.setContent(vo.getContent());
                                            report.setPublisherid(vo.getUid());
                                            PubReport.doReport(activity,report,user);
                                        }
                                    })
                                    .build();

                            report_content = (EditText) dialog.findViewById(R.id.report_content);
                            positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

                            report_content.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                                    positiveAction.setEnabled(s.toString().trim().length() > 0);
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });

                            dialog.show();
                            positiveAction.setEnabled(false);
                        }

                    }
                })
        );


    }

    @Override
    protected void onStart() {
        super.onStart();

        //댓글을 달았던 사람들 리스트

            getBoardLineList();

    }

    public void getLineCountText(int count){
        String replayString = getString(R.string.momboard_comment_msg) +" "+ String.valueOf(count)+getString(R.string.momboard_comment_ea);
        boardlineCount.setText(replayString);
    }

    @OnClick(R.id.sendBtn)
    public void sendBtn(){
        WaitingDialog.showWaitingDialog(activity,false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getApplicationContext(),user);
        MomBoard momBoard = new MomBoard();
        momBoard.setBoardid(boardid);
        momBoard.setUid(user.getUid());
        momBoard.setContent(comment.getText().toString());
        momBoard.setUsername(activity.getResources().getString(R.string.momboard_write2)+user.getUsername());

        Call<ServerResult> c = momBoardService.saveBoardLine(momBoard);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    if(result.getResult().equals("S")){

                        sendWrite = true;
                        getBoardLineList();
                        comment.setText(null);

                        replyRecview.scrollToPosition(lineCount-1);

                    }else{

                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.getMessage();
            }
        });

    }


    public void getBoardLineList(){

        WaitingDialog.showWaitingDialog(activity,false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getApplicationContext(),user);
        MomBoard query = new MomBoard();
        query.setBoardid(boardid);
        Call<List<MomBoard>> c = momBoardService.getBoardLineList(query);
        c.enqueue(new Callback<List<MomBoard>>() {
            @Override
            public void onResponse(Call<List<MomBoard>> call, Response<List<MomBoard>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){

                    List<MomBoard> momBoardList = response.body();
                    lineCount = momBoardList.size();

                    String replayString = getString(R.string.momboard_comment_msg) +" "+ String.valueOf(lineCount)+getString(R.string.momboard_comment_ea);
                    boardlineCount.setText(replayString);

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
                    replyRecview.scrollToPosition(momBoardList.size()-1); //입력후 맨아래 자기 자신의 글 보이기
                }
            }

            @Override
            public void onFailure(Call<List<MomBoard>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){

            Intent backIntent = new Intent();
            backIntent.putExtra("lineCount",lineCount);
            backIntent.putExtra("boardid",boardid);
            setResult(RESULT_OK, backIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        backIntent.putExtra("lineCount",lineCount);
        backIntent.putExtra("posintion",posintion);
        setResult(RESULT_OK, backIntent);
        finish();
    }
}
