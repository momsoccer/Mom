package com.mom.soccer.common;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.mom.soccer.R;

/**
 * Created by sungbo on 2016-09-12.
 */
public class MomSnakBar {

    public static void show(View finalView, Activity activity,String showMsg){
        Snackbar snackbar;
        snackbar = Snackbar.make(finalView,showMsg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(activity.getResources().getColor(R.color.color6));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(activity.getResources().getColor(R.color.mom_color1));
        snackbar.show();
    }

}
