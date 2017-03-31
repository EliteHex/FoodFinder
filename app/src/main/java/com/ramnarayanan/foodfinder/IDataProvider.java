package com.ramnarayanan.foodfinder;

/**
 * Created by Shadow on 3/31/2017.
 */

//Generic interface to be used in fragments
//Allows Activity to signal fragment
public interface IDataProvider {

    //data is ready for consumption by fragment
    void dataReady();

    //request data from fragment
    void requestInformation();
}

