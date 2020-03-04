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

public class LoginFragment extends Fragment {
    EditText usernameEdit;
    Button loginButton;

    String username;
    String password;

    private static final String TAG = "LoginFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, container, false);


        // get reference
        usernameEdit = (EditText) v.findViewById(R.id.username);
        loginButton = (Button) v.findViewById(R.id.submitLogin);

        // onClickListeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEdit.getText().toString();
                Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
