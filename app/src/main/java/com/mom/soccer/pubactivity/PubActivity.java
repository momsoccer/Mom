package com.mom.soccer.pubactivity;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.dataDto.UserLevelDataVo;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-23.
 */
public class PubActivity {

    Activity activity;
    User user;
    int queryUid;

    TextView tx_levelmark,
             drible_level,complex_level,flick_level,around_level,lifting_level,traping_level;
    ImageView batch_drible,batch_traping,batch_lifting,batch_around,batch_flick,batch_complex;


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
        tx_levelmark = (TextView) dialog.getCustomView().findViewById(R.id.tx_levelmark);

        drible_level = (TextView) dialog.getCustomView().findViewById(R.id.drible_level);
        flick_level = (TextView) dialog.getCustomView().findViewById(R.id.flick_level);
        around_level = (TextView) dialog.getCustomView().findViewById(R.id.around_level);
        lifting_level = (TextView) dialog.getCustomView().findViewById(R.id.lifting_level);
        traping_level = (TextView) dialog.getCustomView().findViewById(R.id.traping_level);
        complex_level = (TextView) dialog.getCustomView().findViewById(R.id.complex_level);

        batch_drible = (ImageView) dialog.getCustomView().findViewById(R.id.batch_drible);
        batch_traping = (ImageView) dialog.getCustomView().findViewById(R.id.batch_traping);
        batch_lifting = (ImageView) dialog.getCustomView().findViewById(R.id.batch_lifting);
        batch_around = (ImageView) dialog.getCustomView().findViewById(R.id.batch_around);
        batch_flick = (ImageView) dialog.getCustomView().findViewById(R.id.batch_flick);
        batch_complex = (ImageView) dialog.getCustomView().findViewById(R.id.batch_complex);

        WaitingDialog.showWaitingDialog(activity,false);
        DataService dataService = ServiceGenerator.createService(DataService.class,activity,user);
        Call<List<UserLevelDataVo>> c = dataService.getUserLevelDataList(queryUid);
        c.enqueue(new Callback<List<UserLevelDataVo>>() {
            @Override
            public void onResponse(Call<List<UserLevelDataVo>> call, Response<List<UserLevelDataVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    List<UserLevelDataVo> dataVos = response.body();

                    for(int i=0; i < dataVos.size(); i++) {
                        UserLevelDataVo levelDataVo = dataVos.get(i);
                        if (levelDataVo.getMittiontype().equals("DRIBLE")) {
                            drible_level.setText(String.valueOf(levelDataVo.getLevel()));

                            if(levelDataVo.getLevel()!=0){
                                batch_drible.setImageResource(R.drawable.batch_drible_on);
                            }

                        } else if (levelDataVo.getMittiontype().equals("LIFTING")) {
                            lifting_level.setText(String.valueOf(levelDataVo.getLevel()));
                            if(levelDataVo.getLevel()!=0){
                                batch_lifting.setImageResource(R.drawable.batch_lifting_on);
                            }
                        } else if (levelDataVo.getMittiontype().equals("TRAPING")) {
                            traping_level.setText(String.valueOf(levelDataVo.getLevel()));
                            if(levelDataVo.getLevel()!=0){
                                batch_traping.setImageResource(R.drawable.batch_traping_on);
                            }
                        } else if (levelDataVo.getMittiontype().equals("AROUND")) {
                            around_level.setText(String.valueOf(levelDataVo.getLevel()));
                            if(levelDataVo.getLevel()!=0){
                                batch_around.setImageResource(R.drawable.batch_around_on);
                            }
                        } else if (levelDataVo.getMittiontype().equals("FLICK")) {
                            flick_level.setText(String.valueOf(levelDataVo.getLevel()));
                            if(levelDataVo.getLevel()!=0){
                                batch_flick.setImageResource(R.drawable.batch_flick_on);
                            }
                        } else if (levelDataVo.getMittiontype().equals("COMPLEX")) {
                            complex_level.setText(String.valueOf(levelDataVo.getLevel()));
                            if(levelDataVo.getLevel()!=0){
                                batch_complex.setImageResource(R.drawable.batch_complex_on);
                            }
                        } else if (levelDataVo.getMittiontype().equals("TOTAL")) {
                            tx_levelmark.setText(String.valueOf(levelDataVo.getLevel()));
                        }
                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<List<UserLevelDataVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
            }
        });

    }


}
