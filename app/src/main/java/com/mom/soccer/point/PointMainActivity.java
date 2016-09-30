package com.mom.soccer.point;

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
import com.mom.soccer.dto.User;
import com.mom.soccer.fragment.PointFragment;

import butterknife.ButterKnife;

public class PointMainActivity extends AppCompatActivity {

    private static final String TAG = "InsMainActivity";

    ViewPager viewPager;
    PagerSlidingTabStrip tabsStrip;

    private User user;
    private PrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_point_main_layout);
        ButterKnife.bind(this);
        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.point_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        viewPager = (ViewPager) findViewById(R.id.point_viewpager);

        PointViewPagerAdapter pagerAdapter = new PointViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.point_tabs);

        //setting
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.enabled_red));
        tabsStrip.setTextColor(getResources().getColor(R.color.color6));
        tabsStrip.setViewPager(viewPager);

        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public class PointViewPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 2;

        private String tabTitles[] = new String[] {
                getString(R.string.point_fragment_title1),getString(R.string.point_fragment_title2)
        };

        public PointViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return PointFragment.newInstance(position + 1,user);
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

    @Override
    protected void onStart() {
        super.onStart();
    }
}
