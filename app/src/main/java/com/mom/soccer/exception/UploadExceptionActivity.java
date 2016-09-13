package com.mom.soccer.exception;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mom.soccer.R;

import butterknife.ButterKnife;

public class UploadExceptionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_upload_exception_layout);
        ButterKnife.bind(this);
    }

}
