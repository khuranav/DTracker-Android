package com.mycompany.hometomap;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import com.google.android.gms.maps.model.LatLng;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class EndRideActivity extends AppCompatActivity {

    ArrayList<LatLng> routePoints = new ArrayList<LatLng>();
    ArrayList<Location> routeLocation = new ArrayList<Location>();
    List<Float> speedPoints = new ArrayList<Float>();
    List<Long> timePoints = new ArrayList<Long>();

    public float avSpeed;
    public int totalSpeed = 0;
    public float totalDistance = 0;
    public long duration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final TextView dateText = (TextView) findViewById(R.id.dateText);
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yy hh:mm");
        String format = s.format(new Date());
        dateText.setText(format);

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng trick = new LatLng(location.getLatitude(), location.getLongitude());
                routePoints.add(trick);
                routeLocation.add(location);
//                speedPoints.add(location.getSpeed());
                timePoints.add(location.getElapsedRealtimeNanos());
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
        };
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listener);

    }

    public void onShowClick(View view){
//        final TextView speedText = (TextView) findViewById(R.id.showSpeed);
//        final TextView timeText = (TextView) findViewById(R.id.showTime);
//        final TextView distanceText = (TextView) findViewById(R.id.showDistance);


//        for (int x = 0; x < speedPoints.size(); x++) {
//            totalSpeed = totalSpeed + Math.round(speedPoints.get(x));se
//        }

//        float[] result = new float[3];
//        for (int x = 0; x < routeLocation.size()-1; x++) {
//            Location.distanceBetween(routeLocation.get(x).getLatitude(), routeLocation.get(x).getLongitude(),
//                    routeLocation.get(x+1).getLatitude(), routeLocation.get(x+1).getLongitude(), result);
//            totalDistance = totalDistance + result[0];
//        }
//
//
////        avSpeed = (totalSpeed)/(timePoints.size());
//        duration = timePoints.get(timePoints.size()-1) - timePoints.get(0);
//        duration = duration/1000000000;
//        avSpeed = totalDistance/duration;
//        speedText.setText("" + avSpeed + " meters/seconds");
//        timeText.setText("" + duration + " seconds");
//        distanceText.setText("" + totalDistance + " meters");
    }

    public void onEndClick(View view){
//        LatLng sydney = new LatLng(37.3, -122);
//        routePoints.add(sydney);
        Intent i = new Intent(this, MapsActivity.class);
        i.putParcelableArrayListExtra("point", routePoints);

//        Location syd = new Location("dummyProvider");
//        syd.setLatitude(37.3);
//        syd.setLongitude(-122);
//        routeLocation.add(syd);

        float[] result = new float[3];
        for (int x = 0; x < routeLocation.size()-1; x++) {
            Location.distanceBetween(routeLocation.get(x).getLatitude(), routeLocation.get(x).getLongitude(),
                    routeLocation.get(x+1).getLatitude(), routeLocation.get(x+1).getLongitude(), result);
            totalDistance = totalDistance + result[0];
        }

        duration = timePoints.get(timePoints.size()-1) - timePoints.get(0);
        duration = duration/1000000000;
        avSpeed = totalDistance/duration;


        i.putExtra("duration", duration);
        i.putExtra("distance", totalDistance);
        i.putExtra("speed", avSpeed);
        startActivity(i);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
