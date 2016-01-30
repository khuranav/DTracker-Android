package com.mycompany.hometomap;

import java.util.Arrays;

/**
 * Created by max on 1/28/16.
 */



public class BluetoothPacket {
    private static final int BLT_PKT_TYPE_LOC = 256;
    private BluetoothPacketType packetType;
    private byte _data[] = new byte[256];


    BluetoothPacket(byte data[])
    {
        this.packetType = BluetoothPacketType.fromInt(data[BLT_PKT_TYPE_LOC]);
        this._data = Arrays.copyOf(data, 256);
    }

    public byte[] toByteArray()
    {
        byte data[] = new byte[257];
        data = Arrays.copyOf(this._data, 256);
        data[BLT_PKT_TYPE_LOC] = (byte) packetType.getValue();
        return data;
    }
}
