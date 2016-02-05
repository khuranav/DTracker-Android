package com.mycompany.hometomap;

import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by max on 1/28/16.
 */



public class BluetoothPacket {
    private static final int BLT_PKT_TYPE_LOC = 256;
    private BluetoothPacketType packetType;
    private byte _data[] = new byte[256];

    /* Expectes data[257] */
    BluetoothPacket(byte data[])
    {
        Log.d("INFO", "Recieved packet: " + data.toString());
        if (data.length >= BLT_PKT_TYPE_LOC)
            this.packetType = BluetoothPacketType.fromInt(data[BLT_PKT_TYPE_LOC]);
        else
            this.packetType = BluetoothPacketType.fromInt(0);
        this._data = Arrays.copyOf(data, 256);
    }

    /* Expectes data[256] */
    BluetoothPacket(byte data[], BluetoothPacketType bluetoothPacketType)
    {
        this.packetType = bluetoothPacketType;
        this._data = Arrays.copyOf(data, 256);
    }

    public static List<BluetoothPacket> BluetoothPacketsFromString(String string, BluetoothPacketType bluetoothPacketType)
    {
        int i = 0;
        List<BluetoothPacket> bluetoothPacketList = new LinkedList<BluetoothPacket>();
        byte subData[] = new byte[256];
        while (i < string.length())
        {
            int n = Math.min(string.length(), i+256);
            subData = Arrays.copyOf(string.substring(i, n).getBytes(), 256);
            bluetoothPacketList.add(new BluetoothPacket(subData, bluetoothPacketType));
            i += n;
        }
        return bluetoothPacketList;
    }

    public byte[] toByteArray()
    {
        byte data[] = new byte[257];
        data = Arrays.copyOf(this._data, 257);
        data[BLT_PKT_TYPE_LOC] = (byte) packetType.getValue();
        return data;
    }

    public BluetoothPacketType getPacketType()
    {
        return this.packetType;
    }

    public String toString()
    {
        String message = new String(_data);
        return message;
    }
}
