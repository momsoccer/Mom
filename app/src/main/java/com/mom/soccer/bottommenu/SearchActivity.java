package com.mom.soccer.bottommenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.mom.soccer.R;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;
import com.mom.soccer.fragment.SearchFragment;

import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    ViewPager viewPager;
    PagerSlidingTabStrip tabsStrip;
    private static int PAGE=0;

    PrefUtil prefUtil;
    User user;
    private Intent intent;
    private int goPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bottom_search_layout);
        ButterKnife.bind(this);
        Log.i(TAG,"onCreate()======================================");

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        intent = getIntent();
        goPage = intent.getExtras().getInt("goPage");

        viewPager = (ViewPager) findViewById(R.id.search_viewpager);
        ViewFragmentPagerAdapter b = new ViewFragmentPagerAdapter(getSupportFragmentManager(),null);
        viewPager.setAdapter(b);

        viewPager.setCurrentItem(goPage);

        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.search_tabs);
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.enabled_red));
        tabsStrip.setTextColor(getResources().getColor(R.color.color6));

        tabsStrip.setViewPager(viewPager);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                PAGE = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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
        Log.i(TAG,"onStart()======================================");
    }

    public class ViewFragmentPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 2;
        final String search;

        private String tabTitles[] = new String[] {
                getString(R.string.tab_coach),getString(R.string.tab_user)};

        public ViewFragmentPagerAdapter(FragmentManager fm,String search) {

            super(fm);
            this.search = search;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return SearchFragment.newInstance(position + 1,user);
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




}
