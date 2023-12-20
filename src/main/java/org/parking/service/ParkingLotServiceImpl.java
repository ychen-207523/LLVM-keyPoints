package org.parking.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import org.parking.model.ParkingLot;

/**
 * Implements support for the database interactions relating to parking lots.
 */
public class ParkingLotServiceImpl implements ParkingLotService {
    /**
     * Holds the instance of the database org.parking.service.
     */
    private final DBService dbService;

    /**
     * Constructor for the ParkingLotServiceImpl.
     * Allows for the program to call on database operations relating to parking
     * lots.
     * 
     * @param dbService The database org.parking.service that the functions will use.
     */
    public ParkingLotServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Collection<ParkingLot> getAll() {
        Collection<ParkingLot> parkingLots = new ArrayList<ParkingLot>();
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM ParkingLots");
            results = pstate.executeQuery();

            if (!results.next())
                System.out.println("There are no parking lots in the system.");
            else {
                do {
                    String name = results.getString("name");
                    String address = results.getString("address");

                    parkingLots.add(new ParkingLot(name, address));
                } while (results.next());
            }
        } catch (SQLException e) {
            System.out.println("Error executing getAll query");
        } finally {
            // If the connection is null it means we didn't allocation any db related
            // objects to close so theres nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, results);

            }
        }

        return parkingLots;
    }

    @Override
    public ParkingLot getParkingLot(String name) {
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        ParkingLot parkingLot = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM ParkingLots WHERE name = ?");
            pstate.setString(1, name);
            results = pstate.executeQuery();

            // If there are no results
            if (!results.next()) {
                // then do nothing. Leave parkingLot null.
            } else {
                String resultName = results.getString("name");
                String resultAddress = results.getString("address");
                parkingLot = new ParkingLot(resultName, resultAddress);
            }
        } catch (SQLException e) {
            System.out.println("Error executing getParkingLot query");
        } finally {
            // If the connection is null it means we didn't allocation any db related
            // objects to close so theres nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, results);

            }
        }
        return parkingLot;
    }

    @Override
    public boolean createParkingLot(ParkingLot lot) {
        Connection conn = null;
        PreparedStatement pstate = null;
        int rowCount = 0;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("INSERT INTO ParkingLots VALUES (?, ?)");
            pstate.setString(1, lot.getName());
            pstate.setString(2, lot.getAddress());
            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing createParkingLot query");
        } finally {
            // If the connection is null it means we didn't allocation any db related
            // objects to close so theres nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);
            }
        }
        return rowCount != 0;
    }

    @Override
    public boolean updateParkingLot(String name, ParkingLot lot) {
        // Connect to the database
        // executeUpdate update table set name/address where name = name
        // if success
        // return true;
        // if failed or error
        Connection conn = null;
        PreparedStatement pstate = null;
        int rowCount = 0;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("UPDATE ParkingLots " +
                    "SET name=?, address=? " +
                    "WHERE name=?");
            pstate.setString(1, lot.getName());
            pstate.setString(2, lot.getAddress());
            pstate.setString(3, name);
            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing createParkingLot query");
        } finally {
            // If the connection is null it means we didn't allocation any db related
            // objects to close so theres nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }
        return rowCount != 0;
    }

    @Override
    public boolean deleteParkingLot(String name) {
        // Connect to the database
        // executeUpdate delete from table where name = name
        // if success
        // return true;
        // if failed or error
        Connection conn = null;
        PreparedStatement pstate = null;
        int rowCount = 0;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("DELETE FROM ParkingLots WHERE name=?");
            pstate.setString(1, name);

            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing deleteParkingLot query");
        } finally {
            // If the connection is null it means we didn't allocation any db related
            // objects to close so theres nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }
        return rowCount != 0;
    }
}
