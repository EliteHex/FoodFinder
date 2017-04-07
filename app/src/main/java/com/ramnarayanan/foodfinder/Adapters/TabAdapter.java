package com.ramnarayanan.foodfinder.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ramnarayanan.foodfinder.Fragments.MapFragment;
import com.ramnarayanan.foodfinder.Fragments.RecyclerViewFragment;

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
                return MapFragment.newInstance(position);
            case 1:
                return RecyclerViewFragment.newInstance(position);
            //case 2:
            //    return PlaceholderFragment.newInstance(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MAP";
            case 1:
                return "LIST";
        }
        return null;
    }
}

