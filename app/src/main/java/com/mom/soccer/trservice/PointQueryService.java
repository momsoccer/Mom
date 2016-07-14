package com.mom.soccer.trservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import com.mom.soccer.dto.CpBalanceHeader;
import com.mom.soccer.dto.SpBalanceHeader;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.PointService;
import com.mom.soccer.retropitutil.ServiceGenerator;

import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by sungbo on 2016-06-11.
 */
public class PointQueryService {

    private String TAG = "PointQueryService";

    private Context context;
    private SpBalanceHeader spBalanceHeader;
    private CpBalanceHeader cpBalanceHeader;
    private TextView textView;
    private User user;
    private NumberFormat numberFormat = NumberFormat.getInstance();

    //셀프포인트를 가져올때
    public PointQueryService(String TAG, Context context, SpBalanceHeader spBalanceHeader, TextView textView, User user) {
        this.TAG = TAG;
        this.context = context;
        this.spBalanceHeader = spBalanceHeader;
        this.textView = textView;
        this.user = user;
    }

    //캐쉬포인트를 가져올때
    public PointQueryService(String TAG, Context context, CpBalanceHeader cpBalanceHeader, TextView textView, User user) {
        this.TAG = TAG;
        this.context = context;
        this.cpBalanceHeader = cpBalanceHeader;
        this.textView = textView;
        this.user = user;
    }


    //셀프포인트
    public void getSelfPoint(){

        final ProgressDialog dialog;

        dialog = ProgressDialog.show(context, "서버와 통신", "셀프 포인트를 조회합니다", true);
        dialog.show();

        PointService pointService = ServiceGenerator.createService(PointService.class,context,user);

        final Call<SpBalanceHeader> spBalanceHeaderCall = pointService.getSelfAmt(user.getUid());

        spBalanceHeaderCall.enqueue(new Callback<SpBalanceHeader>() {

            @Override
            public void onResponse(Call<SpBalanceHeader> call, Response<SpBalanceHeader> response) {
                try {

                    Log.d(TAG, "서버에서 셀프 포인트 정보를 가져옵니다");
                    spBalanceHeader = response.body();

                    String self_point = String.valueOf(spBalanceHeader.getAmount());
                    textView.setText(numberFormat.format(Integer.valueOf(self_point)));

                    Log.d(TAG,"셀프 포인트 값은 : " + spBalanceHeader.getAmount() );
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.d(TAG, "서버와 통신중 오류 발생 :" + e.getMessage());
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SpBalanceHeader> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가 : " + t.getMessage());
                t.printStackTrace();
                dialog.dismiss();
            }

        });
    }

    //현금포인트
    public void getCashPoint(){

        final ProgressDialog dialog;

        dialog = ProgressDialog.show(context, "서버와 통신", "Cash 포인트를 조회합니다", true);
        dialog.show();

        PointService pointService = ServiceGenerator.createService(PointService.class,context,user);

        final Call<CpBalanceHeader> call = pointService.getCashAmt(user.getUid());

        call.enqueue(new Callback<CpBalanceHeader>() {

            @Override
            public void onResponse(Call<CpBalanceHeader> call, Response<CpBalanceHeader> response) {
                try {

                    Log.d(TAG, "서버에서 캐쉬 포인트 정보를 가져옵니다");

                    cpBalanceHeader = response.body();

                    String self_point = String.valueOf(cpBalanceHeader.getAmount());

                    textView.setText(numberFormat.format(Integer.valueOf(self_point)));

                    Log.d(TAG,"캐쉬 포인트 값은 : " + cpBalanceHeader.getAmount() );
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.d(TAG, "서버와 통신중 오류 발생 :" + e.getMessage());
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CpBalanceHeader> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가 : " + t.getMessage());
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

}
