package com.mom.soccer.Ranking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.adapter.MainRankingAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.UserRangkinVo;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRankingActivity extends AppCompatActivity {

    private static final String TAG = "UserRankingActivity";

    String pageFlag= null;
    MainRankingAdapter mainRankingAdapter;
    ListView listView;

    User user;
    PrefUtil prefUtil;

    @Bind(R.id.ranking_title)
    TextView textView_ranking_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_ranking_layout);
        ButterKnife.bind(this);

        Log.i(TAG,"onCreate() ===============================================");

        Intent intent = getIntent();
        pageFlag = intent.getExtras().getString("pageparam");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.ranking_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        if(pageFlag.equals("total")){
            textView_ranking_title.setText(R.string.toolbar_ranking_all);
        }else if((pageFlag.equals("team"))){
            textView_ranking_title.setText(R.string.toolbar_ranking_friend);
        }else if(pageFlag.equals("friend")){
            textView_ranking_title.setText(R.string.toolbar_ranking_team);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart() ===============================================");

        if(pageFlag.equals("total")){
            listView = (ListView) findViewById(R.id.rankingpage_total_ranking);
            getTotalRankingList();
        }else if((pageFlag.equals("team"))){

        }else if(pageFlag.equals("friend")){

        }

    }

    public void getTotalRankingList(){

        DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);

        UserRangkinVo userRangkinVo = new UserRangkinVo();
        userRangkinVo.setQueryRow(30);
        userRangkinVo.setOrderbytype("totalscore");

        final Call<List<UserRangkinVo>> call = dataService.getTotalRanking(userRangkinVo);

        call.enqueue(new Callback<List<UserRangkinVo>>() {
            @Override
            public void onResponse(Call<List<UserRangkinVo>> call, Response<List<UserRangkinVo>> response) {
                if(response.isSuccessful()){
                    List<UserRangkinVo> listVos = response.body();
                    mainRankingAdapter = new MainRankingAdapter(getApplicationContext(), R.layout.adabter_mainlist_layout,listVos);
                    listView.setAdapter(mainRankingAdapter);
                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserRangkinVo>> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가 : " + t.getMessage());
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.ranking_bell)
    public void btnBell(){

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

}
