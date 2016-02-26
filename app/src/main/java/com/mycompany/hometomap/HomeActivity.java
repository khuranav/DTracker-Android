package com.mycompany.hometomap;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.location.Location;
import java.lang.Object;
import android.net.Uri;
import java.lang.String;
import android.app.Service;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.EditText;

import com.google.android.gms.location.places.ui.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;


public class HomeActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private static LocationListener locationListener;
    private static LocationManager locationManager;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Place placePicked;
    double placeLat;
    double placeLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                                       startActivity(settingsIntent);
                                   }
                               });
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //Bluetooth.ReconnectBluetooth(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Bluetooth.ReconnectBluetooth(this);
    }

    public void onStartClick(View view) {
        Intent ii = new Intent(this, EndRideActivity.class);
        startActivity(ii);
    }

    public void onBlueClick(View view) {
        Intent ff = new Intent(this, BlueActivity.class);
        startActivity(ff);
    }


    public void onDestClick(View view) {
        Intent gg = new Intent(this, DestinationActivity.class);
        startActivity(gg);
    }

}

    //BELOW IS THE CODE MOVED TO THE DESTACTIVITY CLASS
    /*
    public void onSearchClick(View view) {
        int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        final EditText setRad = (EditText) findViewById(R.id.setRad);
        int mileRad = Integer.parseInt(setRad.getText().toString());

        double latRad = mileRad / 68.70749821;
        double longRad = mileRad / (69.1710411 * Math.cos(mLastLocation.getLatitude()));
        longRad = Math.abs(longRad);

        LatLng southWest = new LatLng(mLastLocation.getLatitude() - latRad, mLastLocation.getLongitude() - longRad);
        LatLng northEast = new LatLng(mLastLocation.getLatitude() + latRad, mLastLocation.getLongitude() + longRad);

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setBoundsBias(new LatLngBounds(
                                    southWest,
                                    northEast))
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                placePicked = PlaceAutocomplete.getPlace(this, data);
                LatLng placePickedLatLng = placePicked.getLatLng();
                placeLat = placePickedLatLng.latitude;
                placeLong = placePickedLatLng.longitude;
                String toParse = "google.navigation:q=" + placeLat + "," + placeLong + "&mode=b";
                Uri gmmIntentUri = Uri.parse(toParse);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

*/
//    public void onPlaceClick (View view) {
//        Intent gg = new Intent(this, PlacePickerActivity.class);
//        startActivity(gg);
//    }


//    public void onLastClick (View view) {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//        double latrad2 = 5/68.70749821;
//        double longrad2 = 5/(69.1710411*Math.cos(mLastLocation.getLatitude()));
//        longrad2 = Math.abs(longrad2);
//        final TextView latView = (TextView) findViewById(R.id.latView);
//        final TextView longView = (TextView) findViewById(R.id.longView);
//        latView.setText(String.valueOf(latrad2));
//        longView.setText(String.valueOf(longrad2));
//
//
//
//
//    }


