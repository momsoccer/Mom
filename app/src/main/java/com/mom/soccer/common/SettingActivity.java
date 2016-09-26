package com.mom.soccer.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.mom.soccer.MainActivity;
import com.mom.soccer.R;
import com.mom.soccer.bottommenu.UserProfile;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.CommonService;
import com.mom.soccer.retrofitdao.MomComService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by sungbo on 2016-06-07.
 */
public class SettingActivity  extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    SwitchPreference autoUpdate,arlim_check,sound_check;
    MomPreference deleteuserkey;
    Activity activity;

    private User user;
    private PrefUtil prefUtil;
    private String sfName = "momSoccerSetup";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_layout);

        activity = this;

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();

        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.setBackgroundColor(getResources().getColor(R.color.mom_color2));
        root.addView(bar, 0); // insert at top

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Preference keyhelp = (Preference)findPreference("keyhelp");
        Preference userprofile = (Preference)findPreference("userprofile");

        //keyhelp.setOnPreferenceClickListener(this);
        userprofile.setOnPreferenceClickListener(this);
        arlim_check = (SwitchPreference) findPreference("arlim_check");
        sound_check = (SwitchPreference) findPreference("sound_check");

        deleteuserkey = (MomPreference) findPreference("deleteuserkey");

        arlim_check.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isVibrateOn = (Boolean) newValue;
                if(isVibrateOn){


                    SharedPreferences sf = getSharedPreferences(sfName, 0);
                    SharedPreferences.Editor editor = sf.edit();
                    editor.putString("push", "Y"); // 입력
                    editor.commit(); // 파일에 최종 반영함

                }else{
                    SharedPreferences sf = getSharedPreferences(sfName, 0);
                    SharedPreferences.Editor editor = sf.edit();
                    editor.putString("push", "N"); // 입력
                    editor.commit();
                }
                return true;
            }
        });

        sound_check.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isVibrateOn = (Boolean) newValue;
                if(isVibrateOn){
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                    SharedPreferences.Editor pre = sp.edit();
                    pre.putString("soundcheck", "Y");
                    pre.commit();
                }else{
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                    SharedPreferences.Editor pre = sp.edit();
                    pre.putString("soundcheck", "N");
                    pre.commit();
                }
                return true;
            }
        });


        deleteuserkey.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new MaterialDialog.Builder(SettingActivity.this)
                        .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                        .title(R.string.all_user_page_btn5)
                        .titleColor(getResources().getColor(R.color.color6))
                        .content(R.string.all_user_page_btn6)
                        .contentColor(getResources().getColor(R.color.color6))
                        .positiveText(R.string.mom_diaalog_confirm)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteUser();
                            }
                        })
                        .negativeText(R.string.mom_diaalog_cancel)
                        .show();
                return false;
            }
        });
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {

        if(preference.getKey().equals("keyhelp")) {

        }else if(preference.getKey().equals("userprofile")) {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        }
        return false;
    }


    public void deleteUser(){
        WaitingDialog.showWaitingDialog(SettingActivity.this,false);
        CommonService commonService = ServiceGenerator.createService(CommonService.class,getApplicationContext(),user);
        Call<ServerResult> serverResultCall = commonService.deleteUser(user.getUid());
        serverResultCall.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult serverResult = response.body();
                    if(serverResult.getCount()==1){
                        new MaterialDialog.Builder(SettingActivity.this)
                                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                .title(R.string.all_user_page_btn5)
                                .titleColor(getResources().getColor(R.color.color6))
                                .content(R.string.all_user_page_btn7)
                                .contentColor(getResources().getColor(R.color.color6))
                                .positiveText(R.string.mom_diaalog_confirm)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        logOut();
                                    }
                                })
                                .negativeText(R.string.mom_diaalog_cancel)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    public void logOut(){

        if(user.getSnstype().equals("facebook")){


            //페이스북 토큰 및 세션 끈기
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

        }else if(user.getSnstype().equals("kakao")){

            onClickLogout();
        }else if(user.getSnstype().equals("app")){

        }
        prefUtil.clearPrefereance();

        //강사정보 지우기
        SharedPreferences pref = getApplicationContext().getSharedPreferences("insinfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();


        //서버 세션을 초기화해준다.
        WaitingDialog.showWaitingDialog(SettingActivity.this,false);
        MomComService momComService = ServiceGenerator.createService(MomComService.class,this,user);
        final Call<ServerResult> logOutCall = momComService.logOut();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                logOutCall.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                        if(response.isSuccessful()) {
                            WaitingDialog.cancelWaitingDialog();
                            VeteranToast.makeToast(getApplicationContext(),getString(R.string.app_logout), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            WaitingDialog.cancelWaitingDialog();
                            VeteranToast.makeToast(getApplicationContext(),getString(R.string.app_logout), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();

                        }
                    }
                    @Override
                    public void onFailure(Call<ServerResult> call, Throwable t) {
                        WaitingDialog.cancelWaitingDialog();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();

                    }
                });
            }
        }, 1000);

    }

    //카카오 세션 끈기 메소드
    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
            }
        });
    }

}
