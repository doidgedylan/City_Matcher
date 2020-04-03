package com.example.city_matcher.Controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class GPSTracker implements LocationListener {
    private Context context;
    public GPSTracker(Context c) {
        context = c;
    }

    public Location getLocation() {
        //is location enabled by the user
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        //get location
        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isLocationOn = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isLocationOn) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 10,this);
            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return loc;
        } else {
            Toast.makeText(context, "enable gps please",Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    @Override
    public void onLocationChanged(Location location) { }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onProviderDisabled(String provider) { }
}
