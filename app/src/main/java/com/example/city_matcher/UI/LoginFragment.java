package com.example.city_matcher.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.city_matcher.Model.Account;
import com.example.city_matcher.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    // define UI elements
    EditText emailEdit;
    EditText passwordEdit;
    String email;
    String password;
    Button loginButton;
    Button newAccountButton;

    // get Firebase reference
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mAccountsRef = mRootRef.child("accounts");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        // get UI elements
        emailEdit = (EditText) v.findViewById(R.id.username);
        passwordEdit = (EditText) v.findViewById(R.id.password);
        loginButton = (Button) v.findViewById(R.id.submitLogin);
        newAccountButton = (Button) v.findViewById(R.id.goToCreateAccount);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        // set listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get email and password
                email = emailEdit.getText().toString();
                password = passwordEdit.getText().toString();

                // write them to real time database
                Account testAccount = new Account(email, password);
                mAccountsRef.child(email).setValue(testAccount);

                //mAccountsRef.setValue(email);
                Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();
            }
        });
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getActivity(), NewUserActivity.class);
                startActivity(mIntent);
            }
        });
    }
}
