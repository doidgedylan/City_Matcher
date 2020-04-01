package com.example.city_matcher.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.city_matcher.R;
import com.example.city_matcher.Controller.ResultsCalculator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";
    private ResultsCalculator resultEngine;
    private Button submitButton;

    private Spinner valuesSpinner;
    private Spinner industrySpinner;
    private Spinner drinkSpinner;
    private Spinner distSpinner;

    // get database references
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCityRef = mRootRef.child("cities");
    private ValueEventListener processFirebaseRead;
    private QuestionActivity test = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);

        // grab ui elements
        valuesSpinner = findViewById(R.id.valuesSpinner);
        industrySpinner = findViewById(R.id.industrySpinner);
        drinkSpinner = findViewById(R.id.drinkSpinner);
        distSpinner = findViewById(R.id.distanceSpinner);
        submitButton = findViewById(R.id.submitButton);

        // init spinners and grab submitted values
        initSpinners();
        valuesSpinnerData();
        industrySpinnerData();
        drinkSpinnerData();
    }

    @Override
    public void onStart() {
        super.onStart();

        //handle submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResult();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        resultEngine = new ResultsCalculator(); //reset engine
    }

    // ***** PRIVATE HELPER METHODS ***** //
    private void openResult() {
        calculateResult();
    }

    private void calculateResult() {
        // calculate and get result and pass the information to the result activity/fragment
        processFirebaseRead = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cityReadResult = dataSnapshot.getValue().toString();
                String parentCity = dataSnapshot.getRef().getParent().getKey();
                String readKey = dataSnapshot.getRef().getKey();

                resultEngine.processData(cityReadResult, readKey, parentCity);
                if (resultEngine.getIterateCount() >= 10) { //***
                    showResult();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };

        /*
        * process input scores
        * note: industry input is to calculate "career"
        * value, so no processing needed for that option
        */
        processValueScore();
        processDrinkScore();
    }

    private void processValueScore() {
        switch(resultEngine.getHighestValue()) {
            case ("Career"):
                // calculate based on job count
                String jobCountIndexByIndustry = resultEngine.getJobCountIndex(resultEngine.getIndustry());
                for (int i = 1; i <= 10; i++) {
                    mCityRef.child(Integer.toString(i)).child(jobCountIndexByIndustry).addValueEventListener(processFirebaseRead);
                }
                break;
            case ("Family"):
                // calculate based on cost of living and public park count
                for (int i = 1; i <=10; i++) {
                    mCityRef.child(Integer.toString(i)).child("5").addValueEventListener(processFirebaseRead);
                }
                break;
            case ("Cost of Living"):
                // calculate based on cost of living index
                for (int i = 1; i <= 10; i++) {
                    mCityRef.child(Integer.toString(i)).child("3").addValueEventListener(processFirebaseRead);
                }
                break;
            case ("Warm Weather"):
                // calculate based on average summer temp
                Log.d(TAG, "processValueScore: education value");
                break;
            default:
                Toast.makeText(getBaseContext(), "Please select what's most important" , Toast.LENGTH_LONG).show();
        }
    }
    
    private void processDrinkScore() {
        Log.d(TAG, "processDrinkScore: ");
    }

    private void showResult() {
        Intent mIntent = new Intent(test, ResultActivity.class);
        mIntent.putExtra("result", resultEngine.getResult());
        startActivity(mIntent);
    }

    private void initSpinners() {
        // init adapters
        ArrayAdapter<String> valQuestionAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.priorityQuestionAnswers));
        ArrayAdapter<String> indQuestionAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.industryQuestionAnswers));
        ArrayAdapter<String> drinkQuestionAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.drinkQuestionAnswers));
        ArrayAdapter<String> distQuestionAdapter = new ArrayAdapter<>(this,
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
                resultEngine.setValue(s);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void industrySpinnerData() {
        industrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                resultEngine.setIndustry(s);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void drinkSpinnerData() {
        drinkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                resultEngine.setDrink(s);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}