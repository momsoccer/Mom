package com.mom.soccer.bookmark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.MyBookMark;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.UserMissionActivity;
import com.mom.soccer.retrofitdao.PickService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-02.
 */
public class MyBookMarkActivity extends AppCompatActivity {

    private static final String TAG = "MyBookMarkActivity";

    GridMissionAdapter gridMissionAdapter;
    GridView videoGridView;
    List<UserMission> userMissions;

    User user;
    PrefUtil prefUtil;

    @Bind(R.id.book_no_data_found)
    TextView book_no_data_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bookmarck_layout);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        videoGridView = (GridView) findViewById(R.id.book_grid_view);

        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.d(TAG,"포지션 아이디는 : " +position);


                Intent userMissionListIntent = new Intent(getApplicationContext(),UserMissionActivity.class);
                userMissionListIntent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMissions.get(position));
                startActivity(userMissionListIntent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        myBookMarkList();
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


    public void myBookMarkList(){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);

        MyBookMark myBookMark = new MyBookMark();
        myBookMark.setUid(user.getUid());

        PickService pickService = ServiceGenerator.createService(PickService.class,getApplicationContext(),user);
        Call<List<UserMission>> call = pickService.getMyBookMark(myBookMark);
        call.enqueue(new Callback<List<UserMission>>() {
            @Override
            public void onResponse(Call<List<UserMission>> call, Response<List<UserMission>> response) {

                if(response.isSuccessful()){
                    userMissions = response.body();
                    gridMissionAdapter = new GridMissionAdapter(getApplicationContext(),R.layout.adapter_book_mark_layout,userMissions,"ME");
                    videoGridView.setAdapter(gridMissionAdapter);
                    dialog.dismiss();

                    if(userMissions.size()==0){
                        book_no_data_found.setVisibility(View.VISIBLE);
                        book_no_data_found.setText("등록된 영상이 없습니다");
                    }else{
                        book_no_data_found.setVisibility(View.INVISIBLE);
                    }

                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<UserMission>> call, Throwable t) {
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.book_bell)
    public void book_bell(){
        VeteranToast.makeToast(getApplicationContext(),"준비중", Toast.LENGTH_SHORT).show();
    }
}
