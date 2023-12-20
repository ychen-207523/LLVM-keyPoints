package org.parking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DriverTest {

    @Test
    public void checkDriverName() {
        Driver driver = new Driver("123", "Bob", "S");
        assertEquals("Bob", driver.getName(), "Driver name should be what we created it with");
    }

    @Test
    public void checkDriverStatus() {
        Driver driver = new Driver("123", "Bob", "S");
        assertEquals("S", driver.getStatus(), "Driver status should be what we created it with");
    }

    @Test
    public void checkDriverId() {
        Driver driver = new Driver("123", "Bob", "S");
        assertEquals("123", driver.getId(), "Driver status should be what we created it with");
    }

    @Test
    public void checkDriverSetId() {
        Driver driver = new Driver("123", "Bob", "S");
        driver.setId("456");
        assertEquals("456", driver.getId(), "Driver status should be what we created it with");
    }
}