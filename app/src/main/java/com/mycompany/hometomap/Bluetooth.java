package com.mycompany.hometomap;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by max on 1/28/16.
 */
public class Bluetooth {
    private static BluetoothAdapter btAdapter;
    private static BluetoothDevice btDevice;
    private static BluetoothSocket btSocket;
    private static OutputStream btOutput;
    private static InputStream btInput;
    private static final UUID SSP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final String btDeviceAddress = "98:4F:EE:04:A1:E1";
    private final static int REQUEST_ENABLE_BT = 1;

    public static void Connect(Intent intent)
    {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    protected void checkBluetoothAdapter() {
        if (btAdapter == null) {
            Log.d("Error", "Bluetooth is not supported on your device");
        } else {
            if (btAdapter.isEnabled() == false) {
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    protected void reconnectBluetooth() {
        checkBluetoothAdapter();

        btDevice = btAdapter.getRemoteDevice(btDeviceAddress);
        try {
            btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(SSP_UUID);
        } catch (IOException e) {
            Log.d("ERROR", "Failed to create bluetooth socket:" + e.getMessage());
        }

        btAdapter.cancelDiscovery();

        try {
            btSocket.connect();
        } catch (IOException e1) {
            Log.d("ERROR", "Failed to connect to device " + btDeviceAddress + " because: " + e1.getMessage());
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.d("ERROR", "Failed to close socket after failing to connect to device: " + e1.getMessage());
            }
        }

        try {
            btOutput = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.d("ERROR", "Failed to get output stream from bluetooth: " + e.getMessage());
        }

        try {
            btInput = btSocket.getInputStream();
        } catch (IOException e) {
            Log.d("ERROR", "Failed to get inpput stream from bluetooth: " + e.getMessage());
        }
    }

    public static void SendLocationUpdate(Location location)
    {

    }

    private static void SendOverBluetooth(byte[] data, int dataSize)
    {

    }

    private static void GetFromBluetooth(byte[] data, int dataSize)
    {

    }

    private static void SendPacket(BluetoothPacket packet)
    {
        byte data[] = new byte[257];
        data = packet.toByteArray();
        SendOverBluetooth(data, 257);
    }

    private static BluetoothPacket RecievePacket()
    {
        byte data[] = new byte[257];
        GetFromBluetooth(data, 257);
        return new BluetoothPacket(data);
    }

//    public void AlertBox(String title, String message) {
//        new AlertDialog.Builder(this)
//                .setTitle(title)
//                .setMessage(message + " Press OK to exit.")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        //finish();
//                        arg0.cancel();
//                    }
//                }).show();
//    }
}
