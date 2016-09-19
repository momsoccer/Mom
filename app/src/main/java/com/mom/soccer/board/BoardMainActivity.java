package com.mom.soccer.board;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.Board;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.UserMissionActivity;
import com.mom.soccer.retrofitdao.BoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardMainActivity extends AppCompatActivity {

    private static final String TAG = "BoardMainActivity";

    @Bind(R.id.et_board_contnet)
    EditText editText_bd_content;

    private PrefUtil prefUtil;
    private User user;

    private int userMissionId = 0;
    private int missionuId = 0;

    @Bind(R.id.comment_title)
    TextView comment_title;

    Activity activity;
    UserMission userMission;

    @Bind(R.id.userimge)
    ImageView userimge;

    @Bind(R.id.txt_username)
    TextView txt_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_board_layout);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        userMission = (UserMission) intent.getSerializableExtra(MissionCommon.USER_MISSTION_OBJECT);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        comment_title.setText(getString(R.string.toolbar_comment_page));
        activity = this;

        if(!Compare.isEmpty(user.getProfileimgurl())){
            Glide.with(activity)
                    .load(user.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(userimge);
        }

        if(!Compare.isEmpty(user.getUsername())){
            txt_username.setText(user.getUsername());
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() get List ============================================");
    }

    //댓글을 입력한다
    @OnClick(R.id.btn_boardcreate)
    public void createContent(){
        if(editText_bd_content.getText().length()== 0){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.board_validation_word_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        WaitingDialog.showWaitingDialog(BoardMainActivity.this,false);
        BoardService boardService = ServiceGenerator.createService(BoardService.class,this,user);

        Board board = new Board();
        board.setUid(userMission.getUid());
        board.setWriteuid(user.getUid());
        board.setUsermissionid(userMission.getUsermissionid());
        board.setComment(editText_bd_content.getText().toString());
        board.setUsername(user.getUsername());

        Call<ServerResult> call = boardService.saveBoard(board);
        call.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_create_complete),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity,UserMissionActivity.class);
                    intent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMission);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(intent);
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

}
