package com.mom.soccer.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {

    private static final String TAG = "JoinActivity";

    //구글 아이디 선택하기
    GoogleAccountCredential credential;
    private static final int REQUEST_ACCOUNT_PICKER = 2;

    private User userVo;
    private PrefUtil prefUtil;

    @Bind(R.id.joingmail)
    EditText Edit_joingmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_join_layout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        userVo = new User();
        prefUtil = new PrefUtil(this);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        initialSet(intent);

        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(Auth.SCOPES));
        credential.setBackOff(new ExponentialBackOff());
    }


    protected void initialSet(Intent intent){

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
        userVo.setPassword(snsid);
        userVo.setTeampushflag("Y");
        userVo.setApppushflag("Y");
        userVo.setSerialnumber(Common.getDeviceSerialNumber());
        userVo.setFcmToken(FirebaseInstanceId.getInstance().getToken());

        Log.d(TAG,"로그인 정보는 : " + userVo.toString());

    }

    @OnClick(R.id.btn_join_closed)
    public void closedButton(){
        finish();
    }

    @OnClick(R.id.btn_googleId)
    public void choosGoogle(){
        Edit_joingmail.setText("안녕");
    }

}
