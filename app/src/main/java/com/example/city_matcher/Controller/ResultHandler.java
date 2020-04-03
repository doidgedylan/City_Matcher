package com.example.city_matcher.Controller;
import android.util.Log;
import com.example.city_matcher.Model.CityIndexToName;
import com.example.city_matcher.Model.CoordinatesWrapper;
import com.example.city_matcher.Model.WeatherWrapper;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResultHandler {
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

    // map to store real time database data for processing
    private static HashMap<String,Integer> industryJobCounts;
    private static HashMap<String, Integer> costOfLivingCounts;
    private static HashMap<String, Integer> parkCounts;
    private static HashMap<String, WeatherWrapper> weatherCounts;
    private static HashMap<String, Integer> drinkCounts;
    private static HashMap<String, CoordinatesWrapper> distanceCounts;

    // used to keep track of city scores and answers
    private static HashMap<String, Integer> cityScores;
    private static String highestValue;
    private static String industry;
    private static String drink;
    private static String maxDistance;
    private static int iterateCount;
    private static CoordinatesWrapper currentLoc;

    public ResultHandler() {
        cityScores = new HashMap<>(); // keep track of city scores
        industryJobCounts = new HashMap<>(); // keep track of jobs for selected industry
        costOfLivingCounts = new HashMap<>(); // keep track of cost of living indexes by city
        parkCounts = new HashMap<>(); // keep track of park counts by city for score calculations
        weatherCounts = new HashMap<>(); //hold weather data for processing
        distanceCounts = new HashMap<>(); //hold distance calc from each city
        drinkCounts = new HashMap<>(); // keep track of how many drinks are in each city for processing
        currentLoc = new CoordinatesWrapper(0,0);
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
                String city = CityIndexToName.convertIndex(entry.getKey());
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
                String city = CityIndexToName.convertIndex(entry.getKey());
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
                String city = CityIndexToName.convertIndex(entry.getKey());
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
                String city = CityIndexToName.convertIndex(entry.getKey());
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
                String city = CityIndexToName.convertIndex(entry.getKey());
                cityScores.put(city, cityScores.get(city) + i);
                i += 1;
            }
        }
    }

    private static void processDistanceData(String data, String valueIndex, String parentCityIndex) {
        if (valueIndex.equals("13")) {
            if (distanceCounts.containsKey(parentCityIndex)) {
                distanceCounts.get(parentCityIndex).setLatitude(Double.parseDouble(data));
            } else {
                distanceCounts.put(parentCityIndex, new CoordinatesWrapper(Double.parseDouble(data),0));
            }
        } else {
            if (distanceCounts.containsKey(parentCityIndex)) {
                distanceCounts.get(parentCityIndex).setLongitude(Double.parseDouble(data));
            } else {
                distanceCounts.put(parentCityIndex, new CoordinatesWrapper(0,Double.parseDouble(data)));
            }
        }
        if (finishedGettingDistanceData(distanceCounts)) {
            double maxDi = parseDistanceString(maxDistance);
            // filter and remove cities with a distance further than 'maxDi'
            for (Map.Entry<String,CoordinatesWrapper> entry : distanceCounts.entrySet()) {
                double dist = DistanceHandler.distanceInMiles(currentLoc,entry.getValue());
                if (dist > maxDi) {
                    cityScores.put(CityIndexToName.convertIndex(entry.getKey()),0);
                }
            }
        }
    }

    private static boolean finishedGettingDistanceData(HashMap<String, CoordinatesWrapper> dCounts) {
        for (Map.Entry<String,CoordinatesWrapper> entry : dCounts.entrySet()) {
            if ((entry.getValue().getLatitude() == 0 ||
                    entry.getValue().getLongitude() == 0) || !(dCounts.size() == 10)) {
                return false;
            }
        }
        return true;
    }

    private static double parseDistanceString(String disString) {
        double result = 0;
        if (maxDistance.equals("less than 250mi")) {
            String number = disString.substring(disString.indexOf("2"),disString.indexOf("2")+3);
            result = Double.parseDouble(number);
        } else if (maxDistance.equals("less than 500mi")) {
            String number = disString.substring(disString.indexOf("5"),disString.indexOf("5")+3);
            result = Double.parseDouble(number);
        }
        return result;
    }

    // **** PUBLIC METHODS ****
    public void processData(String data, String valueIndex, String parentCity) {
        iterateCount+=1; // when this is 10 move to result page
        if (Integer.parseInt(valueIndex) > 6 && Integer.parseInt(valueIndex) < 13){
            processIndustryData(data, parentCity);
        }
        else if(Integer.parseInt(valueIndex) == 3) { processCostOfLivingData(data, parentCity); }
        else if (Integer.parseInt(valueIndex) == 5) {processParkData(data, parentCity);}
        else if (Integer.parseInt(valueIndex) == 1 || Integer.parseInt(valueIndex)== 2) {
            processWeatherData(data, valueIndex, parentCity);
        }
        else if (Integer.parseInt(valueIndex) == 4 || Integer.parseInt(valueIndex) == 6) {
            processDrinkData(data, parentCity);
        }
        else if (Integer.parseInt(valueIndex) == 13 || Integer.parseInt(valueIndex) == 14) {
            processDistanceData(data, valueIndex, parentCity);
        }
    }

    public String getResult() {
        String result = "";
        int maxScore = 0;
        for (Map.Entry<String,Integer> entry : cityScores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                result = entry.getKey();
            }
        }
        if (maxScore == 0) {
            result = "nothing found";
        }
        return result;
    }

    // setter methods for question activity to call
    public void setValue(String highestPriority) { highestValue = highestPriority; }
    public void setIndustry(String mIndustry) { industry = mIndustry; }
    public void setDrink(String mDrink) { drink = mDrink; }
    public void setMaxDistance(String mDistance) {maxDistance = mDistance;}
    public String getHighestValue() { return highestValue; }
    public String getIndustry() {return industry; }
    public String getDrink() {return drink; }
    public String getMaxDistance() {return maxDistance; }
    public int getIterateCount() {return iterateCount; }
    public void addToIterateCount(int amt) {iterateCount+=amt;}
    public void setCurrentLocation(double lat, double lng) {
        currentLoc.setLongitude(lng);
        currentLoc.setLatitude(lat);
    }
}