package com.example.city_matcher.UI;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.city_matcher.Controller.GPSTracker;
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
    private QuestionActivity tLocal = this;

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
        distanceSpinnerData();
    }

    @Override
    public void onStart() {
        super.onStart();
        // calculate and get result and pass the information to the result activity/fragment
        processFirebaseRead = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cityReadResult = dataSnapshot.getValue().toString();
                String parentCity = dataSnapshot.getRef().getParent().getKey();
                String readKey = dataSnapshot.getRef().getKey();

                resultEngine.processData(cityReadResult, readKey, parentCity);
                Log.d(TAG, "onDataChange: result " + resultEngine.getIterateCount());
                processShowResultCommand();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };

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

    private void openResult() {
        calculateResult();
    }

    private void calculateResult() {
        /*
        * process input scores
        * note: industry input is to calculate "career"
        * value, so no processing needed for that option
        */
        processValueScore();
        processDrinkScore();
        processDistanceScore();
    }

    private void processValueScore() {
        switch(resultEngine.getHighestValue()) {
            case ("Career"):
                // calculate based on job count
                if (industrySelected()) {
                    String jobCountIndexByIndustry = resultEngine.getJobCountIndex(resultEngine.getIndustry());
                    for (int i = 1; i <= 10; i++) {
                        mCityRef.child(Integer.toString(i)).child(jobCountIndexByIndustry).addValueEventListener(processFirebaseRead);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "select industry" , Toast.LENGTH_SHORT).show();
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
                // calculate based on lowest difference between avg winter temp and avg summer temp
                for (int i = 1; i <= 10; i++) {
                    mCityRef.child(Integer.toString(i)).child("1").addValueEventListener(processFirebaseRead);
                    mCityRef.child(Integer.toString(i)).child("2").addValueEventListener(processFirebaseRead);
                }
                break;
            default:
                Toast.makeText(getBaseContext(), "select highest value" , Toast.LENGTH_SHORT).show();
        }
    }

    private boolean industrySelected() {
        if (resultEngine.getIndustry().equals("Select")) {
            return false;
        } else {
            return true;
        }
    }

    private void processDrinkScore() {
        switch(resultEngine.getDrink()) {
            case("Coffee"):
                for (int i = 1; i <= 10; i++) {
                    mCityRef.child(Integer.toString(i)).child("6").addValueEventListener(processFirebaseRead);
                }
                break;
            case("Beer"):
                for (int i = 1; i <= 10; i++) {
                    mCityRef.child(Integer.toString(i)).child("4").addValueEventListener(processFirebaseRead);
                }
                break;
            case("Select"):
                Toast.makeText(getBaseContext(), "select favorite drink" , Toast.LENGTH_SHORT).show();
                break;
            default:
                // add ten iterations for result processing to be activated. But don't process data because
                // "soda" and "water" answers don't really affect a moving choice.
                resultEngine.addToIterateCount(10);
        }
    }

    private void processDistanceScore() {
        switch (resultEngine.getMaxDistance()) {
            case("no limit"):
                Log.d(TAG, "processDistanceScore: no limit ");
                break;
            case ("Select"):
                Toast.makeText(getBaseContext(), "select a distance" , Toast.LENGTH_SHORT).show();
                break;
            default:
                // calculate based on lowest difference between avg winter temp and avg summer temp
                for (int i = 1; i <= 10; i++) {
                    mCityRef.child(Integer.toString(i)).child("13").addValueEventListener(processFirebaseRead);
                    mCityRef.child(Integer.toString(i)).child("14").addValueEventListener(processFirebaseRead);
                }
        }
    }

    private void processShowResultCommand() {
        if (resultEngine.getHighestValue().equals("Warm Weather") &&
                resultEngine.getIterateCount() >= 50) { //***
            showResult();
        } else if (!resultEngine.getHighestValue().equals("Warm Weather") &&
                resultEngine.getIterateCount() >= 40) {
            showResult();
        }
    }

    private void showResult() {
        Intent mIntent = new Intent(tLocal, ResultActivity.class);
        mIntent.putExtra("result", resultEngine.getResult());
        startActivity(mIntent);
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

    public void distanceSpinnerData() {
        distSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                resultEngine.setMaxDistance(s);
                // set current location
                GPSTracker g = new GPSTracker(getApplicationContext());
                Location l = g.getLocation();
                if (l != null) {
                    resultEngine.setCurrentLocation(l.getLatitude(), l.getLongitude());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}