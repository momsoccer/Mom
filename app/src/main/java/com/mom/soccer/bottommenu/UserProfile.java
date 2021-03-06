package com.mom.soccer.bottommenu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.trservice.UserTRService;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {

    private static final String TAG = "UserProfile";

    User user;
    PrefUtil prefUtil;
    Activity activity;

    TextInputLayout layoutUserNicname;
    TextInputLayout layoutUserPhone;

    @Bind(R.id.image_user_image)
    ImageView user_image;

    @Bind(R.id.et_nicname)
    EditText et_nicName;

    @Bind(R.id.et_phone)
    EditText et_phone;

    ImageView backImage;

    private Uri mImageCaptureUri;
    private String absoultePath;
    private String RealFilePath;
    private String fileName;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bottom_userprofile_layout);
        ButterKnife.bind(this);

        activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        layoutUserNicname = (TextInputLayout) findViewById(R.id.layout_user_nicname);
        layoutUserPhone = (TextInputLayout) findViewById(R.id.layout_user_phone);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        et_nicName.setText(user.getUsername());

        if(!Compare.isEmpty(user.getProfileimgurl())) {
            Glide.with(UserProfile.this)
                    .load(user.getProfileimgurl())
                    .into(user_image);
        }

        if(!Compare.isEmpty(user.getBackimage())){
            backImage = (ImageView) findViewById(R.id.back_image);
            //리니어 레이아웃에 블러드 효과 주기
            Glide.with(UserProfile.this)
                    .load(user.getBackimage())
                    .into(backImage);
        }


        //TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //String phoneNum = tm.getLine1Number();

        if(Compare.isEmpty(user.getPhone())){
            et_phone.setText("000");
        }else{
            et_phone.setText(user.getPhone());
        }

    }

    //이미지 클릭시
    public void imageOnClick(View v){
        changeImage();
    }

    //버튼 클릭시
    @OnClick(R.id.modify_picture)
    public void modifyBtn(){
        changeImage();
    }

    //닉네임 및 핸드폰 저장
    @OnClick(R.id.btn_user_save)
    public void btnUsersave(){

        user.setUsername(et_nicName.getText().toString());
        user.setPhone(et_phone.getText().toString());


        WaitingDialog.showWaitingDialog(activity,false);
        UserService userService = ServiceGenerator.createService(UserService.class,this,user);
        final Call<ServerResult> call = userService.updateUser(user);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                call.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                        WaitingDialog.cancelWaitingDialog();
                        if(response.isSuccessful()){
                            ServerResult serverResult = response.body();

                            //유저사진을 쉐어퍼런스에 저장해준다(업데이트)
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(UserProfile.this);
                            SharedPreferences.Editor pre = sp.edit();
                            pre.putString("username", et_nicName.getText().toString());
                            pre.putString("phone", et_phone.getText().toString());
                            pre.commit();
                            VeteranToast.makeToast(getApplicationContext(),getString(R.string.user_profile_update), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResult> call, Throwable t) {
                        WaitingDialog.cancelWaitingDialog();
                        t.printStackTrace();
                    }
                });
            }
        }, 500);// 0.5초 정도 딜레이를 준 후 시작

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //홈버튼클릭시
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){
            case PICK_FROM_ALBUM:

                Log.d(TAG,"사진선택");
                mImageCaptureUri = data.getData();
                Intent intenti = new Intent("com.android.camera.action.CROP");
                intenti.setDataAndType(mImageCaptureUri, "image/*");

                //크롭할 이미지를 100*100 크기로 저장한다
                intenti.putExtra("outputX",100);
                intenti.putExtra("outputY",100);
                intenti.putExtra("aspectX",100); //크롭 박스의 x축 비율
                intenti.putExtra("aspectY",100);
                intenti.putExtra("scale",true);
                intenti.putExtra("return-data", true);
                startActivityForResult(intenti,CROP_FROM_IMAGE);

                break;

            case PICK_FROM_CAMERA:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                //크롭할 이미지를 100*100 크기로 저장한다
                intent.putExtra("outputX",100);
                intent.putExtra("outputY",100);
                intent.putExtra("aspectX",100); //크롭 박스의 x축 비율
                intent.putExtra("aspectY",100);
                intent.putExtra("scale",true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent,CROP_FROM_IMAGE);
                break;

            case CROP_FROM_IMAGE:
                if(resultCode != RESULT_OK){
                    return;
                }

                final Bundle extras = data.getExtras();

                //크롭된 이미지를 저장하기 위한 file 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ Common.IMAGE_MOM_PATH+System.currentTimeMillis()+".jpg";

                Log.d(TAG,"파일 경로는 : " + filePath);

                RealFilePath = filePath;
                fileName    = System.currentTimeMillis()+".jpg";

                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");
                    user_image.setImageBitmap(photo);

                    storeCropImage(photo, filePath);
                    absoultePath = filePath;
                    break;
                }
                File file = new File(mImageCaptureUri.getPath());
                if(file.exists()){
                    file.delete();
                }
        }
    }

    private void storeCropImage(Bitmap bitmap,String filePath){

        Log.d(TAG,"이미지 잘라서 저장하기 시작 ============================");

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+Common.IMAGE_MOM_PATH;
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        //서버에 파일을 업로드 합니다
        String profileimgurl = Common.SERVER_USER_IMGFILEADRESS + fileName;
        String StrUid = String.valueOf(user.getUid());


        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            Log.d(TAG,"파일 갱신 할 정보는  : " + copyFile);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(copyFile)));
            out.flush();
            out.close();

            //서버에가기전 적용해주기
            backImage.setImageBitmap(bitmap);
            user_image.setImageBitmap(bitmap);

        }catch (Exception e){
            e.printStackTrace();
        }

        //이미지 업로드
        Log.d(TAG,"User Upload End===================================================================");
        UserTRService userTRService = new UserTRService(this,user);
        userTRService.updateUserImage(StrUid,fileName,RealFilePath,"front");

        //유저사진을 쉐어퍼런스에 저장해준다(업데이트)
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor pre = sp.edit();
        pre.putString("profileImgUrl", profileimgurl);
        pre.commit();
        Log.d(TAG,"User Upload End ===================================================================");

    }

    //앨범에서 이미지 가져오기
    public void doTakeAlbumAction(){
        //앨범호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //카메라에서 사진촬영
    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String uri = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),uri));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent,PICK_FROM_CAMERA);
    }

    public void changeImage(){

        new MaterialDialog.Builder(UserProfile.this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(R.string.mom_diaalog_photo_title)
                .titleColor(getResources().getColor(R.color.color6))
                .content(R.string.mom_diaalog_photo_contnet)
                .contentColor(getResources().getColor(R.color.color6))
                .positiveText(R.string.mom_diaalog_photo_gallery)
                .neutralText(R.string.mom_diaalog_photo_camera)
                .negativeText(R.string.mom_diaalog_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakeAlbumAction();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakePhotoAction();
                    }
                })
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG,"온 스타트==============================");

    }
}
