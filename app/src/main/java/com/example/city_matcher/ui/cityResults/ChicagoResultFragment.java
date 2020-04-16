package com.example.city_matcher.ui.cityResults;

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
import com.example.city_matcher.ui.LandingActivity;

public class ChicagoResultFragment extends Fragment {
    private static final String TAG = "ChicagoResultFragment";

    private int jobCount;
    private TextView textInjectJobsParks;
    private TextView weatherSummer;
    private TextView weatherWinter;
    private Button returnHomeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView: started.");
        View v = inflater.inflate(R.layout.chicago_fragment, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            jobCount = bundle.getInt("jobCount", -1);
        }
        textInjectJobsParks = v.findViewById(R.id.openingsOrParkTextView);
        weatherSummer = v.findViewById(R.id.weatherSummer);
        weatherWinter = v.findViewById(R.id.weatherWinter);
        returnHomeButton = v.findViewById(R.id.returnHomeButton);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        int parkCount = 570;
        int summerTemp = 75;
        int winterTemp = 29;

        if (jobCount != 0) {
            textInjectJobsParks.append(getContext().getResources().getString(R.string.jobOpeningsPrompt) + " " + jobCount);
        } else {
            textInjectJobsParks.append(getContext().getResources().getString(R.string.parkCountPrompt) + " " + parkCount);
        }
        weatherSummer.append("AVG summer temp: " + summerTemp);
        weatherWinter.append("AVG winter temp: " + winterTemp);

        //listener
        returnHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getActivity(), LandingActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                getActivity().finish();
            }
        });
    }
}
