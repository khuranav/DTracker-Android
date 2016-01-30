package com.mycompany.hometomap;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.UUID;
import java.util.jar.Manifest;
import java.util.jar.Pack200;

public class EndRideActivity extends AppCompatActivity {

    ArrayList<LatLng> routePoints = new ArrayList<LatLng>();
    ArrayList<Location> routeLocation = new ArrayList<Location>();
    List<Float> speedPoints = new ArrayList<Float>();
    List<Long> timePoints = new ArrayList<Long>();

    public float avSpeed;
    public int totalSpeed = 0;
    public float totalDistance = 0;
    public long duration = 0;

    private GoogleApiClient client;

    /* Bluetooth Stuff */
    private static BluetoothAdapter btAdapter;
    private static BluetoothDevice btDevice;
    private static BluetoothSocket btSocket;
    private static OutputStream btOutput;
    private static InputStream btInput;
    private static final UUID SSP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final String btDeviceAddress = "98:4F:EE:04:A1:E1";
    private final static int REQUEST_ENABLE_BT = 1;

    private static LocationListener locationListener;
    private static LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertBox("ERROR", "in onCreate");
        //btAdapter = BluetoothAdapter.getDefaultAdapter();
        //reconnectBluetooth();

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

        setupLocationUpdates();

    }

    protected void onResume(Bundle savedInstanceState) {
        super.onResume();

        AlertBox("ERROR", "in onResume");

        //reconnectBluetooth();
        setupLocationUpdates();


        //if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        //        checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        //manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listener);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void setupLocationUpdates() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                LatLng trick = new LatLng(location.getLatitude(), location.getLongitude());
                routePoints.add(trick);
                routeLocation.add(location);
//                speedPoints.add(location.getSpeed());
                timePoints.add(location.getElapsedRealtimeNanos());
                sendLocationOverBluetooth(trick);
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

        /* Permissions Check */
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10000,
                0,
                locationListener
        );
    }

//    protected void reconnectBluetooth() {
//        checkBluetoothAdapter();
//
//        btDevice = btAdapter.getRemoteDevice(btDeviceAddress);
//        try {
//            btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(SSP_UUID);
//        } catch (IOException e) {
//            AlertBox("ERROR", "Failed to create bluetooth socket:" + e.getMessage());
//        }
//
//        btAdapter.cancelDiscovery();
//
//        try {
//            btSocket.connect();
//        } catch (IOException e1) {
//            AlertBox("ERROR", "Failed to connect to device " + btDeviceAddress + " because: " + e1.getMessage());
//            try {
//                btSocket.close();
//            } catch (IOException e2) {
//                AlertBox("ERROR", "Failed to close socket after failing to connect to device: " + e1.getMessage());
//            }
//        }
//
//        try {
//            btOutput = btSocket.getOutputStream();
//        } catch (IOException e) {
//            AlertBox("ERROR", "Failed to get output stream from bluetooth: " + e.getMessage());
//        }
//
//        try {
//            btInput = btSocket.getInputStream();
//        } catch (IOException e) {
//            AlertBox("ERROR", "Failed to get inpput stream from bluetooth: " + e.getMessage());
//        }
//    }

//    protected void checkBluetoothAdapter() {
//        if (btAdapter == null) {
//            AlertBox("Error", "Bluetooth is not supported on your device");
//        } else {
//            if (btAdapter.isEnabled() == false) {
//                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            }
//        }
//    }

    protected void sendLocationOverBluetooth(LatLng location) {
        String message = Double.toString(location.latitude);
        message += ",";
        message += Double.toString(location.longitude);
        message += "\n\0";

        byte[] messageBuffer = message.getBytes();
        try {
            btOutput.write(messageBuffer);
        } catch (IOException e) {
            AlertBox("ERROR", "Failed to write " + message + " to output stream: " + e.getMessage());
        }

    }

    protected void endRideOverBluetooth() {
        String message = "END\n\0";

        if (btOutput == null)
            AlertBox("ERROR", "btOutput is null");
        else {
            byte[] messageBuffer = message.getBytes();
            try {
                btOutput.write(messageBuffer);
            } catch (IOException e) {
                AlertBox("ERROR", "Failed to write " + message + " to output stream: " + e.getMessage());
            }
        }

//        locationManager.removeUpdates(locationListener);
//        locationManager = null;
    }

    public void AlertBox(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message + " Press OK to exit.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish();
                        arg0.cancel();
                    }
                }).show();
    }

//    public void onShowClick(View view){
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
//    }

    public void onEndClick(View view) {
        //FAKE DATA 1
        LatLng sydney = new LatLng(34.063677, -118.445439);
        routePoints.add(sydney);
        Location syd = new Location("dummyProvider");
        syd.setLatitude(34.063677);
        syd.setLongitude(-118.445439);
        routeLocation.add(syd);

        //FAKE DATA 2
        LatLng sydney2 = new LatLng(34.063647, -118.448181);
        routePoints.add(sydney2);
        Location syd2 = new Location("dummyProvider");
        syd2.setLatitude(34.063647);
        syd2.setLongitude(-118.448181);
        routeLocation.add(syd2);

        //FAKE DATA 3
        LatLng sydney3 = new LatLng(34.064865, -118.44801);
        routePoints.add(sydney3);
        Location syd3 = new Location("dummyProvider");
        syd3.setLatitude(34.064865);
        syd3.setLongitude(-118.44801);
        routeLocation.add(syd3);

        //FAKE DATA 4
        LatLng sydney4 = new LatLng(34.065942, -118.447595);
        routePoints.add(sydney4);
        Location syd4 = new Location("dummyProvider");
        syd4.setLatitude(34.065942);
        syd4.setLongitude(-118.447595);
        routeLocation.add(syd4);

        Intent i = new Intent(this, MapsActivity.class);
        i.putParcelableArrayListExtra("point", routePoints);


        float[] result = new float[3];
        if (routeLocation.size() > 0) {
            for (int x = 0; x < routeLocation.size() - 1; x++) {
                Location.distanceBetween(routeLocation.get(x).getLatitude(), routeLocation.get(x).getLongitude(),
                        routeLocation.get(x + 1).getLatitude(), routeLocation.get(x + 1).getLongitude(), result);
                totalDistance = totalDistance + result[0];
            }

//            duration = timePoints.get(timePoints.size()-1) - timePoints.get(0);
//            duration = duration/1000000000;

            duration = 5;

            avSpeed = totalDistance / duration;

        }


//        i.putExtra("date", dateformatStr);
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

//
//    @Override
//    public void onStart() {
//        super.onStart();
//    }
