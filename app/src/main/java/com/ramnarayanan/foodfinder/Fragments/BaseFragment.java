package com.ramnarayanan.foodfinder.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.ramnarayanan.foodfinder.Interfaces.IDataProvider;

/**
 * Created by Shadow on 4/5/2017.
 */

public class BaseFragment extends Fragment
        implements IDataProvider,
        GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
    }

    @Override
    public void dataReady() {
    }
    @Override
    public void requestInformation() {
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //handle failed connection
    }
}
