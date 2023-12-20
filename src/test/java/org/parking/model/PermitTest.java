package org.parking.model;

import org.parking.model.Permit;

import java.sql.Date;
import java.sql.Time;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class PermitTest {

    private Permit testPermit;


    @Test
    public void checkPermitConstructor() {
        testPermit = new Permit("TTFWX", "Commuter", "B",
                "466399121", "GGdel", "Regular",
                Date.valueOf("2022-01-01"), Date.valueOf("2025-10-22"), Time.valueOf("20:00:00"));

        String permitID = "TTFWX";
        String permitType = "Commuter";
        String zoneID = "B";
        String associatedID = "466399121";
        String carLicenseNum = "GGdel";
        String spaceType = "Regular";
        Date startDate = Date.valueOf("2022-01-01");
        Date expirationDate = Date.valueOf("2025-10-22");
        Time expirationTime = Time.valueOf("20:00:00");

        assertEquals(permitID, testPermit.getPermitID(), "Permit ID should match the constructor argument");
        assertEquals(permitType, testPermit.getPermitType(), "Permit type should match the constructor argument");
        assertEquals(zoneID, testPermit.getZoneID(), "Zone ID should match the constructor argument");
        assertEquals(associatedID, testPermit.getAssociatedID(), "Associated ID should match the constructor argument");
        assertEquals(carLicenseNum, testPermit.getCarLicenseNum(), "Car license number should match the constructor argument");
        assertEquals(spaceType, testPermit.getSpaceType(), "Space type should match the constructor argument");
        assertEquals(startDate, testPermit.getStartDate(), "Start date should match the constructor argument");
        assertEquals(expirationDate, testPermit.getExpirationDate(), "Expiration date should match the constructor argument");
        assertEquals(expirationTime, testPermit.getExpirationTime(), "Expiration time should match the constructor argument");
    }

    @Test
    public void testToString() {
        testPermit = new Permit("TTFWX", "Commuter", "B",
                "466399121", "GGdel", "Regular",
                Date.valueOf("2022-01-01"), Date.valueOf("2025-10-22"), Time.valueOf("20:00:00"));
        // Given
        String expectedString = "Permit{permitID='TTFWX', permitType='Commuter', zoneID='B', associatedID='466399121', carLicenseNum='GGdel', " +
                "spaceType='Regular', startDate=2022-01-01, expirationDate=2025-10-22, expirationTime=20:00:00}";

        String actualString = testPermit.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testAllSetters() {
        // Arrange
        Permit testPermit = new Permit("TTFWX", "Commuter", "B",
                "466399121", "GGdel", "Regular",
                Date.valueOf("2022-01-01"), Date.valueOf("2025-10-22"), Time.valueOf("20:00:00"));

        // Act and Assert
        testPermit.setPermitID("NEWID");
        assertEquals("NEWID", testPermit.getPermitID(), "Permit ID should be updated.");

        testPermit.setPermitType("Handicap");
        assertEquals("Handicap", testPermit.getPermitType(), "Permit type should be updated.");

        testPermit.setZoneID("C");
        assertEquals("C", testPermit.getZoneID(), "Zone ID should be updated.");

        testPermit.setAssociatedID("999999999");
        assertEquals("999999999", testPermit.getAssociatedID(), "Associated ID should be updated.");

        testPermit.setCarLicenseNum("NEWCAR");
        assertEquals("NEWCAR", testPermit.getCarLicenseNum(), "Car license number should be updated.");

        testPermit.setSpaceType("VIP");
        assertEquals("VIP", testPermit.getSpaceType(), "Space type should be updated.");

        Date newStartDate = Date.valueOf("2023-01-01");
        testPermit.setStartDate(newStartDate);
        assertEquals(newStartDate, testPermit.getStartDate(), "Start date should be updated.");

        Date newExpirationDate = Date.valueOf("2026-10-22");
        testPermit.setExpirationDate(newExpirationDate);
        assertEquals(newExpirationDate, testPermit.getExpirationDate(), "Expiration date should be updated.");

        Time newExpirationTime = Time.valueOf("22:00:00");
        testPermit.setExpirationTime(newExpirationTime);
        assertEquals(newExpirationTime, testPermit.getExpirationTime(), "Expiration time should be updated.");
    }



}
