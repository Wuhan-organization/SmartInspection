package com.whut.smartinspection.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Fortuner on 2017/11/29.
 */

public class PageViewAdapter  extends FragmentPagerAdapter {
    private List<Fragment> list;

    public PageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    public PageViewAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int postion) {
        return list.get(postion);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
