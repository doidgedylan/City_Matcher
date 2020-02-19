package com.example.city_matcher;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        Log.d(TAG, "OnCreate: started.");
        initSpinners();
    }

    // ***** HELPER METHODS ***** //


    /*
        method used to initialize and setup the spinners for the personality quiz
     */
    private void initSpinners() {
        Spinner valuesSpinner = findViewById(R.id.valuesSpinner);
        Spinner industrySpinner = findViewById(R.id.industrySpinner);
        Spinner distSpinner = findViewById(R.id.distanceSpinner);

        // init adapters
        ArrayAdapter<String> valQuestionAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.priorityQuestionAnswers));
        ArrayAdapter<String> indQuestionAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.industryQuestionAnswers));
        ArrayAdapter<String> distQuestionAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.distanceQuestionAnswers));

        valQuestionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        indQuestionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        distQuestionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // set adapters
        valuesSpinner.setAdapter(valQuestionAdapter);
        industrySpinner.setAdapter(indQuestionAdapter);
        distSpinner.setAdapter(distQuestionAdapter);
    }
}
