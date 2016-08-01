package com.mom.soccer.mission;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.MyBookMark;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.fragment.YoutubeFragment;
import com.mom.soccer.retrofitdao.PickService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

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
    Button btnFllow;

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

        getSupportActionBar().setTitle(getString(R.string.toolbar_video)+"("+userMission.getUsername() +")");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //정보 적용
        text_userName.setText(userMission.getUsername());
        if(userMission.getTeamname() == null){
            text_teamName.setText(getString(R.string.user_team_yet_join));
        }else{
            text_teamName.setText(userMission.getTeamname());
        }
        textView_pickupCount.setText(String.valueOf(userMission.getBookmarkcount()));


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
            btnFllow.setVisibility(View.INVISIBLE);
        }else{
            btnFllow.setVisibility(View.VISIBLE);
        }

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

    @OnClick(R.id.ac_user_mission_video_pickup)
    public void imageButton_pickup(){

        myBookMark.setUid(user.getUid());
        myBookMark.setUsermissionid(userMission.getUsermissionid());

        if(pickUpFlag=="Y"){
            PickService pickService = ServiceGenerator.createService(PickService.class, this, user);
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
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });

        }else {
            //플래그가 N일 경우 좋아요 안한 상태입.
            PickService pickService = ServiceGenerator.createService(PickService.class, this, user);
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
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }

            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() ============================================ ");
        initialPickCheck();
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

    public void initialPickCheck(){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "",getString(R.string.network_get_list), true);
        dialog.show();


        PickService pickService = ServiceGenerator.createService(PickService.class, this, user);
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
                    dialog.dismiss();
                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_LONG).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

}
