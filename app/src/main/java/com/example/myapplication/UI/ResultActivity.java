package com.example.myapplication.UI;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.myapplication.Model.AccountSingleton;
import com.example.myapplication.R;
import com.example.myapplication.UI.cityResults.ChicagoResultFragment;
import com.example.myapplication.UI.cityResults.DallasResultFragment;
import com.example.myapplication.UI.cityResults.HoustonResultFragment;
import com.example.myapplication.UI.cityResults.LosAngelesResultFragment;
import com.example.myapplication.UI.cityResults.NewYorkResultFragment;
import com.example.myapplication.UI.cityResults.PhiladelphiaResultFragment;
import com.example.myapplication.UI.cityResults.PhoenixResultFragment;
import com.example.myapplication.UI.cityResults.SanAntonioResultFragment;
import com.example.myapplication.UI.cityResults.SanDiegoResultFragment;
import com.example.myapplication.UI.cityResults.SanJoseResultFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mAccountsRef = mRootRef.child("accounts");
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        // get max city
        String maxScoreCity = getIntent().getStringExtra("result");
        Toast.makeText(this, "Select: " + maxScoreCity, Toast.LENGTH_SHORT).show();
        if (maxScoreCity.isEmpty()) {
            maxScoreCity = "New York";
        }

        // update database with max city
        user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        Log.d(TAG, "onCreate: ******* " + id + " ******");
        mAccountsRef.child(id).child("city").setValue(maxScoreCity);

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

    @Override
    public void onStart() {
        super.onStart();
        // update database with max city
        user = FirebaseAuth.getInstance().getCurrentUser();
        String id = AccountSingleton.getInstance().getID(user.getEmail());
        Log.d(TAG, "onStart: " +AccountSingleton.getInstance().getMap().toString() );
        Log.d(TAG, "onCreate: ******* " + id + " ******");
        Log.d(TAG, "UID: " + user.getUid());
    }
}

