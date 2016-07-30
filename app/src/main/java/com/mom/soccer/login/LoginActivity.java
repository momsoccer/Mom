package com.mom.soccer.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.mom.soccer.R;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.momactivity.MomMainActivity;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    //회원가입추가 정보를 받아온다.
    final static int FACEBOOK_RESULT=64206; //페이스북

    TextInputLayout layoutUserEmail;
    TextInputLayout layoutPassword;


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

    //로그인을 위한 유저입력 정보
    private User user;
    //서버로 부터 받은 유저 정보
    private User SERVERUSER;
    private PrefUtil prefUtil;

    @Bind(R.id.tx_find_id)
    TextView textView_find_id;
    @Bind(R.id.tx_find_pw)
    TextView textView_find_pw;

    //메일,비번
    @Bind(R.id.login_email)
    EditText et_Login_Email;

    @Bind(R.id.login_password)
    EditText et_Login_password;

    private String tempEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        String strId = getString(R.string.member_text_id);
        String strPw = getString(R.string.member_text_pw);

        layoutUserEmail = (TextInputLayout) findViewById(R.id.layout_login_email);
        layoutPassword = (TextInputLayout) findViewById(R.id.layout_login_password);

        textView_find_id = (TextView) findViewById(R.id.tx_find_id);
        textView_find_pw = (TextView) findViewById(R.id.tx_find_pw);
        textView_find_id.setText(Html.fromHtml("<u>" + strId + "</u>"));
        textView_find_pw.setText(Html.fromHtml("<u>" + strPw + "</u>"));

        prefUtil = new PrefUtil(this);

        user = new User();
        user.setSnstype("app");
        user.setSnsid(Common.VETERAN_SNSID);

        SERVERUSER = new User(); //서버로 부터 받은 유저정보

    }

        @OnClick(R.id.btn_kakao_login)
        public void kakaoLogin(){
            Session.getCurrentSession().removeCallback(kakaoCallback);
            //카카오 세션을 초기화 해준다
            kakaoCallback = new SessionCallback();
            Session.getCurrentSession().addCallback(kakaoCallback);


            if(!Session.getCurrentSession().checkAndImplicitOpen()){
                Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginActivity.this);
            }
            Log.d(TAG, "카카오 로그인 시도");
        }

        @OnClick(R.id.btn_facebook_login)
        public void faceBoookLogin(){
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

                    validateSnsUserID(snstype, snsid,"","");

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

                validateSnsUserID(snstype, snsid,"","");
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

    public void textOnClick(View view){
        switch (view.getId()){
            case R.id.tx_find_id:
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation), Toast.LENGTH_SHORT).show();
                break;
            case R.id.tx_find_pw:
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /****************************************************************
     * 각 버튼에 대한 이벤트
     ************************/
    @OnClick(R.id.btn_login)
    public void btnLogin(){
        if(!Compare.checkEmail(et_Login_Email.getText().toString())) {
            layoutUserEmail.setError("이메일 형식이 맞지 않습니다");
            return;
        }else if (!Compare.validatePassword(et_Login_password.getText().toString())) {
            layoutPassword.setError("비밀번호는 5자 이상 입력해주세요");
            return;
        }else{
            //밸리데이션 초기화
            layoutUserEmail.setError(null);
            layoutPassword.setError(null);

            user.setUseremail(et_Login_Email.getText().toString());
            user.setPassword(et_Login_password.getText().toString());
            tempEmail = et_Login_Email.getText().toString();
            //서버에서 아이디와 비번 검증
            userLogin();
        }
    }

    @OnClick(R.id.btn_join)
    public void momJoin(){
        Intent intent = new Intent(this, JoinActivity.class);
        intent.putExtra("app_action_flag","email_user");  //email_user, sns_user
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void userLogin(){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "",getString(R.string.network_valid_user), true);
        dialog.show();

        UserService userService = ServiceGenerator.createService(UserService.class,this,user);
        final Call<ServerResult> getUserInfo = userService.getUserCheck(user.getSnstype(),user.getUseremail());
        getUserInfo.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){

                    Log.d(TAG, "서버 조회 결과 성공");
                    ServerResult serverResult = response.body();
                    Log.d(TAG, "서버 조회 결과 값은 : " + serverResult.getResult());
                    dialog.dismiss();

                    if(serverResult.getCount()==1){
                        getUserSet();
                    }else{
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.valid_user),Toast.LENGTH_SHORT).show();
                        et_Login_Email.setText(tempEmail);
                    }
                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.valid_user),Toast.LENGTH_SHORT).show();
                    et_Login_Email.setText(tempEmail);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_LONG).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    public void getUserSet(){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "",getString(R.string.network_get_user), true);
        dialog.show();

        UserService userService = ServiceGenerator.createService(UserService.class);

        final Call<User> getUserInfo = userService.getUser(user.getSnstype(),
                user.getSnsid(),
                user.getPassword(),
                user.getUseremail());

        getUserInfo.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    SERVERUSER = response.body();
                    SERVERUSER.setPassword(user.getPassword());
                    dialog.dismiss();
                    userExist();
                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_LONG).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    public void userExist(){

        prefUtil.saveUser(SERVERUSER);
        Log.d("User Info", "************************************************");
        Log.d("User Info", "SNS TYPE : " + SERVERUSER.getSnstype());
        Log.d("User Info", "EMAIL : " + SERVERUSER.getUseremail());
        Log.d("User Info", "PW : " + SERVERUSER.getPassword());
        Log.d("User Info", "************************************************");

        Intent intent = new Intent(getApplicationContext(), MomMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();

    }

    //SNS전용
    public void validateSnsUserID(
            String type, //snstype
            String id,  //snsid
            String pw,  //password
            String email //useremail
    ){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "",getString(R.string.network_valid_user), true);
        dialog.show();

        UserService userService = ServiceGenerator.createService(UserService.class);
        final Call<User> getUserInfo = userService.getUser(type, id, pw, email);


        getUserInfo.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "서버 조회 결과 성공");

                    SERVERUSER = response.body();

                    if (SERVERUSER.getUseremail().equals("null@co.com")) {
                        Log.d(TAG, "우리 서버에 처음 오신분 입니다. ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                        //처음이면 가입 페이지로 이동 시킨다
                        Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                        intent.putExtra("app_action_flag","sns_user");  //email_user, sns_user
                        intent.putExtra("snstype", snstype);
                        intent.putExtra("username", username);
                        intent.putExtra("snsname", snsname);
                        intent.putExtra("useremail", useremail);
                        intent.putExtra("snsid", snsid);
                        intent.putExtra("profileImgUrl", profileImgUrl);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                    } else {
                        //처음이 아니라면 MomMain으로 이동 시킨다
                        userExist();
                    }

                } else {
                    Log.d(TAG, "조회 결과 실패 ===");

                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 서버와 통신 불가" + t.getMessage());
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_LONG).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });

    }

}
