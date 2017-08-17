package com.example.georgi.gpstracker;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationResult;

/**
 * Created by georgi on 8/15/17.
 */

class GPSLocationCallback extends com.google.android.gms.location.LocationCallback {

    private double latitude;
    private double longitude;
    private MainActivity mContext;

    GPSLocationCallback(MainActivity context){
        mContext = context;
    }

    public void onLocationResult(LocationResult result){
        mContext.UpdateLocation(result.getLastLocation());
     }

    public void onLocationAvailability(LocationAvailability locationAvailability){

    }
}

