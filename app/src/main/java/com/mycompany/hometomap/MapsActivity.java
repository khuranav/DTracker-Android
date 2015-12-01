package com.mycompany.hometomap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.content.Intent;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        Bundle mapData = getIntent().getExtras();

        ArrayList<LatLng> syd = mapData.getParcelableArrayList("point");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(syd.get(syd.size() - 1), 10));
        mMap.addMarker(new MarkerOptions().position(syd.get(0)).title("Start"));
        mMap.addMarker(new MarkerOptions().position(syd.get(syd.size() - 1)).title("Finish"));
        Polyline route = mMap.addPolyline(new PolylineOptions());
        route.setPoints(syd);

        final TextView speedText2 = (TextView) findViewById(R.id.showSpeed2);
        final TextView timeText2 = (TextView) findViewById(R.id.showTime2);
        final TextView distanceText2 = (TextView) findViewById(R.id.showDistance2);
        timeText2.setText("" + mapData.getLong("duration") + " seconds");
        distanceText2.setText("" + mapData.getFloat("distance") + " meters");
        speedText2.setText("" + mapData.getFloat("speed") + " meters/seconds");

    }
}
