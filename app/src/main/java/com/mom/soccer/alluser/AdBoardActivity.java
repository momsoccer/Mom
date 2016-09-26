package com.mom.soccer.alluser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.AdBoardFile;
import com.mom.soccer.dto.AdBoardVo;
import com.mom.soccer.fragment.YoutubeSeedMissionFragment;
import com.mom.soccer.retrofitdao.AdBoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdBoardActivity extends AppCompatActivity {

    private static final String TAG = "AdBoardActivity";

    private Intent intent;
    private AdBoardVo adBoardVo = new AdBoardVo();
    private List<AdBoardFile> adBoardFiles = new ArrayList<AdBoardFile>();

    private int advid=0;
    private Activity activity;

    TextView addr,phone,introduce,subcontent1,subcontent2,subcontent3;
    ImageView img1,img2,img3,img4,img5;

    @Bind(R.id.li_layout)
    LinearLayout li_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_board);
        ButterKnife.bind(this);

        activity = this;

        addr = (TextView) findViewById(R.id.addr);
        phone = (TextView) findViewById(R.id.phone);
        introduce = (TextView) findViewById(R.id.introduce);
        subcontent1 = (TextView) findViewById(R.id.subcontent1);
        subcontent2 = (TextView) findViewById(R.id.subcontent2);
        subcontent3 = (TextView) findViewById(R.id.subcontent3);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        intent = getIntent();
        advid = intent.getExtras().getInt("adboardid");

        WaitingDialog.showWaitingDialog(activity,false);
        AdBoardService adBoardService = ServiceGenerator.createService(AdBoardService.class);
        Call<AdBoardVo> c = adBoardService.getHeader(advid);
        c.enqueue(new Callback<AdBoardVo>() {
            @Override
            public void onResponse(Call<AdBoardVo> call, Response<AdBoardVo> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    adBoardVo = response.body();

                    Log.i(TAG,"adBoardVo : " + adBoardVo.toString());

                    addr.setText(adBoardVo.getAddr());
                    phone.setText(adBoardVo.getPhone());
                    introduce.setText(adBoardVo.getIntroduce());
                    subcontent1.setText(adBoardVo.getSubcontent1());
                    subcontent2.setText(adBoardVo.getSubcontent2());
                    subcontent3.setText(adBoardVo.getSubcontent3());

                    if(adBoardVo.getYoutubeaddr().length() > 0){
                        li_layout.setVisibility(View.VISIBLE);

                        YoutubeSeedMissionFragment youtubeFragment = new YoutubeSeedMissionFragment(AdBoardActivity.this,adBoardVo.getYoutubeaddr());
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction tc = fm.beginTransaction();
                        tc.add(R.id.youtube_seed_frame_layout,youtubeFragment,"");
                        tc.commit();

                    }else{
                        li_layout.setVisibility(View.GONE);
                    }


                    adBoardFiles = adBoardVo.getAdBoardFiles();

                    if(adBoardVo.getFilecount() > 0){
                        for(int i=0;i < adBoardFiles.size(); i++){

                            Log.i(TAG,"주소는 : " +adBoardFiles.get(i).getFileaddr());

                            if(i==0){
                                Glide.with(activity)
                                        .load(adBoardFiles.get(i).getFileaddr())
                                        .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                                        .fitCenter()
                                        .thumbnail(0.1f)
                                        .into(img1);
                            }else if(i==1){
                                Glide.with(activity)
                                        .load(adBoardFiles.get(i).getFileaddr())
                                        .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                                        .fitCenter()
                                        .thumbnail(0.1f)
                                        .into(img2);
                            }else if(i==2){
                                Glide.with(activity)
                                        .load(adBoardFiles.get(i).getFileaddr())
                                        .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                                        .fitCenter()
                                        .thumbnail(0.1f)
                                        .into(img3);
                            }else if(i==3){
                                Glide.with(activity)
                                        .load(adBoardFiles.get(i).getFileaddr())
                                        .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                                        .fitCenter()
                                        .thumbnail(0.1f)
                                        .into(img4);
                            }else if(i==4){
                                Glide.with(activity)
                                        .load(adBoardFiles.get(i).getFileaddr())
                                        .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                                        .fitCenter()
                                        .thumbnail(0.1f)
                                        .into(img5);
                            }
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<AdBoardVo> call, Throwable t) {
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
