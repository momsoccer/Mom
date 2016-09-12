package com.mom.soccer.Ranking;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.adapter.RankingItemAdapter;
import com.mom.soccer.adapter.TeamRankingItemAdapter;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.TeamRankingVo;
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
    TeamRankingItemAdapter teamRankingItemAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;

    private int page= 0;
    private int offset = 0;
    private int limit = 40;
    private boolean lastData = false;

    List<TeamRankingVo> teamRankingVos = new ArrayList<TeamRankingVo>();
    List<UserRangkinVo> userRangkinVos = new ArrayList<UserRangkinVo>();


    @TargetApi(Build.VERSION_CODES.M)
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

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        if(pageFlag.equals("total")){
            textView_ranking_title.setText(R.string.toolbar_ranking_all);
            li_bacground_lyout.setBackground(getResources().getDrawable(R.drawable.ranking));
            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
            getTotalRankingList("new");
        }else if((pageFlag.equals("friend"))){
            textView_ranking_title.setText(R.string.toolbar_ranking_friend);
            li_bacground_lyout.setBackground(getResources().getDrawable(R.drawable.freind_ranking));
            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
            getTotalRankingList("new");
        }else if((pageFlag.equals("team"))){
            textView_ranking_title.setText(R.string.toolbar_ranking_team);
            li_bacground_lyout.setBackground(getResources().getDrawable(R.drawable.team_ranking));
            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
            getTotalRankingList("new");
        }else if((pageFlag.equals("allteam"))){
            textView_ranking_title.setText(R.string.toolbar_ranking_allteam);
            li_bacground_lyout.setBackground(getResources().getDrawable(R.drawable.team_ranking));
            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
            getTeamRankingScore();
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        if(pageFlag.equals("total")){

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    View parentLayout = findViewById(R.id.rootview);
                    MomSnakBar.show(parentLayout,activity,getString(R.string.bottom_msg4));
                    getTotalRankingList("new");
                    swipeRefreshLayout.setRefreshing(false);
                }
            });


            //2.스와이프 이벤트 버전별
            if(Build.VERSION.SDK_INT  >= 20) {
                totalRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() <= 0) {
                            Log.i(TAG,"스와이프 1 " + linearLayoutManager.findFirstCompletelyVisibleItemPosition());
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            Log.i(TAG,"스와이프 2 " + linearLayoutManager.findFirstCompletelyVisibleItemPosition());
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }else{
                totalRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }

            totalRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        if (linearLayoutManager.findLastVisibleItemPosition() == userRangkinVos.size()-1) {
                            if(!lastData){
                                getTotalRankingList("next");
                            }else{
                                View parentLayout = findViewById(R.id.rootview);
                                MomSnakBar.show(parentLayout,activity,getString(R.string.bottom_msg3));
                            }
                        }
                    }
                }
            });


        }else{
            swipeRefreshLayout.setEnabled(false);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart() ===============================================");

    }

    public void getTeamRankingScore(){
        WaitingDialog.showWaitingDialog(UserRankingActivity.this,false);
        DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);

        Call<List<TeamRankingVo>> listCall = dataService.getTeamRankingScore(40);
        listCall.enqueue(new Callback<List<TeamRankingVo>>() {
            @Override
            public void onResponse(Call<List<TeamRankingVo>> call, Response<List<TeamRankingVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    teamRankingVos = response.body();
                    teamRankingItemAdapter = new TeamRankingItemAdapter(activity,teamRankingVos,user,0);
                    totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
                    totalRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    totalRecyclerView.setHasFixedSize(true);
                    totalRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    totalRecyclerView.setAdapter(teamRankingItemAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<TeamRankingVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void getTotalRankingList(final String pagingStatus){
        WaitingDialog.showWaitingDialog(UserRankingActivity.this,false);
        DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);
        UserRangkinVo query = new UserRangkinVo();
        query.setQueryRow(40);
        query.setOrderbytype("totalscore");
        query.setUid(user.getUid());

        if(pagingStatus.equals("next")){
            page = page + 1;
            offset = limit * page;
        }else{
            offset = 0;
        }
        query.setOffset(offset);
        query.setLimit(limit);

        if(pageFlag.equals("total")){
            final Call<List<UserRangkinVo>> call = dataService.getTotalRanking(query);
            call.enqueue(new Callback<List<UserRangkinVo>>() {
                @Override
                public void onResponse(Call<List<UserRangkinVo>> call, Response<List<UserRangkinVo>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        if(pagingStatus.equals("new")){

                            userRangkinVos = response.body();
                            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
                            rankingItemAdapter = new RankingItemAdapter(activity,userRangkinVos,user);
                            totalRecyclerView.setLayoutManager(linearLayoutManager);
                            totalRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                            totalRecyclerView.setHasFixedSize(true);
                            totalRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            totalRecyclerView.setAdapter(rankingItemAdapter);
                            //페이징 초기화
                            lastData = false;
                            offset  = 0;
                            page    = 0;
                        }else{
                            List<UserRangkinVo> userMainVoList = response.body();

                            for(int i = 0 ; i < userMainVoList.size() ; i++)
                            {
                                userRangkinVos.add(userMainVoList.get(i));
                            }

                            rankingItemAdapter.notifyDataSetChanged();

                            if(userMainVoList.size() != limit){
                                lastData = true;
                                userMainVoList = new ArrayList<UserRangkinVo>();
                            }
                            WaitingDialog.cancelWaitingDialog();
                        }
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
