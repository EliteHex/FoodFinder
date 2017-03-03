package com.ramnarayanan.foodfinder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Shadow on 3/2/2017.
 */

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MapFragment();
            case 1:
                return PlaceholderFragment.newInstance(position + 1);
            case 2:
                return PlaceholderFragment.newInstance(position + 2);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

