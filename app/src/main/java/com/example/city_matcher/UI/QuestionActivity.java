package com.example.city_matcher.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        Log.d(TAG, "OnCreate: started.");
        initSpinners();

        //handle submit button
        button = (Button) findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResult();
            }
        });
    }


    // ***** PRIVATE HELPER METHODS ***** //

    private void openResult() {
        Intent mIntent = new Intent(this, ResultActivity.class);
        startActivity(mIntent);
    }

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
