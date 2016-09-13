package com.mom.soccer.besideactivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.mom.soccer.retrofitdao.FavoriteMissionService;
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

public class FavoriteMissionActivity extends AppCompatActivity {

    private static final String TAG = "FavoriteMissionActivity";

    private int NUMBER_OF_FRAGMENTS;

    private FlippableStackView mFlippableStack;

    private MissionFragmentAdapter mPageAdapter;

    private List<Fragment> mViewPagerFragments;

    private List<Mission> missionList;

    private String missionType;

    private User user;
    private PrefUtil prefUtil;

    @Bind(R.id.fa_title)
    TextView textView_fa_title;

    @Bind(R.id.fuser_point)
    TextView tx_mission_point;

    @Bind(R.id.discreteSeekBar)
    DiscreteSeekBar discreteSeekBar;

    @Bind(R.id.li_no_found)
    LinearLayout li_no_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_favorite_mission_layout);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.fa_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        textView_fa_title.setText(R.string.toolbar_favorite_page);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

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

    private void createViewPagerFragments() {
        mViewPagerFragments = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_FRAGMENTS; ++i) {
            mViewPagerFragments.add(MainMissionFragment.newInstance(i, missionList.get(i),user,"FAV"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserPoint(user.getUid());
        getMissionList();
    }

    public void getMissionList(){
        Mission mission = new Mission();
        mission.setUid(user.getUid());
        mFlippableStack = (FlippableStackView) findViewById(R.id.user_flippable_stack_view);

        WaitingDialog.showWaitingDialog(FavoriteMissionActivity.this,false);
        FavoriteMissionService fservice = ServiceGenerator.createService(FavoriteMissionService.class,this,user);
        final Call<List<Mission>> call = fservice.getFavoriteMissionList(mission);

        call.enqueue(new Callback<List<Mission>>() {
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    missionList = response.body();
                    if(missionList.size()==0){
                        li_no_found.setVisibility(View.VISIBLE);
                    }else{
                        li_no_found.setVisibility(View.GONE);

                        NUMBER_OF_FRAGMENTS = missionList.size();
                        createViewPagerFragments();
                        mPageAdapter = new MissionFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);
                        boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

                        mFlippableStack.initStack(NUMBER_OF_FRAGMENTS,
                                portrait ? StackPageTransformer.Orientation.VERTICAL : StackPageTransformer.Orientation.HORIZONTAL
                                ,0.9f  //전체크기
                                ,0.9f
                                ,0.4f   //
                                ,StackPageTransformer.Gravity.CENTER
                        );
                        mFlippableStack.setAdapter(mPageAdapter);

                        //Seekbar apply
                        discreteSeekBar.setMin(0);
                        //discreteSeekBar.setProgress(NUMBER_OF_FRAGMENTS-1);
                        discreteSeekBar.setMax(NUMBER_OF_FRAGMENTS-1);
                        discreteSeekBar.setProgress(NUMBER_OF_FRAGMENTS);

                        discreteSeekBar.setTrackColor(getResources().getColor(R.color.color6)); //시크바 트랙색
                        discreteSeekBar.setScrubberColor(getResources().getColor(R.color.enabled_red));  //진행색
                        discreteSeekBar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.enabled_red)); //시크바원
                        discreteSeekBar.setRippleColor(getResources().getColor(R.color.enabled_red));
                        discreteSeekBar.setThumbColor(getResources().getColor(R.color.enabled_red),getResources().getColor(R.color.enabled_red)); //시크바 풍선 바탕색

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
            }

            @Override
            public void onFailure(Call<List<Mission>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void getUserPoint(int uid){
        WaitingDialog.showWaitingDialog(FavoriteMissionActivity.this,false);
        PointService pointService = ServiceGenerator.createService(PointService.class,this,user);
        Call<SpBalanceHeader> call = pointService.getSelfAmt(uid);
        call.enqueue(new Callback<SpBalanceHeader>() {
            @Override
            public void onResponse(Call<SpBalanceHeader> call, Response<SpBalanceHeader> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    SpBalanceHeader spBalanceHeader = response.body();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    tx_mission_point.setText(numberFormat.format(spBalanceHeader.getAmount()));
                }
            }

            @Override
            public void onFailure(Call<SpBalanceHeader> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
            }
        });
    }

}
