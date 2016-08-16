package com.mom.soccer.bottommenu;

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
import com.mom.soccer.fragment.FollowerFragment;

import butterknife.ButterKnife;

public class FollowActivity extends AppCompatActivity {

    private static final String TAG = "FollowActivity";

    ViewPager viewPager;
    PagerSlidingTabStrip tabsStrip;
    private static int PAGE=0;

    private User user;
    private PrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_follow_layout);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.follow_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        viewPager = (ViewPager) findViewById(R.id.follow_viewpager);
        FollowViewPagerAdapter b = new FollowViewPagerAdapter(getSupportFragmentManager(),null);
        viewPager.setAdapter(b);

        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.follow_tabs);

        //setting
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.enabled_red));
        tabsStrip.setTextColor(getResources().getColor(R.color.color6));
        tabsStrip.setViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    //뷰페이퍼 만들어준다
    public class FollowViewPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 2;
        final String search;

        private String tabTitles[] = new String[] {
                getString(R.string.follow_toolbar_page_text),getString(R.string.follow_view_pager)};

        public FollowViewPagerAdapter(FragmentManager fm, String search) {

            super(fm);
            this.search = search;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return FollowerFragment.newInstance(position + 1,user); //여기를 만들고
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
