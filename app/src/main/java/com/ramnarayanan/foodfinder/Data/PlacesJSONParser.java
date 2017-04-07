package com.ramnarayanan.foodfinder.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shadow on 3/6/2017.
 */

public class PlacesJSONParser {
    private static final String TAG = "PlacesJSONParser";

    /***
     * Receives a JSON Object and returns a list
     */
    public List<MapPlace> parse(JSONObject jObject) {
        //List<HashMap<String, String>>
        JSONArray jPlaces = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jPlaces);
    }

    private List<MapPlace> getPlaces(JSONArray jPlaces) {
        int placesCount = jPlaces.length();
        //List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        //HashMap<String, String> place = null;
        List<MapPlace> placesList = new ArrayList<>();
        MapPlace place;

        /** Taking each place, parses and adds to list object */
        for (int i = 0; i < placesCount; i++) {
            try {
                /** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject) jPlaces.get(i));
                if (place != null) placesList.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    /**
     * Parsing the Place JSON object
     */
    private MapPlace getPlace(JSONObject jPlace) {

        //HashMap<String, String> place = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";

        MapPlace mapPlace = new MapPlace();

        try {
            // Extracting Place name, if available
            if (!jPlace.isNull("name")) {
                placeName = jPlace.getString("name");
            }

            // Extracting Place Vicinity, if available
            if (!jPlace.isNull("vicinity")) {
                vicinity = jPlace.getString("vicinity");
            }

            String latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
            String reference = jPlace.getString("reference");
            String placeid = jPlace.getString("place_id");
            String icon = jPlace.getString("icon");
            String photoreference = "";
            if (jPlace.has("photos")) {
                photoreference = jPlace.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            }
            
            mapPlace.placeName = placeName;
            mapPlace.vicinity = vicinity;
            mapPlace.latitude = latitude;
            mapPlace.longitude = longitude;
            mapPlace.reference = reference;
            mapPlace.placeid = placeid;
            mapPlace.icon = icon;
            mapPlace.photoreference = photoreference;

            //place.put("place_name", placeName);
            //place.put("vicinity", vicinity);
            //place.put("lat", latitude);
            //place.put("lng", longitude);
            //place.put("reference", reference);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mapPlace;
    }

}

