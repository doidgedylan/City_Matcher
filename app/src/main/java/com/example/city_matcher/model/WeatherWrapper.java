package com.example.city_matcher.model;

public class WeatherWrapper {
    private int avgSummerTemp;
    private int avgWinterTemp;
    public WeatherWrapper(int high, int low) {
        avgSummerTemp = high;
        avgWinterTemp = low;
    }
    public int getAvgSummerTemp() { return avgSummerTemp; }
    public int getAvgWinterTemp() { return avgWinterTemp; }
    public void setAvgSummerTemp(int temp) { avgSummerTemp = temp; }
    public void setAvgWinterTemp(int temp) { avgWinterTemp = temp; }
}
