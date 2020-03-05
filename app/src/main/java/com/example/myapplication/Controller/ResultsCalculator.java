package com.example.myapplication.Controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class ResultsCalculator {

    private static final String TAG = "ResultCalculator";

    // used to keep track of cities
    private static ArrayList<City> cityScores;

    // random generator for results. Right now we want to practice CRUD
    // results calculations becoming more advanced is next on the to-do list
    private Random random = new Random();

    // wrapper for cities
    private class City {
        private String cityName;
        private int cityScore;
        private City(String name, int score) {
            cityName = name;
            cityScore = score;
        }
        private String getCityName() { return cityName; }
        private int getCityScore() { return cityScore; }
        private void setCityScore(int cityScore) { this.cityScore = cityScore; }
    }

    public ResultsCalculator() {
        cityScores = new ArrayList<>();

        cityScores.add(new City("Chicago", 0));
        cityScores.add(new City("New York", 0));
        cityScores.add(new City("Los Angeles", 0));
        cityScores.add(new City("Houston", 0));
        cityScores.add(new City("Phoenix", 0));
        cityScores.add(new City("Philadelphia", 0));
        cityScores.add(new City("San Antonio", 0));
        cityScores.add(new City("San Diego", 0));
        cityScores.add(new City("Dallas", 0));
        cityScores.add(new City("San Jose", 0));
    }

    private int getRandPosInt() {
        // for now we'll use a random number to generate the result
        // so we can practice CRUD operations. This will be more advanced
        // in the next checkpoint
        int randomInteger = random.nextInt();
        if (randomInteger < 0) {
            randomInteger = randomInteger * -1;
        }
        return randomInteger;
    }

    public String calculateResult() {
        String result = "";
        int maxScore = 0;
        for (int i = 0; i < cityScores.size(); i++) {
            Log.d(TAG, "calculateResult: CITY: " + cityScores.get(i).getCityName());
            Log.d(TAG, "calculateResult: SCORE: " + cityScores.get(i).getCityScore());
            if (cityScores.get(i).getCityScore() > maxScore) {
                maxScore = cityScores.get(i).getCityScore();
                result = cityScores.get(i).getCityName();
            }
        }
        return result;
    }

    public void wipeResult() {
        for (int i = 0; i < cityScores.size(); i++) {
            cityScores.get(i).setCityScore(0);
        }
    }

    public void calculateValuesScore(String highestPriority) {
        if (highestPriority.equals("Career")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
        if (highestPriority.equals("Family")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
        if (highestPriority.equals("Cost of Living")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
        if (highestPriority.equals("Education")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
    }

    public void calculateIndustryScore(String industry) {
        if (industry.equals("Engineering")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
        if (industry.equals("Marketing")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
        if (industry.equals("Finance and Accounting")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
        if (industry.equals("Teaching")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
        if (industry.equals("Human Resources")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
        if (industry.equals("Arts and Design")) {
            for (int i = 0; i < cityScores.size(); i++) {
                cityScores.get(i).setCityScore(cityScores.get(i).getCityScore() + getRandPosInt());
            }
        }
    }
}
