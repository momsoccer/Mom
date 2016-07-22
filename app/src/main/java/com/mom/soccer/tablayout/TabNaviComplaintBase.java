package com.mom.soccer.tablayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.mom.soccer.R;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.bottommenu.SearchActivity;

import butterknife.Bind;

/**
 * Created by sungbo on 2016-04-25.
 * 탭메뉴를 생성한다
 */
public class TabNaviComplaintBase extends RelativeLayout {

    Context context;

    @Bind(R.id.ib_appbar_home)
    ImageButton imageBtnHome;

    @Bind(R.id.ib_appbar_profile)
    ImageButton imageBtnProfile;

    @Bind(R.id.ib_appbar_ball)
    ImageButton imageBtnBall;

    @Bind(R.id.ib_appbar_search)
    ImageButton imageBtnSearch;

    @Bind(R.id.ib_appbar_setup)
    ImageButton imageBtnSetup;

    public TabNaviComplaintBase(final Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_bottom_bar,this,true);

        imageBtnHome    = (ImageButton) findViewById(R.id.ib_appbar_home);
        imageBtnProfile = (ImageButton) findViewById(R.id.ib_appbar_profile);
        imageBtnBall    = (ImageButton) findViewById(R.id.ib_appbar_ball);
        imageBtnSearch  = (ImageButton) findViewById(R.id.ib_appbar_search);
        imageBtnSetup   = (ImageButton) findViewById(R.id.ib_appbar_setup);

        imageBtnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        imageBtnProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MyPageActivity.class);
                context.startActivity(intent);
                if (context instanceof Activity) {
                    ((Activity) context).overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
                }
            }
        });

        imageBtnBall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        imageBtnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SearchActivity.class);
                context.startActivity(intent);
                if (context instanceof Activity) {
                    ((Activity) context).overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
                }
            }
        });

        imageBtnSetup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }


}
