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
        Preference userprofile = (Preference)findPreference("userprofile");
        Preference keyMaildev = (Preference)findPreference("keyMaildev");
        Preference mominfo = (Preference)findPreference("mominfo");

        keyhelp.setOnPreferenceClickListener(this);
        userprofile.setOnPreferenceClickListener(this);
        mominfo.setOnPreferenceClickListener(this);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        // 도움말 선택시

        if(preference.getKey().equals("keyhelp")) {
            VeteranToast.makeToast(getApplicationContext(),"도움말 창으로 이동", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(Setting.this, appHelp.class);
            //startActivityForResult(intent, 0);
        }else if(preference.getKey().equals("userprofile")) {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else if(preference.getKey().equals("mominfo")) {
            //버전정보,이용약관,개인정보 취급방지,회사소개
            Intent intent = new Intent(this, MomInforActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
        return false;
    }


}
