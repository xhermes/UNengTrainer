package me.xeno.unengtrainer.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xeno on 2017/5/16.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final String[] TABS = {"控制器", "快捷方式"};

    public static final int PAGE_MAIN_CONTROL = 0;
    public static final int PAGE_SHORTCUT = 1;

    private List<Fragment> mFragmentList;

    public MainPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }
}
