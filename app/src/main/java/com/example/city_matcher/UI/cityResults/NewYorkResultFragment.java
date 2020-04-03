package com.example.city_matcher.UI.cityResults;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.city_matcher.R;
import com.example.city_matcher.UI.LandingActivity;

import org.w3c.dom.Text;

public class NewYorkResultFragment extends Fragment {
    private static final String TAG = "NewYorkResultFragment";

    private int jobCount;
    private TextView textInjectJobsParks;
    private Button returnHomeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView: started.");
        View v = inflater.inflate(R.layout.new_york_fragment, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            jobCount = bundle.getInt("jobCount", -1);
        }
        textInjectJobsParks = v.findViewById(R.id.openingsOrParkTextView);
        returnHomeButton = v.findViewById(R.id.returnHomeButton);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        int parkCount = 366;
        if (jobCount != 0) {
            textInjectJobsParks.setText("Job openings in your industry: " + jobCount);
        } else {
            textInjectJobsParks.setText("Estimate of total number of parks: " + parkCount);
        }

        //listener
        returnHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getActivity(), LandingActivity.class);
                startActivity(mIntent);
                getActivity().finish();
            }
        });
    }
}