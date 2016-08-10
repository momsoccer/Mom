package com.mom.soccer.besideactivity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mom.soccer.widget.VeteranToast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_favorite_mission_layout);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.fa_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
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
            mViewPagerFragments.add(MainMissionFragment.newInstance(i, missionList.get(i),user));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart()===========================================");
        getUserPoint(user.getUid());
        getMissionList();
    }

    public void getMissionList(){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);
        dialog.show();

        Mission mission = new Mission();
        mission.setUid(user.getUid());

        FavoriteMissionService fservice = ServiceGenerator.createService(FavoriteMissionService.class,this,user);
        final Call<List<Mission>> call = fservice.getFavoriteMissionList(mission);

        call.enqueue(new Callback<List<Mission>>() {
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {

                if(response.isSuccessful()){
                    missionList = response.body();
                    Log.d(TAG,"이곳은 missionService()");
                    dialog.dismiss();

                    if(missionList.size()==0){
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.valid_mission_nodata_found), Toast.LENGTH_SHORT).show();
                    }else{


                        NUMBER_OF_FRAGMENTS = missionList.size();
                        createViewPagerFragments();
                        mPageAdapter = new MissionFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);
                        boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                        mFlippableStack = (FlippableStackView) findViewById(R.id.user_flippable_stack_view);
                        mFlippableStack.initStack(NUMBER_OF_FRAGMENTS,
                                portrait ? StackPageTransformer.Orientation.VERTICAL : StackPageTransformer.Orientation.HORIZONTAL
                                ,0.9f  //전체크기
                                ,0.9f
                                ,0.4f   //
                                ,StackPageTransformer.Gravity.CENTER
                        );

                        /*
          public void initStack(int numberOfStacked,
                      StackPageTransformer.Orientation orientation,
                      float currentPageScale,
                      float topStackedScale,
                      float overlapFactor,
                      StackPageTransformer.Gravity gravity)

                         */
                        mFlippableStack.setAdapter(mPageAdapter);
                    }
                }else{
                    //NUMBER_OF_FRAGMENTS = missionList.size();
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Mission>> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가 : " + t.getMessage());
                dialog.dismiss();
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
            }
        });
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
                    //VeteranToast.makeToast(getApplicationContext(),"getPoint "+getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SpBalanceHeader> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가 : " + t.getMessage());
                VeteranToast.makeToast(getApplicationContext(),"getPoint "+ getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
