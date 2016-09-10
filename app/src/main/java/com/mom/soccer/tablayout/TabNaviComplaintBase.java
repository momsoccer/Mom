package com.mom.soccer.tablayout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.mom.soccer.R;
import com.mom.soccer.ball.PlayerMainActivity;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.bottommenu.SearchActivity;
import com.mom.soccer.ins.InsDashboardActivity;
import com.mom.soccer.momactivity.MomMainActivity;
import com.mom.soccer.pubactivity.Param;

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


    @Bind(R.id.ib_appbar_coach)
    ImageButton ib_appbar_coach;

    @Bind(R.id.ib_board)
    ImageButton ib_board;


    public TabNaviComplaintBase(final Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_bottom_bar,this,true);
        imageBtnHome    = (ImageButton) findViewById(R.id.ib_appbar_home);
        imageBtnProfile = (ImageButton) findViewById(R.id.ib_appbar_profile);
        imageBtnBall    = (ImageButton) findViewById(R.id.ib_appbar_ball);
        imageBtnSearch  = (ImageButton) findViewById(R.id.ib_appbar_search);

        ib_appbar_coach = (ImageButton) findViewById(R.id.ib_appbar_coach);
        ib_board    = (ImageButton) findViewById(R.id.ib_board);

        //imageBtnSetup   = (ImageButton) findViewById(R.id.ib_appbar_setup);


        ib_appbar_coach.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,InsDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Param.FRAGMENT_COUNT,0);
                context.startActivity(intent);
            }
        });


        imageBtnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MomMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        imageBtnProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MyPageActivity.class);
                intent.putExtra("pageflag","me");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        imageBtnBall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PlayerMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Param.FRAGMENT_COUNT,0);
                context.startActivity(intent);

            }
        });

        imageBtnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SearchActivity.class);
                intent.putExtra("goPage",0);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        ib_board.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

/*        imageBtnSetup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });*/

    }


}
