package com.mom.soccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;
import com.mom.soccer.login.LoginActivity;
import com.mom.soccer.momactivity.MomMainActivity;
import com.mom.soccer.widget.VeteranToast;

import java.security.MessageDigest;
import java.util.Locale;

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

    String upset ="N";
    private String sfName = "momSoccerSetup";
    private String setupValue= null;
    private String langValue= null;
    Locale locale;
    String language;


    public void setLocale(String charicter) {
        Locale locale = new Locale(charicter);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.i(TAG,"몸싸커 앱을 시작합니다");
        getAppKeyHash();



        SharedPreferences sf = getSharedPreferences(sfName, 0);
        setupValue = sf.getString("chanel","N");
        langValue= sf.getString("lang","N");

        /******************************************
         * 언어 설정
         */

        if(langValue.equals("N")){
            SharedPreferences.Editor editor = sf.edit();
            editor.putString("lang", "N"); // 입력
            editor.commit(); // 파일에 최종 반영함

            locale = getResources().getConfiguration().locale;
            language =  locale.getLanguage();
            Common.LANGUAGE = language;

        }else{
            //N 이 아니라면 유저가 언어셋팅을 한 것임
            Common.LANGUAGE = langValue;

            setLocale(Common.LANGUAGE);
        }

        //Log.i(TAG,"설정한 언어는 : " + Common.LANGUAGE + getResources().getString(R.string.lang_test));


        if(setupValue.equals("N")){
            SharedPreferences.Editor editor = sf.edit();
            editor.putString("chanel", "N"); // 입력
            editor.commit(); // 파일에 최종 반영함
        }else{
            //Log.i(TAG,"Y 입니다");
        }

         /*
        //초기 유투브 도움말 페이지 셋업
        SharedPreferences sf = getSharedPreferences(sfName, 0);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("chanel", "N"); // 입력
        editor.commit(); // 파일에 최종 반영함

        꺼내 쓸때는
            private String setupValue= null;
            SharedPreferences sf = getSharedPreferences(sfName,0);
            setupValue = sf.getString("chanel","");

         */

        activity = this;
        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        upset = prefUtil.getUploadFlag();
        String getPushcheck = prefUtil.getPushcheck();
        String getSoundCheck = prefUtil.getSoundCheck();

        if(upset==null){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
            SharedPreferences.Editor pre = sp.edit();
            pre.putString("uploadflag", "Y");
            pre.commit();
        }

        if(getPushcheck==null){
            prefUtil.setPushcheck("Y");
        }

        if(getSoundCheck==null){
            prefUtil.setSoundCheck("Y");
        }

        FirebaseMessaging.getInstance().subscribeToTopic("news");



        //버전체크하기
        //MarketVersionChecker checker = new MarketVersionChecker();
        //checker.getMarketVersion("com.mom.soccer",activity);

        /**************************************************************************
         * 로그인 부분
         */

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


    //외부폰트 설정
    /*
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    */

    //디버그 해쉬키 구하기
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

}
