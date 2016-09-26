package com.mom.soccer.besideactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsApplyVo;
import com.mom.soccer.retrofitdao.InsApplyService;
import com.mom.soccer.retrofitdao.InstructorService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyCoachActivity extends AppCompatActivity {

    //backImage.setImageBitmap(bitmap);

    private static final String TAG = "ApplyCoachActivity";

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private Uri mImageCaptureUri;
    private String absoultePath;
    private String RealFilePath;
    private String fileName;

    TextInputLayout layout_name,layout_age,
            layout_playeryear, layout_insyear, layout_bankname, layout_address, layout_momteamname, layout_cuteamname,
            layout_phone, layout_resume, layout_career1, layout_career2, layout_career3, layout_career4, layout_career5;

    EditText ins_name,ins_age,ins_playeryear,ins_year,
            ins_bankname, ins_bankaccount,ins_address,
            ins_momteamname, ins_cuteamname, ins_phone, ins_resume,
            ins_career1, ins_career2, ins_career3, ins_career4, ins_career5;

    @Bind(R.id.btnApply)
    Button btnApply;

    @Bind(R.id.btnUpdate)
    Button btnUpdate;

    @Bind(R.id.coachapply_title)
    TextView textView_coachapply_title;

    @Bind(R.id.im_ins_insimg)
    ImageView im_ins_insimg;

    @Bind(R.id.im_ins_teaimg)
    ImageView im_ins_teaimg;

    @Bind(R.id.li_request_layout)
    LinearLayout li_request_layout;

    @Bind(R.id.li_confirm_layout)
    LinearLayout li_confirm_layout;

    @Bind(R.id.tx_request_status)
    TextView tx_request_status;

    @Bind(R.id.tx_request_date)
    TextView tx_request_date;

    @Bind(R.id.tx_approval_date)
    TextView tx_approval_date;

    private User user;
    private PrefUtil prefUtil;
    private InsApplyVo insApplyVo = new InsApplyVo();
    private InsApplyVo resultInsVo = new InsApplyVo();

    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //이미지 컨트롤을 위한 변수
        Common.IMAGE_CHOOSE_FALG = "";

        setContentView(R.layout.ac_apply_coach);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.coachapply_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        textView_coachapply_title.setText(R.string.toolbar_apply_coach);

        layout_name = (TextInputLayout) findViewById(R.id.layout_name);
        layout_age = (TextInputLayout) findViewById(R.id.layout_age);
        layout_playeryear = (TextInputLayout) findViewById(R.id.layout_playeryear);
        layout_insyear = (TextInputLayout) findViewById(R.id.layout_insyear);
        layout_bankname = (TextInputLayout) findViewById(R.id.layout_bankname);
        layout_address = (TextInputLayout) findViewById(R.id.layout_address);
        layout_momteamname = (TextInputLayout) findViewById(R.id.layout_momteamname);
        layout_cuteamname = (TextInputLayout) findViewById(R.id.layout_cuteamname);
        layout_phone = (TextInputLayout) findViewById(R.id.layout_phone);
        layout_resume = (TextInputLayout) findViewById(R.id.layout_resume);
        layout_career1 = (TextInputLayout) findViewById(R.id.layout_career1);
        layout_career2 = (TextInputLayout) findViewById(R.id.layout_career2);
        layout_career3 = (TextInputLayout) findViewById(R.id.layout_career3);
        layout_career4 = (TextInputLayout) findViewById(R.id.layout_career4);
        layout_career5 = (TextInputLayout) findViewById(R.id.layout_career5);

        ins_name = (EditText) findViewById(R.id.ins_name);
        ins_age = (EditText) findViewById(R.id.ins_age);
        ins_playeryear = (EditText) findViewById(R.id.ins_playeryear);
        ins_year = (EditText) findViewById(R.id.ins_year);
        ins_bankname = (EditText) findViewById(R.id.ins_bankname);
        ins_bankaccount = (EditText) findViewById(R.id.ins_bankaccount);
        ins_address = (EditText) findViewById(R.id.ins_address);
        ins_momteamname = (EditText) findViewById(R.id.ins_momteamname);
        ins_cuteamname = (EditText) findViewById(R.id.ins_cuteamname);
        ins_phone = (EditText) findViewById(R.id.ins_phone);
        ins_resume = (EditText) findViewById(R.id.ins_resume);
        ins_career1 = (EditText) findViewById(R.id.ins_career1);
        ins_career2 = (EditText) findViewById(R.id.ins_career2);
        ins_career3 = (EditText) findViewById(R.id.ins_career3);
        ins_career4 = (EditText) findViewById(R.id.ins_career4);
        ins_career5 = (EditText) findViewById(R.id.ins_career5);

        //TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //String phoneNumber = telManager.getLine1Number();

        ins_name.setText(user.getUsername());

        //ins_phone.setText(phoneNumber);

        if(!Compare.isEmpty(user.getProfileimgurl())) {
            Glide.with(ApplyCoachActivity.this)
                    .load(user.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(ApplyCoachActivity.this,10,5))
                    .into(im_ins_insimg);
        }

        getIns();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart()==============================");
    }

    public void getIns(){
        WaitingDialog.showWaitingDialog(ApplyCoachActivity.this,false);
        InsApplyVo qvo = new InsApplyVo();
        qvo.setUid(user.getUid());
        InstructorService service = ServiceGenerator.createService(InstructorService.class,this,user);
        Call<InsApplyVo> c = service.getIns(qvo);
        c.enqueue(new Callback<InsApplyVo>() {
            @Override
            public void onResponse(Call<InsApplyVo> call, Response<InsApplyVo> response) {
                if(response.isSuccessful()) {

                    resultInsVo = response.body();
                    WaitingDialog.cancelWaitingDialog();

                    if(resultInsVo.getUid()==0){

                        btnUpdate.setVisibility(View.GONE);
                        li_confirm_layout.setVisibility(View.GONE);

                    }else {

                        if (resultInsVo.getApplystatus().equals("REQUEST")) {

                            li_request_layout.setVisibility(View.VISIBLE); //상단 요청 정보를 보여준다
                            btnApply.setText(getString(R.string.coach_req_cancel_btn)); //승인요청 버튼을 취로 변경
                            btnUpdate.setVisibility(View.VISIBLE); //수정가능버튼

                            tx_request_date.setText(resultInsVo.getChange_creationdate());
                            tx_request_status.setText(getString(R.string.coach_req_status_r));

                            getDatafild();

                            Log.i(TAG,"=========================" + resultInsVo.getTeamimg());

                            if(resultInsVo.getTeamimg()!=null){
                                if(!Compare.isEmpty(user.getProfileimgurl())) {
                                    Glide.with(ApplyCoachActivity.this)
                                            .load(resultInsVo.getTeamimg())
                                            .asBitmap().transform(new RoundedCornersTransformation(ApplyCoachActivity.this,10,5))
                                            .into(im_ins_teaimg);
                                }
                            }

                        } else if (resultInsVo.getApplystatus().equals("APPROVAL")) {

                            btnApply.setVisibility(View.GONE); //지원,취소 버튼
                            btnUpdate.setVisibility(View.VISIBLE); //업데이트 버튼
                            li_request_layout.setVisibility(View.VISIBLE); //상단 지원정보

                            tx_request_date.setText(resultInsVo.getChange_creationdate());
                            tx_approval_date.setText(resultInsVo.getChange_updatedate());
                            tx_request_status.setText(getString(R.string.coach_req_status_a));

                            getDatafild();
                            textView_coachapply_title.setText(getString(R.string.coach_page_title));

                            if(resultInsVo.getTeamimg()!=null){
                                if(!Compare.isEmpty(user.getProfileimgurl())) {
                                    Glide.with(ApplyCoachActivity.this)
                                            .load(resultInsVo.getTeamimg())
                                            .asBitmap().transform(new RoundedCornersTransformation(ApplyCoachActivity.this,10,5))
                                            .into(im_ins_teaimg);
                                }
                            }

                        }
                    }
                }else{
                    Log.i(TAG,"정보를 못가져옵");
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<InsApplyVo> call, Throwable t) {
                Log.i(TAG,"문제발생");
                t.printStackTrace();
                WaitingDialog.cancelWaitingDialog();
            }
        });
    }

    //조회시 기존 자료를 뿌려준다
    public void getDatafild(){
        ins_name.setText(resultInsVo.getName());
        ins_age.setText(String.valueOf(resultInsVo.getAge()));
        ins_playeryear.setText(String.valueOf(resultInsVo.getPlayeryear()));
        ins_year.setText(String.valueOf(resultInsVo.getInstructoryear()));
        ins_bankname.setText(resultInsVo.getBankname());
        ins_bankaccount.setText(resultInsVo.getBankaccount());
        ins_cuteamname.setText(resultInsVo.getCurrentteamname());
        ins_phone.setText(resultInsVo.getPhonenumber());
        ins_resume.setText(resultInsVo.getResume());
        ins_career1.setText(resultInsVo.getCareer1());
        ins_career2.setText(resultInsVo.getCareer2());
        ins_career3.setText(resultInsVo.getCareer3());
        ins_career4.setText(resultInsVo.getCareer4());
        ins_career5.setText(resultInsVo.getCareer5());
        ins_address.setText(resultInsVo.getAddress());
        ins_momteamname.setText(resultInsVo.getMomappteamname());
    }

    @OnClick(R.id.btnApply)
    public void btnApply(){

        if(resultInsVo.getUid()==0){
            valiDation();

            new MaterialDialog.Builder(this)
                    .titleColor(getResources().getColor(R.color.color6))
                    .title(R.string.mom_diaalog_alert)
                    .content(R.string.coach_req_messge)
                    .positiveText(R.string.mom_diaalog_confirm)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            reqInsFile();
                        }
                    })
                    .negativeText(R.string.mom_diaalog_cancel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override

                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        }
                    })
                    .backgroundColor(getResources().getColor(R.color.mom_color1))
                    .show();
        }else{

            if(resultInsVo.getApplystatus().equals("REQUEST")){
                //강사지원 취소

                new MaterialDialog.Builder(this)
                        .titleColor(getResources().getColor(R.color.color6))
                        .title(R.string.mom_diaalog_alert)
                        .content(R.string.coach_cancel_messge)
                        .positiveText(R.string.mom_diaalog_confirm)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ApplyCancel();
                            }
                        })
                        .negativeText(R.string.mom_diaalog_cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override

                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        })
                        .backgroundColor(getResources().getColor(R.color.mom_color1))
                        .show();
            }
        }


    }

    public void valiDation(){
        if (Compare.isEmpty(ins_name.getText().toString())) {
            layout_name.setError(getString(R.string.coach_vali_name));
            return;
        }  else if((Compare.isEmpty(ins_age.getText().toString()))){
            layout_age.setError(getString(R.string.coach_vali_age));
            return;
        }  else if((Compare.isEmpty(ins_resume.getText().toString()))){
            layout_resume.setError(getString(R.string.coach_vali_resume));
            return;
        }  else if((Compare.isEmpty(ins_phone.getText().toString()))){
            layout_phone.setError(getString(R.string.coach_vali_phone));
            return;
        }  else if((Compare.isEmpty(ins_momteamname.getText().toString()))){
            layout_momteamname.setError(getString(R.string.coach_vali_teamname));
            return;
        }  else if((Compare.isEmpty(ins_playeryear.getText().toString()))){
            layout_playeryear.setError(getString(R.string.coach_vali_playeryear));
            return;
        }  else if((Compare.isEmpty(ins_year.getText().toString()))){
            layout_insyear.setError(getString(R.string.coach_vali_insyear));
            return;
        }
    }

    public void imageOnClick(View v){
        switch (v.getId()){
            case R.id.im_ins_teaimg:
                Common.IMAGE_CHOOSE_FALG = "INS";
                changeImage();
                break;

            case R.id.im_ins_insimg:
                Common.IMAGE_CHOOSE_FALG = "USER";
                changeImage();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reqInsSave(){
        WaitingDialog.showWaitingDialog(ApplyCoachActivity.this,false);
        InstructorService service = ServiceGenerator.createService(InstructorService.class,this,user);
        Call<ServerResult> c = service.insApply(insApplyVo);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){

                    Log.i(TAG,"성공 ======================= : " +response.body());
                    WaitingDialog.cancelWaitingDialog();

                    new MaterialDialog.Builder(ApplyCoachActivity.this)
                            .titleColor(getResources().getColor(R.color.color6))
                            .title(R.string.mom_diaalog_alert)
                            .content(R.string.coach_reqconfim_message)
                            .positiveText(R.string.mom_diaalog_confirm)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                                }
                            })
                            .backgroundColor(getResources().getColor(R.color.mom_color1))
                            .show();
                }else{
                    Log.i(TAG,"실패1 =======================");
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                Log.i(TAG,"실패2 =======================");
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void reqInsFile(){

        WaitingDialog.showWaitingDialog(ApplyCoachActivity.this,false);

        insApplyVo.setUid(user.getUid());
        insApplyVo.setName(ins_name.getText().toString());
        insApplyVo.setAge(Integer.parseInt(ins_age.getText().toString()));
        insApplyVo.setPlayeryear(Integer.parseInt(ins_playeryear.getText().toString()));
        insApplyVo.setInstructoryear(Integer.parseInt(ins_playeryear.getText().toString()));
        insApplyVo.setBankname(ins_bankname.getText().toString());
        insApplyVo.setBankaccount(ins_bankaccount.getText().toString());
        insApplyVo.setCurrentteamname(ins_cuteamname.getText().toString());
        insApplyVo.setPhonenumber(ins_phone.getText().toString());
        insApplyVo.setResume(ins_resume.getText().toString());
        insApplyVo.setCareer1(ins_career1.getText().toString());
        insApplyVo.setCareer2(ins_career2.getText().toString());
        insApplyVo.setCareer3(ins_career3.getText().toString());
        insApplyVo.setCareer4(ins_career4.getText().toString());
        insApplyVo.setCareer5(ins_career5.getText().toString());
        insApplyVo.setAddress(ins_address.getText().toString());
        insApplyVo.setMomappteamname(ins_momteamname.getText().toString());
        insApplyVo.setApplystatus("REQUEST");
        insApplyVo.setEmail(user.getUseremail());

        if(RealFilePath==null){
            reqInsSave();
        }else {
            insApplyVo.setTeamimg(Common.SERVER_TEAM_IMGFILEADRESS + fileName);


            File readFile = new File(RealFilePath);
            RequestBody uid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(user.getUid()));
            RequestBody updateflag = RequestBody.create(MediaType.parse("multipart/form-data"), "N");
            RequestBody fileaddr = RequestBody.create(MediaType.parse("multipart/form-data"), insApplyVo.getTeamimg());

            //파일명
            RequestBody imgFilename =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), fileName);

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), readFile);

            MultipartBody.Part file =
                    MultipartBody.Part.createFormData("file", readFile.getName(), requestFile);

            Log.i(TAG, "File : " + readFile.getName());
            Log.i(TAG, "File : " + readFile.getPath());

            InstructorService service = ServiceGenerator.createService(InstructorService.class, this, user);
            Call<ServerResult> c = service.insApplyfile(uid, updateflag, fileaddr, imgFilename, file);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if (response.isSuccessful()) {
                        //1.파일을 업로드 후 바로 데이터를 넘긴다 복잡성을 단순하게..
                        ServerResult result = response.body();
                        Log.i(TAG, "업로드 성공 : " + result.toString());
                        reqInsSave();
                        WaitingDialog.cancelWaitingDialog();
                    } else {
                        Log.i(TAG, "오류 입니다(1) file upload");
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    Log.i(TAG, "오류 입니다(2) file upload");
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }
    }



    //사진선택..
    public void changeImage(){

        new MaterialDialog.Builder(ApplyCoachActivity.this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(R.string.mom_diaalog_photo_title)
                .titleColor(getResources().getColor(R.color.color6))
                .content(R.string.mom_diaalog_team_contnet)
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

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
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
                    photo = extras.getParcelable("data");

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

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+Common.IMAGE_MOM_PATH;
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);


            if(Common.IMAGE_CHOOSE_FALG.equals("USER")){

                im_ins_insimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                im_ins_insimg.setImageBitmap(photo);

                final File readFile = new File(RealFilePath);
                RequestBody userid =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), String.valueOf(user.getUid()));

                String fileaddr = Common.SERVER_USER_IMGFILEADRESS + fileName;

                //강사인지 신청단계인지...resultInsVo.getApplystatus()
                RequestBody teamStatus =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), resultInsVo.getApplystatus());

                //실제파일위치
                RequestBody serverPath =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), fileaddr);


                //파일명
                RequestBody imgFilename =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), fileName);

                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), readFile);

                MultipartBody.Part file =
                        MultipartBody.Part.createFormData("file", readFile.getName(), requestFile);

                String ins_addr = Common.SERVER_INS_IMGFILEADRESS + fileName;

                //강사사진 주소
                RequestBody insfileAddr =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), ins_addr);


                InsApplyService insApplyService = ServiceGenerator.createService(InsApplyService.class,getApplicationContext(),user);
                Call<ServerResult> c = insApplyService.fileupload(userid,serverPath,imgFilename,file,teamStatus,insfileAddr);
                c.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                        if(response.isSuccessful()){
                            ServerResult result = response.body();
                            Log.i(TAG,"성공 : " + result.toString());
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResult> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                //유저사진을 쉐어퍼런스에 저장해준다(업데이트)
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor pre = sp.edit();
                String profileimgurl = Common.SERVER_USER_IMGFILEADRESS + fileName;
                pre.putString("profileImgUrl", profileimgurl);
                pre.commit();

                //강사라면 승인 상태와 승인전 상태를 구분해서 파일을 업로드 해준다.
            }else{

                im_ins_teaimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                im_ins_teaimg.setImageBitmap(photo);

                if(resultInsVo.getUid()!=0){

                    String status = "";


                    if(resultInsVo.getApplystatus().equals("APPROVAL")){
                        status = "DONE";
                    }else{
                        status = "Y";
                    }


                    final File readFile = new File(RealFilePath);
                    RequestBody updateFlag =
                            RequestBody.create(
                                    MediaType.parse("multipart/form-data"),status);
                    RequestBody userid =
                            RequestBody.create(
                                    MediaType.parse("multipart/form-data"), String.valueOf(user.getUid()));

                    String fileaddr = Common.SERVER_TEAM_IMGFILEADRESS + fileName;
                    //실제파일위치
                    RequestBody serverPath =
                            RequestBody.create(
                                    MediaType.parse("multipart/form-data"), fileaddr);


                    //파일명
                    RequestBody imgFilename =
                            RequestBody.create(
                                    MediaType.parse("multipart/form-data"), fileName);

                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), readFile);

                    MultipartBody.Part file =
                            MultipartBody.Part.createFormData("file", readFile.getName(), requestFile);

                    InstructorService service = ServiceGenerator.createService(InstructorService.class,getApplicationContext(),user);
                    Call<ServerResult> c = service.insApplyfile(userid,updateFlag,serverPath,imgFilename,file);
                    c.enqueue(new Callback<ServerResult>() {
                        @Override
                        public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                            if (response.isSuccessful()) {
                                //1.파일을 업로드 후 바로 데이터를 넘긴다 복잡성을 단순하게..
                                ServerResult result = response.body();
                                Log.i(TAG, "업로드 성공 : " + result.toString());
                                WaitingDialog.cancelWaitingDialog();
                            } else {
                                Log.i(TAG, "오류 입니다(1) file upload");
                                WaitingDialog.cancelWaitingDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResult> call, Throwable t) {
                            Log.i(TAG, "오류 입니다(2) file upload");
                            WaitingDialog.cancelWaitingDialog();
                            t.printStackTrace();
                        }
                    });
                }

                im_ins_teaimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                im_ins_teaimg.setImageBitmap(photo);
            }

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(copyFile)));
            out.flush();
            out.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //접수중 취소를 한다면...
    public void ApplyCancel(){

        new MaterialDialog.Builder(this)
                .titleColor(getResources().getColor(R.color.color6))
                .title(R.string.mom_diaalog_title_append)
                .content(R.string.coach_req_cancell)
                .positiveText(R.string.mom_diaalog_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        deleteApply();
                    }
                })
                .negativeText(R.string.mom_diaalog_cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override

                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .backgroundColor(getResources().getColor(R.color.mom_color1))
                .show();
    }

    public void deleteApply(){

        InsApplyVo vo = new InsApplyVo();
        vo.setUid(user.getUid());
        InstructorService service = ServiceGenerator.createService(InstructorService.class,this,user);
        Call<ServerResult> c = service.delete(vo);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    WaitingDialog.cancelWaitingDialog();

                    finish();
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                }else{
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                t.printStackTrace();
                WaitingDialog.cancelWaitingDialog();
            }
        });
    }

    //저장을 한다면...
    @OnClick(R.id.btnUpdate)
    public void btnUpdate(){

        if(RealFilePath!=null){

            //업데이트를 시켜준다.
            insApplyVo.setTeamimg(Common.SERVER_TEAM_IMGFILEADRESS + fileName);

            File readFile = new File(RealFilePath);
            RequestBody uid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(user.getUid()));
            RequestBody updateflag = RequestBody.create(MediaType.parse("multipart/form-data"), "Y");
            RequestBody fileaddr = RequestBody.create(MediaType.parse("multipart/form-data"), insApplyVo.getTeamimg());

            //파일명
            RequestBody imgFilename =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), fileName);

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), readFile);

            MultipartBody.Part file =
                    MultipartBody.Part.createFormData("file", readFile.getName(), requestFile);

            Log.i(TAG, "File : " + readFile.getName());
            Log.i(TAG, "File : " + readFile.getPath());

            InstructorService service = ServiceGenerator.createService(InstructorService.class, this, user);
            Call<ServerResult> c = service.insApplyfile(uid, updateflag, fileaddr, imgFilename, file);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if (response.isSuccessful()) {
                        //1.파일을 업로드 후 바로 데이터를 넘긴다 복잡성을 단순하게..
                        ServerResult result = response.body();
                        Log.i(TAG, "업로드 성공 : " + result.toString());
                        WaitingDialog.cancelWaitingDialog();
                    } else {
                        Log.i(TAG, "오류 입니다(1) file upload");
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    Log.i(TAG, "오류 입니다(2) file upload");
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }

        insApplyVo.setUid(user.getUid());
        insApplyVo.setName(ins_name.getText().toString());
        insApplyVo.setAge(Integer.parseInt(ins_age.getText().toString()));
        insApplyVo.setPlayeryear(Integer.parseInt(ins_playeryear.getText().toString()));
        insApplyVo.setInstructoryear(Integer.parseInt(ins_playeryear.getText().toString()));
        insApplyVo.setBankname(ins_bankname.getText().toString());
        insApplyVo.setBankaccount(ins_bankaccount.getText().toString());
        insApplyVo.setCurrentteamname(ins_cuteamname.getText().toString());
        insApplyVo.setPhonenumber(ins_phone.getText().toString());
        insApplyVo.setResume(ins_resume.getText().toString());
        insApplyVo.setCareer1(ins_career1.getText().toString());
        insApplyVo.setCareer2(ins_career2.getText().toString());
        insApplyVo.setCareer3(ins_career3.getText().toString());
        insApplyVo.setCareer4(ins_career4.getText().toString());
        insApplyVo.setCareer5(ins_career5.getText().toString());
        insApplyVo.setAddress(ins_address.getText().toString());
        insApplyVo.setMomappteamname(ins_momteamname.getText().toString());
        insApplyVo.setEmail(user.getUseremail());

        WaitingDialog.showWaitingDialog(ApplyCoachActivity.this,false);
        InstructorService service = ServiceGenerator.createService(InstructorService.class,this,user);
        Call<ServerResult> c = service.updateIns(insApplyVo);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    WaitingDialog.cancelWaitingDialog();

                    new MaterialDialog.Builder(ApplyCoachActivity.this)
                            .titleColor(getResources().getColor(R.color.color6))
                            .title(R.string.mom_diaalog_alert)
                            .content(R.string.coach_req_update_message)
                            .positiveText(R.string.mom_diaalog_confirm)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                                }
                            })
                            .backgroundColor(getResources().getColor(R.color.mom_color1))
                            .show();
                }else{
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                t.printStackTrace();
                WaitingDialog.cancelWaitingDialog();
            }
        });
    }

}
