package com.example.city_matcher.model;

public class Account {
    private String email;
    private String matchedCity;

    public Account(String mEmail, String mCity) {
        email = mEmail;
        matchedCity = mCity;
    }

    public String getEmail() { return email; }

    public String getCity() {
        return matchedCity;
    }

    public void setMatchedCity(String mCity) { matchedCity = mCity; }
}
