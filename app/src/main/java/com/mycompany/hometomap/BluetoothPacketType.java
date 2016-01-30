package com.mycompany.hometomap;

import java.util.HashMap;

/**
 * Created by max on 1/28/16.
 */
public enum BluetoothPacketType {
    UPDATE_CHECK    (0),
    UPDATE_RUNS_CNT (1),
    UPDATE_RUNS_END (2),
    NEW_RUN_CNT     (3),
    NEW_RUN_END     (4),
    NO_UPDATE       (5),
    GPS_UPDATE      (6);

    private final int value;

    private static HashMap<Integer, BluetoothPacketType> map = new HashMap<Integer, BluetoothPacketType>();
    static
    {
        map.put(0, UPDATE_CHECK);
        map.put(1, UPDATE_RUNS_CNT);
        map.put(2, UPDATE_RUNS_END);
        map.put(3, NEW_RUN_CNT);
        map.put(4, NEW_RUN_END);
        map.put(5, NO_UPDATE);
        map.put(6, GPS_UPDATE);
    }

    public static BluetoothPacketType fromInt(int i)
    {
        return map.get(i);
    }

    BluetoothPacketType(int value)
    {
        this.value = value;
    }

    int getValue()
    {
        return this.value;
    }
}
