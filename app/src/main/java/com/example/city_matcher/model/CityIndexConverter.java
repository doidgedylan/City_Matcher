package com.example.city_matcher.model;

import java.util.HashMap;

public class CityIndexConverter {
    // indexed of cities in real time database
    private static HashMap<String, String> citiesIndex = new HashMap<>();
    private CityIndexConverter() {} //shouldn't be instantiated

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
    public static String convertCity(String city) {
        citiesIndex.put("New York", "1");
        citiesIndex.put("Chicago", "2");
        citiesIndex.put("Dallas", "3");
        citiesIndex.put("Houston", "4");
        citiesIndex.put("Los Angeles", "5");
        citiesIndex.put("Philadelphia", "6");
        citiesIndex.put("Phoenix", "7");
        citiesIndex.put("San Antonio", "8");
        citiesIndex.put("San Diego", "9");
        citiesIndex.put("San Jose", "10");
        return citiesIndex.get(city);
    }
}
