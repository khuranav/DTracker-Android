package com.mycompany.hometomap;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by max on 1/30/16.
 */
class BluetoothThread extends Thread {
    private final OutputStream outputStream;
    private final InputStream inputStream;

    public BluetoothThread(OutputStream outputStream, InputStream inputStream)
    {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
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

    public BluetoothPacket RecievePacket()
    {
        Log.d("INFO", "Recieving packet...\n");
        byte data[] = this.GetFromBluetooth();
        BluetoothPacket bluetoothPacket = new BluetoothPacket(data);

        return bluetoothPacket;
    }



    @Override
    public void run() {
        while (true)
        {
            Log.d("INFO", "Waiting to recieve packet...");
            BluetoothPacket bluetoothPacket = RecievePacket();
            Log.d("INFO", "Packet recieved");

            Bluetooth.HandlePacket(bluetoothPacket);
        }
    }

};