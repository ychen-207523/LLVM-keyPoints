package org.parking.service;

import org.parking.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class VehicleServiceImpl implements VehicleService {
    private final DBService dbService;

    public VehicleServiceImpl(DBService dbService) { this.dbService = dbService; }

    /***************************************************************************
     * Get a list of all registered vehicles and formats them in an array list of
     * vehicle objects. If none exist, return null.
     * ---------------------------------------------------------------------------
     * SQL Statement: "SELECT * FROM Vehicles"
     ****************************************************************************/
    public Collection<Vehicle> getAll() throws SQLException {
        // Define variables for query
        Collection<Vehicle> vehicles = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;

        try {
            // Open connection to DB and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Vehicles");

            //Execute query and iterate over results
            results = pstate.executeQuery();
            while (results.next()) {
                // For each result, create a new vehicle object and append to collection
                vehicles.add(new Vehicle(results.getString("carLicenseNumber"),
                        results.getString("model"),
                        results.getString("color"),
                        results.getString("manufacturer"),
                        results.getInt("year")
                ));
            }

             if(vehicles.isEmpty()) return null;

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

        return vehicles;
    }

    /***************************************************************************
     * Get a specific vehicle based on a given license plate number. If none exist,
     * return null.
     * ---------------------------------------------------------------------------
     * SQL Statement: "SELECT * FROM Vehicles WHERE carLicenseNumber = license"
     ****************************************************************************/
    public Vehicle getByLicense(String license) throws SQLException {
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        Vehicle vehicle = null;

        try {
            // Open connection to Database and prepare query
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Vehicles WHERE carLicenseNumber = ?");
            pstate.setString(1, license);

            // Execute query and if a result is listed, create new object for return
            results = pstate.executeQuery();
            if (results.next()) {
                vehicle = new Vehicle(results.getString("carLicenseNumber"),
                        results.getString("model"),
                        results.getString("color"),
                        results.getString("manufacturer"),
                        results.getInt("year")
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
        return vehicle;
    }

    /***************************************************************************
     * Update a specific vehicle in the database. Return boolean indicating the
     * success of execution
     * ---------------------------------------------------------------------------
     * SQL Statement: "UPDATE Vehicles SET org.parking.model = ?, color = ?, manufacturer = ?,
     *      year = ? WHERE carLicenseNumber = ?"
     ****************************************************************************/
    public boolean update(Vehicle vehicle) throws SQLException {
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        int count = 0;


        try {
            // Open connection to database and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("UPDATE Vehicles SET model = ?, color = ?, manufacturer = ?, year = ? WHERE carLicenseNumber = ?");
            pstate.setString(1, vehicle.getModel());
            pstate.setString(2, vehicle.getColor());
            pstate.setString(3, vehicle.getManufacturer());
            pstate.setInt(4, vehicle.getYear());
            pstate.setString(5, vehicle.getLicense());

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
     * Crate a specific vehicle in the database. Return boolean indicating the
     * success of execution
     * ---------------------------------------------------------------------------
     * SQL Statement: "INSERT INTO Vehicles (carLicenseNumber, org.parking.model, color,
     *      manufacturer, year) VALUES (?, ?, ?, ?, ?)"
     ****************************************************************************/
    public boolean create(Vehicle vehicle) throws SQLException {
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        int count = 0;

        try {
            // Open database connection and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("INSERT INTO Vehicles (carLicenseNumber, model, color, manufacturer, year) VALUES (?, ?, ?, ?, ?)");
            pstate.setString(1, vehicle.getLicense());
            pstate.setString(2, vehicle.getModel());
            pstate.setString(3, vehicle.getColor());
            pstate.setString(4, vehicle.getManufacturer());
            pstate.setInt(5, vehicle.getYear());

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

        // return boolean indicating the success of the execution
        return (count > 0);
    }

    /***************************************************************************
     * Delete a specific vehicle in the database. Return boolean indicating the
     * success of execution
     * ---------------------------------------------------------------------------
     * SQL Statement: "DELETE FROM Vehicles WHERE carLicenseNumber = ?"
     ****************************************************************************/
    public boolean delete(String license) throws SQLException {
        // Define variables for query
        Connection conn = null;
        PreparedStatement pstate = null;
        int count = 0;

        try {
            // Open connection to the databse and prepare statement
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("DELETE FROM Vehicles WHERE carLicenseNumber = ?");
            pstate.setString(1, license);

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

        // Return boolean indicating the success of execution
        return (count > 0);

    }

}
