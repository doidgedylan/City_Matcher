package com.example.city_matcher.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.city_matcher.R;
import com.example.city_matcher.Controller.ResultsCalculator;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";
    private ResultsCalculator resultEngine = new ResultsCalculator();
    private Button button;

    private Spinner valuesSpinner;
    private Spinner industrySpinner;
    private Spinner drinkSpinner;
    private Spinner distSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        Log.d(TAG, "OnCreate: hit and activated");

        // grab ui elements
        valuesSpinner = findViewById(R.id.valuesSpinner);
        industrySpinner = findViewById(R.id.industrySpinner);
        drinkSpinner = findViewById(R.id.drinkSpinner);
        distSpinner = findViewById(R.id.distanceSpinner);
        button = (Button) findViewById(R.id.submitButton);

        // init spinners and grab submitted values
        initSpinners();
        valuesSpinnerData();
        industrySpinnerData();
    }

    @Override
    public void onStart() {
        super.onStart();
        //handle submit button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResult();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        resultEngine.wipeResult();
    }

    // ***** PRIVATE HELPER METHODS ***** //
    private void openResult() {
        Intent mIntent = new Intent(this, ResultActivity.class);
        mIntent.putExtra("result", resultEngine.calculateResult());
        startActivity(mIntent);
    }

    private void initSpinners() {
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

        valuesSpinner.setAdapter(valQuestionAdapter);
        industrySpinner.setAdapter(indQuestionAdapter);
        drinkSpinner.setAdapter(drinkQuestionAdapter);
        distSpinner.setAdapter(distQuestionAdapter);
    }

    public void valuesSpinnerData() {
        valuesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                resultEngine.calculateValuesScore(s);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void industrySpinnerData() {
        industrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                resultEngine.calculateIndustryScore(s);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}