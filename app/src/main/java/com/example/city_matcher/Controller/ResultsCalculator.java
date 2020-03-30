package com.example.city_matcher.Controller;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ResultsCalculator {

    /*
     * BASIC IDEA:
     * calculate a score for each city based on the users answers
     * and return the city with the highest score as the result.
     * To calculate the score of a choice/question, rank the city's from best to worst
     * using their respective values in the firebase real time database. Add 10 points
     * to the best city, and 1 point to the worst city. This will equalize the weight
     * of each question. If we didn't do this, the industry score would drown out
     * every other option because job counts are so much higher than any
     * other metric we look at.
     */

    // error logging
    private static final String TAG = "ResultCalculator";

    // get database references
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCityRef = mRootRef.child("cities");
    private ValueEventListener industryListener;

    // map to store city index numbers for firebase reference
    private HashMap<String, String> citiesIndex;
    private HashMap<String, String> jobCountIndex;
    private String cityReadResult;
    HashMap<String,String> industryJobCounts;

    // used to keep track of city scores and answers
    private static HashMap<String, Integer> cityScores;
    private static String highestValue;
    private static String industry;
    //private static String favoriteDrink;
    //private static String distance;


    public ResultsCalculator() {
        cityScores = new HashMap<>(); // keep track of city scores
        industryJobCounts = new HashMap<>(); // keep track of jobs for selected industry

        citiesIndex = new HashMap<>();
        jobCountIndex = new HashMap<>();

        cityScores.put("Chicago", 0);
        cityScores.put("New York", 0);
        cityScores.put("Los Angeles", 0);
        cityScores.put("Houston", 0);
        cityScores.put("Phoenix", 0);
        cityScores.put("Philadelphia", 0);
        cityScores.put("San Antonio", 0);
        cityScores.put("San Diego", 0);
        cityScores.put("Dallas", 0);
        cityScores.put("San Jose", 0);

        // corresponds to firebase index for easy retrieval
        citiesIndex.put("1", "New York");
        citiesIndex.put("2", "Chicago");
        citiesIndex.put("3", "Dallas");
        citiesIndex.put("4", "Houston");
        citiesIndex.put("5", "Los Angeles");
        citiesIndex.put("6", "Philadelphia");
        citiesIndex.put("7", "Phoenix");
        citiesIndex.put("8", "San Antonio");
        citiesIndex.put("9", "San Diego");
        citiesIndex.put("10", "San Jose");
        jobCountIndex.put("Engineering", "7");
        jobCountIndex.put("Marketing", "8");
        jobCountIndex.put("Finance and Accounting", "9");
        jobCountIndex.put("Teaching", "10");
        jobCountIndex.put("Human Resources", "11");
        jobCountIndex.put("Arts and Design", "12");
    }

    // partial credit: using a tutorial on sorting here to help write this function
    private static HashMap sortByValues(HashMap map) {
        List tempList = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(tempList, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = tempList.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    /*
    * rank cities with the job count that's best for their industry,
    * add the corresponding point totals (1-10)
    */
    private void processCareerCalculation(String mIndustry) {
        // define value event listeners for read processing
        //cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
        industryJobCounts.clear();

        industryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cityReadResult = dataSnapshot.getValue().toString();
                DatabaseReference parentCity = dataSnapshot.getRef().getParent();
                industryJobCounts.put(parentCity.getKey(), cityReadResult);
                if (industryJobCounts.size() > 1) {
                    HashMap<String, String> sortedCounts = sortByValues(industryJobCounts);

                    // add points accordingly
                    int i = 1;
                    for (Map.Entry<String,String> entry : sortedCounts.entrySet()) {
                        Log.d(TAG, "onDataChange: entry " + entry.getValue());
                        String city = citiesIndex.get(entry.getKey());
                        Log.d(TAG, "onDataChange: city " + city);
                        cityScores.put(city, cityScores.get(city) + i);
                        i += 1;
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        mCityRef.child("1").child(jobCountIndex.get(mIndustry)).addValueEventListener(industryListener);
        mCityRef.child("2").child(jobCountIndex.get(mIndustry)).addValueEventListener(industryListener);
    }

    private void calculateValuesScore() {
        switch(highestValue) {
            case("Career"):
                processCareerCalculation(industry);
                break;
            case("Family"): break;
            case("Education"): break;
            case("Cost of Living"): break;
            default: break;
        }
    }

    private void calculateIndustryScore() {

    }

    private void calculateDrinkScore() {

    }

    private void calculateDistanceScore() {

    }

    public String getResult() {
        Log.d(TAG, "getResult: result " + cityScores.toString());
        String result = "";
        int maxScore = 0;
        for (Map.Entry<String,Integer> entry : cityScores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                result = entry.getKey();
            }
        }
        return "New York";
    }

    public void calculateResult() {
        calculateValuesScore();
        calculateIndustryScore();
        calculateDrinkScore();
        calculateDistanceScore();
    }

    public void wipeResult() {
        for (Map.Entry<String,Integer> entry : cityScores.entrySet()) {
            cityScores.put(entry.getKey(), 0);
        }
    }

    // setter methods for question activity to call
    public void setValue(String highestPriority) { highestValue = highestPriority; }
    public void setIndustry(String mIndustry) { industry = mIndustry; }
}
