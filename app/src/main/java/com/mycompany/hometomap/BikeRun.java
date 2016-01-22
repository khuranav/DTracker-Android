package com.mycompany.hometomap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Varun on 12/1/15.
 */
public class BikeRun {
    ArrayList<LatLng> routePointsObj = new ArrayList<LatLng>();
    public float avSpeedObj;
    public float totalDistanceObj;
    public long durationObj;

    public BikeRun(ArrayList<LatLng> routePointsObj, float totalDistanceObj, long durationObj, float avSpeedObj) {
        this.routePointsObj = routePointsObj;
        this.totalDistanceObj = totalDistanceObj;
        this.durationObj = durationObj;
        this.avSpeedObj = avSpeedObj;
    }

    public ArrayList<LatLng> getRoutePointsObj() {
        return routePointsObj;
    }

    public float getTotalDistanceObj() {
        return totalDistanceObj;
    }

    public float getDurationObj() {
        return durationObj;
    }

    public float getAvSpeedObj() {
        return avSpeedObj;
    }
}
