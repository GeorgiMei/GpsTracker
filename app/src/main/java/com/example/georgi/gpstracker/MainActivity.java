package com.example.georgi.gpstracker;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnSuccessListener<Location>{

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest             mLocationRequest;
    private GPSLocationCallback         mLocationCallback;
    private Location                    mLastLocation;
    private GoogleMap                   mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onSuccess(Location location) {
        // Configure location update
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLocationCallback = new GPSLocationCallback(this);

        // This will start periodic location updates
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    public void UpdateLocation(Location newLocation){
        Location currentLocation = mLastLocation;
        mLastLocation = newLocation;

        if (null != mLastLocation &&
            null != currentLocation)
        {
            PolylineOptions trackOptions = new PolylineOptions();
            trackOptions.width(10);
            trackOptions.color(Color.RED);
            //trackOptions.geodesic(true);
            //trackOptions.zIndex(z));
            LatLng pos1 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            LatLng pos2 = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            trackOptions.add(pos1);
            trackOptions.add(pos2);
            TextView altitude = (TextView)findViewById(R.id.textViewAltitude);
            altitude.setText(String.valueOf(mLastLocation.getAltitude()));

            mMap.addPolyline(trackOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos2));
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
