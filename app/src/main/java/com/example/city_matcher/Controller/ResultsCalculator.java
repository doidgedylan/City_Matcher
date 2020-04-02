package com.example.city_matcher.Controller;

import android.content.Context;
import android.util.Log;

import com.example.city_matcher.Model.WeatherWrapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ResultsCalculator {
    /*
     * BASIC IDEA:
     * calculate a score for each city based on the users answers
     * and return the city with the highest score as the result.
     * To calculate the score of a choice/question, rank the city's from best to worst
     * using their respective values in the firebase real time database. Add 10 points
     * to the best city, and 1 point to the worst city. This will equalize the weight
     * of each question, while providing value for being 2nd best etc.
     * If we didn't do this, the industry score would drown out
     * every other option because job counts are so much higher than any
     * other metric we look at.
     */

    // error logging
    private static final String TAG = "ResultCalculator";

    // map to store city index numbers for firebase reference
    private HashMap<String, String> jobCountIndex;
    private static HashMap<String, String> citiesIndex;

    // map to store real time database data for processing
    private static HashMap<String,Integer> industryJobCounts;
    private static HashMap<String, Integer> costOfLivingCounts;
    private static HashMap<String, Integer> parkCounts;
    private static HashMap<String, WeatherWrapper> weatherCounts;
    private static HashMap<String, Integer> drinkCounts;

    // used to keep track of city scores and answers
    private static HashMap<String, Integer> cityScores;
    private static String highestValue;
    private static String industry;
    private static String drink;
    private static int iterateCount;

    public ResultsCalculator() {

        cityScores = new HashMap<>(); // keep track of city scores
        industryJobCounts = new HashMap<>(); // keep track of jobs for selected industry
        costOfLivingCounts = new HashMap<>(); // keep track of cost of living indexes by city
        parkCounts = new HashMap<>(); // keep track of park counts by city for score calculations
        weatherCounts = new HashMap<>(); //hold weather data for processing
        drinkCounts = new HashMap<>(); // keep track of how many drinks are in each city for processing
        citiesIndex = new HashMap<>(); // index of cities in real time database
        jobCountIndex = new HashMap<>(); // index of job count in real time database
        iterateCount = 0;

        // scores to track option
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

    // function to sort hashmap by values
    private static HashMap<String, Integer> sortByValues(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list = new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    private static void processIndustryData(String data, String parentCityIndex) {
        industryJobCounts.put(parentCityIndex, Integer.parseInt(data));
        if (industryJobCounts.size() >= 10) {
            // add points according to ranking now that we've added every city count
            HashMap<String, Integer> sortedCounts = sortByValues(industryJobCounts);
            int i = 1;
            for (Map.Entry<String,Integer> entry : sortedCounts.entrySet()) {
                String city = citiesIndex.get(entry.getKey());
                cityScores.put(city, cityScores.get(city) + i);
                i += 1;
            }
        }
    }

    private static void processCostOfLivingData(String data, String parentCityIndex) {
        costOfLivingCounts.put(parentCityIndex, Integer.parseInt(data));
        if (costOfLivingCounts.size() >= 10) {
            // add points according to ranking now that we've added every city index
            HashMap<String, Integer> sortedCounts = sortByValues(costOfLivingCounts);
            int i = 10; // because lower is better for cost of living index
            for (Map.Entry<String,Integer> entry : sortedCounts.entrySet()) {
                String city = citiesIndex.get(entry.getKey());
                cityScores.put(city, cityScores.get(city) + i);
                i -= 1;
            }
        }
    }

    private static void processParkData(String data, String parentCityIndex) {
        parkCounts.put(parentCityIndex, Integer.parseInt(data));
        if (parkCounts.size() >= 10) {
            // add points according to ranking now that we've added every city index
            HashMap<String, Integer> sortedCounts = sortByValues(parkCounts);
            int i = 1;
            for (Map.Entry<String,Integer> entry : sortedCounts.entrySet()) {
                String city = citiesIndex.get(entry.getKey());
                cityScores.put(city, cityScores.get(city) + i);
                i += 1;
            }
        }
    }

    private static void processWeatherData (String data, String valueIndex, String parentCityIndex){
        if (valueIndex.equals("1")) {
            if (weatherCounts.containsKey(parentCityIndex)) {
                weatherCounts.get(parentCityIndex).setAvgSummerTemp(Integer.parseInt(data));
            } else {
                weatherCounts.put(parentCityIndex, new WeatherWrapper(Integer.parseInt(data),0));
            }
        } else {
            if (weatherCounts.containsKey(parentCityIndex)) {
                weatherCounts.get(parentCityIndex).setAvgWinterTemp(Integer.parseInt(data));
            } else {
                weatherCounts.put(parentCityIndex, new WeatherWrapper(0,Integer.parseInt(data)));
            }
        }
        if (finishedGettingWeatherData(weatherCounts)) {
            // calculate difference in averages and sort based on that
            HashMap<String, Integer> weatherDifferenceMapping = new HashMap<>();
            for (Map.Entry<String,WeatherWrapper> entry : weatherCounts.entrySet()) {
                int avgDifference = entry.getValue().getAvgSummerTemp()
                        - entry.getValue().getAvgWinterTemp();
                weatherDifferenceMapping.put(entry.getKey(), avgDifference);
            }

            // add points according to ranking now that we've calculated difference
            HashMap<String, Integer> sortedCounts = sortByValues(weatherDifferenceMapping);
            int i = 10;
            for (Map.Entry<String,Integer> entry : sortedCounts.entrySet()) {
                String city = citiesIndex.get(entry.getKey());
                cityScores.put(city, cityScores.get(city) + i);
                i -= 1;
            }
        }
    }

    private static boolean finishedGettingWeatherData(HashMap<String, WeatherWrapper> wCounts) {
        for (Map.Entry<String,WeatherWrapper> entry : wCounts.entrySet()) {
            if ((entry.getValue().getAvgSummerTemp() == 0 ||
                    entry.getValue().getAvgWinterTemp() == 0) || !(wCounts.size() == 10)) {
                return false;
            }
        }
        return true;
    }

    private static void processDrinkData(String data, String parentCityIndex) {
        drinkCounts.put(parentCityIndex, Integer.parseInt(data));
        if (drinkCounts.size() >= 10) {
            // add points according to ranking now that we've added every city index
            HashMap<String, Integer> sortedCounts = sortByValues(drinkCounts);
            int i = 1;
            for (Map.Entry<String,Integer> entry : sortedCounts.entrySet()) {
                String city = citiesIndex.get(entry.getKey());
                cityScores.put(city, cityScores.get(city) + i);
                i += 1;
            }
            Log.d(TAG, "processDrinkData: " + sortedCounts.toString());
        }
    }

    // **** PUBLIC METHODS ****
    public void processData(String data, String valueIndex, String parentCity) {
        iterateCount+=1; // when this is 10 move to result page
        if (Integer.parseInt(valueIndex) > 6 && Integer.parseInt(valueIndex) < 13) {
            processIndustryData(data, parentCity);
        }
        else if(Integer.parseInt(valueIndex) == 3) {
            processCostOfLivingData(data, parentCity);
        }
        else if (Integer.parseInt(valueIndex) == 5) {
            processParkData(data, parentCity);
        }
        else if (Integer.parseInt(valueIndex) == 1
                || Integer.parseInt(valueIndex)== 2) {
            processWeatherData(data, valueIndex, parentCity);
        }
        else if (Integer.parseInt(valueIndex) == 4
                || Integer.parseInt(valueIndex) == 6) {
            processDrinkData(data, parentCity);
        }
    }

    public String getResult() {
        //test location tracker class
        Log.d(TAG, "getResult: result cities " + cityScores.toString());
        String result = "";
        int maxScore = 0;
        for (Map.Entry<String,Integer> entry : cityScores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                result = entry.getKey();
            }
        }
        return result;
    }

    public void clearData() {
        cityScores = new HashMap<>(); // keep track of city scores
        industryJobCounts = new HashMap<>(); // keep track of jobs for selected industry
        costOfLivingCounts = new HashMap<>(); // keep track of cost of living indexes by city
        parkCounts = new HashMap<>(); // keep track of park counts by city for score calculations
        citiesIndex = new HashMap<>(); // index of cities in real time database
        jobCountIndex = new HashMap<>(); // index of job count in real time database
        weatherCounts = new HashMap<>(); //hold weather data for processing
        iterateCount = 0;
    }

    // setter methods for question activity to call
    public void setValue(String highestPriority) { highestValue = highestPriority; }
    public void setIndustry(String mIndustry) { industry = mIndustry; }
    public void setDrink(String mDrink) { drink = mDrink; }
    public String getHighestValue() { return highestValue; }
    public String getIndustry() {return industry; }
    public String getDrink() {return drink; }
    public String getJobCountIndex(String city) { return jobCountIndex.get(city); }
    public int getIterateCount() {return iterateCount; }
    public void addTenIterateCount() {iterateCount+=10;}
}
