package com.mom.soccer.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.momactivity.MomMainActivity;

import java.util.Locale;


/**
 * Created by sungbo on 2016-06-08.
 */
public class LangActivity extends PreferenceActivity {

    SwitchPreference cnKey, enKey, koKey;
    PrefUtil prefUtil;
    Activity activity;
    private String sfName = "momSoccerSetup";
    Locale locale;
    String language;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.lang_setting);
        activity = this;
        prefUtil = new PrefUtil(this);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        final Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.setBackgroundColor(getResources().getColor(R.color.mom_color2));
        root.addView(bar, 0);


        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SharedPreferences sf = getSharedPreferences(sfName, 0);
        Common.LANGUAGE = sf.getString("lang","N");

        Log.i("TAG","언어 설정 상태는 : " + Common.LANGUAGE);

        koKey = (SwitchPreference) findPreference("kolang");
        enKey = (SwitchPreference) findPreference("enlang");
        cnKey = (SwitchPreference) findPreference("cnlang");

        koKey.setChecked(false);
        enKey.setChecked(false);
        cnKey.setChecked(false);

        if(Common.LANGUAGE.equals("ko")){
            koKey.setChecked(true);
            enKey.setChecked(false);
            cnKey.setChecked(false);
        };

        if(Common.LANGUAGE.equals("zh")){
            koKey.setChecked(false);
            enKey.setChecked(false);
            cnKey.setChecked(true);
        };

        if(Common.LANGUAGE.equals("en")){
            koKey.setChecked(false);
            enKey.setChecked(true);
            cnKey.setChecked(false);
        };

        if(Common.LANGUAGE.equals("N")){

            //Log.i("TAG","핸드폰 기본 언어로 설정된 상태입니다");

            locale = getResources().getConfiguration().locale;
            language =  locale.getLanguage();
            Common.LANGUAGE = language;

            if(Common.LANGUAGE.equals("ko")){
                koKey.setChecked(true);
                enKey.setChecked(false);
                cnKey.setChecked(false);
            };

            if(Common.LANGUAGE.equals("zh")){
                koKey.setChecked(false);
                enKey.setChecked(false);
                cnKey.setChecked(true);
            };

            if(Common.LANGUAGE.equals("en")){
                koKey.setChecked(false);
                enKey.setChecked(true);
                cnKey.setChecked(false);
            };

        };



        koKey.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isVibrateOn = (Boolean) newValue;
                if(isVibrateOn){
                    new MaterialDialog.Builder(activity)
                            .content(R.string.lang_change)
                            .contentColor(activity.getResources().getColor(R.color.color6))
                            .positiveText(R.string.applay_app)
                            .negativeText(R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    cnKey.setChecked(false);
                                    koKey.setChecked(false);
                                    SharedPreferences sf = getSharedPreferences(sfName, 0);
                                    SharedPreferences.Editor editor = sf.edit();
                                    editor.putString("lang", "ko"); // 입력
                                    editor.commit(); // 파일에 최종 반영함
                                    setLocale("ko");
                                    Common.LANGUAGE = "ko";

                                    finish();
                                    Intent intent = new Intent(LangActivity.this,MomMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();

                }else{


                }
                return true;
            }
        });

        cnKey.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isVibrateOn = (Boolean) newValue;
                if(isVibrateOn){
                    new MaterialDialog.Builder(activity)
                            .content(R.string.lang_change)
                            .contentColor(activity.getResources().getColor(R.color.color6))
                            .positiveText(R.string.applay_app)
                            .negativeText(R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    cnKey.setChecked(false);
                                    koKey.setChecked(false);
                                    SharedPreferences sf = getSharedPreferences(sfName, 0);
                                    SharedPreferences.Editor editor = sf.edit();
                                    editor.putString("lang", "zh"); // 입력
                                    editor.commit(); // 파일에 최종 반영함
                                    setLocale("zh");
                                    Common.LANGUAGE = "zh";

                                    finish();
                                    Intent intent = new Intent(LangActivity.this,MomMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();

                }else{

                }
                return true;
            }
        });

        enKey.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isVibrateOn = (Boolean) newValue;
                if(isVibrateOn){

                    new MaterialDialog.Builder(activity)
                            .content(R.string.lang_change)
                            .contentColor(activity.getResources().getColor(R.color.color6))
                            .positiveText(R.string.applay_app)
                            .negativeText(R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    cnKey.setChecked(false);
                                    koKey.setChecked(false);
                                    SharedPreferences sf = getSharedPreferences(sfName, 0);
                                    SharedPreferences.Editor editor = sf.edit();
                                    editor.putString("lang", "en"); // 입력
                                    editor.commit(); // 파일에 최종 반영함
                                    setLocale("en");
                                    Common.LANGUAGE = "en";

                                    finish();
                                    Intent intent = new Intent(LangActivity.this,MomMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();

                }else{


                }
                return true;
            }
        });
    }


    public void setLocale(String charicter) {
        Locale locale = new Locale(charicter);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
