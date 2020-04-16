package com.example.city_matcher.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.city_matcher.R;
import com.example.city_matcher.ui.cityResults.ChicagoResultFragment;
import com.example.city_matcher.ui.cityResults.DallasResultFragment;
import com.example.city_matcher.ui.cityResults.HoustonResultFragment;
import com.example.city_matcher.ui.cityResults.LosAngelesResultFragment;
import com.example.city_matcher.ui.cityResults.NewYorkResultFragment;
import com.example.city_matcher.ui.cityResults.NothingResultFragment;
import com.example.city_matcher.ui.cityResults.PhiladelphiaResultFragment;
import com.example.city_matcher.ui.cityResults.PhoenixResultFragment;
import com.example.city_matcher.ui.cityResults.SanAntonioResultFragment;
import com.example.city_matcher.ui.cityResults.SanDiegoResultFragment;
import com.example.city_matcher.ui.cityResults.SanJoseResultFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";

    // get database reference
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mAccountsRef = mRootRef.child("accounts");

    //matched city
    private String maxScoreCity;
    private int jobCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        // get max city
        maxScoreCity = getIntent().getStringExtra("city");
        jobCount = getIntent().getIntExtra("jobCount", -1);

        // set real time database persistence
        mAccountsRef.keepSynced(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // update database with max city
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        mAccountsRef.child(id).child("city").setValue(maxScoreCity);


        // display max value fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        Bundle arguments = new Bundle();
        arguments.putInt("jobCount", jobCount);

        if (fragment == null) {
            switch (maxScoreCity) {
                case ("New York"): fragment = new NewYorkResultFragment(); break;
                case ("Los Angeles"): fragment = new LosAngelesResultFragment(); break;
                case ("Chicago"): fragment = new ChicagoResultFragment(); break;
                case ("Houston"): fragment = new HoustonResultFragment(); break;
                case ("Phoenix"): fragment = new PhoenixResultFragment(); break;
                case ("Philadelphia"): fragment = new PhiladelphiaResultFragment(); break;
                case ("San Antonio"): fragment = new SanAntonioResultFragment(); break;
                case ("San Diego"): fragment = new SanDiegoResultFragment(); break;
                case ("Dallas"): fragment = new DallasResultFragment(); break;
                case ("San Jose"): fragment = new SanJoseResultFragment(); break;
                default: fragment = new NothingResultFragment(); break;
            }
        }
        if (!fragment.isAdded()) {
            fragment.setArguments(arguments);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}