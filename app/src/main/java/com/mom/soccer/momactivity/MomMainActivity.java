package com.mom.soccer.momactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.mom.soccer.MainActivity;
import com.mom.soccer.R;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;
import com.mom.soccer.widget.DialogBuilder;
import com.mom.soccer.widget.VeteranToast;

import butterknife.ButterKnife;

public class MomMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private static final String TAG = "MomMainActivity";

    public static NavigationView navigationView;

    private User user;
    private PrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main_layout);
        ButterKnife.bind(this);

        Log.d(TAG, "onCreate ===========================================================");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Log.d(TAG,"로그인 유저 정보 확인" + user.toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.mom_hd_mk);
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
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();
        }else if(id == R.id.mn_item_fe){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();

        }else if(id == R.id.mn_item_gansim){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();

        }else if(id == R.id.mn_item_search){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();

        }else if(id == R.id.mn_item_mypage){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();

        }else if(id == R.id.mn_item_coachreq){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();

        }else if(id == R.id.mn_item_mypage){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();

        }else if(id == R.id.mn_item_coachreq){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();

        }else if(id == R.id.mn_item_setup){

            Intent intent = new Intent(this,SetupActivity.class);
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

        VeteranToast.makeToast(getApplicationContext(),getString(R.string.app_logout), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
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
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();
                break;
            case R.id.hd_bell:
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.preparation),Toast.LENGTH_SHORT).show();
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
}