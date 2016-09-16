package com.mom.soccer.ball;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.MomBoardFile;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.Team;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsDashboardActivity;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retrofitdao.TeamService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.trservice.MultiImageTrService;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamBoardActivity extends AppCompatActivity {

    private static final String TAG = "TeamBoardActivity";

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

    @Bind(R.id.upload)
    ImageButton upload;

    @Bind(R.id.img1)
    ImageView img1;

    @Bind(R.id.img2)
    ImageView img2;

    @Bind(R.id.img3)
    ImageView img3;

    @Bind(R.id.li_imageview)
    LinearLayout li_imageview;

    @Bind(R.id.textImageCount)
    TextView textImageCount;


    RelativeLayout li_image1,li_image2,li_image3;
    ImageView closebtn1,closebtn2,closebtn3;

    private MomBoard momBoard = new MomBoard();
    private int tag=1;
    private String boardFlag="new"; //new,modify
    private Intent intent;
    private int boardid=0;
    private String callpage = "";
    private String tx_categoryType ="B";

    //image upload
    ArrayList<Image> images = new ArrayList<Image>();

    private boolean isfileSizeA = false;
    private boolean isfileSizeB = false;
    private boolean isfileSizeC = false;

    private boolean existImageA,existImageB,existImageC;

    //기존 추가 이미지 삭제 플래그
    private boolean onlyImageUpdate = false;
    private boolean deleteImg = false;
    private int imageCount=0;
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

        li_imageview.setVisibility(View.GONE);

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


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        li_image1 = (RelativeLayout) findViewById(R.id.li_image1);
        li_image2 = (RelativeLayout) findViewById(R.id.li_image2);
        li_image3 = (RelativeLayout) findViewById(R.id.li_image3);

        existImageA = false;
        existImageB = false;
        existImageC = false;
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

            if(isfileSizeA || isfileSizeB || isfileSizeC){
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.fileupload), Toast.LENGTH_SHORT).show();
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
            momBoard.setUsername(activity.getResources().getString(R.string.momboard_write)+user.getUsername());

            if(tag == 1){
                momBoard.setPubtype("Y");
            }else{
                momBoard.setPubtype("N");
            }

            InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);

            Call<ServerResult> c = boardService.saveBoardheader(momBoard);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){

                        ServerResult result = response.body();

                        Log.i(TAG,"*** 생성된 보드 아이디는 : " + result.getCount());

                        if(images.size()!=0){
                            uploadImagefile(result.getCount());
                        }

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

                        if(deleteImg==true){
                            if(images.size()!=0){
                                //기존자료 삭제 후 다시 업로드 파일 첨부
                                deleteBoardFile(momBoard.getBoardid());
                            }
                        }

                        if(onlyImageUpdate){
                            VeteranToast.makeToast(getApplicationContext(),"이미지 정보를 업데이트 합니다",Toast.LENGTH_SHORT).show();
                        }

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

    //읽어 올때 수정...
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

                    if(momBoard.getFilecount()!=0){
                        li_imageview.setVisibility(View.VISIBLE);

                        for(int i=0; i < momBoard.getBoardFiles().size();i++){

                            if(momBoard.getBoardFiles().size()==1){
                                textImageCount.setText("1/3");
                                li_image1.setVisibility(View.VISIBLE);
                                li_image2.setVisibility(View.GONE);
                                li_image3.setVisibility(View.GONE);
                            }else if(momBoard.getBoardFiles().size()==2){
                                textImageCount.setText("2/3");
                                li_image1.setVisibility(View.VISIBLE);
                                li_image2.setVisibility(View.VISIBLE);
                                li_image3.setVisibility(View.GONE);
                            }else if(momBoard.getBoardFiles().size()==3){
                                textImageCount.setText("3/3");
                                li_image1.setVisibility(View.VISIBLE);
                                li_image2.setVisibility(View.VISIBLE);
                                li_image3.setVisibility(View.VISIBLE);
                            }

                            if(i==0){
                                Glide.with(activity)
                                        .load(momBoard.getBoardFiles().get(i).getFileaddr())
                                        .fitCenter()
                                        .into(img1);
                            }else if(i==1){
                                Glide.with(activity)
                                        .load(momBoard.getBoardFiles().get(i).getFileaddr())
                                        .fitCenter()
                                        .into(img2);
                            }else if(i==2){
                                Glide.with(activity)
                                        .load(momBoard.getBoardFiles().get(i).getFileaddr())
                                        .fitCenter()
                                        .into(img3);
                            }

                        }

                    }else{
                        li_imageview.setVisibility(View.GONE);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //홈버튼클릭시
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.upload)
    public void upload(){

        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);

        if(boardFlag.equals("modify")){
            new MaterialDialog.Builder(TeamBoardActivity.this)
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.mom_diaalog_alert)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(R.string.image_pick_msg1)
                    .contentColor(getResources().getColor(R.color.color6))
                    .positiveText(R.string.mom_diaalog_confirm)
                    .negativeText(R.string.mom_diaalog_cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteImg = true;
                            pickUpImage();
                        }
                    })
                    .show();
        }else{
            pickUpImage();
        }
    }

    public void pickUpImage(){

        Intent intent = new Intent(TeamBoardActivity.this, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 3);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            if(images.size()==0){
                li_imageview.setVisibility(View.GONE);
                li_image1.setVisibility(View.GONE);
                li_image2.setVisibility(View.GONE);
                li_image3.setVisibility(View.GONE);
            }else if(images.size()==1){
                li_imageview.setVisibility(View.VISIBLE);
                li_image1.setVisibility(View.VISIBLE);
                li_image2.setVisibility(View.GONE);
                li_image3.setVisibility(View.GONE);
                textImageCount.setText("1/3");
            }else if(images.size()==2){
                li_imageview.setVisibility(View.VISIBLE);
                li_image1.setVisibility(View.VISIBLE);
                li_image2.setVisibility(View.VISIBLE);
                li_image3.setVisibility(View.GONE);
                textImageCount.setText("2/3");
            }else if(images.size()==3){
                li_imageview.setVisibility(View.VISIBLE);
                li_image1.setVisibility(View.VISIBLE);
                li_image2.setVisibility(View.VISIBLE);
                li_image3.setVisibility(View.VISIBLE);
                textImageCount.setText("3/3");
            }

            for (int i = 0, l = images.size(); i < l; i++) {
                File file = new File(images.get(i).path);
                if(file.exists()){

                    Log.i(TAG,"*** Choose file name ***");
                    Log.i(TAG,"file name : " + images.get(i).name);
                    Log.i(TAG,"file path : " + images.get(i).path);

                    long file_l = file.length();

                    if(i==0){
                        li_image1.setVisibility(View.VISIBLE);
                        Glide.with(activity)
                                .load(file)
                                .into(img1);


                    }else if(i==1){
                        li_image2.setVisibility(View.VISIBLE);
                        Glide.with(activity)
                                .load(file)
                                .into(img2);

                    }else if(i==2){
                        li_image3.setVisibility(View.VISIBLE);
                        Glide.with(activity)
                                .load(file)
                                .into(img3);

                    }

                }else{
                    Log.i(TAG,"파일이 없습니다");
                }
            }
        }
    }

    public void removeImage(View view){

        //수정일때 첨부 전 삭제 , 모두삭제
        if(boardFlag.equals("modify") && deleteImg){
            imageCount();

            switch (view.getId()){
                case R.id.closebtn1:

                    li_image1.setVisibility(View.GONE);
                    img1.setImageResource(0);
                    imageRemove(1);
                    existImageA = true;
                    isfileSizeA = false;

                    break;

                case R.id.closebtn2:

                    li_image2.setVisibility(View.GONE);
                    img2.setImageResource(0);
                    imageRemove(2);
                    existImageB = true;
                    isfileSizeB = false;

                    break;

                case R.id.closebtn3:

                    li_image3.setVisibility(View.GONE);
                    img3.setImageResource(0);
                    imageRemove(3);
                    existImageC = true;
                    isfileSizeC = false;

                    break;
            }
        }else if(boardFlag.equals("new")){
            imageCount();

            switch (view.getId()){
                case R.id.closebtn1:

                    li_image1.setVisibility(View.GONE);
                    img1.setImageResource(0);
                    imageRemove(1);
                    existImageA = true;
                    isfileSizeA = false;

                    break;

                case R.id.closebtn2:

                    li_image2.setVisibility(View.GONE);
                    img2.setImageResource(0);
                    imageRemove(2);
                    existImageB = true;
                    isfileSizeB = false;

                    break;

                case R.id.closebtn3:

                    li_image3.setVisibility(View.GONE);
                    img3.setImageResource(0);
                    imageRemove(3);
                    existImageC = true;
                    isfileSizeC = false;

                    break;
            }
        }else if(boardFlag.equals("modify") && !deleteImg) {

            onlyImageUpdate = true;

            switch (view.getId()){
                case R.id.closebtn1:

                    li_image1.setVisibility(View.GONE);
                    img1.setImageResource(0);
                    existImageA = true;
                    imageCount = 1+imageCount;
                    imageCountText();
                    break;
                case R.id.closebtn2:

                    li_image2.setVisibility(View.GONE);
                    img2.setImageResource(0);
                    existImageB = true;
                    imageCountText();
                    imageCount = 1+imageCount;
                    break;
                case R.id.closebtn3:
                    li_image3.setVisibility(View.GONE);
                    img3.setImageResource(0);
                    existImageC = true;
                    imageCount = 1+imageCount;
                    imageCountText();
                    break;
            }

        }
    }

    public void imageCountText(){
        if(imageCount==1){
            textImageCount.setText("2/3");
        }else if(imageCount==2){
            textImageCount.setText("1/3");
        }else if(imageCount==3){
            li_imageview.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart() ============================================");
    }

    public void uploadImagefile(int boardid){

        if(images.size()!=0){
            for(int i = 0 ; i < images.size() ; i++){
                MultiImageTrService multiImageTrService = new MultiImageTrService(activity,user,images.get(i).path);
                multiImageTrService.upLoadImage(images.get(i).name,images.get(i).path,boardid);
            }
        }

    };

    public void imageCount(){

        if(images.size()==3){
            textImageCount.setText("2/3");
        }else if(images.size()==2){
            textImageCount.setText("1/3");
        }else if(images.size()==1){
            textImageCount.setText("0/3");
            li_imageview.setVisibility(View.GONE);
        }

    }

    public void imageRemove(int i){

        if(i == 1){
            images.remove(0);
        }else if(i ==2){

            if(existImageA && existImageC){
                images.remove(0);
            }

            if(existImageA && !existImageC){
                images.remove(1);
            }

            if(!existImageA && existImageC){
                images.remove(0);
            }

            if(!existImageA && !existImageC){
                images.remove(1);
            }

        }else if(i ==3){
            if(!existImageA && !existImageB){
                images.remove(2);
            }
            if(existImageA && !existImageB){
                images.remove(1);
            }
            if(!existImageA && existImageB){
                images.remove(1);
            }

            if(existImageA && existImageB){
                images.remove(0);
            }
        }
    }

    public void deleteBoardFile(int boardid){
        WaitingDialog.showWaitingDialog(activity,false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,activity,user);
        MomBoardFile qboard = new MomBoardFile();
        qboard.setBoardid(boardid);
        Call<ServerResult> c = momBoardService.deleteBoardFile(qboard);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    if(result.getResult().equals("S")){
                        //새로 업로드
                        uploadImagefile(momBoard.getBoardid());
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

}
