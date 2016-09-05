package com.mom.soccer.ins;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.adapter.InsPointBalaceAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.InsPointBalance;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.InsPointBalanceService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsPointHistory extends AppCompatActivity {

    @Bind(R.id.li_no_found)
    LinearLayout li_no_found;

    private SlidingUpPanelLayout mLayout;

    @Bind(R.id.insPointRecylerView)
    RecyclerView insPointRecylerView;

    InsPointBalaceAdapter insPointBalaceAdapter;
    List<InsPointBalance> insPointBalances;

    Activity activity;
    PrefUtil prefUtil;
    Instructor ins;
    User user;

    @Bind(R.id.slidingimg)
    ImageView slidingimg;

    @Bind(R.id.cashtotalamount)
    TextView cashtotalamount;

    private int cashAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_point_history);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        ins = prefUtil.getIns();
        user = prefUtil.getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        activity = this;
        insPointHistory();

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState.toString().equals("COLLAPSED")){
                    slidingimg.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                }else if(newState.toString().equals("EXPANDED")){
                    slidingimg.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                }
            }
        });

    }

    public void insPointHistory(){
        WaitingDialog.cancelWaitingDialog();
        InsPointBalanceService insPointBalanceService = ServiceGenerator.createService(InsPointBalanceService.class,getApplicationContext(),ins);
        Call<List<InsPointBalance>> call = insPointBalanceService.getInsPointBalanceList(ins.getInstructorid());
        call.enqueue(new Callback<List<InsPointBalance>>() {
            @Override
            public void onResponse(Call<List<InsPointBalance>> call, Response<List<InsPointBalance>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    insPointBalances = response.body();
                    if(insPointBalances.size()==0){
                        li_no_found.setVisibility(View.VISIBLE);
                    }else{
                        li_no_found.setVisibility(View.GONE);
                    }

                    insPointBalaceAdapter = new InsPointBalaceAdapter(activity,ins,insPointBalances);
                    insPointRecylerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    insPointRecylerView.setHasFixedSize(true);
                    insPointRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(insPointBalaceAdapter);
                    alphaAdapter.setDuration(500);
                    insPointRecylerView.setAdapter(alphaAdapter);

                    //cashAmount
                    for(int i=0; insPointBalances.size() > i; i++){
                        cashAmount = insPointBalances.get(i).getCashpoint() + cashAmount;
                    }

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    cashtotalamount.setText(numberFormat.format(cashAmount));

                }
            }

            @Override
            public void onFailure(Call<List<InsPointBalance>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
