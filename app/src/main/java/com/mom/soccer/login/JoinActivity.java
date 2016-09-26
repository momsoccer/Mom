package com.mom.soccer.login;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.momactivity.MomMainActivity;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {

    private static final String TAG = "JoinActivity";

    //구글 아이디 선택하기
    GoogleAccountCredential credential;
    private static final int REQUEST_ACCOUNT_PICKER = 2;

    private User userVo;
    private PrefUtil prefUtil;
    private String appActionFlag;
    private int UID=0;

    TextInputLayout layoutLoginEmail;
    TextInputLayout layoutLoginPassword;
    TextInputLayout layoutLoginPasswordConfirm;

    @Bind(R.id.joingmail)
    TextView Tx_joingmail;

    @Bind(R.id.join_email)
    EditText joinEmail;

    @Bind(R.id.join_password)
    EditText joinPassword;

    @Bind(R.id.join_password_confirm)
    EditText joinPasswordConfirm;

    @Bind(R.id.layout_username)
    TextInputLayout layout_username;

    @Bind(R.id.username)
    EditText username;


    //@Bind(R.id.txhintpassword)
    //TextView txhintpassword;

    @Bind(R.id.li_pw)
    LinearLayout layoutPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_join_layout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        userVo = new User();
        prefUtil = new PrefUtil(this);

        layoutLoginEmail = (TextInputLayout) findViewById(R.id.layout_join_email);
        layoutLoginPassword = (TextInputLayout) findViewById(R.id.layout_join_password);
        layoutLoginPasswordConfirm = (TextInputLayout) findViewById(R.id.layout_join_password_confirm);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        initialSet(intent);

        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(Auth.SCOPES));
        credential.setBackOff(new ExponentialBackOff());
    }

    protected void initialSet(Intent intent){

        //app_action_flag = email_user or sns_user
        //sns 유저일때는 비번이 필요가 없다
        appActionFlag = intent.getExtras().getString("app_action_flag");
        if(appActionFlag.equals("sns_user")){
            layoutPw.setVisibility(View.INVISIBLE);
            //TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //String phoneNum = tm.getLine1Number();

            String snstype = intent.getExtras().getString("snstype");
            String username = intent.getExtras().getString("username");
            String snsname = intent.getExtras().getString("snsname");
            String useremail = intent.getExtras().getString("useremail");
            String snsid = intent.getExtras().getString("snsid");
            String profileImgUrl = intent.getExtras().getString("profileImgUrl");

            userVo.setUsername(username);
            userVo.setSnsid(snsid);
            userVo.setSnsname(snsname);
            userVo.setUseremail(useremail);
            userVo.setProfileimgurl(profileImgUrl);
            userVo.setSnstype(snstype);
            userVo.setLocation(0);
            userVo.setTeampushflag("Y");
            userVo.setApppushflag("Y");
            userVo.setWifi("Y");
            userVo.setSerialnumber(Common.getDeviceSerialNumber());
            userVo.setFcmToken(FirebaseInstanceId.getInstance().getToken());
        }else{
            layoutPw.setVisibility(View.VISIBLE);
        }

        Log.d(TAG,"로그인 정보는 : " + userVo.toString());

    }

    @OnClick(R.id.btn_join_closed)
    public void closedButton(){
        finish();
    }

    @OnClick(R.id.btn_join)
    public void btnJoin(){

        if(Compare.isEmpty(Tx_joingmail.getText())){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.valid_gmail_req),Toast.LENGTH_SHORT).show();
            return;
        }

        if(Compare.isEmpty(joinEmail.getText())){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.valid_email_req), Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Compare.checkEmail(joinEmail.getText().toString())) {
            layoutLoginEmail.setError(getString(R.string.valid_useremail));
            return;
        }


        if(Compare.isEmpty(username.getText().toString())) {
            layout_username.setError(getString(R.string.valid_username));
            return;
        }

        if(appActionFlag.equals("email_user")){

            if (!Compare.validatePassword(joinPassword.getText().toString())) {
                joinPassword.setError(getString(R.string.valid_password));
                return;
            }else{

                if(!joinPassword.getText().toString().equals(joinPasswordConfirm.getText().toString())){
                    joinPasswordConfirm.setError(getString(R.string.valid_password_confirm));
                    return;
                }
                //밸리데이션 초기화
                layoutLoginEmail.setError(null);
                layoutLoginPassword.setError(null);
                layoutLoginPasswordConfirm.setError(null);
                layout_username.setError(null);
                userVo.setSnstype("app");
                userVo.setSnsid(Common.VETERAN_SNSID);
            }
        }

        Log.d(TAG,"문제없습니다 회원 가입을 진행합니다");
        userVo.setUseremail(joinEmail.getText().toString());
        userVo.setPassword(joinPassword.getText().toString());
        userVo.setUsername(username.getText().toString());
        userCreate(userVo);

        //푸쉬설정
        SharedPreferences sf = getSharedPreferences("momSoccerSetup", 0);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("push", "Y"); // 입력
        editor.commit(); // 파일에 최종 반영함
    }


    @OnClick(R.id.btn_googleId)
    public void choosGoogle(){
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() ========================================");

        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null
                        && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(
                            AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        Tx_joingmail.setText(accountName);
                        joinEmail.setText(accountName);
                        userVo.setGoogleemail(accountName);
                        userVo.setLocation(0);
                        userVo.setTeampushflag("Y");
                        userVo.setApppushflag("Y");
                        userVo.setWifi("Y");
                        userVo.setSerialnumber(Common.getDeviceSerialNumber());
                        userVo.setFcmToken(FirebaseInstanceId.getInstance().getToken());
                    }
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() ========================================");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() ========================================");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() ========================================");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() ========================================");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() ========================================");
    }

    public void userCreate(final User user){
        final MaterialDialog materialDialog =  new MaterialDialog.Builder(this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(R.string.network_join_user)
                .titleColor(getResources().getColor(R.color.color6))
                .content(getString(R.string.network_valid_user))
                .contentColor(getResources().getColor(R.color.color6))
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

        UserService userService = ServiceGenerator.createService(UserService.class);

        final Call<ServerResult> userCre = userService.createUser(user);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                userCre.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                        materialDialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                ServerResult serverResult = response.body();
                                UID = serverResult.getCount();
                                Log.d(TAG, "서버에서 생성된 아이디는 : " + UID);


                                if (UID == 0) {
                                    Log.d(TAG, "이미 회원으로 가입 되어 있는 메일주소 : " + UID);
                                    VeteranToast.makeToast(getApplicationContext(), user.getUseremail() + " 은 이미 가입되어 있는 주소입니다", Toast.LENGTH_SHORT).show();
                                } else {
                                    userVo.setUid(UID);
                                    userVo.setCommontokenid(serverResult.getGetid());
                                    Log.d(TAG, "생성된 유저아이디는  : " + UID);
                                    prefUtil.saveUser(userVo);

                                    Intent intent = new Intent(getApplicationContext(), MomMainActivity.class);
                                    intent.putExtra("join_y", "join_flag");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();
                                }

                            } catch (Exception e) {
                                Log.d(TAG, "서버와 통신중 오류 발생");
                                UID = 0;
                            }
                        }else{
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResult> call, Throwable t) {
                        materialDialog.dismiss();
                        t.printStackTrace();
                        UID = -1;
                    }
                });
            }        }, 500);// 0.5초 정도 딜레이를 준 후 시작

    }

}
