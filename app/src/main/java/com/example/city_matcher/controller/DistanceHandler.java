package com.example.city_matcher.controller;
import com.example.city_matcher.model.CoordinatesWrapper;

/* class to perform distance calculations for gps sensor */
public class DistanceHandler {
    private DistanceHandler() {} // class can't be instantiated
    // class to get distance between coordinates in miles
    public static double distanceInMiles(CoordinatesWrapper loc1, CoordinatesWrapper loc2) {
        double result = 0;

        //convert from degrees to radians
        double lng1Rad = Math.toRadians(loc1.getLongitude());
        double lng2Rad = Math.toRadians(loc2.getLongitude());
        double lat1Rad = Math.toRadians(loc1.getLatitude());
        double lat2Rad = Math.toRadians(loc2.getLatitude());

        //haversine formula
        double diffLng = lng2Rad - lng1Rad;
        double diffLat = lat2Rad - lat1Rad;
        double a = Math.pow(Math.sin(diffLat / 2), 2)
                 + Math.cos(lat1Rad)*Math.cos(lat2Rad)
                 * Math.pow(Math.sin(diffLng / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        //Radius of earth in miles
        double radius = 3956;

        //calculate and return result
        result = c * radius;
        return result;
    }
}
