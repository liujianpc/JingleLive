package com.tekinarslan.material.sample;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by liujian on 2017/4/1.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<View> viewList = null;
    private List<String> titleList = null;

    MyPagerAdapter(List<View> viewList, List<String> titleList) {
        // TODO Auto-generated constructor stub
        this.viewList = viewList;
        this.titleList = titleList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return viewList.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        container.removeView(viewList.get(position));

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return titleList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == object;
    }
}
