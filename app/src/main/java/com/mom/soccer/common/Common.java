package com.mom.soccer.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sungbo on 2016-05-27.
 */
public class Common {
    public static String SERVER_ADRESS = "http://14.63.220.208:80";
    //http://14.63.220.208:80
    //"http://192.168.0.48:8080";

    public static String PUSH = "Y";

    public static String INSFLAG = "USER";

    public static String SERVER_USER_IMGFILEADRESS = SERVER_ADRESS + "/resources/userimg/";
    public static String SERVER_INS_IMGFILEADRESS = SERVER_ADRESS + "/resources/instructorimg/";
    public static String SERVER_TEAM_IMGFILEADRESS = SERVER_ADRESS + "/resources/teamimg/";

    public static String SERVER_BOARD_IMGFILEADRESS = SERVER_ADRESS + "/resources/board/";

    public static String VETERAN_SNSID = "999999999999999";

    public static String YOUTUBE_ADDR = "https://youtu.be/";
    public static Boolean NETWORK_COOKIE = false;

    public static String IMAGE_MOM_PATH = "/MomSoccerImage/";
    public static String VERSITON_CODE = "1";
    public static String DIVICEVERSITON_CODE = "1";

    public static boolean upflag;
    public static String IMAGE_CHOOSE_FALG = "";
    public static String YOUTUBEVIDEO = "YOUTUBEVIDEO";

    //if user use android Fullscreen.. randspace. => reset orient

    public static String YOUTUBESCREEN_CONDITION = "Y"; //LANDSCAPE
    public static String YOUTUBESCREEN_STATUS = "PORTRAIT"; //LANDSCAPE

    public static int teamid = 0;

    public static String getDeviceSerialNumber() {
        try {
            return (String) Build.class.getField("SERIAL").get(null);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static boolean isUpflag() {
        return upflag;
    }

    public static void setUpflag(boolean upflag) {
        Common.upflag = upflag;
    }

    public static Bitmap blur(Context context, Bitmap sentBitmap, int radius) {

        Bitmap bitmap;

        //if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius); //0.0f ~ 25.0f
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        //}
    }

    public static Bitmap getBitmapFromURL(String src) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            if(connection!=null)connection.disconnect();
        }
    }

}
