package com.example.city_matcher.controller;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.File;

public class GPSTracker implements LocationListener {
    private Context context;
    private static final String TAG = "GPSTracker";

    // shared preferences strings for caching location
    private static final String SHARED = "sharedPref";
    private static final String LONGITUDE = "lng";
    private static final String LATITUDE = "lat";


    public GPSTracker(Context c) {
        context = c;
    }

    public Location getLocation() {
        //is location permission enabled by the user
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        //get location
        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isLocationOn = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isLocationOn) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 10,this);
            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // write the location to shared preferences for a later use
            saveLocation(loc);
            return loc;
        } else {
            // use the last known location if available
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
            String lng = sharedPreferences.getString(LONGITUDE, "nil");
            String lat = sharedPreferences.getString(LATITUDE, "nil");

            if (!lng.equals("nil") && !lat.equals("nil")) {
                Location loc = new Location(LocationManager.GPS_PROVIDER);
                loc.setLongitude(Double.parseDouble(lng));
                loc.setLatitude(Double.parseDouble(lat));
                return loc;
            } else {
                return null;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}


    // *** PRIVATE HELPER METHODS ***
    private void saveLocation(Location loc) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (loc != null) {
            editor.putString(LONGITUDE, Double.toString(loc.getLongitude()));
            editor.putString(LATITUDE, Double.toString(loc.getLatitude()));
            editor.apply();
        }
    }
}
