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
import com.mom.soccer.pubactivity.Param;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsDashboardActivity extends AppCompatActivity {

    private static final String TAG = "InsDashboardActivity";

    ViewPager viewPager;
    PagerSlidingTabStrip tabsStrip;
    private static int PAGE=0;

    private User user;
    private PrefUtil prefUtil;
    Instructor instructor;

    private Intent intent;
    private int pageCall = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_ins_dashboard_layout);
        ButterKnife.bind(this);

        intent = getIntent();
        pageCall = intent.getExtras().getInt(Param.FRAGMENT_COUNT);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();
        instructor = prefUtil.getIns();


        Toolbar toolbar = (Toolbar) findViewById(R.id.dash_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        viewPager = (ViewPager) findViewById(R.id.dash_viewpager);
        insDashPagerAdapter pagerAdapter = new insDashPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.dash_tabs);

        viewPager.setCurrentItem(pageCall);

        //setting
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.enabled_red));
        tabsStrip.setTextColor(getResources().getColor(R.color.color6));
        tabsStrip.setViewPager(viewPager);

    }

    public class insDashPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 3;

        private String tabTitles[] = new String[] {
                getString(R.string.my_fragment_title5),getString(R.string.ins_pass_title),getString(R.string.ins_common_title)
        };

        public insDashPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return insDashFragment.newInstance(position + 1,user,instructor);
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

    @OnClick(R.id.im_point)
    public void im_point(){
        Intent intent = new Intent(InsDashboardActivity.this,InsPointHistory.class);
        startActivity(intent);
    }


}
