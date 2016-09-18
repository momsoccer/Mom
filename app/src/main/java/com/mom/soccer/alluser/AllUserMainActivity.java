package com.mom.soccer.alluser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.mom.soccer.R;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.pubactivity.Param;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AllUserMainActivity extends AppCompatActivity {

    private static final String TAG = "AllUserMainActivity";

    private PrefUtil prefUtil;
    private User user;
    private Instructor instructor;
    private Intent intent;
    private int PageCall = 0;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabsStrip;

    @Bind(R.id.textWrite)
    TextView textWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_main);
        ButterKnife.bind(this);
        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();
        instructor = prefUtil.getIns();

        intent = getIntent();
        PageCall = intent.getExtras().getInt(Param.FRAGMENT_COUNT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        viewPager = (ViewPager) findViewById(R.id.player_ollow_viewpager);

        AllPageViewPagerAdapter pagerAdapter = new AllPageViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.player_follow_tabs);

        tabsStrip.setIndicatorColor(getResources().getColor(R.color.enabled_red));
        tabsStrip.setTextColor(getResources().getColor(R.color.color6));
        tabsStrip.setViewPager(viewPager);
        viewPager.setCurrentItem(PageCall);

        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position == 0){
                    textWrite.setText(getResources().getString(R.string.all_user_page_btn1));
                }else if(position==1){
                    textWrite.setText(getResources().getString(R.string.all_user_page_btn2));
                }else if(position==2){
                    textWrite.setText(getResources().getString(R.string.all_user_page_btn1));
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public class AllPageViewPagerAdapter extends FragmentStatePagerAdapter {
        final int PAGE_COUNT = 3;

        private String tabTitles[] = new String[] {
                getString(R.string.all_user_page_title1),
                getString(R.string.all_user_page_title2),
                getString(R.string.all_user_page_title3)
        };

        public AllPageViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            AllUserFragment playerFragment = AllUserFragment.newInstance(position + 1,user);
            return playerFragment;
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
