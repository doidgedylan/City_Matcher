package com.example.city_matcher.UI;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.example.city_matcher.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    // define UI elements
    EditText usernameEdit;
    EditText passwordEdit;
    Button loginButton;
    String username;
    String password;

    // get Firebase reference
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mAccountsRef = mRootRef.child("accounts");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        // get UI elements
        usernameEdit = (EditText) v.findViewById(R.id.username);
        passwordEdit = (EditText) v.findViewById(R.id.password);
        loginButton = (Button) v.findViewById(R.id.submitLogin);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        // set listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get username and password
                username = usernameEdit.getText().toString();
                password = passwordEdit.getText().toString();

                // write them to real time database
                //String id = mAccountsRef.push().getKey();
                
                mAccountsRef.setValue(username);

                Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
