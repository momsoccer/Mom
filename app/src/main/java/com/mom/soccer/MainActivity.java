package com.mom.soccer;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.FcmToken;
import com.mom.soccer.dto.User;
import com.mom.soccer.login.LoginActivity;
import com.mom.soccer.momactivity.Main_Activity;
import com.mom.soccer.trservice.FcmTokenTRService;

import java.security.MessageDigest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.btn_login_pre)
    Button btnLogin;

    @Bind(R.id.btn_join_pre)
    Button btnJoin;

    private User user;
    private PrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Log.d(TAG,"유저 정보 : " + user.toString());

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        if (!Compare.isEmpty(user.getUseremail())){
            Log.d(TAG,"로그인 유저입니다.. 메인 화면으로 이동합니다");
            Intent intent = new Intent(MainActivity.this, Main_Activity.class); //유저라면 메인 화면으로 이동시킨다
            startActivity(intent);
        }else{
            //비로그인유저
            Log.d(TAG,"로그인 필요");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart ===========================================================");
        user = prefUtil.getUser();
        String shToken = prefUtil.getFcmToken();

        Log.d(TAG,"onStart() : "+user.toString());
        Log.d(TAG,"토큰 값은 : " + shToken);

        if(Compare.isEmpty(shToken)){
            //토큰이 없다면 프리퍼런스/DB에 저장한다
            Log.d(TAG,"토큰이 없습니다");
            prefUtil.saveFcmToken(FirebaseInstanceId.getInstance().getToken());
        }else{
            if(!shToken.equals(FirebaseInstanceId.getInstance().getToken())){
                Log.d(TAG,"토큰이 값이 다르기 때문에 업데이트를 합니다");
                //디비의 토큰을 일괄로 업데이트 한다
                FcmToken token = new FcmToken();
                token.setSerialnumber(Common.getDeviceSerialNumber());
                token.setFcmtoken(shToken);
                FcmTokenTRService service = new FcmTokenTRService(this);
                service.updateToken(token);
                //쉐어프리퍼런스 토큰값을 변경
                prefUtil.saveFcmToken(FirebaseInstanceId.getInstance().getToken());
            }
        }

        if (!Compare.isEmpty(user.getUseremail())){
            Log.d(TAG,"로그인 유저입니다.. 메인 화면으로 이동합니다");
            Intent intent = new Intent(this, Main_Activity.class); //유저라면 메인 화면으로 이동시킨다
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
    }

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
                Log.d(TAG,"HASH KEY : =====================" + something);
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
