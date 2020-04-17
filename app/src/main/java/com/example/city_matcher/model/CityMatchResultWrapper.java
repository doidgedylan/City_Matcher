package com.example.city_matcher.model;

public class CityMatchResultWrapper {
    private String city;
    private int jobCount;
    public CityMatchResultWrapper(String mCity) {
        city = mCity;
    }
    public void setJobCount(int count) {jobCount = count;}
    public int getJobCount() {return jobCount;}
    public String getCity() {return city;}
}
