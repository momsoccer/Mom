package com.mom.soccer.momactivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.mom.soccer.MainActivity;
import com.mom.soccer.R;
import com.mom.soccer.Ranking.UserRankingActivity;
import com.mom.soccer.adapter.MainRankingAdapter;
import com.mom.soccer.besideactivity.ApplyCoachActivity;
import com.mom.soccer.besideactivity.FavoriteMissionActivity;
import com.mom.soccer.besideactivity.StudentMainActivity;
import com.mom.soccer.bookmark.MyBookMarkActivity;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.bottommenu.SearchActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.SettingActivity;
import com.mom.soccer.dataDto.UserRangkinVo;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.TeamMember;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionActivity;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.InstructorService;
import com.mom.soccer.retrofitdao.MomComService;
import com.mom.soccer.retrofitdao.TeamService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.DialogBuilder;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MomMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private static final String TAG = "MomMainActivity";

    public static NavigationView navigationView;

    private User user;
    private PrefUtil prefUtil;
    private Instructor instructor;
    private ViewPager viewPager;
    private MomViewPagerAdapter momViewPagerAdapter;
    private MainRankingAdapter mainRankingAdapter;
    private TeamMember teamMember = new TeamMember();

    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    View header;

    //@Bind(R.id.grid_view)
    //public GridView mGridView;

    //네비게이션 상단 아이템
    TextView navHeaderUserName,navHeaderUserEmail,navHeaderCoachName,navHeaderTeamName;
    ImageView navHeaderImage;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main_layout);
        ButterKnife.bind(this);

        Log.d(TAG, "onCreate ===========================================================");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();
        instructor = prefUtil.getIns();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP );

        //만든네비케이터를 붙였기 때문에 이벤트가 죽는다 다시 붙여준다

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.homeindicator);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //네비 상단 정보
        header = navigationView.getHeaderView(0);
        navHeaderUserName = (TextView) header.findViewById(R.id.nav_header_username);
        navHeaderUserEmail = (TextView) header.findViewById(R.id.nav_header_useremail);
        navHeaderCoachName = (TextView) header.findViewById(R.id.nav_header_coachname);
        navHeaderTeamName = (TextView) header.findViewById(R.id.nav_header_teamname);
        navHeaderImage  = (ImageView) header.findViewById(R.id.nav_header_image);
        navHeaderUserName.setText(user.getUsername());
        navHeaderUserEmail.setText(user.getUseremail());


        //일반 회원 가입시 이미지가 없기 때문에..초기 셋팅
        if(!Compare.isEmpty(user.getProfileimgurl())){
            Glide.with(MomMainActivity.this)
                    .load(user.getProfileimgurl())
                    .into(navHeaderImage);

        Log.d(TAG,"유저 이미지 있다면... : " + user.getProfileimgurl());
        }else {
            Log.d(TAG,"유저 이미지 없다면...: " + user.getProfileimgurl());
        }




        //팀명 셋팅 조건이 필요

        /*****************************************************************************
         * 뷰 페이저 설정 부분
         ****************************************************************************/
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

        layouts = new int[]{
                R.layout.mom_main_slide1,
                R.layout.mom_main_slide3,
                R.layout.mom_main_slide2,
        };

        addBottomDots(0);
        changeStatusBarColor();

        momViewPagerAdapter = new MomViewPagerAdapter();

        viewPager.setAdapter(momViewPagerAdapter);


        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        //스크롤 안에 뷰페이저 처리
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Log.d(TAG, "onCreate ===========================================================" + user.getProfileimgurl());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"on Start ======================================================");

        user = prefUtil.getUser();

        //만약 앱을 연 상태에서 설정을 했다면 다시 불러오기
        if(user.getUseremail() != null){
            navHeaderUserName = (TextView) header.findViewById(R.id.nav_header_username);
            navHeaderUserEmail = (TextView) header.findViewById(R.id.nav_header_useremail);
            navHeaderCoachName = (TextView) header.findViewById(R.id.nav_header_coachname);
            navHeaderTeamName = (TextView) header.findViewById(R.id.nav_header_teamname);
            navHeaderImage  = (ImageView) header.findViewById(R.id.nav_header_image);
            navHeaderUserName.setText(user.getUsername());
            navHeaderUserEmail.setText(user.getUseremail());
        }

        if(!Compare.isEmpty(user.getProfileimgurl())){
            Glide.with(MomMainActivity.this)
                    .load(user.getProfileimgurl())
                    .into(navHeaderImage);
        }

        //강사정보 셋팅
        getInstructorInfo();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.mn_item_home) {
            Intent intent = new Intent(this, MomMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        }else if(id==R.id.mn_item_teammemberlist){
            Intent intent = new Intent(this,StudentMainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else if(id == R.id.mn_item_favorite){
            Intent intent = new Intent(this,FavoriteMissionActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else if(id == R.id.mn_item_gansim){
            Intent intent = new Intent(getApplicationContext(),MyBookMarkActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        }else if(id == R.id.mn_item_search){

            Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택에 쌓이는 것을 방지 한다. 필수
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        }else if(id == R.id.mn_item_mypage){

            Intent intent = new Intent(getApplicationContext(),MyPageActivity.class);
            intent.putExtra("pageflag","me");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        }else if(id == R.id.mn_item_coachreq){
            Intent intent = new Intent(this,ApplyCoachActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else if(id == R.id.mn_item_setup){
            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        }else if(id == R.id.mn_item_logout){
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut(){



        if(user.getSnstype().equals("facebook")){
            Log.d(TAG,"페이스북 회원가입 유저 로그아웃을 합니다");

            //페이스북 토큰 및 세션 끈기
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

        }else if(user.getSnstype().equals("kakao")){
            Log.d(TAG,"카카오 회원가입 유저 로그아웃을 합니다");
            onClickLogout();
        }else if(user.getSnstype().equals("app")){
            Log.d(TAG,"자체 회원가입 유저 로그아웃을 합니다");
        }
        prefUtil.clearPrefereance();


        //서버 세션을 초기화해준다.
        WaitingDialog.showWaitingDialog(MomMainActivity.this,false);
        MomComService momComService = ServiceGenerator.createService(MomComService.class,this,user);
        final Call<ServerResult> logOutCall = momComService.logOut();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                logOutCall.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                        if(response.isSuccessful()) {
                            WaitingDialog.cancelWaitingDialog();
                            VeteranToast.makeToast(getApplicationContext(),getString(R.string.app_logout), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            WaitingDialog.cancelWaitingDialog();
                            VeteranToast.makeToast(getApplicationContext(),getString(R.string.app_logout), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();

                        }
                    }
                    @Override
                    public void onFailure(Call<ServerResult> call, Throwable t) {
                        WaitingDialog.cancelWaitingDialog();
                        Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.app_logout), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();

                    }
                });
            }
        }, 1000);

    }

    //카카오 세션 끈기 메소드
    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
            }
        });
    }

    public void OnClickHeader(View v){
        switch (v.getId()){
            case R.id.mom_hd_mk:
                break;
            case R.id.hd_bell:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:

                new DialogBuilder(MomMainActivity.this)
                        //.setTitle("질문")
                        .setMessage(getString(R.string.app_end_action))
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        }).create().show();
                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }



    /*
    아이콘 관련 디테일하게 꾸미기
    http://stackoverflow.com/questions/31097716/how-to-style-the-design-support-librarys-navigationview
     */

    /************************************
     *
     * 뷰페이저 설정
     *
     * **********************************/

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MomViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MomViewPagerAdapter() {
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }


        //메인 랭킹 표현부분

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View view = layoutInflater.inflate(layouts[position],container,false);
            container.addView(view);


            if(position == 0){
                Log.i(TAG,"뷰 페이저 값을 갱신 합니다");
                final LinearLayout li_main_slid_ranking,li_main_slid_ranking_no_data;
                li_main_slid_ranking = (LinearLayout) view.findViewById(R.id.li_main_slid_ranking);
                li_main_slid_ranking_no_data = (LinearLayout) view.findViewById(R.id.li_main_slid_ranking_no_data);

                UserRangkinVo userRangkinVo = new UserRangkinVo();
                userRangkinVo.setQueryRow(3);
                userRangkinVo.setOrderbytype("totalscore");

                WaitingDialog.showWaitingDialog(MomMainActivity.this,false);
                DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);
                final Call<List<UserRangkinVo>> call = dataService.getTotalRanking(userRangkinVo);
                call.enqueue(new Callback<List<UserRangkinVo>>() {


                    @Override
                    public void onResponse(Call<List<UserRangkinVo>> call, Response<List<UserRangkinVo>> response) {
                        if(response.isSuccessful()){
                            WaitingDialog.cancelWaitingDialog();
                            li_main_slid_ranking.setVisibility(View.VISIBLE);
                            li_main_slid_ranking_no_data.setVisibility(View.GONE);

                            List<UserRangkinVo> listVos = response.body();
                            MainRankingAdapter mainRankingAdapter = new MainRankingAdapter(getApplicationContext(), R.layout.adabter_mainlist_layout,listVos);
                            ListView listView = (ListView) container.findViewById(R.id.list_total_ranking);
                            listView.setAdapter(mainRankingAdapter);
                        }else{
                            WaitingDialog.cancelWaitingDialog();
                            li_main_slid_ranking.setVisibility(View.GONE);
                            li_main_slid_ranking_no_data.setVisibility(View.VISIBLE);
                            btnMethodAllRanking(view);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserRangkinVo>> call, Throwable t) {
                        WaitingDialog.cancelWaitingDialog();
                        li_main_slid_ranking.setVisibility(View.GONE);
                        li_main_slid_ranking_no_data.setVisibility(View.VISIBLE);
                        btnMethodAllRanking(view);

                        Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가 : " + t.getMessage());
                        t.printStackTrace();
                    }
                });

                Button button = (Button) container.findViewById(R.id.btn_more_total_ranking);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),UserRankingActivity.class);
                        intent.putExtra("pageparam","total");
                        startActivity(intent);
                    }
                });


            }else if(position==1){

                final LinearLayout li_main_team_layout,li_main_noData_layout;

                li_main_team_layout = (LinearLayout) view.findViewById(R.id.li_main_team_layout);
                li_main_noData_layout = (LinearLayout) view.findViewById(R.id.li_main_noData_layout);

                WaitingDialog.showWaitingDialog(MomMainActivity.this,false);
                TeamService teamService = ServiceGenerator.createService(TeamService.class,MomMainActivity.this,user);
                TeamMember member = new TeamMember();
                member.setUid(user.getUid());
                Call<TeamMember> c = teamService.getTeamMemeber(member);
                c.enqueue(new Callback<TeamMember>() {
                    @Override
                    public void onResponse(Call<TeamMember> call, Response<TeamMember> response) {
                        if(response.isSuccessful()){
                            teamMember = response.body();
                            WaitingDialog.cancelWaitingDialog();

                            if(teamMember.getUid()==0){
                                li_main_noData_layout.setVisibility(View.VISIBLE);
                                li_main_team_layout.setVisibility(View.GONE);
                            }else{
                                li_main_noData_layout.setVisibility(View.GONE);
                                li_main_team_layout.setVisibility(View.VISIBLE);

                                //데이터를 읽어 들여 뿌리는 작업을 한다
                                ListView listView = (ListView) container.findViewById(R.id.list_team_ranking);
                            }

                        }else{
                            WaitingDialog.cancelWaitingDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeamMember> call, Throwable t) {
                        WaitingDialog.cancelWaitingDialog();
                        t.printStackTrace();
                    }
                });

                Button button = (Button) container.findViewById(R.id.btn_more_team_ranking);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),UserRankingActivity.class);
                        intent.putExtra("pageparam","team");
                        startActivity(intent);
                    }
                });

            }else if(position==2){
                ListView listView = (ListView) container.findViewById(R.id.list_friend_ranking);
                Button button = (Button) container.findViewById(R.id.btn_more_friend_ranking);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),UserRankingActivity.class);
                        intent.putExtra("pageparam","friend");
                        startActivity(intent);
                    }
                });
            }

            return view;
        }

        public void btnMethodAllRanking(View view){
            Button btn_refresh = (Button) view.findViewById(R.id.btn_refresh_ranking);
            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MomMainActivity.this,MomMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

    //각 타입 별로 이동
    public void mOnClick(View v){
        switch (v.getId()){
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume ======================================================");
        Log.d(TAG, "onResume ===========================================================" + user.getProfileimgurl());

        user = prefUtil.getUser();
        Glide.with(MomMainActivity.this)
                .load(user.getProfileimgurl())
                .into(navHeaderImage);

    }

    /******************************
     * Mission Activity choose
     ******************************/

    public void OnClickMission(View v){

        Intent intent = new Intent(this, MissionActivity.class);

        switch (v.getId()){

            case R.id.main_btn_drible:
                intent.putExtra(MissionCommon.MISSIONTYPE,"DRIBLE");
                break;

            case R.id.main_btn_trapping:
                intent.putExtra(MissionCommon.MISSIONTYPE,"TRAPING");
                break;

            case R.id.main_btn_lifting:
                intent.putExtra(MissionCommon.MISSIONTYPE,"LIFTING");
                break;

            case R.id.main_btn_around:
                intent.putExtra(MissionCommon.MISSIONTYPE,"AROUND");
                break;

            case R.id.main_btn_flick:
                intent.putExtra(MissionCommon.MISSIONTYPE,"FLICK");
                break;

            case R.id.main_btn_crosbar:
                intent.putExtra(MissionCommon.MISSIONTYPE,"COMPLEX");
                break;

        }
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }


    public void getInstructorInfo(){

        WaitingDialog.showWaitingDialog(this,false);

        InstructorService service = ServiceGenerator.createService(InstructorService.class,getApplicationContext(),user);
        Call<Instructor> c = service.getFindIns(user.getUid());
        c.enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                if(response.isSuccessful()){
                    instructor = response.body();
                    prefUtil.saveIns(instructor);
                    WaitingDialog.cancelWaitingDialog();

                    Log.i(TAG,"ins : " + instructor.toString());

                    if(instructor.getUid()==0){
                        navigationView.getMenu().findItem(R.id.mn_item_coachreq).setTitle(getString(R.string.mom_menu_title_coach_apply));
                        navigationView.getMenu().findItem(R.id.mn_item_teammemberlist).setVisible(false);
                    }else{
                        navigationView.getMenu().findItem(R.id.mn_item_coachreq).setTitle(getString(R.string.mom_menu_title_coach_info));
                        navigationView.getMenu().findItem(R.id.mn_item_teammemberlist).setVisible(true);
                    }


                }else{
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });

    }

}
