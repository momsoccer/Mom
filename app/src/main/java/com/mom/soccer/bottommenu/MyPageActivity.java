package com.mom.soccer.bottommenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.BlurTransformation;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyPageActivity extends AppCompatActivity {

    private static final String TAG = "MyPageActivity";

    private User user;
    private PrefUtil prefUtil;

    @Bind(R.id.mypage_image_user_image)
    ImageView mypageImage;

    @Bind(R.id.mypage_back_image)
    ImageView mypageBackImage;

    //바인드 필요

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bottom_mypage_layout);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("마이페이지");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.mom_hd_mk);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        if(!Compare.isEmpty(user.getProfileimgurl())) {
            Glide.with(MyPageActivity.this)
                    .load(user.getProfileimgurl())
                    .into(mypageImage);


            //리니어 레이아웃에 블러드 효과 주기
            Glide.with(MyPageActivity.this)
                    .load(user.getProfileimgurl())
                    .asBitmap().transform(new BlurTransformation(this, 25))
                    .into(mypageBackImage);


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
