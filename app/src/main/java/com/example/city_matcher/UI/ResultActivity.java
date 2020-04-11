package com.example.city_matcher.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.city_matcher.Model.AccountSingleton;
import com.example.city_matcher.Model.CityMatchResultWrapper;
import com.example.city_matcher.R;
import com.example.city_matcher.UI.cityResults.ChicagoResultFragment;
import com.example.city_matcher.UI.cityResults.DallasResultFragment;
import com.example.city_matcher.UI.cityResults.HoustonResultFragment;
import com.example.city_matcher.UI.cityResults.LosAngelesResultFragment;
import com.example.city_matcher.UI.cityResults.NewYorkResultFragment;
import com.example.city_matcher.UI.cityResults.NothingResultFragment;
import com.example.city_matcher.UI.cityResults.PhiladelphiaResultFragment;
import com.example.city_matcher.UI.cityResults.PhoenixResultFragment;
import com.example.city_matcher.UI.cityResults.SanAntonioResultFragment;
import com.example.city_matcher.UI.cityResults.SanDiegoResultFragment;
import com.example.city_matcher.UI.cityResults.SanJoseResultFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";

    // get database reference
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mAccountsRef = mRootRef.child("accounts");
    private FirebaseUser user;

    //fragment manager
    private FragmentManager fm;
    private Fragment fragment;

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
        Log.d(TAG, "onCreate: job count result " + jobCount);
        Log.d(TAG, "onCreate: city result " + maxScoreCity);

        // update database with max city
        user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        mAccountsRef.child(id).child("city").setValue(maxScoreCity);
    }

    @Override
    public void onStart() {
        super.onStart();

        // display max value fragment
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
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