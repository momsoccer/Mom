package com.mom.soccer.besideactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsApplyVo;
import com.mom.soccer.retrofitdao.InstructorService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.DialogBuilder;
import com.mom.soccer.widget.WaitingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyCoachActivity extends AppCompatActivity {

    private static final String TAG = "ApplyCoachActivity";

    TextInputLayout layout_name,layout_age,
            layout_playeryear, layout_insyear, layout_bankname, layout_address, layout_momteamname, layout_cuteamname,
            layout_phone, layout_resume, layout_career1, layout_career2, layout_career3, layout_career4, layout_career5;

    EditText ins_name,ins_age,ins_playeryear,ins_year,
            ins_bankname, ins_bankaccount,ins_address,
            ins_momteamname, ins_cuteamname, ins_phone, ins_resume,
            ins_career1, ins_career2, ins_career3, ins_career4, ins_career5;

    @Bind(R.id.coachapply_title)
    TextView textView_coachapply_title;

    @Bind(R.id.im_ins_insimg)
    ImageView im_ins_insimg;

    private User user;
    private PrefUtil prefUtil;
    private InsApplyVo insApplyVo = new InsApplyVo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_apply_coach);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.coachapply_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        textView_coachapply_title.setText(R.string.toolbar_apply_coach);

        /*
        InsApplyFragment insApplyFragment = new InsApplyFragment(this,user);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.ins_frame,insApplyFragment,"");
        tc.commit();
        */

        layout_name = (TextInputLayout) findViewById(R.id.layout_name);
        layout_age = (TextInputLayout) findViewById(R.id.layout_age);
        layout_playeryear = (TextInputLayout) findViewById(R.id.layout_playeryear);
        layout_insyear = (TextInputLayout) findViewById(R.id.layout_insyear);
        layout_bankname = (TextInputLayout) findViewById(R.id.layout_bankname);
        layout_address = (TextInputLayout) findViewById(R.id.layout_address);
        layout_momteamname = (TextInputLayout) findViewById(R.id.layout_momteamname);
        layout_cuteamname = (TextInputLayout) findViewById(R.id.layout_cuteamname);
        layout_phone = (TextInputLayout) findViewById(R.id.layout_phone);
        layout_resume = (TextInputLayout) findViewById(R.id.layout_resume);
        layout_career1 = (TextInputLayout) findViewById(R.id.layout_career1);
        layout_career2 = (TextInputLayout) findViewById(R.id.layout_career2);
        layout_career3 = (TextInputLayout) findViewById(R.id.layout_career3);
        layout_career4 = (TextInputLayout) findViewById(R.id.layout_career4);
        layout_career5 = (TextInputLayout) findViewById(R.id.layout_career5);

        ins_name = (EditText) findViewById(R.id.ins_name);
        ins_age = (EditText) findViewById(R.id.ins_age);
        ins_playeryear = (EditText) findViewById(R.id.ins_playeryear);
        ins_year = (EditText) findViewById(R.id.ins_year);
        ins_bankname = (EditText) findViewById(R.id.ins_bankname);
        ins_bankaccount = (EditText) findViewById(R.id.ins_bankaccount);
        ins_address = (EditText) findViewById(R.id.ins_address);
        ins_momteamname = (EditText) findViewById(R.id.ins_momteamname);
        ins_cuteamname = (EditText) findViewById(R.id.ins_cuteamname);
        ins_phone = (EditText) findViewById(R.id.ins_phone);
        ins_resume = (EditText) findViewById(R.id.ins_resume);
        ins_career1 = (EditText) findViewById(R.id.ins_career1);
        ins_career2 = (EditText) findViewById(R.id.ins_career2);
        ins_career3 = (EditText) findViewById(R.id.ins_career3);
        ins_career4 = (EditText) findViewById(R.id.ins_career4);
        ins_career5 = (EditText) findViewById(R.id.ins_career5);

        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telManager.getLine1Number();

        ins_name.setText(user.getUsername());
        ins_phone.setText(phoneNumber);

        if(!Compare.isEmpty(user.getProfileimgurl())) {
            Glide.with(ApplyCoachActivity.this)
                    .load(user.getProfileimgurl())
                    .into(im_ins_insimg);
        }


    }

    @OnClick(R.id.btnApply)
    public void btnApply(){

        if (Compare.isEmpty(ins_name.getText().toString())) {
            layout_name.setError(getString(R.string.coach_vali_name));
            return;
        }  else if((Compare.isEmpty(ins_age.getText().toString()))){
            layout_age.setError(getString(R.string.coach_vali_age));
            return;
        }  else if((Compare.isEmpty(ins_resume.getText().toString()))){
            layout_resume.setError(getString(R.string.coach_vali_resume));
            return;
        }  else if((Compare.isEmpty(ins_phone.getText().toString()))){
            layout_phone.setError(getString(R.string.coach_vali_phone));
            return;
        }  else if((Compare.isEmpty(ins_momteamname.getText().toString()))){
            layout_momteamname.setError(getString(R.string.coach_vali_teamname));
            return;
        }  else if((Compare.isEmpty(ins_playeryear.getText().toString()))){
            layout_playeryear.setError(getString(R.string.coach_vali_playeryear));
            return;
        }  else if((Compare.isEmpty(ins_year.getText().toString()))){
            layout_insyear.setError(getString(R.string.coach_vali_insyear));
            return;
        }

        new DialogBuilder(ApplyCoachActivity.this)
                //.setTitle("Title")
                .setMessage(getString(R.string.coach_req_messge))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reqIns();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

    }

    public void imageOnClick(View v){
        switch (v.getId()){
            case R.id.im_ins_teaimg:
                break;

            case R.id.im_ins_insimg:
                break;
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


    public void reqIns(){

        insApplyVo.setUid(user.getUid());
        WaitingDialog.showWaitingDialog(ApplyCoachActivity.this,false);

        InstructorService service = ServiceGenerator.createService(InstructorService.class,this,user);
        Call<ServerResult> c = service.insApply(insApplyVo);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){

                    ServerResult result = response.body();

                    Log.i(TAG,"값은 : " + result.toString());
                    WaitingDialog.cancelWaitingDialog();
                }else{
                    Log.i(TAG,"오류 입니다1");
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                Log.i(TAG,"오류 입니다2");
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });

    }

}
