package com.mom.soccer.alluser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.alluser.adapter.InsVideoLineAdapter;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.dto.InsVideoVo;
import com.mom.soccer.dto.InsVideoVoLine;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.fragment.YoutubeSeedMissionFragment;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.pubactivity.Param;
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
public class InsVideoActivity extends AppCompatActivity {

    private static final String TAG = "InsVideoActivity";

    private Instructor instructor;
    private Intent intent;

    @Bind(R.id.subject)
    EditText subject;

    @Bind(R.id.content)
    EditText content;

    @Bind(R.id.layout_youtubeaddr)
    TextInputLayout layout_youtubeaddr;

    @Bind(R.id.layout_subject)
    TextInputLayout layout_subject;

    @Bind(R.id.youtubeaddr)
    EditText youtubeaddr;

    @Bind(R.id.ins_video_btn)
    Button ins_video_btn;

    @Bind(R.id.upload_video)
    Button upload_video;

    @Bind(R.id.video_check)
    Button video_check;

    @Bind(R.id.li_layout)
    LinearLayout li_layout;

    @Bind(R.id.li_youtube_addr)
    LinearLayout li_youtube_addr;

    @Bind(R.id.li_reply)
    LinearLayout li_reply;

    @Bind(R.id.comment)
    EditText comment;

    @Bind(R.id.sendBtn)
    Button sendBtn;

    @Bind(R.id.replyRecview)
    RecyclerView replyRecview;

    private InsVideoLineAdapter insVideoLineAdapter;

    private String youtubeAdress;
    private String viewtype;
    private InsVideoVo insVideoVo;
    private User user;
    private Activity activity;

    private List<InsVideoVoLine> insVideoVoLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_video);
        ButterKnife.bind(this);

        intent = getIntent();
        instructor = (Instructor) intent.getSerializableExtra(MissionCommon.INS_OBJECT);
        user = (User) intent.getSerializableExtra(MissionCommon.USER_OBJECT);
        viewtype = intent.getExtras().getString("viewtype");

        activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        if(viewtype.equals("new")){
            li_layout.setVisibility(View.GONE);
            video_check.setVisibility(View.GONE);
            li_reply.setVisibility(View.GONE);
            youtubeaddr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().length() > 0 ){
                        upload_video.setEnabled(false);
                        video_check.setVisibility(View.VISIBLE);
                    }else{
                        video_check.setVisibility(View.GONE);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }else{
            li_reply.setVisibility(View.VISIBLE);

            comment.requestFocus();

            insVideoVo = (InsVideoVo) intent.getSerializableExtra(MissionCommon.OBJECT);
            Log.i(TAG,"insVideoVo : " +insVideoVo.toString());
            upload_video.setVisibility(View.GONE);
            video_check.setVisibility(View.GONE);
            ins_video_btn.setVisibility(View.GONE);

            subject.setText(insVideoVo.getSubject());
            subject.setEnabled(false);

            if(!Compare.isEmpty(insVideoVo.getContent())){
                content.setText(insVideoVo.getContent());
            }
            content.setEnabled(false);
            li_youtube_addr.setVisibility(View.GONE);

            li_layout.setVisibility(View.VISIBLE);
            YoutubeSeedMissionFragment youtubeFragment = new YoutubeSeedMissionFragment(InsVideoActivity.this,insVideoVo.getYoutubeaddr());
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction tc = fm.beginTransaction();
            tc.add(R.id.youtube_seed_frame_layout,youtubeFragment,"");
            tc.commit();

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


        }


    }

    @OnClick(R.id.ins_video_btn)
    public void ins_video_btn(){
        if(Compare.isEmpty(subject.getText().toString())) {
            layout_subject.setError(getString(R.string.ins_video_subject_empty));
            return;
        }
        if(Compare.isEmpty(youtubeaddr.getText().toString())) {
            layout_youtubeaddr.setError(getString(R.string.ins_video_youtube_empty));
            return;
        }
        saveInsVideoContent();
    }

    @OnClick(R.id.upload_video)
    public void uploadBtn(){
        VeteranToast.makeToast(getApplicationContext(),"준비 중입니다", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.video_check)
    public void video_check(){
        int lastString = youtubeaddr.getText().toString().length();
        youtubeAdress = youtubeaddr.getText().toString().substring(17,lastString);
        Log.i(TAG,"youtubeAdress : " + youtubeAdress);


        li_layout.setVisibility(View.VISIBLE);
        YoutubeSeedMissionFragment youtubeFragment = new YoutubeSeedMissionFragment(InsVideoActivity.this,youtubeAdress);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.youtube_seed_frame_layout,youtubeFragment,"");
        tc.commit();
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

    //network
    public void saveInsVideoContent(){
        InsVideoService insVideoService = ServiceGenerator.createService(InsVideoService.class,getApplicationContext(),instructor);

        InsVideoVo insVideoVo = new InsVideoVo();
        insVideoVo.setInstructorid(instructor.getInstructorid());
        insVideoVo.setSubject(subject.getText().toString());
        insVideoVo.setContent(content.getText().toString());
        insVideoVo.setYoutubeaddr(youtubeAdress);
        insVideoVo.setTeamid(instructor.getTeamid());

        WaitingDialog.showWaitingDialog(InsVideoActivity.this,false);
        Call<ServerResult> c = insVideoService.saveVideo(insVideoVo);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    new MaterialDialog.Builder(InsVideoActivity.this)
                            .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                            .title(R.string.mom_diaalog_alert)
                            .titleColor(getResources().getColor(R.color.color6))
                            .content(R.string.ins_video_save)
                            .contentColor(getResources().getColor(R.color.color6))
                            .positiveText(R.string.mom_diaalog_confirm)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(InsVideoActivity.this, AllUserMainActivity.class);
                                    intent.putExtra(Param.FRAGMENT_COUNT,1);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!viewtype.equals("new")){
            InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(subject.getWindowToken(), 0);
            mInputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);
            mInputMethodManager.hideSoftInputFromWindow(youtubeaddr.getWindowToken(), 0);

            if(insVideoVo.getCommentcount() > 0){
                getLineList();
            }
        }
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
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
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
                    replyRecview.setHasFixedSize(true);
                    replyRecview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    insVideoLineAdapter = new InsVideoLineAdapter(activity,insVideoVoLines,user);

                    replyRecview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    replyRecview.getItemAnimator().setAddDuration(300);
                    replyRecview.getItemAnimator().setRemoveDuration(300);
                    replyRecview.getItemAnimator().setMoveDuration(300);
                    replyRecview.getItemAnimator().setChangeDuration(300);
                    replyRecview.setHasFixedSize(true);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(insVideoLineAdapter);
                    alphaAdapter.setDuration(500);
                    replyRecview.setAdapter(insVideoLineAdapter);
                    replyRecview.scrollToPosition(insVideoVoLines.size()-1); //입력후 맨아래 자기 자신의 글 보이기
                }
            }

            @Override
            public void onFailure(Call<List<InsVideoVoLine>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

}
