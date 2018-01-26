package com.hyst.bus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

public class FragmentAdapter2 extends FragmentStatePagerAdapter {
    public FragmentManager fm;
    public List<Fragment> list;
    private List<String> titles;

    public FragmentAdapter2(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fm = fm;
        this.list = list;
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            transaction.add(list.get(i),list.get(i).getTag());
        }
        transaction.commit();
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

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        if(fragment.isAdded()){
            fm.beginTransaction().show(fragment).commitNow();
        }else {
//            fm.beginTransaction().add(container.getId(),fragment).commit();
        }
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container,position,object);
        Fragment fragment = list.get(position);
        fm.beginTransaction().hide(fragment).commit();
    }
}