package com.mom.soccer.pubretropit;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.MomComService;
import com.mom.soccer.retropitutil.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-09-11.
 * 신고하기
 */
public class PubReport {

    public static String REPORTTYPE_FEEDBACK = "FEEDBACK";
    public static String REPORTTYPE_MOMBOARD_HEADER = "BOARDHEADER";
    public static String REPORTTYPE_MOMBOARD_COMMENT = "BOARDCOMMENT";
    public static String REPORTTYPE_MISSION_COMMENT = "MISSIONCOMMENT";
    public static String REPORTTYPE_MISSION_PASS = "MISSIONPASS";
    public static String REPORTTYPE_INS_VIDEO = "NS_VIDEO";
    public static String REPORTTYPE_INS_VIDEO_COMMENT = "IDEO_COMMENT";

    public static void doReport(final Activity activity, Report report, User user){

        final MaterialDialog materialDialog =  new MaterialDialog.Builder(activity)
                .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(R.string.app_loging_title)
                .titleColor(activity.getResources().getColor(R.color.color6))
                .content(R.string.report_title)
                .contentColor(activity.getResources().getColor(R.color.color6))
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

        MomComService momComService = ServiceGenerator.createService(MomComService.class,activity,user);
        Call<ServerResult> c = momComService.saveReport(report);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                materialDialog.dismiss();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    if(result.getCount()==1){
                        new MaterialDialog.Builder(activity)
                                .content(R.string.report_title2)
                                .positiveText(R.string.mom_diaalog_confirm)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                materialDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

}

