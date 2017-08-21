package com.ccl.ccltools.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private String[] mTabNames = new String[]{"歌单", "我的"};

    public MainViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        if(mFragments != null){
            return mFragments.size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNames[position];
    }
}
