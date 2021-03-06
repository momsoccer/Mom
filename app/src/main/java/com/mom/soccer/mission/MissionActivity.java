package com.mom.soccer.mission;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.bartoszlipinski.flippablestackview.StackPageTransformer;
import com.mom.soccer.R;
import com.mom.soccer.adapter.MissionFragmentAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.SpBalanceHeader;
import com.mom.soccer.dto.User;
import com.mom.soccer.fragment.MainMissionFragment;
import com.mom.soccer.point.PointMainActivity;
import com.mom.soccer.retrofitdao.MissionService;
import com.mom.soccer.retrofitdao.PointService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionActivity extends AppCompatActivity {

    private static final String TAG = "MissionActivity";

    private int NUMBER_OF_FRAGMENTS;

    private FlippableStackView mFlippableStack;

    private MissionFragmentAdapter mPageAdapter;

    private List<Fragment> mViewPagerFragments;

    private List<Mission> missionList;

    private String missionType;

    private User user;
    private PrefUtil prefUtil;

    @Bind(R.id.mission_title)
    TextView textViewTitle;

    @Bind(R.id.user_point)
    TextView tx_mission_point;

    @Bind(R.id.discreteSeekBar)
    DiscreteSeekBar discreteSeekBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_mission_layout);
        ButterKnife.bind(this);

        Log.d(TAG,"onCreate() ===========================================");

        Toolbar toolbar = (Toolbar) findViewById(R.id.mission_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Intent intent = getIntent();
        missionType = intent.getExtras().getString(MissionCommon.MISSIONTYPE);



        Log.d(TAG,"onCreate() missiontype : " + user.toString());

        if(missionType.equals("DRIBLE")){
            textViewTitle.setText(getString(R.string.mission_toolbar_title)+getString(R.string.drrible));
        }else if(missionType.equals("LIFTING")){
            textViewTitle.setText(getString(R.string.mission_toolbar_title)+getString(R.string.lifting));
        }else if(missionType.equals("TRAPING")){
            textViewTitle.setText(getString(R.string.mission_toolbar_title)+getString(R.string.trapping));
        }else if(missionType.equals("AROUND")){
            textViewTitle.setText(getString(R.string.mission_toolbar_title)+getString(R.string.around));
        }else if(missionType.equals("FLICK")){
            textViewTitle.setText(getString(R.string.mission_toolbar_title)+getString(R.string.flick));
        }else if(missionType.equals("COMPLEX")){
            textViewTitle.setText(getString(R.string.mission_toolbar_title)+getString(R.string.crossbar));
        }

    }

    private void createViewPagerFragments() {
        Log.d(TAG,"이곳은 createViewPagerFragments()");
        mViewPagerFragments = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_FRAGMENTS; ++i) {
            mViewPagerFragments.add(MainMissionFragment.newInstance(i, missionList.get(i),user,"NOL"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart()===========================================");
        getMissionList(missionType);
        getUserPoint(user.getUid());
    }


    public void getUserPoint(int uid){
        PointService pointService = ServiceGenerator.createService(PointService.class,this,user);
        Call<SpBalanceHeader> call = pointService.getSelfAmt(uid);
        call.enqueue(new Callback<SpBalanceHeader>() {
            @Override
            public void onResponse(Call<SpBalanceHeader> call, Response<SpBalanceHeader> response) {
                if(response.isSuccessful()){
                    SpBalanceHeader spBalanceHeader = response.body();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    tx_mission_point.setText(numberFormat.format(spBalanceHeader.getAmount()));
                }else{

                }
            }

            @Override
            public void onFailure(Call<SpBalanceHeader> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가 : " + t.getMessage());
            }
        });
    }

    public void getMissionList(String missiontype){
        WaitingDialog.showWaitingDialog(this,false);
        MissionService missionService = ServiceGenerator.createService(MissionService.class,this,user);

        final Call<List<Mission>> call = missionService.getMissionList(missiontype,user.getUid());

        mFlippableStack = (FlippableStackView) findViewById(R.id.flippable_stack_view);

        call.enqueue(new Callback<List<Mission>>() {
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    missionList = response.body();

                        NUMBER_OF_FRAGMENTS = missionList.size();

                        createViewPagerFragments();

                        mPageAdapter = new MissionFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);


                        boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                        mFlippableStack.initStack(NUMBER_OF_FRAGMENTS, portrait ?
                                StackPageTransformer.Orientation.VERTICAL :
                                StackPageTransformer.Orientation.HORIZONTAL
                                ,0.9f
                                ,0.9f
                                ,0.9f
                                ,StackPageTransformer.Gravity.CENTER
                        );
                        mFlippableStack.setAdapter(mPageAdapter);


                        discreteSeekBar.setMin(0);
                        //discreteSeekBar.setProgress(NUMBER_OF_FRAGMENTS-1);
                        discreteSeekBar.setMax(NUMBER_OF_FRAGMENTS-1);
                        discreteSeekBar.setProgress(NUMBER_OF_FRAGMENTS);

                        //미션 시크바 색조절
                        //discreteSeekBar.setBackgroundColor(getResources().getColor(R.color.bg_screen2)); 시크바 바탕화면

                        if(missionType.equals("DRIBLE")){
                            discreteSeekBar.setTrackColor(getResources().getColor(R.color.color6)); //시크바 트랙색
                            discreteSeekBar.setScrubberColor(getResources().getColor(R.color.mission_color_dribble));  //진행색
                            discreteSeekBar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.enabled_red)); //시크바원
                            discreteSeekBar.setRippleColor(getResources().getColor(R.color.enabled_red));
                            discreteSeekBar.setThumbColor(getResources().getColor(R.color.mission_color_dribble),getResources().getColor(R.color.mission_color_dribble)); //시크바 풍선 바탕색
                        }else if(missionType.equals("LIFTING")){
                            discreteSeekBar.setTrackColor(getResources().getColor(R.color.color6)); //시크바 트랙색
                            discreteSeekBar.setScrubberColor(getResources().getColor(R.color.mission_color_lifting));  //진행색
                            discreteSeekBar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.enabled_red)); //시크바원
                            discreteSeekBar.setRippleColor(getResources().getColor(R.color.enabled_red));
                            discreteSeekBar.setThumbColor(getResources().getColor(R.color.mission_color_lifting),getResources().getColor(R.color.mission_color_lifting)); //시크바 풍선 바탕색
                        }else if(missionType.equals("TRAPING")){
                            discreteSeekBar.setTrackColor(getResources().getColor(R.color.color6)); //시크바 트랙색
                            discreteSeekBar.setScrubberColor(getResources().getColor(R.color.mission_color_trraping));  //진행색
                            discreteSeekBar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.enabled_red)); //시크바원
                            discreteSeekBar.setRippleColor(getResources().getColor(R.color.enabled_red));
                            discreteSeekBar.setThumbColor(getResources().getColor(R.color.mission_color_trraping),getResources().getColor(R.color.mission_color_trraping)); //시크바 풍선 바탕색
                        }else if(missionType.equals("AROUND")){
                            discreteSeekBar.setTrackColor(getResources().getColor(R.color.color6)); //시크바 트랙색
                            discreteSeekBar.setScrubberColor(getResources().getColor(R.color.mission_color_around));  //진행색
                            discreteSeekBar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.enabled_red)); //시크바원
                            discreteSeekBar.setRippleColor(getResources().getColor(R.color.enabled_red));
                            discreteSeekBar.setThumbColor(getResources().getColor(R.color.mission_color_around),getResources().getColor(R.color.mission_color_around)); //시크바 풍선 바탕색
                        }else if(missionType.equals("FLICK")){
                            discreteSeekBar.setTrackColor(getResources().getColor(R.color.color6)); //시크바 트랙색
                            discreteSeekBar.setScrubberColor(getResources().getColor(R.color.mission_color_flick));  //진행색
                            discreteSeekBar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.enabled_red)); //시크바원
                            discreteSeekBar.setRippleColor(getResources().getColor(R.color.enabled_red));
                            discreteSeekBar.setThumbColor(getResources().getColor(R.color.mission_color_flick),getResources().getColor(R.color.mission_color_flick)); //시크바 풍선 바탕색
                        }else if(missionType.equals("COMPLEX")){
                            discreteSeekBar.setTrackColor(getResources().getColor(R.color.color6)); //시크바 트랙색
                            discreteSeekBar.setScrubberColor(getResources().getColor(R.color.mission_color_complex));  //진행색
                            discreteSeekBar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.enabled_red)); //시크바원
                            discreteSeekBar.setRippleColor(getResources().getColor(R.color.enabled_red));
                            discreteSeekBar.setThumbColor(getResources().getColor(R.color.mission_color_complex),getResources().getColor(R.color.mission_color_complex)); //시크바 풍선 바탕색
                        }

                        discreteSeekBar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
                            @Override
                            public int transform(int value) {
                                mFlippableStack.setCurrentItem(value);
                                return value+1;
                            }

                            @Override
                            public String transformToString(int value) {
                                mFlippableStack.setCurrentItem(value);
                                value = value + 1 ;
                                return "";
                            }

                            @Override
                            public boolean useStringTransform() {
                                return true;
                            }
                        });
                    }
            }
            @Override
            public void onFailure(Call<List<Mission>> call, Throwable t) {
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


    public void poinOnClick(View v){
        Intent intent = new Intent(this, PointMainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult() ===============================");
    }

}
