package com.mycompany.hometomap;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by max on 1/28/16.
 */
public class Bluetooth {
    private static BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
//    private static BluetoothDevice btDevice;
//    private static BluetoothSocket btSocket;
//    private static OutputStream btOutput;
//    private static InputStream btInput;
    private static final UUID SSP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String btDeviceAddress = "98:4F:EE:04:A1:E1";
    private static final int REQUEST_ENABLE_BT = 1;
    private static Activity _currentActivity;



    private static BluetoothThread bluetoothThread;

//    public static void Connect(Intent intent)
//    {
//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//    }

//    @Override
//    public void run() {
//        while (true)
//        {
//            Log.d("INFO", "Waiting to recieve packet...");
//            BluetoothPacket bluetoothPacket = RecievePacket();
//            Log.d("INFO", "Packet recieved");
//            if (bluetoothPacket.getPacketType() == BluetoothPacketType.UPDATE_CHECK)
//            {
//                Log.d("INFO", "Packet is of type UPDATE_CHECK");
//                NoUpdate();
//            }
//        }
//    }

    private static void checkBluetoothAdapter() {
        Log.d("INFO", "Checking Bluetooth Adapter");
        if (btAdapter == null) {
            Log.d("Error", "Bluetooth is not supported on your device");
        } else {
            if (!btAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                _currentActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        Log.d("INFO", "Finished checking Bluetooth adapter");
    }

    public static void ReconnectBluetooth(Activity currentActivity) {
        Log.d("INFO", "Reconnecting Bluetooth...");
        _currentActivity = currentActivity;
        checkBluetoothAdapter();

        Log.d("INFO", "Checking Bluetooth Adapter");
        if (btAdapter == null) {
            Log.d("Error", "Bluetooth is not supported on your device");
        } else {
            if (!btAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                _currentActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        Log.d("INFO", "Finished checking Bluetooth adapter");

        Log.d("INFO", "Checking to see if we are still connected...");
        if (bluetoothThread != null && bluetoothThread.isSocketConnected()){
            return;
        }

        bluetoothThread = new BluetoothThread(btAdapter);
        bluetoothThread.start();

        Log.d("INFO", "Ending bluetooth.reconnect\n");
//        Log.d("INFO", "Getting Bluetooth device...");
//        btDevice = btAdapter.getRemoteDevice(btDeviceAddress);
//
//        Log.d("INFO", "Trying to create Bluetooth socket...\n");
//        try {
//            btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(SSP_UUID);
//        } catch (IOException e) {
//            Log.d("ERROR", "Failed to create bluetooth socket:" + e.getMessage());
//        }
//        Log.d("INFO", "Created Bluetooth socket...\n");
//
//        btAdapter.cancelDiscovery();
        //BluetoothConnectAsync connectAsync = new BluetoothConnectAsync(btSocket, btOutput, btInput);

//        Log.d("INFO", "Trying to connect via Bluetooth...");
//        try {
//            btSocket.connect();
//        } catch (IOException e1) {
//            Log.d("ERROR", "Failed to connect to device " + btDeviceAddress + " because: " + e1.getMessage());
//            try {
//                btSocket.close();
//            } catch (IOException e2) {
//                Log.d("ERROR", "Failed to close socket after failing to connect to device: " + e1.getMessage());
//            }
//        }
//        Log.d("INFP", "Connected via Bluetooth");

//        Log.d("INFO", "Trying to get output stream...");
//        try {
//            btOutput = btSocket.getOutputStream();
//        } catch (IOException e) {
//            Log.d("ERROR", "Failed to get output stream from bluetooth: " + e.getMessage());
//        }
//        Log.d("INFO", "Got output stream");
//
//        Log.d("INFO", "Trying to get input stream...");
//        try {
//            btInput = btSocket.getInputStream();
//        } catch (IOException e) {
//            Log.d("ERROR", "Failed to get inpput stream from bluetooth: " + e.getMessage());
//        }
//        Log.d("INFO", "Got input stream");
//
//        bluetoothThread = new BluetoothThread(btOutput, btInput);
//        bluetoothThread.start();

//
//        if (DataIsAvailable())
//            RecievePacket();
    }

    public static void SendLocationUpdate(Location location)
    {
        String message = Double.toString(location.getLatitude());
        message += ",";
        message += Double.toString(location.getLongitude());
        message += '\n';
        List<BluetoothPacket> bluetoothPacketList = BluetoothPacket.BluetoothPacketsFromString(message, BluetoothPacketType.GPS_UPDATE);
        for (BluetoothPacket packet : bluetoothPacketList)
        {
            SendPacket(packet);
        }
    }

//    private static void SendOverBluetooth(byte[] data, int dataSize) {
//        try {
//            btOutput.write(data, 0, dataSize);
//        } catch (IOException e) {
//            Log.d("ERROR", "Failed to write data to Bluetooth Output Stream: " + e.getMessage());
//        }
//    }
//
//    private static void GetFromBluetooth(byte[] data) {
//        Log.d("INFO", "Trying to read from bluetooth...");
//        try {
//            btInput.read(data);
//        } catch (IOException e)
//        {
//            Log.d("ERROR", "Failed to read data from Bluetooth Input Stream: " + e.getMessage());
//        }
//    }

//    public static boolean DataIsAvailable() {
//        if ( bluetoothThread == null || bluetoothThread.isSocketConnected() == false )
//        {
//            Log.d("ERROR", "Tried to check for available data on disconnected socket\n");
//            return false;
//        }
//        try {
//            if ()
//                return true;
//            else
//                return false;
//        } catch (IOException e)
//        {
//            Log.d("ERROR", "Failed to check for data: " + e.getMessage());
//            return false;
//        }
//    }

    public static void SendPacket(BluetoothPacket packet)
    {
        if ( bluetoothThread == null || bluetoothThread.isSocketConnected() == false )
        {
            Log.d("ERROR", "Tried to send data over disconnected bluetooth socket\n");
            return;
        }
        byte data[] = new byte[257];
        data = packet.toByteArray();
        bluetoothThread.SendOverBluetooth(data, 257);
    }

    public static void HandlePacket(BluetoothPacket bluetoothPacket)
    {
        if (bluetoothPacket == null)
        {
            Log.d("ERROR", "HandlePacket was handed a null bluetoothPacket");
            return;
        }

        Log.d("INFO", "Recieved bluetooth packet of type " + bluetoothPacket.getPacketType());

        switch (bluetoothPacket.getPacketType())
        {
            case UPDATE_CHECK:
                HandleUpdateRequest(bluetoothPacket);
                break;
            case FALL_ALERT:
                HandleFallAlert(bluetoothPacket);
                break;
            default:
                Log.d("ERROR", "Recieved packet that I can't handle: " + bluetoothPacket.getPacketType().getString());
                AlertBox("ERROR", "Recieved packet that I can't handle: " + bluetoothPacket.getPacketType().getString());
                break;

        }
//        if (bluetoothPacket.getPacketType() == BluetoothPacketType.UPDATE_CHECK)
//            HandleUpdateRequest(bluetoothPacket);

    }

    private static void HandleUpdateRequest(BluetoothPacket bluetoothPacket)
    {

        if (bluetoothPacket.getPacketType() != BluetoothPacketType.UPDATE_CHECK)
        {
            Log.d("ERROR", "HandleUpdateRequest expectes UPDATE_CHECK packet, recieved " + bluetoothPacket.getPacketType().getString() + " packet\n");
        }
        Log.d("INFO", bluetoothPacket.toString());
        NoUpdate();
    }


    public static void EndRide()
    {
        String message = "END\n";
        BluetoothPacket bluetoothPacket = new BluetoothPacket(message.getBytes(), BluetoothPacketType.GPS_UPDATE);
        SendPacket(bluetoothPacket);
    }

    private static void NoUpdate()
    {
        Log.d("INFO", "Sending NO_UPDATE packet...");
        BluetoothPacket bluetoothPacket = new BluetoothPacket(new byte[256], BluetoothPacketType.NO_UPDATE);
        SendPacket(bluetoothPacket);
    }

    public static void HandleFallAlert(BluetoothPacket bluetoothPacket)
    {
        if (bluetoothPacket.getPacketType() != BluetoothPacketType.FALL_ALERT)
            Log.d("ERROR", "HandleFallAlert method expects a bluetooth packet of type FALL_ALERT, recieved " + bluetoothPacket.getPacketType().getString());
        Log.d("INFO", "Potential fall detected!");
        AlertBox("WARNING", "Potential Fall Detected!");
    }

    private static void AlertBox(final String title, final String message) {
        _currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(_currentActivity)
                        .setTitle(title)
                        .setMessage(message + " Press OK to exit.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                //finish();
                                arg0.cancel();
                            }
                        }).show();
            }
        });

    }

//    private class BluetoothConnectAsync extends AsyncTask
//    {
//        private BluetoothSocket _bluetoothSocket;
//        private OutputStream _bluetoothOutputStream;
//        private InputStream _blutoothInputStream;
//
//        BluetoothConnectAsync(BluetoothSocket bluetoothSocket, OutputStream bluetoothOutputStream, InputStream bluetoothInputStream)
//        {
//            _bluetoothSocket = bluetoothSocket;
//            _bluetoothOutputStream = bluetoothOutputStream;
//            _blutoothInputStream = bluetoothInputStream;
//        }
//
//        @Override
//        protected Object doInBackground(Object[] params) {
//            while ( ! _bluetoothSocket.isConnected() )
//            {
//                Log.d("INFO", "Trying to connect via Bluetooth...");
//                try {
//                    btSocket.connect();
//                } catch (IOException e1) {
//                    Log.d("ERROR", "Failed to connect to device " + btDeviceAddress + " because: " + e1.getMessage());
//                    try {
//                        btSocket.close();
//                    } catch (IOException e2) {
//                        Log.d("ERROR", "Failed to close socket after failing to connect to device: " + e1.getMessage());
//                    }
//                }
//                Log.d("INFP", "Connected via Bluetooth");
//            }
//
//            Log.d("INFO", "Trying to get output stream...");
//            try {
//                btOutput = btSocket.getOutputStream();
//            } catch (IOException e) {
//                Log.d("ERROR", "Failed to get output stream from bluetooth: " + e.getMessage());
//            }
//            Log.d("INFO", "Got output stream");
//
//            Log.d("INFO", "Trying to get input stream...");
//            try {
//                btInput = btSocket.getInputStream();
//            } catch (IOException e) {
//                Log.d("ERROR", "Failed to get inpput stream from bluetooth: " + e.getMessage());
//            }
//            Log.d("INFO", "Got input stream");
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            BluetoothThread thread = new BluetoothThread(_bluetoothOutputStream, _blutoothInputStream);
//            thread.run();
//        }
//    }
}
