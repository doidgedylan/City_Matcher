package com.example.city_matcher.model;

import android.content.Context;

import com.example.city_matcher.R;

import java.util.HashMap;

public class JobIndustryToIndex {
    // maping for real time database retrieval
    private static HashMap<String, String> jobCountIndex = new HashMap<>();
    private JobIndustryToIndex() {} // shouldn't be instantiated

    // for firebase data, map industry to job count index
    public static String convertJob(String job, Context context) {
        jobCountIndex.put(context.getResources().getString(R.string.Engineering), "7");
        jobCountIndex.put(context.getResources().getString(R.string.Marketing), "8");
        jobCountIndex.put(context.getResources().getString(R.string.financeAndAccounting), "9");
        jobCountIndex.put(context.getResources().getString(R.string.Teaching), "10");
        jobCountIndex.put(context.getResources().getString(R.string.humanResources), "11");
        jobCountIndex.put(context.getResources().getString(R.string.artsAndDesign), "12");
        return jobCountIndex.get(job);
    }
}
