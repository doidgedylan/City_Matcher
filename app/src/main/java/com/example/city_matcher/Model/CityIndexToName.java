package com.example.city_matcher.Model;

import java.util.HashMap;

public class CityIndexToName {
    // indexed of cities in real time database
    private static HashMap<String, String> citiesIndex = new HashMap<>();
    private CityIndexToName() {} //shouldn't be instantiated

    // for firebase data retrieval, map index to city
    public static String convertIndex(String index) {
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
        return citiesIndex.get(index);
    }
}
