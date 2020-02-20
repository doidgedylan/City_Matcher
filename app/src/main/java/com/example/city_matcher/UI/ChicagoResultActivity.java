package com.example.city_matcher.UI;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.city_matcher.R;

public class ChicagoResultActivity extends AppCompatActivity {
    private static final String TAG = "ChicagoResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chicago_result_activity);
        Log.d(TAG, "OnCreate: started.");
    }
}
