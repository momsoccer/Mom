package com.mom.soccer.mission;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.bartoszlipinski.flippablestackview.StackPageTransformer;
import com.mom.soccer.R;
import com.mom.soccer.adapter.MissionFragmentAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.User;
import com.mom.soccer.fragment.MainMissionFragment;
import com.mom.soccer.retrofitdao.MissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_mission_layout);

        Log.d(TAG,"onCreate() ===========================================");
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Intent intent = getIntent();
        missionType = intent.getExtras().getString(MissionCommon.MISSIONTYPE);
        getMissionList(missionType);

        Locale locale = getResources().getConfiguration().locale;
        String lang = locale.getLanguage();

        Log.d(TAG,"국가 코드는 : " + lang);

    }

    private void createViewPagerFragments() {
        Log.d(TAG,"이곳은 createViewPagerFragments()");
        mViewPagerFragments = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_FRAGMENTS; ++i) {
            mViewPagerFragments.add(MainMissionFragment.newInstance(i,missionList.get(i).getMissionid(),missionList.get(i).getTypename()));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart()===========================================");
    }


    public void getMissionList(String missiontype){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);
        dialog.show();

        MissionService missionService = ServiceGenerator.createService(MissionService.class,this,user);

        final Call<List<Mission>> call = missionService.getMissionList("7",null,null,null);

        call.enqueue(new Callback<List<Mission>>() {
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {

                if(response.isSuccessful()){
                    missionList = response.body();
                    Log.d(TAG,"이곳은 missionService()");
                    dialog.dismiss();

                    NUMBER_OF_FRAGMENTS = missionList.size();

                    createViewPagerFragments();
                    mPageAdapter = new MissionFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);
                    boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                    mFlippableStack = (FlippableStackView) findViewById(R.id.flippable_stack_view);
                    mFlippableStack.initStack(NUMBER_OF_FRAGMENTS, portrait ?
                            StackPageTransformer.Orientation.VERTICAL :
                            StackPageTransformer.Orientation.HORIZONTAL);
                    mFlippableStack.setAdapter(mPageAdapter);

                }else{
                    NUMBER_OF_FRAGMENTS = missionList.size();
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Mission>> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가 : " + t.getMessage());
                dialog.dismiss();
                t.printStackTrace();
            }
        });
    }

}
