package org.parking.service;


import org.parking.model.*;
import org.parking.model.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;

public class ReportsServiceImpl implements ReportsService {

    private final DBService dbService;

    public ReportsServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }

    /***************************************************************************
     * Generate a report for the total number of Citations given in all Zones in
     * the lot for a given time range.
     * Inputs: ParkingLot, Start Date, End Date
     ****************************************************************************/
    public int generateCitationReport(String lot, Date start, Date end) throws SQLException{
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet result = null;
        int count = 0;

        try {
            // Open connection to the database and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT COUNT(*) FROM Citations WHERE lotName = ? AND citationDate " +
                    "BETWEEN ? AND ?");
            pstate.setString(1, lot);
            pstate.setDate(2,start);
            pstate.setDate(3,end);

            // Execute the statement
            result = pstate.executeQuery();

            // Extract the count from the results
            if(result.next()) count = result.getInt(1);

        } catch (SQLException e) {
            // Throw SQL Exception up to caller function
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, result);

            }
        }

        return count;
    }

    /***************************************************************************
     * Generate a list of zones for each parking lot. Return as collection of tuples
     * with (LotName, Zone)
     * Inputs: none
     ****************************************************************************/
    public Collection<Zone> generateZoneReport() throws SQLException{
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        Collection<Zone> zones = new ArrayList<>();

        try {
            // Open connection to the database and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT lotName, id FROM Zones ORDER BY lotName");

            // Execute the statement
            results = pstate.executeQuery();

            // Extract the count from the results
            while (results.next()) {
                // For each result, create a new vehicle object and append to collection
                zones.add(new Zone(results.getString("id"),
                        results.getString("lotName")
                ));
            }

        } catch (SQLException e) {
            // Throw SQL Exception up to caller function
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, results);

            }
        }

        // System.out.println("Functionality in development");
        return zones;
    }

    /***************************************************************************
     * Find the number of violated cars with an unpaid balance
     * Inputs: none
     ****************************************************************************/
    public int generateViolatedCarNumber() throws SQLException {
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet result = null;
        int count = 0;

        try {
            // Open connection to the database and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT count(DISTINCT licenseNum) FROM Citations WHERE " +
                    "paymentStatus = 'DUE'");

            // Execute the statement
            result = pstate.executeQuery();

            // Extract the count from the results
            if(result.next()) count = result.getInt(1);

        } catch (SQLException e) {
            // Throw SQL Exception up to caller function
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, result);

            }
        }

        return count;
    }

    /***************************************************************************
     * Find the number of employees for a specified parking zone
     * Inputs: zone id
     ****************************************************************************/
    public int generateExployeesofZone(String zone) throws SQLException{
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet result = null;
        int count = 0;

        try {
            // Open connection to the database and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT COUNT(*) \n" +
                    "FROM  (SELECT * FROM Drivers WHERE Drivers.status = 'E') Employees \n" +
                    "JOIN (SELECT * FROM Permits WHERE Permits.zoneID = ?) zonePermits \n" +
                    "WHERE Employees.id = zonePermits.associatedID;");
            pstate.setString(1, zone);

            // Execute the statement
            result = pstate.executeQuery();

            // Extract the count from the results
            if(result.next()) count = result.getInt(1);

        } catch (SQLException e) {
            // Throw SQL Exception up to caller function
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, result);

            }
        }

        return count;
    }

    /***************************************************************************
     * Find the permit information for a specified associated driver
     * Inputs: zone id
     ****************************************************************************/
    public Collection<Permit> getPermitForDriver(String id) throws SQLException{
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        Collection<Permit> permits = new ArrayList<>();

        try {
            // Open connection to the database and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Permits WHERE associatedID = ?;");
            pstate.setString(1, id);

            // Execute the statement
            results = pstate.executeQuery();

            // Extract the count from the results
            while (results.next()) {
                // For each result, create a new vehicle object and append to collection
                permits.add(new Permit(
                        results.getString("permitID"),
                        results.getString("permitType"),
                        results.getString("zoneID"),
                        results.getString("associatedID"),
                        results.getString("carLicenseNum"),
                        results.getString("spaceType"),
                        results.getDate("startDate"),
                        results.getDate("expirationDate"),
                        results.getTime("expirationTime")
                ));
            }

        } catch (SQLException e) {
            // Throw SQL Exception up to caller function
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, results);

            }
        }

        return permits;
    }

    /***************************************************************************
     * Get an available space number given a lot name and a space type
     * Inputs: Lot Name (String), Space Type (String)
     ****************************************************************************/
    public int getAvailableSpaceinParkingLot(String lotName, String spaceType) throws SQLException{
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet result = null;
        int spaceNum = 0;

        try {
            // Open connection to the database and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT number  FROM Spaces WHERE type =? AND status = '1' " +
                    "AND lotName =?");
            pstate.setString(1, spaceType);
            pstate.setString(2, lotName);

            // Execute the statement
            result = pstate.executeQuery();

            // Extract the count from the results
            if(result.next()) spaceNum = result.getInt(1);

        } catch (SQLException e) {
            // Throw SQL Exception up to caller function
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, result);

            }
        }

        return spaceNum;
    }

}
