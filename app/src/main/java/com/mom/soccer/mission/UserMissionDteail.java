package com.mom.soccer.mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.UserMission;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserMissionDteail extends AppCompatActivity {

    private static final String TAG = "UserMissionDteail";

    Mission mission;
    UserMission userMission;

    @Bind(R.id.user_mission_title)
    TextView user_mission_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_mission_dteail_layout);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_mission_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        mission = (Mission) intent.getSerializableExtra(MissionCommon.OBJECT);
        userMission = (UserMission) intent.getSerializableExtra(MissionCommon.USER_MISSTION_OBJECT);

        user_mission_title.setText(R.string.user_mission_detail_title);

    }
}
