package com.example.city_matcher.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.city_matcher.Model.AccountSingleton;
import com.example.city_matcher.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LandingActivity extends AppCompatActivity {

    private static final String TAG = "LandingActivity";

    // get database references
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mAccountsRef = mRootRef.child("accounts");
    DatabaseReference mCityRef = mRootRef.child("cities");
    FirebaseUser user;
    ValueEventListener postListener;

    //database authentication reference
    private FirebaseAuth mAuth;

    //intent
    Intent loginIntent;

    // get UI elements
    Button quizButton;
    Button displayMatchedCityButton;
    Button logoutButton;
    Button deleteAccountButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);
        Log.d(TAG, "OnCreate: hit and activated");

        // grab button elements by id
        quizButton = (Button) findViewById(R.id.goToQuizButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);
        displayMatchedCityButton = (Button) findViewById(R.id.showMatchedCityButton);

        // init auth reference
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "landingPageLoginUser: " + currentUser.getEmail());

        loginIntent = new Intent(this, LoginActivity.class);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuiz();
            }
        });
        displayMatchedCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastCity();
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String city = dataSnapshot.getValue().toString();
                    Toast.makeText(getBaseContext(), city , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }


    // ***** PRIVATE HELPER METHODS ***** //
    private void openQuiz() {
        Intent mIntent = new Intent(this, QuestionActivity.class);
        startActivity(mIntent);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(loginIntent);
        this.finish();
    }

    private void deleteAccount() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "user account deleted");

                            // delete from real time database as well
                            String id = user.getUid();
                            AccountSingleton.getInstance().removeAccount(user.getEmail());
                            mAccountsRef.child(id).removeValue();

                            //go back to login
                            startActivity(loginIntent);
                        }
                    }
                });
    }

    private void toastCity() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAccountsRef.child(user.getUid()).child("city").addValueEventListener(postListener);
    }
}
