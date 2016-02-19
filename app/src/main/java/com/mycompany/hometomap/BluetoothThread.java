package com.mycompany.hometomap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.*;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by max on 1/30/16.
 */
class BluetoothThread extends Thread {
    private OutputStream outputStream;
    private InputStream inputStream;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    private BluetoothAdapter bluetoothAdapter;
    private final UUID SSP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String bluetoothDevieceAddress = "98:4F:EE:04:A1:E1";
    private static final int REQUEST_ENABLE_BT = 1;

    public BluetoothThread(BluetoothAdapter bluetoothAdapter)
    {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void SendOverBluetooth(byte[] data, int dataSize) {
        String dataString = new String(data);
        Log.d("INFO", "Sending over bluetooth: " + dataString);
        try {
            outputStream.write(data, 0, dataSize);
        } catch (IOException e) {
            Log.d("ERROR", "Failed to write data to Bluetooth Output Stream: " + e.getMessage());
        }
    }

    private byte[] GetFromBluetooth() {
        byte data[] = new byte[257];
        Log.d("INFO", "Trying to read from bluetooth...");
        try {
            inputStream.read(data);
        } catch (IOException e)
        {
            Log.d("ERROR", "Failed to read data from Bluetooth Input Stream: " + e.getMessage());
        }
        return data;
    }

    private BluetoothPacket RecievePacket()
    {
        Log.d("INFO", "Recieving packet...\n");
        byte data[] = this.GetFromBluetooth();
        BluetoothPacket bluetoothPacket = new BluetoothPacket(data);

        return bluetoothPacket;
    }

    private void connectSocket()
    {
        Log.d("INFO", "Getting bluetooth device...");
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothDevieceAddress);
        Log.d("INFO", "Done\n");

        Log.d("INFO", "Trying to create bluetooth socket to device...\n");
        try
        {
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(SSP_UUID);
        } catch (IOException e)
        {
            Log.d("ERROR", "Failed to create bluetooth socket");
            return;
        }
        Log.d("INFO", "Done\n");

        bluetoothAdapter.cancelDiscovery();

        while ( ! bluetoothSocket.isConnected() )
        {
            Log.d("INFO", "Trying to connect via Bluetooth...");
            try {
                bluetoothSocket.connect();
            } catch (IOException e1) {
                Log.d("ERROR", "Failed to connect to device: " + e1.getMessage());
//                try {
//                    bluetoothSocket.close();
//                } catch (IOException e2) {
//                    Log.d("ERROR", "Failed to close socket after failing to connect to device: " + e1.getMessage());
//                }
            }
            SystemClock.sleep(1000);
        }

        Log.d("INFO", "Trying to get output stream...");
        try {
            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            Log.d("ERROR", "Failed to get output stream from bluetooth: " + e.getMessage());
        }
        Log.d("INFO", "Got output stream");

        Log.d("INFO", "Trying to get input stream...");
        try {
            inputStream = bluetoothSocket.getInputStream();
        } catch (IOException e) {
            Log.d("ERROR", "Failed to get inpput stream from bluetooth: " + e.getMessage());
        }
        Log.d("INFO", "Got input stream");
    }

    public boolean isSocketConnected()
    {
        if (bluetoothSocket == null)
            return false;
        return bluetoothSocket.isConnected();
    }

    public boolean isDataAvailable()
    {
        if (bluetoothSocket == null || bluetoothSocket.isConnected() == false || inputStream == null)
            return false;
        try
        {
            if (inputStream.available() > 0)
                return true;
            else
                return false;
        } catch (IOException e)
        {
            Log.d("ERROR", "Failed to check for available data: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        while (bluetoothSocket == null || bluetoothSocket.isConnected() == false)
            connectSocket();
        while (true)
        {
            Log.d("INFO", "Waiting to recieve packet...");
            BluetoothPacket bluetoothPacket = RecievePacket();
            Log.d("INFO", "Packet recieved");

            Bluetooth.HandlePacket(bluetoothPacket);
        }
    }

};