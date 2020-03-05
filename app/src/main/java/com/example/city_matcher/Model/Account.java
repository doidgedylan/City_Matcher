package com.example.city_matcher.Model;

public class Account {
    private String username;
    private String password;

    public Account(String mUsername, String mPassword) {
        username = mUsername;
        password = mPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
