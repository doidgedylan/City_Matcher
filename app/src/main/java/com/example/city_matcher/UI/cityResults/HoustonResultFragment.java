package com.example.city_matcher.UI.cityResults;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.city_matcher.R;

public class HoustonResultFragment extends Fragment {
    private static final String TAG = "HoustonResultFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView: started.");
        return inflater.inflate(R.layout.houston_fragment, container, false);
    }
}
