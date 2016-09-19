package com.mom.soccer.mission;

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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.adapter.UserMissionAdapter;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.UserMainVo;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.UserMainService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMissionListActivity extends AppCompatActivity {

    @Bind(R.id.u_mission_list_toolbar_title)
    TextView text_toolbar_title;

    private Activity activity;

    private Intent intent;
    private User user;
    private PrefUtil prefUtil;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.searchUserMissionRecyclerview)
    RecyclerView searchUserMissionRecyclerview;

    @Bind(R.id.li_no_found)
    LinearLayout li_no_found;

    private LinearLayoutManager linearLayoutManager;
    private UserMissionAdapter userMissionAdapter;

    //paging function need
    private int page= 0;
    private int offset = 0;
    private int limit = 40;
    private boolean lastData = false;

    //query
    private boolean queryExecute = false;
    private UserMainVo query = new UserMainVo();

    private List<UserMainVo> userMainVos = new ArrayList<UserMainVo>();
    private int missionid;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_usermisstion_list_layout);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.u_mission_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        text_toolbar_title.setText(getString(R.string.toolbar_umission_list_page_title));

        intent = getIntent();
        missionid = intent.getExtras().getInt("missionid");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();
        activity = this;

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        query.setMissionid(missionid);
        getSearchUserMission(query,"new");

        //1.스와이프 이벤트 설정
        final View finalView = getWindow().getDecorView().getRootView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSearchUserMission(query,"new");
                MomSnakBar.show(finalView,activity,getString(R.string.bottom_msg4));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //2
        if(Build.VERSION.SDK_INT  >= 20) {
            searchUserMissionRecyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        swipeRefreshLayout.setEnabled(true);
                    } else {
                        swipeRefreshLayout.setEnabled(false);
                    }
                }
            });
        }else{
            searchUserMissionRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener(){
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


        searchUserMissionRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (linearLayoutManager.findLastVisibleItemPosition() == userMainVos.size()-1) {

                        if(!lastData){
                            getSearchUserMission(query,"next");
                        }else{
                            MomSnakBar.show(finalView,activity,getResources().getString(R.string.bottom_msg2));
                        }
                    }
                }
            }
        });

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

    //Query
    public void getSearchUserMission(UserMainVo query, final String queryStatus){
        WaitingDialog.showWaitingDialog(activity,false);
        UserMainService userMainService = ServiceGenerator.createService(UserMainService.class,getApplicationContext(),user);
        Call<List<UserMainVo>> c = userMainService.getUserMainList(query);

        if(queryStatus.equals("new")) {
            offset = 0;
        }else{
            page = page + 1;
            offset = limit * page;
        }
        query.setOffset(offset);
        query.setLimit(limit);

        c.enqueue(new Callback<List<UserMainVo>>() {
            @Override
            public void onResponse(Call<List<UserMainVo>> call, Response<List<UserMainVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){

                    if(queryStatus.equals("new")){
                        userMainVos = response.body();

                        if(userMainVos.size()==0){
                            li_no_found.setVisibility(View.VISIBLE);
                        }else{
                            li_no_found.setVisibility(View.GONE);
                        }

                        searchUserMissionRecyclerview.setHasFixedSize(true);
                        searchUserMissionRecyclerview.setLayoutManager(linearLayoutManager);
                        userMissionAdapter = new UserMissionAdapter(activity,user,userMainVos);

                        searchUserMissionRecyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        searchUserMissionRecyclerview.getItemAnimator().setAddDuration(300);
                        searchUserMissionRecyclerview.getItemAnimator().setRemoveDuration(300);
                        searchUserMissionRecyclerview.getItemAnimator().setMoveDuration(300);
                        searchUserMissionRecyclerview.getItemAnimator().setChangeDuration(300);
                        searchUserMissionRecyclerview.setHasFixedSize(true);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(userMissionAdapter);
                        alphaAdapter.setDuration(500);
                        searchUserMissionRecyclerview.setAdapter(userMissionAdapter);

                        //페이징 초기화
                        lastData = false;
                        offset  = 0;
                        page    = 0;


                    }else{
                        List<UserMainVo> userMainVoList = response.body();

                        for(int i = 0 ; i < userMainVoList.size() ; i++)
                        {
                            userMainVos.add(userMainVoList.get(i));
                        }

                        userMissionAdapter.notifyDataSetChanged();

                        if(userMainVoList.size() != limit){
                            lastData = true;
                            userMainVoList = new ArrayList<UserMainVo>();
                        }
                        WaitingDialog.cancelWaitingDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserMainVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

}
