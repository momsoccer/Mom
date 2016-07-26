package com.mom.soccer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by sungbo on 2016-07-26.
 */
public class MissionFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public MissionFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {


        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }



}
