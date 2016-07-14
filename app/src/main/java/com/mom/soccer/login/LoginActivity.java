package com.mom.soccer.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.mom.soccer.R;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;
import com.mom.soccer.widget.VeteranToast;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    //회원가입추가 정보를 받아온다.
    final static int FACEBOOK_RESULT=64206; //페이스북

    //kakao callback
    private SessionCallback kakaoCallback;
    //facebook calback
    private CallbackManager callbackManager;

    //회원가입 상세로 넘길 변수들
    private String username;
    private String snsname;
    private String snsid;
    private String useremail;
    private String snstype;
    private String profileImgUrl;

    private User user;
    private PrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
    }

    @OnClick(R.id.btn_login)
    public void btn_login(){
        //Toast.makeText(getApplicationContext(),"클릭함",Toast.LENGTH_LONG).show();
        VeteranToast.makeToast(getApplicationContext(),"클릭했습니다", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"클릭했습니다");
    }

    /*
    @OnClick(R.id.kakaologin)
    public void kakaologin(){

        Session.getCurrentSession().removeCallback(kakaoCallback);

        //카카오세션 오픈
        kakaoCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(kakaoCallback);

        if(!Session.getCurrentSession().checkAndImplicitOpen()){
            Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginActivity.this);
        }

    }

    //페이스북 로그인 처리
    @OnClick(R.id.facebooklogin)
    public void facebooklogin(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
    }
    */


    public class SessionCallback implements ISessionCallback {


        @Override
        public void onSessionOpened() {
            Log.d(TAG, "카카오 SessionCallback");
            kakaoMeCallbackInfo();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "---------------------onActivityResult---------------------");

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FACEBOOK_RESULT:
                Log.d(TAG, "페이스북 로그인 처리 onActivityResult()");
                facebookMeCallbackInfo();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void facebookMeCallbackInfo(){

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try{

                    snstype ="facebook";
                    username = object.getString("name");
                    snsname  = object.getString("name");
                    useremail = object.getString("email");
                    snsid = object.getString("id");
                    profileImgUrl = "https://graph.facebook.com/" + snsid + "/picture?type=large";
                    Log.d(TAG,"페이스북 정보 ======================================================");
                    Log.d(TAG,"닉네임 : " + username);
                    Log.d(TAG,"SNS  이름 : " + snsname);
                    Log.d(TAG,"SNS Id : " + snsid);
                    Log.d(TAG,"이미지는 : " + profileImgUrl);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    protected void kakaoMeCallbackInfo() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSuccess(UserProfile userProfile) {

                Log.d(TAG, "카카오 회원 정보가져오기");

                snstype = "kakao";
                username = userProfile.getNickname();
                snsname = userProfile.getNickname();
                //useremail = 카카오는 정책상 메일을 제공하지 않는다
                snsid = String.valueOf(userProfile.getId());
                profileImgUrl = userProfile.getProfileImagePath();

                Log.d(TAG,"카카오 로그인 정보 가져오기 ======================================================");
                Log.d(TAG,"닉네임 : " + username);
                Log.d(TAG,"SNS  이름 : " + snsname);
                Log.d(TAG,"SNS Id : " + snsid);
                Log.d(TAG,"이미지는 : " + profileImgUrl);
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Log.d("KAKAO_LOG", "서버 네트워크에 문제가 있습니다");
                } else {
                    Log.d("KAKAO_LOG", "오류로 카카오로그인 실패");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("TAG", "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onNotSignedUp() {

            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() ========================================");
        Session.getCurrentSession().removeCallback(kakaoCallback);
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
        Session.getCurrentSession().removeCallback(kakaoCallback);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
