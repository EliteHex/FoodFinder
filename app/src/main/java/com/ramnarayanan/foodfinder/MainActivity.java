package com.ramnarayanan.foodfinder;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, android.location.LocationListener {

    private static final String TAG = "MainActivity";
//    private static final int ACCESS_FINE_LOCATION_INT = 10;
private static int REQUEST_CODE_AUTOCOMPLETE = 1;


    private TabAdapter mTabAdapter;
    private ViewPager mViewPager;
    private LocationManager locationManager;
    //String provider;

    //private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Disable toolbar
        //activateToolbar(false);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mTabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);

    }

//    private void activateToolbar(boolean enableHome){
//        Log.d(TAG, "activateToolbar: starts");
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar==null){
//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//            if(toolbar!=null){
//                setSupportActionBar(toolbar);
//                actionBar = getSupportActionBar();
//            }
//        }
//        if(actionBar!=null){
//            actionBar.setDisplayHomeAsUpEnabled(enableHome);
//        }
//    }

    //region Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle menu item clicks
        int id = item.getItemId();
        if(id == R.id.action_search)
        {
            //Create a new intent to open Search Activity
            //Intent intent = new Intent(this,SearchActivity.class);
            //startActivity(intent);

            //Use Google Places API to open the AutoComplete Activity
            openAutocompleteActivity();
            return true;
        }
        //Parent call
        return super.onOptionsItemSelected(item);
    }
    //endregion Menu

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                //mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                //        place.getId(), place.getAddress(), place.getPhoneNumber(),
                //        place.getWebsiteUri()));

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    //  mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    //  mPlaceAttribution.setText("");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }

    }

    //region Map
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        Double lat = location.getLatitude();
//        Double lng = location.getLongitude();
//
//        Log.d(TAG, "onLocationChanged: Latitude: " + lat.toString());
//        Log.d(TAG, "onLocationChanged: Longitude: " + lng.toString());
//        Log.i("Latitude: ", lat.toString());
//        Log.i("Longitude: ", lng.toString());
//
//        if (mMap != null) {
//            mMap.clear();
//            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Your location"));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
//        }
//
//        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//        try {
//            List<Address> listAdresses = geocoder.getFromLocation(lat, lng, 1);
//            if (listAdresses!=null && listAdresses.size()>0)
//            {
//                Log.i("PlaceInfo",listAdresses.get(0).toString());
//            }
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
    }
    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        //Satellite View
//        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//
//        // Add a marker in New York and move the camera
//        LatLng newyork = new LatLng(40.7128, -74.0059);
//        mMap.addMarker(new MarkerOptions().position(newyork).title("Marker in New York").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(newyork));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
//        try{
//            mMap.setMyLocationEnabled(true);
//        }
//        catch (SecurityException e){
//            e.printStackTrace();
//        }

    }
    //endregion Map

    @Override
    protected void onResume() {
        super.onResume();

//            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_INT);
//            }
//
//            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                if(locationManager!=null)
//                    locationManager.requestLocationUpdates(provider, 400, 1, this);
//            }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            if(locationManager!=null)
//                locationManager.removeUpdates(this);
//        }
//        else {
//            ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_INT);
//        }
    }
}
