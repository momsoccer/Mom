package com.mom.soccer.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mom.soccer.R;
import com.mom.soccer.adapter.FeedBackReqAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retropitutil.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    RecyclerView recyclerview;
    FeedBackReqAdapter feedBackReqAdapter;
    List<FeedbackHeader> feedbackHeaderList;

    User user;
    PrefUtil prefUtil;
    Instructor instructor;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_layout);
        Log.d(TAG, "onCreate =========================================================== ***");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();
        instructor = prefUtil.getIns();

        activity = this;

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        FeedbackHeader header = new FeedbackHeader();


        FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class);
        Call<List<FeedbackHeader>> listCall = feedBackService.getFeedAllList(header);

        listCall.enqueue(new Callback<List<FeedbackHeader>>() {
            @Override
            public void onResponse(Call<List<FeedbackHeader>> call, Response<List<FeedbackHeader>> response) {
                if(response.isSuccessful()){
                    feedbackHeaderList = response.body();

                    feedBackReqAdapter = new FeedBackReqAdapter(activity,feedbackHeaderList,user,instructor);
                    recyclerview.setHasFixedSize(true);
                    recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerview.setAdapter(feedBackReqAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<FeedbackHeader>> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState ===========================================================");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume ===========================================================*");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ===========================================================");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop ===========================================================");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy ===========================================================");
    }
}
