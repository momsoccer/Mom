package com.mom.soccer.alluser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.alluser.adapter.InsVideoLineAdapter;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.EventBus;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.RecyclerItemClickListener;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.common.internal.BusObject;
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.InsVideoLikeVo;
import com.mom.soccer.dto.InsVideoVo;
import com.mom.soccer.dto.InsVideoVoLine;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.fragment.YoutubeSeedMissionFragment;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.pubretropit.PubReport;
import com.mom.soccer.retrofitdao.InsVideoService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
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

public class InsVideoAcView extends AppCompatActivity {

    private static final String TAG = "InsVideoAcView";

    private PrefUtil prefUtil;
    private User user;
    private Activity activity;
    private Instructor instructor;

    private Intent intent;
    private InsVideoVo insVideoVo;
    private List<InsVideoVoLine> insVideoVoLines;
    private int posintion = 0;
    private int like_count = 0;
    private int userlikeCount = 0;

    //layout load
    @Bind(R.id.insimage)
    ImageView insimage;

    @Bind(R.id.insname)
    TextView insname;

    @Bind(R.id.teamname)
    TextView teamname;

    @Bind(R.id.dateformating)
    TextView dateformating;

    @Bind(R.id.subject)
    TextView subject;

    @Bind(R.id.content)
    TextView content;

    @Bind(R.id.replyRecview)
    RecyclerView replyRecview;

    @Bind(R.id.commnetcount)
    TextView commnetcount;

    @Bind(R.id.comment)
    EditText comment;

    @Bind(R.id.sendBtn)
    Button sendBtn;

    @Bind(R.id.li_like)
    LinearLayout li_like;

    @Bind(R.id.likecount)
    TextView likecount;

    @Bind(R.id.ins_video_like)
    TextView ins_video_like;

    @Bind(R.id.scroll_layout)
    NestedScrollView scroll_layout;

    private int lineCount=0;
    private LinearLayoutManager linearLayoutManager;
    private InsVideoLineAdapter insVideoLineAdapter;

    View positiveAction;
    EditText report_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_video_ac_view);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        activity = this;
        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        intent = getIntent();
        instructor = (Instructor) intent.getSerializableExtra(MissionCommon.INS_OBJECT);
        insVideoVo = (InsVideoVo) intent.getSerializableExtra(MissionCommon.OBJECT);
        posintion = intent.getExtras().getInt("position");

        Log.i(TAG,"instructor : " + instructor.toString());
        Log.i(TAG,"insVideoVo : " + insVideoVo.toString());
        Log.i(TAG,"user : " + user.toString());
        Log.i(TAG,"posintion : " + posintion);

        if(!Compare.isEmpty(insVideoVo.getInsimage())){
            Glide.with(activity)
                    .load(insVideoVo.getInsimage())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(insimage);
        }

        insname.setText(insVideoVo.getInsname());
        teamname.setText(insVideoVo.getTeamname());
        dateformating.setText(insVideoVo.getFormatDataSign());
        subject.setText(insVideoVo.getSubject());
        content.setText(insVideoVo.getContent());

        YoutubeSeedMissionFragment youtubeFragment = new YoutubeSeedMissionFragment(InsVideoAcView.this,insVideoVo.getYoutubeaddr());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.youtube_seed_frame_layout,youtubeFragment,"");
        tc.commit();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        lineCount = insVideoVo.getCommentcount();
        commnetcount.setText(getResources().getString(R.string.momboard_comment_msg) + "("+lineCount+")");

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                sendBtn.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                        final InsVideoVoLine vo = insVideoLineAdapter.getBoardLine(position);

                        if(user.getUid()==vo.getUid()){
                            new MaterialDialog.Builder(activity)
                                    .content(R.string.board_main_coment_delete)
                                    .contentColor(activity.getResources().getColor(R.color.color6))
                                    .positiveText(R.string.mom_diaalog_confirm)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            insVideoLineAdapter.deleteLine(position,vo.getLineid());
                                            lineCount = lineCount - 1;
                                            commnetcount.setText(getResources().getString(R.string.momboard_comment_msg) + "("+lineCount+")");

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
                                            report.setType(PubReport.REPORTTYPE_INS_VIDEO_COMMENT);
                                            report.setUid(user.getUid());
                                            report.setReason(report_content.getText().toString());
                                            report.setContent(vo.getComment());
                                            report.setPublisherid(vo.getLineid());
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

        like_count = insVideoVo.getLikecount();
        if(like_count > 0){
            likecount.setText(String.valueOf(like_count));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(subject.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(youtubeaddr.getWindowToken(), 0);
        */

        //댓글이 있다면
        if(insVideoVo.getCommentcount() > 0){
            getLineList();
        }

        //내가 좋아요를 했는지 체크 합니다
        if(like_count > 0){
            getLikeMeCheck();
        }

    }

    public void getLineList(){
        WaitingDialog.showWaitingDialog(activity,false);
        InsVideoService videoService = ServiceGenerator.createService(InsVideoService.class,getApplicationContext(),user);
        Call<List<InsVideoVoLine>> c = videoService.getLineList(insVideoVo.getVideoid());
        c.enqueue(new Callback<List<InsVideoVoLine>>() {
            @Override
            public void onResponse(Call<List<InsVideoVoLine>> call, Response<List<InsVideoVoLine>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    insVideoVoLines = response.body();

                    if(insVideoVoLines.size()==0){

                    }else{

                    }

                    replyRecview.setHasFixedSize(true);
                    replyRecview.setLayoutManager(linearLayoutManager);
                    insVideoLineAdapter = new InsVideoLineAdapter(activity,insVideoVoLines,user);
                    replyRecview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    replyRecview.getItemAnimator().setAddDuration(500);
                    replyRecview.getItemAnimator().setRemoveDuration(500);
                    replyRecview.getItemAnimator().setMoveDuration(500);
                    replyRecview.getItemAnimator().setChangeDuration(500);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(insVideoLineAdapter);
                    alphaAdapter.setDuration(500);
                    replyRecview.setAdapter(insVideoLineAdapter);
                    replyRecview.scrollToPosition(insVideoVoLines.size()-1);
                }
            }

            @Override
            public void onFailure(Call<List<InsVideoVoLine>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.insimage)
    public void insimage(){

    }

    @OnClick(R.id.sendBtn)
    public void sendBtn(){

        InsVideoVoLine insVideoVoLine = new InsVideoVoLine();
        insVideoVoLine.setVideoid(insVideoVo.getVideoid());
        insVideoVoLine.setComment(comment.getText().toString());
        insVideoVoLine.setUid(user.getUid());

        WaitingDialog.showWaitingDialog(activity,false);

        InsVideoService videoService = ServiceGenerator.createService(InsVideoService.class,getApplicationContext(),user);
        Call<ServerResult> c = videoService.saveLine(insVideoVoLine);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    MomSnakBar.show(getWindow().getDecorView().getRootView(),activity,getResources().getString(R.string.ins_video_comment));
                    getLineList();
                    //코멘트 바꿔 주기
                    lineCount = lineCount + 1;
                    commnetcount.setText(getResources().getString(R.string.momboard_comment_msg) + "("+lineCount+")");
                    comment.setText(null);
                    replyRecview.scrollToPosition(lineCount-1);

                    scroll_layout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scroll_layout.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 500);

                    //posintion
                    BusObject busObject = new BusObject();
                    busObject.setPosition(posintion);
                    busObject.setLineCount(lineCount);
                    EventBus.getInstance().post(busObject);
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    //li_like
    @OnClick(R.id.li_like)
    public void li_like()
    {

        if(userlikeCount == 0){
            InsVideoLikeVo vo = new InsVideoLikeVo();
            vo.setUid(user.getUid());
            vo.setVideoid(insVideoVo.getVideoid());

            WaitingDialog.showWaitingDialog(activity,false);
            InsVideoService insVideoService = ServiceGenerator.createService(InsVideoService.class,activity,user);
            Call<ServerResult> c = insVideoService.saveVideoLike(vo);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        VeteranToast.makeToast(activity,getResources().getString(R.string.ins_video_like_msg), Toast.LENGTH_SHORT).show();
                        userlikeCount = 1;
                        like_count = like_count + userlikeCount;

                        likecount.setText(getResources().getString(R.string.ins_video_like_exist) + like_count);
                        BusObject busObject = new BusObject();
                        busObject.setPosition(posintion);
                        busObject.setLineCount(like_count);
                        EventBus.getInstance().post(busObject);
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {

                }
            });
        }else{
            InsVideoLikeVo vo = new InsVideoLikeVo();
            vo.setVideoid(insVideoVo.getVideoid());
            vo.setUid(user.getUid());
            WaitingDialog.showWaitingDialog(activity,false);
            InsVideoService insVideoService = ServiceGenerator.createService(InsVideoService.class,activity,user);
            Call<ServerResult> c = insVideoService.deleteVideoLike(vo);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        VeteranToast.makeToast(activity,getResources().getString(R.string.ins_video_like_msg1),Toast.LENGTH_SHORT).show();
                        like_count = like_count - 1;
                        likecount.setText(String.valueOf(like_count));
                        userlikeCount = 0;

                        BusObject busObject = new BusObject();
                        busObject.setPosition(posintion);
                        busObject.setLineCount(like_count);
                        EventBus.getInstance().post(busObject);
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

    public void getLikeMeCheck(){
        WaitingDialog.showWaitingDialog(activity,false);
        InsVideoService insVideoService = ServiceGenerator.createService(InsVideoService.class,activity,user);
        Call<Integer> c = insVideoService.getLikeVideoCount(user.getUid(),insVideoVo.getVideoid());
        c.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    userlikeCount = response.body();
                    if(userlikeCount>0){
                        likecount.setText(getResources().getString(R.string.ins_video_like_exist) + like_count);
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.i(TAG,"onBackPressed() ==================================");

        if(Common.YOUTUBESCREEN_STATUS .equals("LANDSCAPE")){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


}
