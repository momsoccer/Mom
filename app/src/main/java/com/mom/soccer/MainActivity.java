package com.mom.soccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.FcmToken;
import com.mom.soccer.dto.User;
import com.mom.soccer.login.LoginActivity;
import com.mom.soccer.momactivity.MomMainActivity;
import com.mom.soccer.trservice.FcmTokenTRService;
import com.mom.soccer.widget.VeteranToast;

import java.security.MessageDigest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.btn_login_pre)
    Button btnLogin;

    private User user;
    private PrefUtil prefUtil;
    private Activity activity;

    //인터넷 연결 상태 확인
    ConnectivityManager cManager;
    NetworkInfo mobile;
    NetworkInfo wifi;

    MediaController mc;
    MyVideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        activity = this;
        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        if (!Compare.isEmpty(user.getUseremail())){
            Intent intent = new Intent(MainActivity.this, MomMainActivity.class); //유저라면 메인 화면으로 이동시킨다
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else{
            //비로그인유저
            Log.d(TAG,"로그인 필요");
        }

        //getAppKeyHash();

        cManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(!mobile.isConnected() && !wifi.isConnected()){
            VeteranToast.makeToast(getApplication(),getString(R.string.donotinternet), Toast.LENGTH_LONG).show();
        }
        videoView = (MyVideoView) findViewById(R.id.mainvideoview);

        Uri path = Uri.parse("android.resource://" + getPackageName() + "/raw/splash");
        videoView.setVideoURI(path);
        videoView.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        videoView.start();
        user = prefUtil.getUser();
        String shToken = prefUtil.getFcmToken();

        prefUtil.saveFcmToken(FirebaseInstanceId.getInstance().getToken());

        FcmToken fcmToken = new FcmToken();
        fcmToken.setUid(user.getUid());
        fcmToken.setSerialnumber(user.getSerialnumber());
        fcmToken.setFcmtoken(FirebaseInstanceId.getInstance().getToken());
        FcmTokenTRService.setupToken(activity,user,fcmToken);

        if (!Compare.isEmpty(user.getUseremail())){
            Intent intent = new Intent(this, MomMainActivity.class); //유저라면 메인 화면으로 이동시킨다
            startActivity(intent);
        }else{
            //비로그인유저
            Log.d(TAG,"로그인 필요");
        }
    }

    /***************************************************
     *
     * 버튼 이벤트 로직 시작
     *
     * */


    @OnClick(R.id.btn_login_pre)
    public void login(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    /***************************************************
     * 기타 참고 사항 및 설정
     */

    //해쉬태그
    private void getAppKeyHash() {
        String hasykey=null;

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d(TAG,"해시키값확인 : "+ something);
                hasykey=something;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    //외부폰트 설정
    /*
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    */
}
