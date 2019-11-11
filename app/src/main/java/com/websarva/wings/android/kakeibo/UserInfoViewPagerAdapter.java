package com.websarva.wings.android.kakeibo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.websarva.wings.android.kakeibo.BlankFragment;
import com.websarva.wings.android.kakeibo.calendarFragment;

/**
 * Created by naoi on 2017/04/24.
 */

public class UserInfoViewPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_NUM = 3;

    public UserInfoViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new BlankFragment();
                break;
            case 1:
                fragment = new calendarFragment();
                break;
            case 2:
                fragment = new PieChart();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
}