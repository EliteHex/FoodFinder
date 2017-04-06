package com.ramnarayanan.foodfinder;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Shadow on 3/6/2017.
 */

class PlacesJSONParserTask extends AsyncTask<String, Integer, List<MapPlace>> {
    JSONObject jObject;

    @Override
    protected List<MapPlace> doInBackground(String... jsonData) {
        List<MapPlace> places = null;
        PlacesJSONParser placeJSON = new PlacesJSONParser();
        try {
            jObject = new JSONObject(jsonData[0]);
            places = placeJSON.parse(jObject);
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: ", e);
        }
        return places;
    }
}
