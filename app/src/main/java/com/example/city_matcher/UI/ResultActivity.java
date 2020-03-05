package com.example.city_matcher.UI;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.city_matcher.R;


public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        // get max city
        String maxScoreCity = getIntent().getStringExtra("result");
        Toast.makeText(this, "Select: " + maxScoreCity, Toast.LENGTH_SHORT).show();

        // display max value fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
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
                default: fragment = new NewYorkResultFragment();
            }
        }

        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }
}

