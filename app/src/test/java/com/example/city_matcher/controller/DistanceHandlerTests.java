package com.example.city_matcher.controller;

import com.example.city_matcher.model.CoordinatesWrapper;

import org.junit.Test;

import static org.junit.Assert.*;

public class DistanceHandlerTests {

    private static CoordinatesWrapper location1;
    private static CoordinatesWrapper location2;

    @Test
    public void distanceInMilesSuccessSmallDistance() {
        // assign two locations
        location1 = new CoordinatesWrapper(34, -118);
        location2 = new CoordinatesWrapper(32.7, -117);

        // precondition check
        assertNotNull(location1);
        assertNotNull(location2);

        // set calculated and expected values. Compare for equality
        int calculatedDistance = (int) DistanceHandler.distanceInMiles(location1, location2);
        int expectedDistance = 106; //distance in miles

        assertEquals(expectedDistance, calculatedDistance);
    }

    @Test
    public void distanceInMilesSuccessSmallDistance2() {
        // assign two locations
        location1 = new CoordinatesWrapper(34, -118);
        location2 = new CoordinatesWrapper(34.3, -118.5);

        // precondition check
        assertNotNull(location1);
        assertNotNull(location2);

        // set calculated and expected values. Compare for equality
        int calculatedDistance = (int) DistanceHandler.distanceInMiles(location1, location2);
        int expectedDistance = 35; //distance in miles

        assertEquals(expectedDistance, calculatedDistance);
    }

    @Test
    public void distanceInMilesSuccessLargeDistance() {
        // assign two locations
        location1 = new CoordinatesWrapper(34, -118);
        location2 = new CoordinatesWrapper(53.3, -6.2);

        // precondition check
        assertNotNull(location1);
        assertNotNull(location2);

        // set calculated and expected values. Compare for equality
        int calculatedDistance = (int) DistanceHandler.distanceInMiles(location1, location2);
        int expectedDistance = 5155; //distance in miles

        assertEquals(expectedDistance, calculatedDistance);
    }

    @Test
    public void distanceInMilesSuccessLargeDistance2() {
        // assign two locations
        location1 = new CoordinatesWrapper(40.7, -74);
        location2 = new CoordinatesWrapper(55.7, 37.6);

        // precondition check
        assertNotNull(location1);
        assertNotNull(location2);

        // set calculated and expected values. Compare for equality
        int calculatedDistance = (int) DistanceHandler.distanceInMiles(location1, location2);
        int expectedDistance = 4665; //distance in miles

        assertEquals(expectedDistance, calculatedDistance);
    }

    @Test
    public void distanceInMilesAssertFail() {
        // assign two locations
        location1 = new CoordinatesWrapper(34, -118);
        location2 = new CoordinatesWrapper(33, -117);

        // precondition check
        assertNotNull(location1);
        assertNotNull(location2);

        // set calculated and expected values. Compare for equality
        int calculatedDistance = (int) DistanceHandler.distanceInMiles(location1, location2);
        int excessiveDistance = 1000; //distance in miles

        assertTrue(calculatedDistance < excessiveDistance);
    }
}