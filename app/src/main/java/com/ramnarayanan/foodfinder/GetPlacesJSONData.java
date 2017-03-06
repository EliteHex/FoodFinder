package com.ramnarayanan.foodfinder;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shadow on 3/6/2017.
 */

class GetPlacesJSONData extends AsyncTask<String, Integer, String> implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetPlacesJSONData";
    private String data = null;
    private final OnDataAvailable mCallBack;
    private List<HashMap<String, String>> mPlaces;

    public GetPlacesJSONData(OnDataAvailable callBack) {
        mCallBack = callBack;
    }

    interface OnDataAvailable {
        void onDataAvailable(List<HashMap<String, String>> data, DownloadStatus status);
    }

    private String createURL() {
        double mLatitude = 40.7128;
        double mLongitude = -74.0059;

        StringBuilder urlBuild = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        urlBuild.append("location=" + mLatitude + "," + mLongitude)
                .append("&radius=5000")
                .append("&types=" + "restaurant")
                .append("&sensor=true")
                .append("&key=" + "AIzaSyDSqwO8QnOMqty5laLxP6tEnzZ9P70tBDk");
        //a797e66836c20f9a7d3da4f2edca204587d7b979
        //AIzaSyDSqwO8QnOMqty5laLxP6tEnzZ9P70tBDk

        Log.d(TAG, "createURL: " + urlBuild);
        return urlBuild.toString();
    }

    //Invoke using execute()
    @Override
    protected String doInBackground(String... params) {
        String destinationURL = createURL();
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationURL);
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(s);
        //PlacesJSONParser parserTask = new PlacesJSONParser();
        //parserTask.execute(result)
        Log.d(TAG, "onPostExecute: starts");
        if (mCallBack != null) {
            mCallBack.onDataAvailable(mPlaces, DownloadStatus.OK);
        }
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        //List<HashMap<String, String>> places = null;
        PlacesJSONParser placeJson = new PlacesJSONParser();
        JSONObject jObject;
        if (status == DownloadStatus.OK) {
            mPlaces = new ArrayList<>();
            try {
                jObject = new JSONObject(data);
                mPlaces = placeJson.parse(jObject);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            mCallBack.onDataAvailable(mPlaces, DownloadStatus.OK);
            //return places;
        }
    }
}