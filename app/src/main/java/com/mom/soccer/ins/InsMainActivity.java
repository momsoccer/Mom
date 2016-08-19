package com.mom.soccer.ins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.mom.soccer.R;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.fragment.InsFragmentTeamApply;
import com.mom.soccer.mission.MissionCommon;

import butterknife.ButterKnife;

public class InsMainActivity extends AppCompatActivity {

    private static final String TAG = "InsMainActivity";

    ViewPager viewPager;
    PagerSlidingTabStrip tabsStrip;
    private static int PAGE=0;

    private User user;
    private PrefUtil prefUtil;
    Intent intent;
    Instructor instructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_ins_main_layout);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.ins_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        intent = getIntent();
        instructor = (Instructor) intent.getSerializableExtra(MissionCommon.INS_OBJECT);

        viewPager = (ViewPager) findViewById(R.id.follow_viewpager);

        InsViewPagerAdapter pagerAdapter = new InsViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.follow_tabs);

        //setting
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.enabled_red));
        tabsStrip.setTextColor(getResources().getColor(R.color.color6));
        tabsStrip.setViewPager(viewPager);

    }

    public class InsViewPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 3;

        private String tabTitles[] = new String[] {
        getString(R.string.ins_toolbar_title),getString(R.string.ins_team_info_text),getString(R.string.ins_team_video_text)
        };

        public InsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return InsFragmentTeamApply.newInstance(position + 1,user,instructor); //여기를 만들고
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
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
