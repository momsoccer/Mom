package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * Created by sungbo on 2016-05-27.
 */
public interface UserService {

    @GET("/all/getLevel")
    Call<User> getLevel(@Query("uid") int uid);

    @POST("/all/insert")
    Call<ServerResult> createUser(@Body User user);

    @GET("/all/existuser")
    Call<User> getUser(
            @Query("snstype") String snstype,
            @Query("snsid") String snsid,
            @Query("password") String password,
            @Query("useremail") String useremail);

    @GET("/api/user/userCheck")
    Call<ServerResult> getUserCheck(
            @Query("snstype") String snstype,
            @Query("email") String email);


    @Multipart
    @POST("/api/user/fileupload")
    Call<ServerResult> fileupload(
            @Part("imgtype") RequestBody imgtype,
            @Part("uid") RequestBody uid,
            @Part("filename") RequestBody filename,
            @Part("profileimgurl") RequestBody profileimgurl,
            @Part MultipartBody.Part file
    );

    @POST("/api/user/updateUserSetup")
    Call<ServerResult> updateUser(@Body User user);

    @POST("/api/user/getusersearchlist")
    Call<List<User>> getUserSearList(@Body User user);

    @GET("/api/user/getProfileUser")
    Call<User> getProfileUser(
            @Query("uid") int uid);

}
