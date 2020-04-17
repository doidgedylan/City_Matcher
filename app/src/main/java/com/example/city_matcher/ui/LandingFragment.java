package com.example.city_matcher.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.city_matcher.controller.GPSTracker;
import com.example.city_matcher.model.AccountSingleton;
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

public class LandingFragment extends Fragment {
    // get database references
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mAccountsRef = mRootRef.child("accounts");
    //DatabaseReference mCityRef = mRootRef.child("cities");
    private FirebaseUser user;
    private ValueEventListener postListener;

    //database authentication reference
    private FirebaseAuth mAuth;

    //intent
    private Intent loginIntent;

    // get UI elements
    private Button quizButton;
    private Button displayMatchedCityButton;
    private Button logoutButton;
    private Button deleteAccountButton;
    private Button showCoordinatesButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.landing_fragment, container, false);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);

        // grab button elements by id
        quizButton = v.findViewById(R.id.goToQuizButton);
        logoutButton = v.findViewById(R.id.logoutButton);
        deleteAccountButton = v.findViewById(R.id.deleteAccountButton);
        displayMatchedCityButton = v.findViewById(R.id.showMatchedCityButton);
        showCoordinatesButton = v.findViewById(R.id.showCoordinatesButton);

        // set real time database persistence for accounts
        mAccountsRef.keepSynced(true);

        // init auth reference
        mAuth = FirebaseAuth.getInstance();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        loginIntent = new Intent(getActivity(), LoginActivity.class);
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
        showCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastCoordinates();
            }
        });
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null && getActivity() != null) {
                    String city = dataSnapshot.getValue().toString();
                    Toast.makeText(getActivity(), city , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
    }

    // ***** PRIVATE HELPER METHODS ***** //
    private void openQuiz() {
        Intent mIntent = new Intent(getActivity(), QuestionActivity.class);
        startActivity(mIntent);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(loginIntent);
        getActivity().finish();
    }

    private void deleteAccount() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

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

    private void toastCoordinates() {
        GPSTracker locTracker = new GPSTracker(getContext());
        Location loc = locTracker.getLocation();
        if (loc != null) {
            String coordinates = this.getResources().getString(R.string.latitude)
                    + loc.getLatitude() + "\n" + this.getResources().getString(R.string.longitude)
                    + loc.getLongitude();
            Toast.makeText(getContext(), coordinates , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), this.getResources().getString(R.string.locOffToast) , Toast.LENGTH_LONG).show();
        }
    }
}