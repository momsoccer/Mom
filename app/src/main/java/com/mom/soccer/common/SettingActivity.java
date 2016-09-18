package com.mom.soccer.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mom.soccer.R;
import com.mom.soccer.bottommenu.UserProfile;


/**
 * Created by sungbo on 2016-06-07.
 */
public class SettingActivity  extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    SwitchPreference autoUpdate,arlim_check,sound_check;
    Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_layout);

        activity = this;

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


    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        // 도움말 선택시

        if(preference.getKey().equals("keyhelp")) {




        }else if(preference.getKey().equals("userprofile")) {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        }
        return false;
    }



}
