package com.mom.soccer.momactivity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;
import com.mom.soccer.widget.VeteranToast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private static final String TAG = "Main_Activity";

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
        }
        return true;
    }

    @OnClick(R.id.btntest)
    public void test(){
        Log.d(TAG,"모지모지");
    }

    /*
    아이콘 관련 디테일하게 꾸미기
    http://stackoverflow.com/questions/31097716/how-to-style-the-design-support-librarys-navigationview
     */
}
