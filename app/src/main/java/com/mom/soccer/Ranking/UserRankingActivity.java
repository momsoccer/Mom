package com.mom.soccer.Ranking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.adapter.RankingItemAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.UserRangkinVo;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRankingActivity extends AppCompatActivity {

    private static final String TAG = "UserRankingActivity";

    String pageFlag= null;

    User user;
    PrefUtil prefUtil;

    @Bind(R.id.ranking_title)
    TextView textView_ranking_title;

    @Bind(R.id.li_bacground_lyout)
    LinearLayout li_bacground_lyout;

    private Activity activity;

    RecyclerView totalRecyclerView;
    RankingItemAdapter rankingItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_ranking_layout);
        ButterKnife.bind(this);

        Log.i(TAG,"onCreate() ===============================================");

        activity = this;
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
        }else if((pageFlag.equals("friend"))){
            textView_ranking_title.setText(R.string.toolbar_ranking_friend);
        }else if((pageFlag.equals("team"))){
            textView_ranking_title.setText(R.string.toolbar_ranking_team);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart() ===============================================");

        if(pageFlag.equals("total")){
            li_bacground_lyout.setBackground(getResources().getDrawable(R.drawable.ranking));
            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
            getTotalRankingList();
        }else if((pageFlag.equals("team"))){
            li_bacground_lyout.setBackground(getResources().getDrawable(R.drawable.team_ranking));
            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
            getTotalRankingList();
        }else if((pageFlag.equals("friend"))){
            li_bacground_lyout.setBackground(getResources().getDrawable(R.drawable.freind_ranking));
            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
            getTotalRankingList();
        }

    }

    public void getTotalRankingList(){

        DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);
        UserRangkinVo query = new UserRangkinVo();
        query.setQueryRow(30);
        query.setOrderbytype("totalscore");
        query.setUid(user.getUid());

        if(pageFlag.equals("total")){
            final Call<List<UserRangkinVo>> call = dataService.getTotalRanking(query);
            call.enqueue(new Callback<List<UserRangkinVo>>() {
                @Override
                public void onResponse(Call<List<UserRangkinVo>> call, Response<List<UserRangkinVo>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        List<UserRangkinVo> userRangkinVos = new ArrayList<UserRangkinVo>();
                        userRangkinVos = response.body();
                        totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
                        rankingItemAdapter = new RankingItemAdapter(activity,userRangkinVos,user);

                        totalRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        totalRecyclerView.setHasFixedSize(true);
                        totalRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        totalRecyclerView.setAdapter(rankingItemAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<UserRangkinVo>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }else if((pageFlag.equals("team"))){
            final Call<List<UserRangkinVo>> call = dataService.getTeamRanking(query);
            call.enqueue(new Callback<List<UserRangkinVo>>() {
                @Override
                public void onResponse(Call<List<UserRangkinVo>> call, Response<List<UserRangkinVo>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        List<UserRangkinVo> userRangkinVos = new ArrayList<UserRangkinVo>();
                        userRangkinVos = response.body();
                        totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
                        rankingItemAdapter = new RankingItemAdapter(activity,userRangkinVos,user);

                        totalRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        totalRecyclerView.setHasFixedSize(true);
                        totalRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        totalRecyclerView.setAdapter(rankingItemAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<UserRangkinVo>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }else if((pageFlag.equals("friend"))) {
            WaitingDialog.showWaitingDialog(UserRankingActivity.this,false);
            final Call<List<UserRangkinVo>> call = dataService.getFriendRanking(query);
            call.enqueue(new Callback<List<UserRangkinVo>>() {
                @Override
                public void onResponse(Call<List<UserRangkinVo>> call, Response<List<UserRangkinVo>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if (response.isSuccessful()) {
                        List<UserRangkinVo> userRangkinVos = new ArrayList<UserRangkinVo>();
                        userRangkinVos = response.body();

                        if(userRangkinVos.size()!=0){
                            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
                            rankingItemAdapter = new RankingItemAdapter(activity,userRangkinVos,user);

                            totalRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                            totalRecyclerView.setHasFixedSize(true);
                            totalRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            totalRecyclerView.setAdapter(rankingItemAdapter);
                        }


                    }
                }

                @Override
                public void onFailure(Call<List<UserRangkinVo>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }


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
