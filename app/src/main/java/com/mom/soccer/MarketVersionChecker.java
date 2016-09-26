package com.mom.soccer;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import com.mom.soccer.common.Common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sungbo on 2016-09-24.
 */

public class MarketVersionChecker {
    private static final String TAG = "MarketVersionChecker";


    public void getMarketVersion(final String packageName, final Activity activity) {

        final String returnName = null;

        try {
            Common.DIVICEVERSITON_CODE = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Thread downloadThread = new Thread() {
            public void run() {

                Document doc;
                try {

                    doc = Jsoup.connect(
                            "https://play.google.com/store/apps/details?id="
                                    + packageName).get();
                    Elements Version = doc.select(".content");

                    for (Element mElement : Version) {
                        if (mElement.attr("itemprop").equals("softwareVersion")) {
                            String name =  mElement.text().trim();
                            Common.VERSITON_CODE = name;
                            Log.i(TAG,"몸싸커 앱 버전 체크를 합니다" + name);
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        downloadThread.start();
    }

    public String getMarketVersionFast(String packageName) {
        String mData = "", mVer = null;

        try {
            URL mUrl = new URL("https://play.google.com/store/apps/details?id="
                    + packageName);
            HttpURLConnection mConnection = (HttpURLConnection) mUrl
                    .openConnection();

            if (mConnection == null)
                return null;

            mConnection.setConnectTimeout(5000);
            mConnection.setUseCaches(false);
            mConnection.setDoOutput(true);

            if (mConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader mReader = new BufferedReader(
                        new InputStreamReader(mConnection.getInputStream()));

                while (true) {
                    String line = mReader.readLine();
                    if (line == null)
                        break;
                    mData += line;
                }

                mReader.close();
            }

            mConnection.disconnect();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        String startToken = "softwareVersion\">";
        String endToken = "<";
        int index = mData.indexOf(startToken);

        if (index == -1) {
            mVer = null;

        } else {
            mVer = mData.substring(index + startToken.length(), index
                    + startToken.length() + 100);
            mVer = mVer.substring(0, mVer.indexOf(endToken)).trim();
        }

        return mVer;
    }

}
