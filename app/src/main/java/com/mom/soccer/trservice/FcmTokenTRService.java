package com.mom.soccer.trservice;

import android.app.Activity;

import com.mom.soccer.dto.FcmToken;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.FcmTokenService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-07-01.
 */
public class FcmTokenTRService {

    private static final String TAG = "FcmTokenTRService";


    public  static void  setupToken(Activity activity,User user,FcmToken token){
        WaitingDialog.showWaitingDialog(activity,false);
        FcmTokenService fcmTokenService = ServiceGenerator.createService(FcmTokenService.class,activity,user);
        final Call<ServerResult> call = fcmTokenService.setupToken(token);
        call.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public static void updateToken(Activity activity,User user,FcmToken token){

        WaitingDialog.showWaitingDialog(activity,false);
        FcmTokenService fcmTokenService = ServiceGenerator.createService(FcmTokenService.class,activity,user);
        final Call<ServerResult> call = fcmTokenService.updateToken(token);
        call.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public static void saveToken(Activity activity,User user,FcmToken token){

        WaitingDialog.showWaitingDialog(activity,false);
        FcmTokenService fcmTokenService = ServiceGenerator.createService(FcmTokenService.class,activity,user);
        final Call<ServerResult> call = fcmTokenService.saveToken(token);

        call.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
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
