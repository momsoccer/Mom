package com.mom.soccer.trservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-07-28.
 */
public class UserMissionTRService {

    private static final String TAG = "UserMissionTRService";

    private Context context;
    private User user;
    private UserMission userMission;

    public UserMissionTRService(Context context, UserMission userMission, User user) {
        this.context = context;
        this.userMission = userMission;
        this.user = user;
    }

    public void createUserMission(){

        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,context,user);
        Call<ServerResult> call = userMissionService.createUserMission(userMission);

        call.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){

                    ServerResult result = response.body();

                    Log.d(TAG,"result : " + result.toString());

                }else{
                    VeteranToast.makeToast(context,context.getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                VeteranToast.makeToast(context,context.getString(R.string.network_error_message1), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
