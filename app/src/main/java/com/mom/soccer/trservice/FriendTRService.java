package com.mom.soccer.trservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.mom.soccer.dto.FriendApply;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.FriendService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-07-12.
 */
public class FriendTRService{

    private static final String TAG = "FriendTRService";

    private Activity activity;

    public FriendTRService(Activity activity) {
        this.activity = activity;
    }

    //친구요청한 목록을 보는 리스트

    //친구요청 메서드
    public void reqFriend(FriendApply friendApply, User user){

        FriendService friendService = ServiceGenerator.createService(FriendService.class,activity,user);

        final Call<ServerResult> call = friendService.reqFriend(friendApply);

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(activity, "", "찬구 요청을 합니다", true);
        dialog.show();

        call.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    ServerResult serverResult = response.body();
                    Log.d(TAG,"친구 요청이 되었습니다 : "+serverResult.toString());
                    dialog.dismiss();
                    VeteranToast.makeToast(activity,"친구 요청이 되었습니다", Toast.LENGTH_SHORT).show();


                }else{
                    Log.d(TAG,"서버 컨트롤러에서 값을 받지 못했습니다");
                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                Log.d(TAG,"통신오류 발생 " + t.getMessage());
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

}
