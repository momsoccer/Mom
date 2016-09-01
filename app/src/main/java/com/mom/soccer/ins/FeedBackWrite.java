package com.mom.soccer.ins;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.FeedbackLine;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackWrite extends AppCompatActivity {

    @Bind(R.id.feedback_content)
    EditText feedback_content;

    @Bind(R.id.li_video_layout)
    LinearLayout li_video_layout;

    @Bind(R.id.li_video_title)
    LinearLayout li_video_title;

    @Bind(R.id.feedvideoBtn)
    Button feedvideoBtn;

    @Bind(R.id.title)
    TextView title;

    Intent paramIntent;

    private PrefUtil prefUtil;
    private User user;
    private Instructor instructor;

    //video
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 3;


    private FeedbackHeader feedbackHeader;
    private String videoFlag = "N";
    private Uri mFileUri;
    private static final String EXTRA_LOCAL_ONLY = "android.intent.extra.LOCAL_ONLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_feed_back_write_layout);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();
        instructor = prefUtil.getIns();

        Toolbar toolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        paramIntent = getIntent();
        feedbackHeader = (FeedbackHeader) paramIntent.getSerializableExtra(MissionCommon.FEEDBACKHEADER);

        //요청 내역에 따른 레이아웃 변화
        //li_video_layout

        if(feedbackHeader.getFeedbacktype().equals("video")){
            title.setText(getString(R.string.feedback_write_title_video));
            li_video_layout.setVisibility(View.VISIBLE);
            li_video_title.setVisibility(View.VISIBLE);
            feedvideoBtn.setVisibility(View.VISIBLE);
        }else{
            title.setText(getString(R.string.feedback_write_title_word));
            li_video_layout.setVisibility(View.GONE);
            li_video_title.setVisibility(View.GONE);
            feedvideoBtn.setVisibility(View.GONE);
        }
    }

    //비디오 및 영상 어태치
    public void videoAttach(View view){
        changeImage();
    }

    @OnClick(R.id.feedBtn)
    public void feedWrite(){
        if(Compare.isEmpty(feedback_content.getText().toString())){
            new MaterialDialog.Builder(FeedBackWrite.this)
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.mom_diaalog_alert)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(R.string.feedback_write_msg1)
                    .contentColor(getResources().getColor(R.color.color6))
                    .positiveText(R.string.mom_diaalog_confirm)
                    .show();
            return;
        }

        if(Compare.isEmpty(feedback_content.getText().length()<=11)){
            new MaterialDialog.Builder(FeedBackWrite.this)
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.mom_diaalog_alert)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(R.string.feedback_write_msg2)
                    .contentColor(getResources().getColor(R.color.color6))
                    .positiveText(R.string.mom_diaalog_confirm)
                    .show();
            return;
        }

        if(feedbackHeader.getFeedbacktype().equals("video")){
            if(videoFlag.equals("N")){
                new MaterialDialog.Builder(FeedBackWrite.this)
                        .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                        .title(R.string.mom_diaalog_alert)
                        .titleColor(getResources().getColor(R.color.color6))
                        .content(R.string.feedback_write_msg3)
                        .contentColor(getResources().getColor(R.color.color6))
                        .positiveText(R.string.mom_diaalog_confirm)
                        .show();
                return;
            }
        }

        //디비 실행 및 전 화면으로 이동
        WaitingDialog.showWaitingDialog(FeedBackWrite.this,false);
        FeedbackLine line = new FeedbackLine();

        line.setFeedbackid(feedbackHeader.getFeedbackid());
        line.setType("ins");
        line.setContent(feedback_content.getText().toString());
        //line.setVideoaddr();

        FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,getApplicationContext(),user);
        Call<ServerResult> c = feedBackService.saveLine(line);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    if(result.getCount()==1){
                        //강사 화면으로 다시 이동해줍니다.
                        Intent intent = new Intent(FeedBackWrite.this,InsDashboardActivity.class);
                        intent.putExtra(Param.FRAGMENT_COUNT,0);
                        //finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //홈버튼클릭시
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeImage(){
        new MaterialDialog.Builder(FeedBackWrite.this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title("영상선택")
                .titleColor(getResources().getColor(R.color.color6))
                .content("영상 업로드 유의 사항")
                .contentColor(getResources().getColor(R.color.color6))
                .positiveText("앨범에서 선택")
                .neutralText("카메라촬영")
                .negativeText(R.string.mom_diaalog_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakeAlbumAction();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakePhotoAction();
                    }
                })
                .show();
    }

    //앨범에서 이미지 가져오기
    public void doTakeAlbumAction(){
        //앨범호출
        Intent intent = new Intent(Intent.ACTION_PICK, null).setType("video/*");
        intent.putExtra(EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    //카메라에서 영상촬영
    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

        try {
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PICK_FROM_CAMERA);

        } catch (ActivityNotFoundException e) {

        }
    }

    //선택한 사진 업로드 또는 첨부하기

}
