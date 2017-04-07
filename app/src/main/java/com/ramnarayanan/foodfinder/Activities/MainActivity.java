package com.ramnarayanan.foodfinder.Activities;

import android.content.Intent;
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
import com.google.android.gms.maps.model.LatLng;
import com.ramnarayanan.foodfinder.Data.DownloadStatus;
import com.ramnarayanan.foodfinder.Data.GetPlacesJSONData;
import com.ramnarayanan.foodfinder.Data.IDataProvider;
import com.ramnarayanan.foodfinder.Data.MapPlace;
import com.ramnarayanan.foodfinder.Fragments.MapFragment;
import com.ramnarayanan.foodfinder.R;
import com.ramnarayanan.foodfinder.Adapters.TabAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MapFragment.IMapDataRequested,
        GetPlacesJSONData.IJSONDataAvailable {

    private static final String TAG = "MainActivity";
    //  private static final int ACCESS_FINE_LOCATION_INT = 10;
    private static int REQUEST_CODE_AUTOCOMPLETE = 1;

    private TabAdapter mTabAdapter;
    private ViewPager mViewPager;
    private LocationManager locationManager;
    private List<MapPlace> mapData;
    private IDataProvider dataProviderMapListener;
    private IDataProvider dataProviderListListener;

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

        //initialize data to an empty list
        mapData = new ArrayList<>();
    }
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
        switch (id) {
            case R.id.action_search:
                //Create a new intent to open Search Activity
                //Intent intent = new Intent(this,SearchActivity.class);
                //startActivity(intent);

                //Use Google Places API to open the AutoComplete Activity
                openAutocompleteActivity();
                return true;
            case R.id.action_refresh:
                dataProviderMapListener.requestInformation();
                //android.support.v4.app.Fragment currentMap = getSupportFragmentManager().findFragmentById(R.id.map_fragment);
                //if (currentMap != null) {
                    //OnRefreshClicked();
                    //currentMap.
                //}
                return true;
            default:
                //Parent call
                return super.onOptionsItemSelected(item);
        }
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

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

    @Override
    //callback from MapFragment
    public void onMapDataRequested(GoogleMap googleMap) {
        getNewData(googleMap);
    }

    @Override
    //callback from GetPlacesJSONData
    public void onDataAvailable(List<MapPlace> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK) {
            //loadNewData(data);
        } else {
            Log.e(TAG, "onDataAvailable: failed with status " + status);
        }
        Log.d(TAG, "onDataAvailable: ends");

        mapData = data;
        if (dataProviderMapListener != null) {
            dataProviderMapListener.dataReady();
        }
        if (dataProviderListListener != null) {
            dataProviderListListener.dataReady();
        }

    }

    //set listeners to be used by fragments
    public void setListener(IDataProvider listener, String fragmentname) {
        switch (fragmentname) {
            case "map":
                this.dataProviderMapListener = listener;
                break;
            case "list":
                this.dataProviderListListener = listener;
                break;
        }
    }

    //query Google REST API using map location data
    public void getNewData(GoogleMap googleMap) {
        //use map center location to request data
        LatLng currentLatLng = googleMap.getCameraPosition().target;
        String latitude = String.valueOf(currentLatLng.latitude);
        String longitude = String.valueOf(currentLatLng.longitude);

        GetPlacesJSONData getPlacesJSONData = new GetPlacesJSONData(this);
        getPlacesJSONData.execute(new String[]{"places", latitude, longitude});
    }

    public List<MapPlace> returnData() {
        //exposes access to mapData
        return mapData;
    }
}
