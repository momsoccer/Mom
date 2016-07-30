package com.mom.soccer.test;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.mom.soccer.dto.Mission;
import com.mom.soccer.mission.MissionCommon;

/**
 * Created by sungbo on 2016-07-28.
 */
public class TestService extends IntentService{

    private static final String TAG = "TestService";

    public TestService() {
        super("UploadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "IntentService onCreate  ========================================");



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "IntentService onStartCommand  ========================================");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "IntentService onHandleIntent  ======================================== +++++");

        Bundle extras = intent.getExtras();

        if(extras == null) {
            Log.d("Service","null");
        } else {
            Log.d("Service","not null");
            String name = (String) extras.get("iname");
            Mission mission = (Mission) extras.getSerializable(MissionCommon.OBJECT);
            Log.d(TAG,"전달 받은 값은 : " + name);
            Log.d(TAG,"전달 받은 값은 : " + mission.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.d(TAG, "IntentService onBind  ========================================");

        Log.d(TAG, "2가져온 정보는 =============== : ");
        String name = intent.getExtras().getString("iname");
        Log.d(TAG, "IntentService onBind : " + name);
        return super.onBind(intent);
    }


}
