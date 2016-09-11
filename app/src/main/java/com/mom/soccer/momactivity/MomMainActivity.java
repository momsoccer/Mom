package com.mom.soccer.momactivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.mom.soccer.MainActivity;
import com.mom.soccer.R;
import com.mom.soccer.Ranking.UserRankingActivity;
import com.mom.soccer.adapter.RankingItemAdapter;
import com.mom.soccer.besideactivity.AlramActivity;
import com.mom.soccer.besideactivity.ApplyCoachActivity;
import com.mom.soccer.besideactivity.FavoriteMissionActivity;
import com.mom.soccer.besideactivity.StudentMainActivity;
import com.mom.soccer.bookmark.MyBookMarkActivity;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.bottommenu.SearchActivity;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.CropCircleTransformation;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.common.SettingActivity;
import com.mom.soccer.dataDto.UserRangkinVo;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.TeamMember;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionActivity;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.pubactivity.PubActivity;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.InstructorService;
import com.mom.soccer.retrofitdao.MomComService;
import com.mom.soccer.retrofitdao.TeamService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.trservice.UserTRService;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  MomMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private static final String TAG = "MomMainActivity";

    public static NavigationView navigationView;

    private User user;
    private PrefUtil prefUtil;
    private Instructor instructor;
    private ViewPager viewPager;
    private MomViewPagerAdapter momViewPagerAdapter;

    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    View header;

    //@Bind(R.id.grid_view)
    //public GridView mGridView;

    //네비게이션 상단 아이템
    TextView navHeaderUserName,navHeaderUserEmail,navHeaderCoachName,navHeaderTeamName;
    ImageView navHeaderImage;

    public static Activity activity;

    private Bitmap photo;
    private Uri mImageCaptureUri;
    private String RealFilePath;
    private String fileName;
    private String absoultePath;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    public MomMainActivity(){};

    ImageButton imageBtnBall;
    ImageButton ib_appbar_coach;

    private String imgtype;
    //버젼 체크를 위한...private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    //상단 순위
    RecyclerView totalRecyclerView,friendRecyclerView,teamRecyclerView;
    RankingItemAdapter rankingItemAdapter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main_layout);
        ButterKnife.bind(this);

        activity = this;
        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();


        Intent intent = getIntent();
        instructor = (Instructor) intent.getSerializableExtra("INS");

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
        navHeaderImage  = (ImageView) header.findViewById(R.id.nav_header_image);
        navHeaderUserName.setText(user.getUsername());
        navHeaderUserEmail.setText(user.getUseremail());


        //일반 회원 가입시 이미지가 없기 때문에..초기 셋팅
        if(!Compare.isEmpty(user.getProfileimgurl())){
            Glide.with(MomMainActivity.this)
                    .load(user.getProfileimgurl())
                    .asBitmap().transform(new CropCircleTransformation(this))
                    .into(navHeaderImage);
        }

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
                R.layout.mom_main_slide4
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


        navHeaderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgtype="front";
                changeImage();
            }
        });
        //버젼체크 및 권한 설정 마쉬멜로 이상. 필수
        if(Build.VERSION.SDK_INT  >= 23){

            /*
            Log.d(TAG, "onCreate 퍼미션 체크를 합니다");
            Log.i(TAG, "1.파일 쓰기 : " + checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
            Log.i(TAG, "2.파일 읽기 : " + checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
            Log.i(TAG, "3.네트워크   : " + checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE));
            Log.i(TAG, "4 구글 계정: " + checkSelfPermission(Manifest.permission.GET_ACCOUNTS));
            Log.i(TAG, "5 인텐트: " + checkSelfPermission(Manifest.permission.INTERNET));
            Log.i(TAG, "6 폰정보: " + checkSelfPermission(Manifest.permission.READ_PHONE_STATE));
            Log.i(TAG, "7 웨이크 락: " + checkSelfPermission(Manifest.permission.WAKE_LOCK));
            Log.i(TAG, "8 파일로케이션: " + checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
            Log.i(TAG, "9 SMS: " + checkSelfPermission(Manifest.permission.READ_SMS));
            */

            checkPermission();
        }

        imageBtnBall    = (ImageButton) findViewById(R.id.ib_appbar_ball);
        ib_appbar_coach = (ImageButton) findViewById(R.id.ib_appbar_coach);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //만약 앱을 연 상태에서 설정을 했다면 다시 불러오기
        if(user.getUseremail() != null){
            navHeaderUserName = (TextView) header.findViewById(R.id.nav_header_username);
            navHeaderUserEmail = (TextView) header.findViewById(R.id.nav_header_useremail);
            navHeaderImage  = (ImageView) header.findViewById(R.id.nav_header_image);
            navHeaderUserName.setText(user.getUsername());
            navHeaderUserEmail.setText(user.getUseremail());
        }

        //강사정보 셋팅
        getInstructorInfo();

        //팀아이디 구하기
        getTeamid(user.getUid());
    }

    public void getTeamid(int uid){
        //유저가 강사 팀에 소속되어 있는지 체크
        WaitingDialog.showWaitingDialog(MomMainActivity.this,false);
        TeamService teamService = ServiceGenerator.createService(TeamService.class,getApplicationContext(),user);
        TeamMember query = new TeamMember();
        query.setUid(user.getUid());
        Call<TeamMember> c = teamService.getTeamMemeber(query);

        c.enqueue(new Callback<TeamMember>() {
            @Override
            public void onResponse(Call<TeamMember> call, Response<TeamMember> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    TeamMember member = response.body();
                    if(member.getTeamid()==0){
                        Common.teamid = 0;
                    }else{
                        Common.teamid = member.getTeamid();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeamMember> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
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
            //overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        }else if(id==R.id.mn_item_teammemberlist){
            Intent intent = new Intent(this,StudentMainActivity.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else if(id == R.id.mn_item_favorite){
            Intent intent = new Intent(this,FavoriteMissionActivity.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else if(id == R.id.mn_item_gansim){
            Intent intent = new Intent(getApplicationContext(),MyBookMarkActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        }else if(id == R.id.mn_item_search){

            Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
            intent.putExtra("goPage",0);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택에 쌓이는 것을 방지 한다. 필수
            startActivity(intent);
            //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        }else if(id == R.id.mn_item_mypage){

            Intent intent = new Intent(getApplicationContext(),MyPageActivity.class);
            intent.putExtra("pageflag","me");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else if(id == R.id.mn_item_coachreq){
            Intent intent = new Intent(this,ApplyCoachActivity.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else if(id == R.id.mn_item_setup){
            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        }else if(id == R.id.mn_item_logout){

            new MaterialDialog.Builder(this)
                    .title(R.string.mom_diaalog_title_logout)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(R.string.mom_diaalog_content_logout)
                    .positiveText(R.string.mom_diaalog_confirm)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            logOut();
                        }
                    })
                    .negativeText(R.string.mom_diaalog_cancel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .backgroundColor(getResources().getColor(R.color.mom_color1))
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut(){

        if(user.getSnstype().equals("facebook")){


            //페이스북 토큰 및 세션 끈기
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

        }else if(user.getSnstype().equals("kakao")){

            onClickLogout();
        }else if(user.getSnstype().equals("app")){

        }
        prefUtil.clearPrefereance();

        //강사정보 지우기
        SharedPreferences pref = getApplicationContext().getSharedPreferences("insinfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();


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


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        // Should we show an explanation?
            /*if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "외부 스토리지 파일 권한사용체크" + "", Toast.LENGTH_SHORT).show();
            }*/
            /*
                    Log.i(TAG, "1 : " + checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                    Log.i(TAG, "2 : " + checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
                    Log.i(TAG, "3 : " + checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE));
                    Log.i(TAG, "4 : " + checkSelfPermission(Manifest.permission.GET_ACCOUNTS));
                    Log.i(TAG, "5 : " + checkSelfPermission(Manifest.permission.INTERNET));
                    Log.i(TAG, "6 : " + checkSelfPermission(Manifest.permission.READ_PHONE_STATE));
                    Log.i(TAG, "7 : " + checkSelfPermission(Manifest.permission.WAKE_LOCK));
                    Log.i(TAG, "8 : " + checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
                    Log.i(TAG, "9 : " + checkSelfPermission(Manifest.permission.READ_SMS));

             */
        //1.핸드폰 사진저장 및 일기
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, MY_PERMISSION_REQUEST_STORAGE);
        }

        //2.구글계정사용
        if(checkSelfPermission(Manifest.permission.GET_ACCOUNTS)  != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{
                            Manifest.permission.GET_ACCOUNTS
                    }, MY_PERMISSION_REQUEST_STORAGE);
        }

        //3.전화번호부
/*        if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE)  != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE
                    }, MY_PERMISSION_REQUEST_STORAGE);
        }*/

        //4.폰상태
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, MY_PERMISSION_REQUEST_STORAGE);
        }

        //5.전화번호
        if(checkSelfPermission(Manifest.permission.READ_SMS)  != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{
                            Manifest.permission.READ_SMS
                    }, MY_PERMISSION_REQUEST_STORAGE);
        }


    }




    public void OnClickHeader(View v){
        switch (v.getId()){
            case R.id.mom_hd_mk:

                VeteranToast.makeToast(getApplicationContext(),"퍼미션",Toast.LENGTH_SHORT).show();


                break;
            case R.id.hd_bell:
                Intent intenti = new Intent(this, AlramActivity.class);
                startActivity(intenti);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                new MaterialDialog.Builder(this)
                        .titleColor(getResources().getColor(R.color.color6))
                        .title(R.string.mom_diaalog_title_append)
                        .content(R.string.app_end_action)
                        .positiveText(R.string.mom_diaalog_confirm)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .negativeText(R.string.mom_diaalog_cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        })
                        .backgroundColor(getResources().getColor(R.color.mom_color1))
                        .show();
                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }


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

            if(i==0){
                dots[i].setText(Html.fromHtml("&#8226;"));
            }else if(i==1){
                dots[i].setText(Html.fromHtml("&#8226;"));
            }else if(i==2){
                dots[i].setText(Html.fromHtml("&#8226;"));
            }
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
                UserRangkinVo query = new UserRangkinVo();
                query.setQueryRow(3);
                query.setOrderbytype("totalscore");

                WaitingDialog.showWaitingDialog(MomMainActivity.this,false);
                DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);
                final Call<List<UserRangkinVo>> call = dataService.getTotalRanking(query);
                call.enqueue(new Callback<List<UserRangkinVo>>() {

                    @Override
                    public void onResponse(Call<List<UserRangkinVo>> call, Response<List<UserRangkinVo>> response) {
                        WaitingDialog.cancelWaitingDialog();
                        if(response.isSuccessful()){
                            List<UserRangkinVo> userRangkinVos = new ArrayList<UserRangkinVo>();
                            userRangkinVos = response.body();
                            totalRecyclerView = (RecyclerView) findViewById(R.id.totalRecyclerView);
                            rankingItemAdapter = new RankingItemAdapter(activity,userRangkinVos,user);
                            totalRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                            totalRecyclerView.setHasFixedSize(true);
                            totalRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            totalRecyclerView.setAdapter(rankingItemAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserRangkinVo>> call, Throwable t) {
                        WaitingDialog.cancelWaitingDialog();
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
                WaitingDialog.showWaitingDialog(MomMainActivity.this,false);
                DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);

                //parameter
                UserRangkinVo query = new UserRangkinVo();
                query.setUid(user.getUid());
                query.setQueryRow(3);
                query.setOrderbytype("totalscore");

                Call<List<UserRangkinVo>> c = dataService.getTeamRanking(query);
                c.enqueue(new Callback<List<UserRangkinVo>>() {
                    @Override
                    public void onResponse(Call<List<UserRangkinVo>> call, Response<List<UserRangkinVo>> response) {
                        WaitingDialog.cancelWaitingDialog();
                        if(response.isSuccessful()){
                            List<UserRangkinVo> userRangkinVos = new ArrayList<UserRangkinVo>();
                            userRangkinVos = response.body();
                            teamRecyclerView = (RecyclerView) findViewById(R.id.teamRecyclerView);
                            rankingItemAdapter = new RankingItemAdapter(activity,userRangkinVos,user);

                            teamRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                            teamRecyclerView.setHasFixedSize(true);
                            teamRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            teamRecyclerView.setAdapter(rankingItemAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserRangkinVo>> call, Throwable t) {
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

                WaitingDialog.showWaitingDialog(MomMainActivity.this,false);
                DataService dataService = ServiceGenerator.createService(DataService.class,getApplicationContext(),user);
                //parameter
                UserRangkinVo query = new UserRangkinVo();
                query.setUid(user.getUid());
                query.setQueryRow(3);
                query.setOrderbytype("totalscore");

                Call<List<UserRangkinVo>> c = dataService.getFriendRanking(query);
                c.enqueue(new Callback<List<UserRangkinVo>>() {
                    @Override
                    public void onResponse(Call<List<UserRangkinVo>> call, Response<List<UserRangkinVo>> response) {
                        WaitingDialog.cancelWaitingDialog();
                        if(response.isSuccessful()){
                            List<UserRangkinVo> userRangkinVos = new ArrayList<UserRangkinVo>();
                            userRangkinVos = response.body();
                            friendRecyclerView = (RecyclerView) findViewById(R.id.friendRecyclerView);
                            rankingItemAdapter = new RankingItemAdapter(activity,userRangkinVos,user);

                            friendRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                            friendRecyclerView.setHasFixedSize(true);
                            friendRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            friendRecyclerView.setAdapter(rankingItemAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserRangkinVo>> call, Throwable t) {
                        WaitingDialog.cancelWaitingDialog();
                        t.printStackTrace();
                    }
                });

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

        if(!Compare.isEmpty(RealFilePath)){
            navHeaderImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            navHeaderImage.setImageBitmap(photo);
        }
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

                    if(instructor.getInstructorid()==0){
                        ib_appbar_coach.setVisibility(View.GONE);
                        imageBtnBall.setVisibility(View.VISIBLE);
                    }else{
                        ib_appbar_coach.setVisibility(View.VISIBLE);
                        imageBtnBall.setVisibility(View.GONE);
                    }

                    if(instructor.getUid()==0){
                        navigationView.getMenu().findItem(R.id.mn_item_coachreq).setTitle(getString(R.string.mom_menu_title_coach_apply));
                        navigationView.getMenu().findItem(R.id.mn_item_teammemberlist).setVisible(false);

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("insinfo", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putInt("ins_id",0);
                        editor.commit();

                    }else{
                        navigationView.getMenu().findItem(R.id.mn_item_coachreq).setTitle(getString(R.string.mom_menu_title_coach_info));
                        navigationView.getMenu().findItem(R.id.mn_item_teammemberlist).setVisible(false);

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("insinfo", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putInt("ins_id",instructor.getUid());
                        editor.commit();

                    }
                }
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }



    public void changeImage(){

        new MaterialDialog.Builder(MomMainActivity.this)
                .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(R.string.mom_diaalog_photo_title)
                .titleColor(getResources().getColor(R.color.color6))
                .content(R.string.mom_diaalog_photo_contnet)
                .contentColor(getResources().getColor(R.color.color6))
                .positiveText(R.string.mom_diaalog_photo_gallery)
                .neutralText(R.string.mom_diaalog_photo_camera)
                .negativeText(R.string.mom_diaalog_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakeAlbumAction();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doTakePhotoAction();
                    }
                })
                .show();
    }

    public void doTakeAlbumAction(){
        //앨범호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //카메라에서 사진촬영
    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String uri = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),uri));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent,PICK_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){
            case PICK_FROM_ALBUM:
                mImageCaptureUri = data.getData();
                Intent intenti = new Intent("com.android.camera.action.CROP");
                intenti.setDataAndType(mImageCaptureUri, "image/*");

                //크롭할 이미지를 100*100 크기로 저장한다
                intenti.putExtra("outputX",100);
                intenti.putExtra("outputY",100);
                intenti.putExtra("aspectX",100); //크롭 박스의 x축 비율
                intenti.putExtra("aspectY",100);
                intenti.putExtra("scale",true);
                intenti.putExtra("return-data", true);
                startActivityForResult(intenti,CROP_FROM_IMAGE);

                break;

            case PICK_FROM_CAMERA:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                //크롭할 이미지를 100*100 크기로 저장한다
                intent.putExtra("outputX",100);
                intent.putExtra("outputY",100);
                intent.putExtra("aspectX",100); //크롭 박스의 x축 비율
                intent.putExtra("aspectY",100);
                intent.putExtra("scale",true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent,CROP_FROM_IMAGE);
                break;

            case CROP_FROM_IMAGE:
                if(resultCode != RESULT_OK){
                    return;
                }
                final Bundle extras = data.getExtras();

                //크롭된 이미지를 저장하기 위한 file 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ Common.IMAGE_MOM_PATH+System.currentTimeMillis()+".jpg";

                RealFilePath = filePath;
                fileName    = System.currentTimeMillis()+".jpg";

                if(extras != null){
                    photo = extras.getParcelable("data");

                    storeCropImage(photo, filePath);
                    absoultePath = filePath;
                    break;
                }
                File file = new File(mImageCaptureUri.getPath());
                if(file.exists()){
                    file.delete();
                }
        }
    }

    private void storeCropImage(Bitmap bitmap,String filePath){

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+Common.IMAGE_MOM_PATH;
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);


            final File readFile = new File(RealFilePath);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(copyFile)));
            out.flush();
            out.close();

            //이미지 업로드
            navHeaderImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            navHeaderImage.setImageBitmap(bitmap);
            UserTRService userTRService = new UserTRService(this,user);
            userTRService.updateUserImage(String.valueOf(user.getUid()),fileName,RealFilePath,imgtype);

            //유저사진을 쉐어퍼런스에 저장해준다(업데이트)
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor pre = sp.edit();
            String profileimgurl = Common.SERVER_USER_IMGFILEADRESS + fileName;
            pre.putString("profileImgUrl", profileimgurl);
            pre.commit();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.im_batch)
    public void im_batch(){
        PubActivity pubActivity = new PubActivity(this,user,user.getUid());
        pubActivity.showDialog();
    }

}
