package com.example.city_matcher.controller;

import com.example.city_matcher.model.CityMatchResultWrapper;
import com.example.city_matcher.model.CoordinatesWrapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import javax.xml.transform.Result;

import static org.junit.Assert.*;

public class ResultHandlerTests {

    @Test
    public void getResultAssertSuccess() {
        // define result engine class
        ResultHandler resEngine = new ResultHandler();

        // assert precondition
        assertNotNull(resEngine);

        // call test method
        CityMatchResultWrapper calculatedResult = resEngine.getResult();

        // define calculated and expected cities
        String expectedCity = "nothing found";
        String calculatedCity = calculatedResult.getCity();

        assertEquals(expectedCity, calculatedCity);
    }

    @Test
    public void getResultAssertFailedDallas() {
        // define result engine class
        ResultHandler resEngine = new ResultHandler();

        // assert precondition
        assertNotNull(resEngine);

        // call test method
        CityMatchResultWrapper calculatedResult = resEngine.getResult();

        // define calculated and expected cities
        String expectedCity = "Dallas";
        String calculatedCity = calculatedResult.getCity();

        assertNotEquals(expectedCity, calculatedCity);
    }

    @Test
    public void getResultAssertFailedNewYork() {
        // define result engine class
        ResultHandler resEngine = new ResultHandler();

        // assert precondition
        assertNotNull(resEngine);

        // call test method
        CityMatchResultWrapper calculatedResult = resEngine.getResult();

        // define calculated and expected cities
        String expectedCity = "New York";
        String calculatedCity = calculatedResult.getCity();

        assertNotEquals(expectedCity, calculatedCity);
    }

    @Test
    public void setLocationAssertSuccess() {
        // define result engine class
        ResultHandler resEngine = new ResultHandler();

        // assert precondition
        assertNotNull(resEngine);

        // call test method
        resEngine.setCurrentLocation(40,40);

        // define calculated and expected locations
        CoordinatesWrapper expectedLocation = new CoordinatesWrapper(40, 40);
        CoordinatesWrapper calculatedLocation = resEngine.getCurrentLocation();

        assertEquals((int)expectedLocation.getLatitude(), (int)calculatedLocation.getLatitude());
        assertEquals((int)expectedLocation.getLongitude(), (int)calculatedLocation.getLongitude());
    }

    @Test
    public void iterateCountTestAddTen() {
        // define result engine class
        ResultHandler resEngine = new ResultHandler();

        // assert precondition
        assertNotNull(resEngine);

        // call test method
        resEngine.addToIterateCount(10);

        // define calculated and expected values
        int calculatedCount = resEngine.getIterateCount();
        int expectedCount = 10;

        assertEquals(expectedCount, calculatedCount);
    }
}