package com.mom.soccer.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mom.soccer.R;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestGridActivity extends AppCompatActivity {

    private static final String TAG = "TestGridActivity";

    private GridView videoGridView;
    private GridMissionAdapter gridMissionAdapter;

    private List<UserMission> userMissionList;
    private UserMission qUserMission = new UserMission();

    private User user;
    PrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tc_grid_layout);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        videoGridView = (GridView) findViewById(R.id.tcGrid);

        qUserMission.setUid(user.getUid());

        userMissionList(qUserMission);

        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG,"클릭 아이템은 : " + userMissionList.get(position).getSubject());
            }
        });
    }


    public void userMissionList(UserMission userMission){

        Log.d(TAG,"userMissionList ============================================!!!");

        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,getApplicationContext(),user);
        Call<List<UserMission>> call = userMissionService.uMissionList(userMission);

        call.enqueue(new Callback<List<UserMission>>() {
            @Override
            public void onResponse(Call<List<UserMission>> call, Response<List<UserMission>> response) {

                if(response.isSuccessful()){

                    userMissionList = response.body();
                    gridMissionAdapter = new GridMissionAdapter(getApplicationContext(),R.layout.adapter_user_mission_grid_item,userMissionList,"YOU");
                    videoGridView.setAdapter(gridMissionAdapter);
                }else{

                }

            }

            @Override
            public void onFailure(Call<List<UserMission>> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }

}
