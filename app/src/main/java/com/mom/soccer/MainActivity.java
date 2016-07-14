package com.mom.soccer;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.mom.soccer.login.JoinAndLoginActivity;
import com.mom.soccer.login.LoginActivity;

import java.security.MessageDigest;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_rlogin)
    public void btn_rlogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_main)
    public void btn_main(){
        Intent intent = new Intent(this,JoinAndLoginActivity.class);
        startActivity(intent);
    }

    //+ntsxcDAQk4F7Q2VJ749K9pHH/8= 해쉬태그
    private void getAppKeyHash() {
        String hasykey=null;

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d(TAG,"HASH KEY : =====================" + something);
                hasykey=something;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
}
