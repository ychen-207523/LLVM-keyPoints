package org.parking.service;

import org.parking.model.*;

import java.sql.*;
import java.util.Collection;

public interface ReportsService {
    // public Collection<Citation> retrieveCitationsList() throws SQLException;

    public int generateCitationReport(String lot, Date start, Date end) throws SQLException;

    public Collection<Zone> generateZoneReport() throws SQLException;

    public int generateViolatedCarNumber() throws SQLException;

    public int generateExployeesofZone(String zone) throws SQLException;

    public Collection<Permit> getPermitForDriver(String id) throws SQLException;

    public int getAvailableSpaceinParkingLot(String lotName, String spaceType) throws SQLException;


}
