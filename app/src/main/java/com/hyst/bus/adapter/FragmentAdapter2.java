package com.hyst.bus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class FragmentAdapter2 extends FragmentStatePagerAdapter {
    public FragmentManager fm;
    public List<Fragment> list;
    private List<String> titles;

    public FragmentAdapter2(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fm = fm;
        this.list = list;
    }

    /**
     * TabLayout使用
     *
     * @param titles
     */
    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

//    @Override
//    public Fragment instantiateItem(ViewGroup container, int position) {
//        Fragment fragment = (Fragment) super.instantiateItem(container, position);
//        fm.beginTransaction().show(fragment).commit();
//        return fragment;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        Fragment fragment = list.get(position);
//        fm.beginTransaction().hide(fragment).commit();
//    }
}