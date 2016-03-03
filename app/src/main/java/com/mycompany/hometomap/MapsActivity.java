package com.mycompany.hometomap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.share.internal.ShareFeedContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
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
import com.facebook.FacebookSdk;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ShareButton shareButton;
    private ArrayList<LatLng> points;
    private Bundle bundle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bundle = getIntent().getExtras();
        points = bundle.getParcelableArrayList("point");
        Log.d("INFO", "Number of points: " + points.size());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String staticLink = createStaticLink();
        shareButton = (ShareButton) findViewById(R.id.shareButton);
        shareButton.setShareContent(new ShareFeedContent.Builder()

            .setLinkDescription("See my latest bike ride!").setLink(staticLink).build()
        );
    }

    private String createStaticLink()
    {
        String link = "https://maps.googleapis.com/maps/api/staticmap?size=800x800&path=";
        String key = getString(R.string.google_maps_key);
        ArrayList<LatLng> displayPoints = getPointsToDisplay(link, key);
        int i;
        for (i=0; i < displayPoints.size()-1; i++) {
            String location = String.valueOf(displayPoints.get(i).latitude) + "," + String.valueOf(displayPoints.get(i).longitude) + "|";
            link = link + location;
        }
        link = link + String.valueOf(displayPoints.get(i).latitude) + "," + String.valueOf(displayPoints.get(i).longitude) + "&";
        link = link + key;
        return link;
    }

    private ArrayList<LatLng> getPointsToDisplay(String link, String key)
    {
        final int MAX_URL_LEN = 2024;
        final int POINT_LEN = 25;
        int remainingLength = MAX_URL_LEN - link.length() - key.length();
        int maxPoints = remainingLength / 25;
        Log.d("INFO", "Maximum points" + maxPoints);
        if (maxPoints < points.size() || points.size() == 0)
            return points;

        int i = 0;
        int increment = (maxPoints / points.size() > 0 ? maxPoints / points.size() : 1);
        ArrayList<LatLng> result = new ArrayList<LatLng>();
        while (i < points.size()-1 && i < maxPoints-1)
        {
            result.add(points.get(i));
            i += increment;
        }
        result.add(points.get(points.size() - 1));
        return result;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Bluetooth.ReconnectBluetooth(this);
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
        //Bundle mapData = getIntent().getExtras();

        //ArrayList<LatLng> syd = mapData.getParcelableArrayList("point");
        if (points.size() > 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(points.size() - 1), 14));
            mMap.addMarker(new MarkerOptions().position(points.get(0)).title("Start"));
            mMap.addMarker(new MarkerOptions().position(points.get(points.size() - 1)).title("Finish"));
        }
        Polyline route = mMap.addPolyline(new PolylineOptions());
        route.setPoints(points);

        final TextView dateText2 = (TextView) findViewById(R.id.showDate2);
        final TextView speedText2 = (TextView) findViewById(R.id.showSpeed2);
        final TextView timeText2 = (TextView) findViewById(R.id.showTime2);
        final TextView distanceText2 = (TextView) findViewById(R.id.showDistance2);
        dateText2.setText("Run Date:  " + bundle.getString("date"));
        timeText2.setText("Duration: " + bundle.getLong("duration") + " seconds");
        distanceText2.setText("Distance: " + bundle.getFloat("distance") + " meters");
        speedText2.setText("Speed: " + bundle.getFloat("speed") + " meters/seconds");

    }
}
