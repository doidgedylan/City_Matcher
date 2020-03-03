package com.example.city_matcher.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        //handle goToQuiz button
        Button quizButton = (Button) findViewById(R.id.goToQuizButton);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResult();
            }
        });

        //handle login button
        Button loginButton = (Button) findViewById(R.id.goToLoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });
    }


    // ***** PRIVATE HELPER METHODS ***** //

    private void openResult() {
        Intent mIntent = new Intent(this, QuestionActivity.class);
        startActivity(mIntent);
    }

    private void openLogin() {
        Intent mIntent = new Intent(this, LoginActivity.class);
        startActivity(mIntent);
    }
}
