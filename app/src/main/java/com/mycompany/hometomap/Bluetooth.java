package com.mycompany.hometomap;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

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

    private static long lastFallAlertTime = 0;
    private static final long FALL_ALERT_TIMEOUT = 30L * 1000L * 1000L * 1000L; // 30 oseconds
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

    private static void HandleFallAlert(BluetoothPacket bluetoothPacket)
    {
        if (bluetoothPacket.getPacketType() != BluetoothPacketType.FALL_ALERT)
            Log.d("ERROR", "HandleFallAlert method expects a bluetooth packet of type FALL_ALERT, recieved " + bluetoothPacket.getPacketType().getString());
        long time = System.nanoTime();
        long elapsedTime = time - lastFallAlertTime;
        if (elapsedTime > FALL_ALERT_TIMEOUT) {
            Log.d("INFO", "Potential fall detected!");
            sendTextAlert();
            AlertBox("WARNING", "Potential Fall Detected!");
            lastFallAlertTime = time;
        } else
        {
            Log.d("INFO", "Detected potential fall within 30 seconds of last alert. " + String.valueOf(FALL_ALERT_TIMEOUT - elapsedTime) + " nanoseconds remaining.");
        }
    }

    private static void sendTextAlert()
    {
        String emergencyContactNumber = SettingsActivity.getEmergencyNumber();
        String username = SettingsActivity.getUserName();
        Log.d("INFO", "Emergency number: " + emergencyContactNumber);
        Log.d("INFO", "Username: " + username);
        if ( ! PhoneNumberUtils.isGlobalPhoneNumber(emergencyContactNumber) )
        {
            Log.d("INFO", "Can't send alert text as there is no valid emergency number");
        }
        else
        {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(emergencyContactNumber, null, username + " has fallen during a bike ride", null, null);
            } catch (Exception e)
            {
                Log.d("ERROR", "Failed to send sms alert: " + e.getMessage());
            }
//            Intent textIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + emergencyContactNumber));
//            textIntent.putExtra("sms_body", username + " has fallen during a bike ride");
//            _currentActivity.startActivity(textIntent);
        }

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

}
