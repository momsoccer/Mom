package com.mom.soccer.mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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


        YoutubeFragment youtubeFragment = new YoutubeFragment(this,userMission.getYoutubeaddr());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.youtube_frame_layout,youtubeFragment,"");
        tc.commit();

        if(!Compare.isEmpty(user.getProfileimgurl())) {
            Glide.with(this)
                    .load(user.getProfileimgurl())
                    .into(image_user);
        }

        initialPickCheck();

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
                        ServerResult serverResult = response.body();
                        Log.d(TAG, "정상");
                        imageButton_pickup.setImageResource(R.drawable.ic_white_hart);
                        pickUpFlag = "N";
                    } else {
                        Log.d(TAG, "비정상");
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    Log.d(TAG, "정상");
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
                        ServerResult serverResult = response.body();
                        imageButton_pickup.setImageResource(R.drawable.ic_hart_red);
                        pickUpFlag = "Y";
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
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
                }else{

                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
