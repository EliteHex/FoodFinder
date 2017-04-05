package com.ramnarayanan.foodfinder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * Created by Shadow on 3/2/2017.
 */

public class MapFragment extends BaseFragment
        implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        IDataProvider
        //MainActivity.OnRefreshClicked
        //GetPlacesJSONData.IJSONDataAvailable
{

    public static String TAB_NAME = "Map";
    //map data variables
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    //provider
    private String provider;
    //static strings
    private static final int ACCESS_FINE_LOCATION_INT = 10;
    private static final int PLACE_PICKER_REQUEST = 20;
    private static final String ARG_SECTION_NUMBER = "map_section";
    //interface
    IMapDataRequested mMapDataRequestedCallback;

    public static MapFragment newInstance(int sectionNumber) {
        Log.d(TAG, "newInstance: created");
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        //set up map fragment with the fragment manager
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment mapFragment = new SupportMapFragment();
        transaction.add(R.id.map_fragment, mapFragment);
        transaction.commit();

        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                } else {
                    // permission denied
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //confirm Activity has implemented callback interface
        try {
            mMapDataRequestedCallback = (IMapDataRequested) context;
            ((MainActivity) context).setListener(this, "map");
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement IMapDataRequested");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_INT);
        }

        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null)
                locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null)
                locationManager.removeUpdates(this);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_INT);
        }
    }
    //endregion

    //region Map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnCameraMoveStartedListener(this);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraMoveCanceledListener(this);

        PermissionsManager.checkPermissions(getContext(), ACCESS_FINE_LOCATION);
//
//        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_INT);
//        }
//
        Location location = null;
        if (PermissionsManager.checkPermissions(getContext(), ACCESS_FINE_LOCATION)) {
            try {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    provider = locationManager.getBestProvider(new Criteria(), false);
                    location = locationManager.getLastKnownLocation(provider);
                    if (location != null) {
                        Log.d(TAG, "onCreate: Location info: Location achieved.");
                        Log.i("Location info: ", "Location achieved.");
                        onLocationChanged(location);
                    } else {
                        Log.d(TAG, "onCreate: Location info: No location.");
                        Log.i("Location info: ", "No location.");
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        //Satellite View
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker in New York and move the camera
        LatLng startLatLng;
        if (location != null) {
            startLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            startLatLng = new LatLng(40.7128, -74.0059);
        }

        //mGoogleMap.addMarker(new MarkerOptions().position(newyork).title("Marker in New York").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        try {
            mGoogleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                try {
                    Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
                    onLocationChanged(location);
                } catch (SecurityException e) {
                    Log.e(TAG, "onMyLocationButtonClick: ", new SecurityException(e.getMessage().toString()));
                    e.printStackTrace();
                }
                return true;
            }
        });
//        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                Intent intent;
//                try {
//                    intent = builder.build(getActivity());
//                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        //retrieve places data from the Web API
        ///
        /// mMapDataRequestedCallback.onMapDataRequested(mGoogleMap);
    }
    //end region

    //endregion

    //region Connection Failed Listener
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //handle failed connection here
    }
    //endregion

    //region Location Listener
    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        Log.d(TAG, "onLocationChanged: Latitude: " + lat.toString());
        Log.d(TAG, "onLocationChanged: Longitude: " + lng.toString());
        Log.i("Latitude: ", lat.toString());
        Log.i("Longitude: ", lng.toString());

        if (mGoogleMap != null) {
            //mGoogleMap.clear();
            //mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Your location"));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
        }

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> listAdresses = geocoder.getFromLocation(lat, lng, 1);
            if (listAdresses != null && listAdresses.size() > 0) {
                Log.i("PlaceInfo", listAdresses.get(0).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    //endregion

    //region Camera Listeners
    @Override
    public void onCameraIdle() {
        Toast.makeText(getContext(), "Camera has stopped moving.", Toast.LENGTH_SHORT).show();
        LatLng currentLatLng = mGoogleMap.getCameraPosition().target;
        Log.d(TAG, "onCameraIdle: LatLng " + currentLatLng);
    }

    @Override
    public void onCameraMoveCanceled() {
    }

    @Override
    public void onCameraMove() {
    }
    @Override
    public void onCameraMoveStarted(int i) {
    }
    //endregion

    //region Data
    public interface IMapDataRequested {
        void onMapDataRequested(GoogleMap googleMap);
    }

    @Override
    public void dataReady() {
        Log.d(TAG, "dataReady: dataready callback invoked");
        loadNewData(((MainActivity) getContext()).returnData());
    }

    public void requestInformation() {
        Log.d(TAG, "requestInformation: requesting map information");
        mMapDataRequestedCallback.onMapDataRequested(mGoogleMap);
    }

    //@Override
//    public void onDataAvailable(List<HashMap<String, String>> data, DownloadStatus status) {
//        loadNewData(data);
//    }

    private void loadNewData(List<HashMap<String, String>> dataList) {
        if (dataList.isEmpty()) return;

        Log.d("Map", "list size: " + dataList.size());
        final List<HashMap<String, String>> places = dataList;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Clears all the existing markers;
                mGoogleMap.clear();

                for (int i = 0; i < places.size(); i++) {

                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = places.get(i);
                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));
                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));
                    // Getting name
                    String name = hmPlace.get("place_name");
                    Log.d("Map", "place: " + name);

                    // Getting vicinity
                    String vicinity = hmPlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    // Setting the position for the marker
                    markerOptions.position(latLng);
                    markerOptions.title(name + " : " + vicinity);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_black_18dp));
                    // Placing a marker on the touched position

                    Marker m = mGoogleMap.addMarker(markerOptions);
                }
            }
        });
    }

    //endregion
}
