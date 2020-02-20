package com.example.city_matcher.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.city_matcher.R;

import androidx.appcompat.app.AppCompatActivity;


public class LandingActivity extends AppCompatActivity {

    private static final String TAG = "LandingActivity";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);
        Log.d(TAG, "OnCreate: hit and activated");
        initSpinners();

        //handle goToQuiz button
        button = (Button) findViewById(R.id.goToQuizButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResult();
            }
        });
    }


    // ***** PRIVATE HELPER METHODS ***** //

    private void openResult() {
        Intent mIntent = new Intent(this, QuestionActivity.class);
        startActivity(mIntent);
    }

    private void initSpinners() {

    }
}
