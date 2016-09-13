package com.mom.soccer.ball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.Team;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsDashboardActivity;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retrofitdao.TeamService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamBoardActivity extends AppCompatActivity {

    private Activity activity;
    private PrefUtil prefUtil;
    private User user;
    private Team team;

    @Bind(R.id.action_bar_title)
    TextView action_bar_title;

    @Bind(R.id.content)
    EditText content;

    @Bind(R.id.txt_username)
    TextView txt_username;

    @Bind(R.id.userimg)
    ImageView userimg;

    @Bind(R.id.txt_pub)
    TextView txt_pub;

    @Bind(R.id.categoryType)
    CheckBox categoryType;

    private MomBoard momBoard = new MomBoard();
    private int tag=1;
    private String boardFlag="new"; //new,modify
    private Intent intent;
    private int boardid=0;
    private String callpage = "";
    private String tx_categoryType ="B";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_board);
        ButterKnife.bind(this);

        activity = this;
        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        intent = getIntent();
        boardFlag = intent.getExtras().getString("boardFlag");
        boardid = intent.getExtras().getInt("boardid");
        callpage = intent.getExtras().getString("callpage");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        if(!Compare.isEmpty(user.getProfileimgurl())){
            Glide.with(activity)
                    .load(user.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(userimg);
        }

        if(!Compare.isEmpty(user.getUsername())){
            txt_username.setText(user.getUsername());
        }

        getTeaminfo(user.getUid());

        if(boardid !=0 ){
            getReadBoard();
        }

        if(callpage.equals("ins")){
            categoryType.setVisibility(View.VISIBLE);
        }else{
            categoryType.setVisibility(View.GONE);
        }

        categoryType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    tx_categoryType ="A";
                }else{
                    tx_categoryType ="B";
                }
            }
        });

    }

    @OnClick(R.id.txt_pub)
    public void txt_pub(){
        if(tag == 1){
            txt_pub.setText(getString(R.string.board_pubtype_N));
            tag=2;
        }else{
            tag=1;
            txt_pub.setText(getString(R.string.board_pubtype_Y));
        }
    }

    public void getTeaminfo(int uid){
        WaitingDialog.showWaitingDialog(TeamBoardActivity.this,false);
        TeamService teamService = ServiceGenerator.createService(TeamService.class,getApplicationContext(),user);
        Call<Team> c = teamService.getMyTeaminfo(user.getUid());
        c.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    team = response.body();
                    action_bar_title.setText(team.getName()+" "+getString(R.string.board_title_msg));
                }
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.commit)
    public void commit(){

        if(boardFlag.equals("new")){
            if(content.getText().toString().length()==0){
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.board_team_content_vali), Toast.LENGTH_SHORT).show();
                return;
            }
            WaitingDialog.showWaitingDialog(TeamBoardActivity.this,false);
            MomBoardService boardService = ServiceGenerator.createService(MomBoardService.class,getApplicationContext(),user);
            MomBoard momBoard = new MomBoard();

            momBoard.setUid(user.getUid());
            momBoard.setContent(content.getText().toString());
            momBoard.setBoardtype("team");
            momBoard.setCategory(tx_categoryType);
            momBoard.setBoardtypeid(team.getTeamid());

            if(tag == 1){
                momBoard.setPubtype("Y");
            }else{
                momBoard.setPubtype("N");
            }

            Call<ServerResult> c = boardService.saveBoardheader(momBoard);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){

                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.board_team_content_complate),Toast.LENGTH_SHORT).show();

                        if(callpage.equals("user")){
                            Intent intent = new Intent(TeamBoardActivity.this,PlayerMainActivity.class);
                            intent.putExtra(Param.FRAGMENT_COUNT,0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(TeamBoardActivity.this,InsDashboardActivity.class);
                            intent.putExtra(Param.FRAGMENT_COUNT,0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
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
        }else{//글수정일때...
            if(content.getText().toString().length()==0){
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.board_team_content_vali), Toast.LENGTH_SHORT).show();
                return;
            }
            WaitingDialog.showWaitingDialog(TeamBoardActivity.this,false);
            MomBoardService boardService = ServiceGenerator.createService(MomBoardService.class,getApplicationContext(),user);
            MomBoard upvo = new MomBoard();

            upvo.setBoardid(momBoard.getBoardid());
            upvo.setUid(user.getUid());
            upvo.setContent(content.getText().toString());
            upvo.setBoardtype("team");
            upvo.setCategory(tx_categoryType);
            upvo.setBoardtype(momBoard.getBoardtype());
            upvo.setBoardtypeid(team.getTeamid());

            if(tag == 1){
                upvo.setPubtype("Y");
            }else{
                upvo.setPubtype("N");
            }

            Call<ServerResult> c = boardService.updateBoardHeader(upvo);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.board_main_coment_modity),Toast.LENGTH_SHORT).show();
                        finish();
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

    public void getReadBoard(){
        WaitingDialog.showWaitingDialog(this,false);
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
                    content.setText(momBoard.getContent());
                    if(momBoard.getPubtype().equals("Y")){
                        txt_pub.setText(getString(R.string.board_pubtype_Y));
                    }else{
                        txt_pub.setText(getString(R.string.board_pubtype_N));
                    }
                }
            }

            @Override
            public void onFailure(Call<MomBoard> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public void onBackPressed() {
    }
}
