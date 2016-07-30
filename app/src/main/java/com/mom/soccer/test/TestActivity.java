package com.mom.soccer.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mom.soccer.R;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_layout);
        Log.d(TAG, "onCreate =========================================================== ***");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState ===========================================================");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume ===========================================================*");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ===========================================================");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop ===========================================================");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy ===========================================================");
    }
}
