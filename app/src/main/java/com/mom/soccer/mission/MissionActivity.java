package com.mom.soccer.mission;

import android.app.ProgressDialog;
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
import com.mom.soccer.point.PointMainActivity;
import com.mom.soccer.retrofitdao.MissionService;
import com.mom.soccer.retrofitdao.PointService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult() ===============================");
    }

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

        Log.d(TAG,"onCreate() missiontype : " + missionType);

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


        //getMissionList(missionType);

        //언어 설정 값
        Locale locale = getResources().getConfiguration().locale;
        String lang = locale.getLanguage();

        Log.d(TAG,"국가 코드는 : " + lang);

    }

    private void createViewPagerFragments() {
        Log.d(TAG,"이곳은 createViewPagerFragments()");
        mViewPagerFragments = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_FRAGMENTS; ++i) {

            //MainMissionFragment mainMissionFragment = new MainMissionFragment(user,missionList.get(i),i);
            //mViewPagerFragments.add(mainMissionFragment);

            mViewPagerFragments.add(MainMissionFragment.newInstance(i, missionList.get(i),user));

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
                    //VeteranToast.makeToast(getApplicationContext(),"getPoint "+getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SpBalanceHeader> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가 : " + t.getMessage());
                VeteranToast.makeToast(getApplicationContext(),"getPoint "+ getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                noDataPage();
            }
        });
    }

    public void getMissionList(String missiontype){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);
        dialog.show();

        MissionService missionService = ServiceGenerator.createService(MissionService.class,this,user);

        final Call<List<Mission>> call = missionService.getMissionList(missiontype,user.getUid());

        call.enqueue(new Callback<List<Mission>>() {
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {

                if(response.isSuccessful()){
                    missionList = response.body();
                    Log.d(TAG,"이곳은 missionService()");
                    dialog.dismiss();

                    if(missionList.size()==0){
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.valid_mission_nodata_found),Toast.LENGTH_SHORT).show();
                        noDataPage();
                    }else{
                        NUMBER_OF_FRAGMENTS = missionList.size();
                        createViewPagerFragments();
                        mPageAdapter = new MissionFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);
                        boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                        mFlippableStack = (FlippableStackView) findViewById(R.id.flippable_stack_view);
                        mFlippableStack.initStack(NUMBER_OF_FRAGMENTS, portrait ?
                                StackPageTransformer.Orientation.VERTICAL :
                                StackPageTransformer.Orientation.HORIZONTAL
                                ,0.9f
                                ,0.9f
                                ,0.9f
                                ,StackPageTransformer.Gravity.CENTER
                        );
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
                noDataPage();
            }
        });
    }




    public void noDataPage(){
        //네트워크 오류로 간단히 샘플을 보여준다
        missionList = new ArrayList<Mission>();

        for(int i=0; i < 5 ; i++ ){
            Mission mission = new Mission();
            mission.setTypename(missionType);
            mission.setMissionid(900);
            mission.setSequence(0);
            mission.setMissionname(getString(R.string.dermy_missionname));
            mission.setDescription(getString(R.string.dermy_disp));
            mission.setPrecon(getString(R.string.dermy_precon));
            mission.setGrade(100);
            mission.setPassgrade(100);
            missionList.add(mission);
        }

        NUMBER_OF_FRAGMENTS = missionList.size();

        createViewPagerFragments();
        mPageAdapter = new MissionFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);
        boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        mFlippableStack = (FlippableStackView) findViewById(R.id.flippable_stack_view);
        mFlippableStack.initStack(NUMBER_OF_FRAGMENTS,
                portrait ? StackPageTransformer.Orientation.VERTICAL : StackPageTransformer.Orientation.HORIZONTAL
                ,0.9f  //전체크기
                ,0.9f
                ,0.4f   //
                ,StackPageTransformer.Gravity.CENTER
        );
        mFlippableStack.setAdapter(mPageAdapter);
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

}
