package com.mom.soccer.trservice;

import android.app.Activity;
import android.view.View;

import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-09-13.
 */
public class MomBoardTRService {

    public static void updateHeader(final Activity activity, User user, MomBoard momBoard, final View view, final String msg){

        WaitingDialog.showWaitingDialog(activity,false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,activity,user);
        Call<ServerResult> c = momBoardService.updateBoardHeader(momBoard);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    MomSnakBar.show(view,activity,msg);
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
