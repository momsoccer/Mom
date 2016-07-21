package com.mom.soccer.common;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.bottommenu.UserProfile;
import com.mom.soccer.widget.VeteranToast;


/**
 * Created by sungbo on 2016-06-07.
 */
public class SettingActivity  extends PreferenceActivity implements Preference.OnPreferenceClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_layout);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        root.setBackgroundColor(getResources().getColor(R.color.mom_color2));

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Preference keyhelp = (Preference)findPreference("keyhelp");
        Preference keycontact = (Preference)findPreference("keycontact");
        Preference keyagree = (Preference)findPreference("keyagree");
        Preference keyappversion = (Preference)findPreference("keyappversion");
        Preference userprofile = (Preference)findPreference("userprofile");

        keyhelp.setOnPreferenceClickListener(this);
        keycontact.setOnPreferenceClickListener(this);
        keyagree.setOnPreferenceClickListener(this);
        keyappversion.setOnPreferenceClickListener(this);
        userprofile.setOnPreferenceClickListener(this);

        Preference keyMaildev = (Preference)findPreference("keyMaildev");

    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        // 도움말 선택시

        if(preference.getKey().equals("keyappversion")) {
            VeteranToast.makeToast(getApplicationContext(),"앱 버전 이력 스토리", Toast.LENGTH_SHORT).show();
        }
        else if(preference.getKey().equals("keyagree")) {
            VeteranToast.makeToast(getApplicationContext(),"약관 및 취급방침 창으로 이동", Toast.LENGTH_SHORT).show();
        }
        else if(preference.getKey().equals("keyhelp")) {
            VeteranToast.makeToast(getApplicationContext(),"도움말 창으로 이동", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(Setting.this, appHelp.class);
            //startActivityForResult(intent, 0);
        }
        else if(preference.getKey().equals("keycontact")) {
            VeteranToast.makeToast(getApplicationContext(),"고객센터 연결", Toast.LENGTH_SHORT).show();
        }else if(preference.getKey().equals("userprofile")) {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
        return false;
    }
}
