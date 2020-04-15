package com.example.city_matcher.model;

public class CoordinatesWrapper {
    private double latitude;
    private double longitude;
    public CoordinatesWrapper(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public void setLatitude(double lat) { latitude = lat; }
    public void setLongitude(double lng) { longitude = lng; }
}
