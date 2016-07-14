package com.mom.soccer.retropitutil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

/**
 * Created by sungbo on 2016-06-20.
 * 쿠키 인터셉터
 */
public class DalgonaSharedPreferences {

    public static final String KEY_COOKIE = "com.sports.cookie";

    private Context mContext;
    private SharedPreferences pref;

    private static DalgonaSharedPreferences dsp = null;



    public static DalgonaSharedPreferences getInstanceOf(Context c){
        if(dsp==null){
            dsp = new DalgonaSharedPreferences(c);
        }

        return dsp;
    }

    public DalgonaSharedPreferences(Context c) {
        mContext = c;
        final String PREF_NAME = c.getPackageName();
        pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
    }

    public void putHashSet(String key, HashSet<String> set){
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, set);
        editor.commit();
    }

    public HashSet<String> getHashSet(String key, HashSet<String> dftValue){
        try {
            return (HashSet<String>)pref.getStringSet(key, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            return dftValue;
        }
    }

}
