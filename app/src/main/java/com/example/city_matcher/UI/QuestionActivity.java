package com.example.city_matcher.UI;

import android.content.Intent;
import android.location.Location;
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

import com.example.city_matcher.Controller.GPSTracker;
import com.example.city_matcher.Model.CityMatchResultWrapper;
import com.example.city_matcher.Model.JobIndustryToIndex;
import com.example.city_matcher.R;
import com.example.city_matcher.Controller.ResultHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";
    private QuestionActivity tLocal = this;
    private ResultHandler resultEngine;

    // ui elements
    private Button submitButton;
    private Spinner valuesSpinner;
    private Spinner industrySpinner;
    private Spinner drinkSpinner;
    private Spinner distSpinner;

    // get database references
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCityRef = mRootRef.child("cities");
    private ValueEventListener processFirebaseRead;

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
    }

    @Override
    public void onStart() {
        super.onStart();
        valuesSpinnerData();
        industrySpinnerData();
        drinkSpinnerData();
        distanceSpinnerData();

        // calculate and get result and pass the information to the result activity/fragment
        processFirebaseRead = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cityReadResult = dataSnapshot.getValue().toString();
                String parentCity = dataSnapshot.getRef().getParent().getKey();
                String readKey = dataSnapshot.getRef().getKey();

                resultEngine.processData(cityReadResult, readKey, parentCity);
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
        resultEngine = new ResultHandler(); //reset engine
        // set current location
        GPSTracker locTracker = new GPSTracker(getApplicationContext());
        Location loc = locTracker.getLocation();
        if (loc != null) {
            resultEngine.setCurrentLocation(loc.getLatitude(), loc.getLongitude());
        }
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

        // process select checks before calling these
        if(statementsAreAllChosen()) {
            processValueScore();
            processDrinkScore();
            processDistanceScore();
        }
    }

    private boolean statementsAreAllChosen() {
        boolean result = true;
        if (resultEngine.getIndustry().equals("Select")) {
            result = false;
            Toast.makeText(getBaseContext(), "select industry" , Toast.LENGTH_SHORT).show();
        } else if (resultEngine.getHighestValue().equals("Select")) {
            result = false;
            Toast.makeText(getBaseContext(), "select highest value" , Toast.LENGTH_SHORT).show();
        } else if (resultEngine.getDrink().equals("Select")) {
            result = false;
            Toast.makeText(getBaseContext(), "select drink" , Toast.LENGTH_SHORT).show();
        } else if (resultEngine.getMaxDistance().equals("Select")) {
            result = false;
            Toast.makeText(getBaseContext(), "select a distance", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private void processValueScore() {
        switch(resultEngine.getHighestValue()) {
            case ("Career"):
                // calculate based on job count
                String jobCountIndexByIndustry = JobIndustryToIndex.convertJob(resultEngine.getIndustry());
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
                // calculate based on lowest difference between avg winter temp and avg summer temp
                for (int i = 1; i <= 10; i++) {
                    mCityRef.child(Integer.toString(i)).child("1").addValueEventListener(processFirebaseRead);
                    mCityRef.child(Integer.toString(i)).child("2").addValueEventListener(processFirebaseRead);
                }
                break;
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
            default:
                // add ten iterations for result processing to be activated.
                resultEngine.addToIterateCount(10);
        }
    }

    private void processDistanceScore() {
        if (!resultEngine.getMaxDistance().equals("no limit")) {
            // calculate based on difference between coordinates of current location and city location
            for (int i = 1; i <= 10; i++) {
                mCityRef.child(Integer.toString(i)).child("13").addValueEventListener(processFirebaseRead);
                mCityRef.child(Integer.toString(i)).child("14").addValueEventListener(processFirebaseRead);
            }
        }
    }

    private void processShowResultCommand() {
        double totalIterations = getTotalFinalIterations();
        if (resultEngine.getIterateCount() >= totalIterations) {
            showResult();
        }
    }

    private double getTotalFinalIterations() {
        double result = 0;
        boolean isWarmWeather = resultEngine.getHighestValue().equals("Warm Weather");
        boolean isDistanceSelected = !resultEngine.getMaxDistance().equals("no limit");
        boolean isDistanceNotSelected = resultEngine.getMaxDistance().equals("no limit");
        if (isWarmWeather && isDistanceSelected) {
            // warm weather 20 drink 10, distance 20
            result = 50;
        } else if (!isWarmWeather && isDistanceSelected) {
            // job count 10, drink 10, location 20
            result = 40;
        } else if (isWarmWeather && isDistanceNotSelected) {
            // warm weather 20 drink 10
            result = 30;
        } else if (!isWarmWeather && isDistanceNotSelected) {
            // job count 10, drink 10
            result = 20;
        }
        return result;
    }

    private void showResult() {
        Intent mIntent = new Intent(tLocal, ResultActivity.class);
        CityMatchResultWrapper result = resultEngine.getResult();
        mIntent.putExtra("city", result.getCity());
        mIntent.putExtra("jobCount", result.getJobCount());
        startActivity(mIntent);
        this.finish();
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}