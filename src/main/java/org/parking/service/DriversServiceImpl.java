package org.parking.service;

import org.parking.model.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class DriversServiceImpl implements DriversService {
    private final DBService dbService;

    public DriversServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }

    /***************************************************************************
     * Get a list of all registered drivers and formats them in an array list of
     * driver objects. If none exist, return null.
     * ---------------------------------------------------------------------------
     * SQL Statement: "SELECT * FROM Drivers"
     ****************************************************************************/
    public Collection<Driver> getAll() throws SQLException {
        // Define variables for query
        Collection<Driver> drivers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;

        try {
            // Open connection to DB and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Drivers");

            // Execute query and iterate over results
            results = pstate.executeQuery();
            while (results.next()) {
                // For each result, create a new vehicle object and append to collection
                drivers.add(new Driver(results.getString("id"),
                        results.getString("name"),
                        results.getString("status")
                ));
            }

            if (drivers.isEmpty()) return null;

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

        return drivers;
    }

    /***************************************************************************
     * Get a specific driver based on a given id. If none exist,
     * return null.
     * ---------------------------------------------------------------------------
     * SQL Statement: "SELECT * FROM Drivers WHERE id = id"
     ****************************************************************************/
    public Driver getById(String id) throws SQLException {
        //Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        Driver driver = null;

        try {
            // Open connection to Database and prepare query
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Drivers WHERE id = ?");
            pstate.setString(1, id);

            // Execute query and if a result is listed, create new object for return
            results = pstate.executeQuery();
            if (results.next()) {
                driver = new Driver(results.getString("id"),
                        results.getString("name"),
                        results.getString("status")
                );
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
        return driver;
    }

    /***************************************************************************
     * Update a specific driver in the database. Return boolean indicating the
     * success of execution
     * ---------------------------------------------------------------------------
     * SQL Statement: "UPDATE Drivers SET name = ?, status = ? WHERE id = ?"
     ****************************************************************************/
    public boolean update(Driver driver) throws SQLException {
        //Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        int count = 0;

        try {
            // Open connection to database and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("UPDATE Drivers SET name = ?, status = ? WHERE id = ?");
            pstate.setString(1, driver.getName());
            pstate.setString(2, driver.getStatus());
            pstate.setString(3, driver.getId());

            // Execute the statement
            count = pstate.executeUpdate();

        } catch (SQLException e) {
            // Throw SQL Exception up to caller function
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }

        // Return boolean value indicating success of execution
        return (count > 0);
    }

    /***************************************************************************
     * Crate a specific driver in the database. Return boolean indicating the
     * success of execution
     * ---------------------------------------------------------------------------
     * SQL Statement: "INSERT INTO Drivers (id, name, status, VALUES (?, ?, ?)"
     ****************************************************************************/
    public boolean create(Driver driver) throws SQLException{
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        int count = 0;

        try {
            // Open database connection and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("INSERT INTO Drivers (id, name, status) VALUES (?, ?, ?)");
            pstate.setString(1, driver.getId());
            pstate.setString(2, driver.getName());
            pstate.setString(3, driver.getStatus());

            // Execute the query
            count = pstate.executeUpdate();

        } catch (SQLException e) {
            // Throw SQL Exception up to caller function
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }

        // Return boolean indicating the success of the execution
        return (count > 0);
    }

    /***************************************************************************
     * Delete a specific driver in the database. Return boolean indicating the
     * success of execution
     * ---------------------------------------------------------------------------
     * SQL Statement: "DELETE FROM Drivers WHERE id = ?"
     ****************************************************************************/
    public boolean delete(String id) throws SQLException {
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        int count = 0;

        try {
            // Open connection to the database and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("DELETE FROM Drivers WHERE id = ?");
            pstate.setString(1, id);

            // Execute the statement
            count = pstate.executeUpdate();

        } catch (SQLException e) {
            // Throw SQL Exception up to caller function
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }
        return (count > 0);

    }




}
