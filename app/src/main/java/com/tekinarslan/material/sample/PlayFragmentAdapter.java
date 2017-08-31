package com.tekinarslan.material.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by liujian on 2017/4/7.
 */

public class PlayFragmentAdapter extends FragmentStatePagerAdapter {
    private String[] titles;
    private List<PlayerFragment> fragmentList;
    public PlayFragmentAdapter(FragmentManager fm, String[] titles, List<PlayerFragment> fragmentList) {
        super(fm);
        this.titles = titles;
        this.fragmentList = fragmentList;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }




    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public String getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
