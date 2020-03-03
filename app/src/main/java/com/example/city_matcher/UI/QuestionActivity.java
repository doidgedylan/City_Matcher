package com.example.city_matcher.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.city_matcher.R;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        Log.d(TAG, "OnCreate: hit and activated");
        initSpinners();

        //handle submit button
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResult();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart: on the quiz page.");
    }

    @Override
    protected void onResume () {
        super.onResume();
        Log.d(TAG, "OnResume: returned to the quiz page.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause: paused to go to results activity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop: Stopped activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy: Stopped activity");
    }

    // ***** PRIVATE HELPER METHODS ***** //
    private void openResult() {
        Intent mIntent = new Intent(this, ChicagoResultActivity.class);
        startActivity(mIntent);
    }

    private void initSpinners() {
        Spinner valuesSpinner = findViewById(R.id.valuesSpinner);
        Spinner industrySpinner = findViewById(R.id.industrySpinner);
        Spinner drinkSpinner = findViewById(R.id.drinkSpinner);
        Spinner distSpinner = findViewById(R.id.distanceSpinner);


        // init adapters
        ArrayAdapter<String> valQuestionAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.priorityQuestionAnswers));
        ArrayAdapter<String> indQuestionAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.industryQuestionAnswers));
        ArrayAdapter<String> drinkQuestionAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.drinkQuestionAnswers));
        ArrayAdapter<String> distQuestionAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.distanceQuestionAnswers));


        valQuestionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        indQuestionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        drinkQuestionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        distQuestionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


        // set adapters
        valuesSpinner.setAdapter(valQuestionAdapter);
        industrySpinner.setAdapter(indQuestionAdapter);
        drinkSpinner.setAdapter(drinkQuestionAdapter);
        distSpinner.setAdapter(distQuestionAdapter);

    }
}
