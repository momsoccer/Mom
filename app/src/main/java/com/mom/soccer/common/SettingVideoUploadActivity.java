package com.mom.soccer.common;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mom.soccer.R;


/**
 * Created by sungbo on 2016-06-08.
 */
public class SettingVideoUploadActivity extends PreferenceActivity {

    SwitchPreference complex_upload;
    SwitchPreference wifi_upload;
    PrefUtil prefUtil;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.videoupload_layout);

        prefUtil = new PrefUtil(this);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.setBackgroundColor(getResources().getColor(R.color.mom_color2));
        root.addView(bar, 0);


        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        complex_upload = (SwitchPreference) findPreference("complex_upload");
        wifi_upload = (SwitchPreference) findPreference("wifi_upload");

        complex_upload.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isVibrateOn = (Boolean) newValue;
                if(isVibrateOn){
                    wifi_upload.setChecked(false);
                }else{
                    wifi_upload.setChecked(true);
                }
                return true;
            }
        });

        wifi_upload.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isVibrateOn = (Boolean) newValue;
                if(isVibrateOn){
                    complex_upload.setChecked(false);
                    prefUtil.uploadFlag("N");
                }else{
                    complex_upload.setChecked(true);
                    prefUtil.uploadFlag("Y");
                }
                return true;
            }
        });
    }

    /*

        //기타 설정 값 가져오기 주석 처리 상태
        // 프리퍼런스 화면에서 array.xml로 값을 설정한 예제
        /*
        mPref = PreferenceManager.getDefaultSharedPreferences(this);

        //자동업데이트 기능을 유저가 설정 했는지 알아본다
        Log.d("TestActivity","업데이트 설정 : " + mPref.getBoolean("autoUpdate", false));
        Log.d("TestActivity", "업데이트 후 알람 : " + mPref.getBoolean("useUpdateNofiti", false));

        String[] array = getResources().getStringArray(R.array.userNameOpen);

        int index = getArrayIndex(R.array.userNameOpen_values, mPref.getString("userNameOpen", "0"));

        Log.d("TestActivity","설정한 배열값은  : " + index);

        if (index != -1) {
            String userNameOpenString = array[index];
            Log.d("TestActivity"," 설정 한 값은 : "+ userNameOpenString);
        }


    //와이파이 연결 설정 여부 확인
    cManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

    btngent = (Button) findViewById(R.id.btngent);

    btngent.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mobile.isConnected()) {
                Log.d(TAG,"통신사 연결 되어 있습니다");
            }else{
                Log.d(TAG,"통신사 연결이 안되어 있습니다");
            }

            if(wifi.isConnected()){
                Log.d(TAG,"WIFI 연결 되어 있습니다");
            }else{
                Log.d(TAG,"WIFI 연결 안되어 있습니다");
            }
        }
    });

     */
}
