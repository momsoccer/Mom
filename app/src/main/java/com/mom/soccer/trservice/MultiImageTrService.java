package com.mom.soccer.trservice;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.Display;

import com.mom.soccer.common.Common;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-09-16.
 */
public class MultiImageTrService {

    private static final String TAG = "MultiImageTrService";

    private Activity activity;
    private User user;
    private String path;

    private String profileimgurl;

    public MultiImageTrService(Activity activity, User user, String path) {
        this.activity = activity;
        this.user = user;
        this.path = path;
    }

    public void upLoadImage(String filename, String path, int boardid){
        //http://blog.naver.com/PostView.nhn?blogId=pluulove84&logNo=100152811486

        //회전 각도
        int degrees = GetExifOrientation(path);

        String newFilePath = boardid+"_"+filename;

        //이미지 취득 옵션(사이즈 정보)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        options.inJustDecodeBounds = true;

        //리사이즈 비율 취득
        //int sampleSize = getSampliSize(options.outWidth, options.outHeight);

        options.inJustDecodeBounds = false;
        //options.inSampleSize = sampleSize;

        //원본 이미지 취득
        Bitmap src = BitmapFactory.decodeFile( path, options );

        //회전 이미지 정보 취득
        src = GetRotatedBitmap(src, degrees);

        String newFilename = saveBitmapToJpeg(src,newFilePath);

        File readFile = new File(newFilename);

        profileimgurl = Common.SERVER_BOARD_IMGFILEADRESS + newFilePath;

        RequestBody rboardid =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), String.valueOf(boardid));

        //파일명
        RequestBody fileName =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), newFilePath);

        //실제파일위치
        RequestBody serverPath =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), profileimgurl);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), readFile);

        MultipartBody.Part file =
                MultipartBody.Part.createFormData("file", readFile.getName(), requestFile);


        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,activity,user);
        Call<ServerResult> fileCall = momBoardService.fileupload(rboardid,fileName,serverPath,file);
        fileCall.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    Log.i(TAG,"*** 이미지 파일 업로드 성공 ***");
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public static String saveBitmapToJpeg(Bitmap bitmap, String newFilePath){

        File tempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ Common.IMAGE_MOM_PATH,newFilePath);

        try{
            tempFile.createNewFile();  // 파일을 생성해주고
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , out);  // 넘거 받은 bitmap을 jpeg(손실압축)으로 저장해줌
            out.close(); // 마무리로 닫아줍니다.

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile.getAbsolutePath();   // 임시파일 저장경로를 리턴해주면 끝!
    }


    private int getSampliSize(int width, int height) {
        // 화면 크기 취득
        Display currentDisplay = activity.getWindowManager().getDefaultDisplay();

        float dw = currentDisplay.getWidth();
        float dh = currentDisplay.getHeight();

        // 가로/세로 축소 비율 취득
        int widthtRatio = (int) Math.ceil(width / dw);
        int heightRatio = (int) Math.ceil(height / dh);

        // 초기 리사이즈 비율
        int sampleSize = 1;

        // 가로 세로 비율이 화면보다 큰경우에만 처리
        if (widthtRatio > 1 && height > 1) {
            if (widthtRatio > heightRatio) {
                // 가로 축소 비율이 큰 경우
                sampleSize = widthtRatio;
            } else {
                // 세로 축소 비율이 큰 경우
                sampleSize = heightRatio;
            }
        }

        return sampleSize;
    }

    private int GetExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (orientation != -1) {
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }

        return degree;
    }

    private Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

                if (bitmap != b2) {
                    bitmap.recycle();
                    bitmap = b2;
                }
            } catch (OutOfMemoryError e) {
                // 메모리 부족에러시, 원본을 반환
            }
        }

        return bitmap;
    }

}
