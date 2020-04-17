package com.example.city_matcher.model;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class City_Matcher extends Application {
    /*
    * This class is used to enable real time database persistence on app launch
    * */
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
