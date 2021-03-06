package com.mom.soccer.trservice;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.common.Common;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-07-08.
 */
public class UserTRService {

    private static final String TAG = "UserTRService";
    private Activity activity;
    private User user;
    private String profileimgurl; //유저 사진

    public UserTRService(Activity activity, User user) {
        this.activity = activity;
        this.user = user;
    }

    public void updateUserImage(String uid, String filename, String realFilePath, String imgetype){

        WaitingDialog.showWaitingDialog(activity,false);

        File readFile = new File(realFilePath);
        profileimgurl = Common.SERVER_USER_IMGFILEADRESS + filename;

        //유저아이디
        RequestBody imgtype =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), imgetype);

        RequestBody userid =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), String.valueOf(uid));
        //파일명
        RequestBody fileName =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), filename);
        //실제파일위치
        RequestBody serverPath =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), profileimgurl);
        //업로드 사진
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), readFile);

        MultipartBody.Part file =
                MultipartBody.Part.createFormData("file", readFile.getName(), requestFile);

        //통신 준비
        UserService userService = ServiceGenerator.createService(UserService.class,activity,user);

        Log.d(TAG,"유저 사진 없로드 : " + user.toString());
        Log.d(TAG,"픽업된 사진은 : " + realFilePath);

        final Call<ServerResult> resultCall = userService.fileupload(imgtype,userid,fileName,serverPath,file);

        //프로그레스바 준비


        resultCall.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult serverResult = response.body();
                    Log.d(TAG,"값 : "+serverResult.toString());
                    VeteranToast.makeToast(activity,activity.getResources().getString(R.string.user_image_update), Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG,"서버 컨트롤러에서 값을 받지 못했습니다");
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                Log.d(TAG,"통신오류 발생 " + t.getMessage());
                t.printStackTrace();
            }
        });
    }


}
