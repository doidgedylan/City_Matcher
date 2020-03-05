package com.example.city_matcher.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.city_matcher.Model.AccountSingleton;
import com.example.city_matcher.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LandingActivity extends AppCompatActivity {

    private static final String TAG = "LandingActivity";

    // get database references
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mAccountsRef = mRootRef.child("accounts");
    FirebaseUser user;

    //intent
    Intent loginIntent;
    Activity classLevelThis;

    // get UI elements
    Button quizButton;
    Button logoutButton;
    Button deleteAccountButton;

    //database reference
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);
        Log.d(TAG, "OnCreate: hit and activated");

        // grab button elements by id
        quizButton = (Button) findViewById(R.id.goToQuizButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);

        // init auth reference
        mAuth = FirebaseAuth.getInstance();

        // to access activity within inner class deleting accounts
        classLevelThis = this;
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
                openResult();
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
    }


    // ***** PRIVATE HELPER METHODS ***** //
    private void openResult() {
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
                            String id = AccountSingleton.getInstance().getID(user.getEmail());
                            mAccountsRef.child(id).removeValue();

                            // go back to sign in page
                            startActivity(loginIntent);
                            classLevelThis.finish();
                        }
                    }
                });
    }
}
