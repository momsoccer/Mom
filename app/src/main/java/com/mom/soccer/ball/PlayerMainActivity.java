package com.mom.soccer.ball;

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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.mom.soccer.R;
import com.mom.soccer.common.ActivityResultEvent;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.EventBus;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;
import com.mom.soccer.fragment.PlayerFragment;
import com.mom.soccer.pubactivity.Param;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayerMainActivity extends AppCompatActivity {

    private static final String TAG = "PlayerMainActivity";

    private static final int COMMENT_LINE_CODE = 201;

    ViewPager viewPager;
    PagerSlidingTabStrip tabsStrip;

    private User user;
    private PrefUtil prefUtil;
    private Intent intent;

    private int PageCall = 0;

    @Bind(R.id.ic_board_write)
    ImageButton ic_board_write;

    public static FloatingActionButton rightLowerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_player_main_layout);
        ButterKnife.bind(this);
        Log.i(TAG,"====================== onCreate() ======================");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        intent = getIntent();
        PageCall = intent.getExtras().getInt(Param.FRAGMENT_COUNT);

        //템체크체

        Toolbar toolbar = (Toolbar) findViewById(R.id.player_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        viewPager = (ViewPager) findViewById(R.id.player_ollow_viewpager);

        PlayerViewPagerAdapter pagerAdapter = new PlayerViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.player_follow_tabs);

        //setting
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.enabled_red));
        tabsStrip.setTextColor(getResources().getColor(R.color.color6));
        tabsStrip.setViewPager(viewPager);
        viewPager.setCurrentItem(PageCall);


        //floating button
        final ImageView fabIconNew = new ImageView(this);

        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_tune_white_24dp));
        int redActionButtonSize = 70;
        int redActionButtonMargin = 10;

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconNew.setLayoutParams(starParams);

        rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .build();

        rightLowerButton.setVisibility(View.GONE);

        //팀 게시판 기능
        ic_board_write.setVisibility(View.VISIBLE);

        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position==0) {
                    if(Common.teamid != 0){
                        ic_board_write.setVisibility(View.VISIBLE);
                    }
                    rightLowerButton.setVisibility(View.GONE);
                }else if(position==1){
                    ic_board_write.setVisibility(View.GONE);
                    rightLowerButton.setVisibility(View.VISIBLE);
                }else if(position==2){
                    ic_board_write.setVisibility(View.GONE);
                    rightLowerButton.setVisibility(View.GONE);
                }else if(position==3){
                    ic_board_write.setVisibility(View.GONE);
                    rightLowerButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        //상단글쓰기 버튼 클릭시
        ic_board_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(PlayerMainActivity.this,TeamBoardActivity.class);
                    intent.putExtra("boardFlag","new");
                    intent.putExtra("boardid",0);
                    startActivity(intent);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"====================== onStart() ======================");
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

    public class PlayerViewPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 3;

        private String tabTitles[] = new String[] {
                getString(R.string.my_fragment_title2),
                getString(R.string.my_fragment_title3),
                getString(R.string.my_fragment_title4)
        };

        public PlayerViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            PlayerFragment playerFragment = PlayerFragment.newInstance(position + 1,user);
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
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"====================== onDestroy() ======================");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        EventBus.getInstance().post(ActivityResultEvent.create(requestCode, resultCode, data));
    }
}
