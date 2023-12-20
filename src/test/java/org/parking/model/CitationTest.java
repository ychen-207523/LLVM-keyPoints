package org.parking.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

class CitationTest {

    @Test
    public void setGetCitationTime() {
        Citation citation = new Citation(0, new Vehicle("test-license", "test-model", "test-color", "test-manf", 2024), "", "", 30.00, "", new Date(1698067480), new Time(1698067480));
        citation.setCitationTime(new Time(1898067480));
        assertEquals(new Time(1898067480), citation.getCitationTime(), "Citation time should be updated");
    }

    @Test
    public void setGetCitationDate() {
        Citation citation = new Citation(0, new Vehicle("test-license", "test-model", "test-color", "test-manf", 2024), "", "", 30.00, "", new Date(1698067480), new Time(1698067480));
        citation.setCitationDate(new Date(1898067480));
        assertEquals(new Date(1898067480), citation.getCitationDate(), "Citation date should be updated");
    }

    @Test
    public void setGetCitationNumber() {
        Citation citation = new Citation(0, new Vehicle("test-license", "test-model", "test-color", "test-manf", 2024), "", "", 30.00, "", new Date(1698067480), new Time(1698067480));
        citation.setNumber(2);
        assertEquals(2, citation.getNumber(), "Citation number should be updated");
    }

    @Test
    public void setGetCitationVehicle() {
        Citation citation = new Citation(0, new Vehicle("test-license", "test-model", "test-color", "test-manf", 2024), "", "", 30.00, "", new Date(1698067480), new Time(1698067480));
        citation.setVehicle(new Vehicle("test-license-new", "test-model-new", "test-color-new", "test-manf-new", 2025));
        assertEquals("test-license-new", citation.getVehicle().getLicense(), "Citation vehicle should be updated");
    }
}