package com.mom.soccer.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.bottommenu.UserProfile;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.CommonService;
import com.mom.soccer.retropitutil.ServiceGenerator;
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
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                    SharedPreferences.Editor pre = sp.edit();
                    pre.putString("pushcheck", "Y");
                    pre.commit();
                }else{
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                    SharedPreferences.Editor pre = sp.edit();
                    pre.putString("pushcheck", "N");
                    pre.commit();
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
                                    //deleteUser()
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


}
