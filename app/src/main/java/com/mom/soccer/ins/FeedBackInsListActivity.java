package com.mom.soccer.ins;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;

import com.mom.soccer.R;
import com.mom.soccer.adapter.InsInfoRecyclerAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.InsInfoVo;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackInsListActivity extends AppCompatActivity {

    private static final String TAG = "FeedBackInsListActivity";

    PrefUtil prefUtil;
    User user;

    RecyclerView recyclerView;
    InsInfoRecyclerAdapter recyclerAdapter;
    List<InsInfoVo> insInfoVoList = new ArrayList<>();

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_feed_back_ins_list);

        activity = this;

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.feddback_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        recyclerView = (RecyclerView) findViewById(R.id.feedback_recyclerview);

    }


    @Override
    protected void onStart() {
        super.onStart();
        getInsInfoList();
    }

    public void getInsInfoList(){
        WaitingDialog.showWaitingDialog(this,false);
        DataService service = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);
        InsInfoVo vo = new InsInfoVo();
        vo.setNouid(user.getUid());
        Call<List<InsInfoVo>> voCall = service.getInsInfoList(vo);
        voCall.enqueue(new Callback<List<InsInfoVo>>() {
            @Override
            public void onResponse(Call<List<InsInfoVo>> call, Response<List<InsInfoVo>> response) {
                if(response.isSuccessful()){
                    WaitingDialog.cancelWaitingDialog();

                    insInfoVoList = response.body();
                    recyclerAdapter = new InsInfoRecyclerAdapter(activity,insInfoVoList);
                    recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    recyclerView.getItemAnimator().setAddDuration(300);
                    recyclerView.getItemAnimator().setRemoveDuration(300);
                    recyclerView.getItemAnimator().setMoveDuration(300);
                    recyclerView.getItemAnimator().setChangeDuration(300);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(FeedBackInsListActivity.this));
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(recyclerAdapter);
                    alphaAdapter.setDuration(500);
                    recyclerView.setAdapter(alphaAdapter);

                }else{
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<List<InsInfoVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

}
