package com.mom.soccer.mission;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.mom.soccer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserMissionListActivity extends AppCompatActivity {

    @Bind(R.id.u_mission_list_toolbar_title)
    TextView text_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_usermisstion_list_layout);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.u_mission_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        text_toolbar_title.setText(getString(R.string.toolbar_umission_list_page_title));
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //홈버튼클릭시
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
