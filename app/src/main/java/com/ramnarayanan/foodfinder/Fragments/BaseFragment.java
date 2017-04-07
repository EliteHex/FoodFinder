package com.ramnarayanan.foodfinder.Fragments;
import android.support.v4.app.Fragment;

import com.ramnarayanan.foodfinder.Interfaces.IDataProvider;

/**
 * Created by Shadow on 4/5/2017.
 */

public class BaseFragment extends Fragment
        implements IDataProvider {
    @Override
    public void dataReady() {
    }
    @Override
    public void requestInformation() {
    }
}
