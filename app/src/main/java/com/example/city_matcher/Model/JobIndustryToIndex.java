package com.example.city_matcher.Model;

import java.util.HashMap;

public class JobIndustryToIndex {
    // maping for real time database retrieval
    private static HashMap<String, String> jobCountIndex = new HashMap<>();
    private JobIndustryToIndex() {} // shouldn't be instantiated

    // for firebase data, map industry to job count index
    public static String convertJob(String job) {
        jobCountIndex.put("Engineering", "7");
        jobCountIndex.put("Marketing", "8");
        jobCountIndex.put("Finance and Accounting", "9");
        jobCountIndex.put("Teaching", "10");
        jobCountIndex.put("Human Resources", "11");
        jobCountIndex.put("Arts and Design", "12");
        return jobCountIndex.get(job);
    }
}
