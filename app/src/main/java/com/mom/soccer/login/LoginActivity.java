package com.mom.soccer.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.momactivity.MomMainActivity;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

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


    //@Bind(R.id.tx_find_pw)
    //TextView textView_find_pw;

    //메일,비번
    @Bind(R.id.login_email)
    EditText et_Login_Email;

    @Bind(R.id.login_password)
    EditText et_Login_password;

    Activity activity;

    private String tempEmail = null;
    private Instructor instructor;

    View positiveAction;
    EditText find_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        String strId = getString(R.string.member_text_id);
        String strPw = getString(R.string.member_text_pw);

        activity = this;
        layoutUserEmail = (TextInputLayout) findViewById(R.id.layout_login_email);
        layoutPassword = (TextInputLayout) findViewById(R.id.layout_login_password);

        //textView_find_pw = (TextView) findViewById(R.id.tx_find_pw);
        //textView_find_pw.setText(Html.fromHtml("<u>" + strPw + "</u>"));

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

                snsid = String.valueOf(userProfile.getId());
                profileImgUrl = userProfile.getProfileImagePath();


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
/*
    public void textOnClick(View view){
        switch (view.getId()){
            case R.id.tx_find_pw:


                MaterialDialog dialog = new MaterialDialog.Builder(activity)
                        .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                        .title(R.string.mom_diaalog_pwd)
                        .titleColor(activity.getResources().getColor(R.color.color6))
                        .customView(R.layout.dialog_getpassword, true)
                        .positiveText(R.string.momboard_edit_send)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                final User user = new User();
                                user.setUsername(find_email.getText().toString());

                                Random rnd =new Random();
                                StringBuffer buf =new StringBuffer();

                                for(int i=0;i<6;i++){
                                    if(rnd.nextBoolean()){
                                        buf.append((char)((int)(rnd.nextInt(6))+97));
                                    }else{
                                        buf.append((rnd.nextInt(10)));
                                    }
                                }
                                user.setPassword(buf.toString());
                                Log.i(TAG,"user pwd : " + user.toString());

                                WaitingDialog.showWaitingDialog(activity,false);
                                UserService userService = ServiceGenerator.createService(UserService.class);
                                Call<ServerResult> c = userService.getEmailFind(user);
                                c.enqueue(new Callback<ServerResult>() {
                                    @Override
                                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                                        WaitingDialog.cancelWaitingDialog();
                                        if(response.isSuccessful()){
                                            ServerResult serverResult = response.body();
                                            VeteranToast.makeToast(activity,activity.getResources().getString(R.string.getfind_email_title4),Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ServerResult> call, Throwable t) {

                                    }
                                });

                            }
                        })
                        .build();

                find_email = (EditText) dialog.findViewById(R.id.find_email);
                positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

                find_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        positiveAction.setEnabled(s.toString().trim().length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                dialog.show();
                positiveAction.setEnabled(false);

                break;
        }
    }*/

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

        final MaterialDialog materialDialog =  new MaterialDialog.Builder(this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(R.string.app_loging_title)
                .titleColor(getResources().getColor(R.color.color6))
                .content(getString(R.string.network_valid_user))
                .contentColor(getResources().getColor(R.color.color6))
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

        UserService userService = ServiceGenerator.createService(UserService.class,this,user);
        final Call<ServerResult> getUserInfo = userService.getUserCheck(user.getSnstype(),user.getUseremail());
        getUserInfo.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                materialDialog.dismiss();
                if(response.isSuccessful()){

                    ServerResult serverResult = response.body();

                    if(serverResult.getCount()==1){
                        getUserSet();
                    }else{
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.valid_user),Toast.LENGTH_SHORT).show();
                        et_Login_Email.setText(tempEmail);
                    }
                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.valid_user),Toast.LENGTH_SHORT).show();
                    et_Login_Email.setText(tempEmail);
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                materialDialog.dismiss();
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void getUserSet(){
        WaitingDialog.showWaitingDialog(LoginActivity.this,false);
        UserService userService = ServiceGenerator.createService(UserService.class);

        final Call<User> getUserInfo = userService.getUser(user.getSnstype(),
                user.getSnsid(),
                user.getPassword(),
                user.getUseremail());

        getUserInfo.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    SERVERUSER = response.body();
                    SERVERUSER.setPassword(user.getPassword());
                    userExist();
                }else{

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void userExist(){

        prefUtil.saveUser(SERVERUSER);
        Log.i("User Info", "************************************************");
        Log.i("User Info", "SNS TYPE : " + SERVERUSER.getSnstype());
        Log.i("User Info", "EMAIL : " + SERVERUSER.getUseremail());
        Log.i("User Info", "PW : " + SERVERUSER.getPassword());
        Log.i("User Info", "************************************************");

        //InstructorService service = ServiceGenerator.createService(InstructorService.class);
        //Call<Instructor> c = service.getFindIns(SERVERUSER.getUid());



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


        WaitingDialog.showWaitingDialog(LoginActivity.this,false);
        UserService userService = ServiceGenerator.createService(UserService.class);
        final Call<User> getUserInfo = userService.getUser(type, id, pw, email);


        getUserInfo.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                WaitingDialog.cancelWaitingDialog();
                if (response.isSuccessful()) {
                    Log.d(TAG, "서버 조회 결과 성공");

                    SERVERUSER = response.body();

                    if (SERVERUSER.getUseremail().equals("null@co.com")) {

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

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

    }

}
