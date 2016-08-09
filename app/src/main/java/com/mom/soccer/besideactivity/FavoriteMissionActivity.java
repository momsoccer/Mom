package com.mom.soccer.besideactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.mom.soccer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavoriteMissionActivity extends AppCompatActivity {

    @Bind(R.id.fa_title)
    TextView textView_fa_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_favorite_mission_layout);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.fa_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        textView_fa_title.setText(R.string.toolbar_favorite_page);
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