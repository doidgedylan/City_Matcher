package com.example.city_matcher.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.city_matcher.Model.Account;
import com.example.city_matcher.Model.AccountSingleton;
import com.example.city_matcher.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUserFragment extends Fragment {
    private static final String TAG = "NewUserFragment";

    // firebase inits
    private DatabaseReference mRootRef;
    private DatabaseReference mAccountsRef;
    private FirebaseAuth mAuth;

    // storage props
    private Account account;
    private String email;
    private String password;

    // set UI element properties
    private EditText EditTextEmail;
    private EditText EditTextPassword;
    private EditText EditTextConfirmPassword;
    private Button mCreateAccountButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_user_fragment, container, false);
        // get UI references by id
        EditTextEmail = v.findViewById(R.id.newAccountEmail);
        EditTextPassword = v.findViewById(R.id.newAccountPassword);
        EditTextConfirmPassword = v.findViewById(R.id.newAccountPasswordConfirm);
        mCreateAccountButton = v.findViewById(R.id.submitCreateAccountButton);

        // firebase inits
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAccountsRef = mRootRef.child("accounts");
        mAuth = FirebaseAuth.getInstance();
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: TEST");
        // onClick sign up new user
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = EditTextEmail.getText().toString();
                password = EditTextPassword.getText().toString();

                // create a new user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // write user to real time database to access later
                                    Account loginAccount = new Account(email, "nothing");
                                    //String id = mAccountsRef.push().getKey();
                                    String id = mAuth.getCurrentUser().getUid();
                                    mAccountsRef.child(id).setValue(loginAccount);

                                    // save unique id
                                    AccountSingleton.getInstance().addAccount(email, id);

                                    // provide confirmation to user of successful account
                                    Toast.makeText(getContext(), "Successfully Created Account", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d(TAG, "createUser failed", task.getException());
                                    Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
