package com.mom.soccer.pubactivity;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.dto.User;

/**
 * Created by sungbo on 2016-08-23.
 */
public class PubActivity {

    Activity activity;
    User user;
    int queryUid;

    public PubActivity(Activity activity, User user,int queryUid) {
        this.activity = activity;
        this.user = user;
        this.queryUid = queryUid;
    }

    public void showDialog(){

        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .icon(activity.getResources().getDrawable(R.drawable.batch_title))
                .titleColor(activity.getResources().getColor(R.color.color6))
                .customView(R.layout.batch_layout, true)
                .positiveText(R.string.mom_diaalog_confirm)
                .build();

        dialog.show();

    }


}
