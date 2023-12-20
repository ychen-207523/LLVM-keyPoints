package org.parking.service;

import org.parking.model.Permit;
import org.parking.model.Vehicle;
import org.parking.model.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;


public class PermitsServiceImpl implements PermitsService {

    private final DBService dbService;

    public PermitsServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }

    /**
     * Retrieves detailed information for a specific permit based on the permit ID.
     * Return an empty collection if there is no permit associated with the permitID.
     */
    @Override
    public Collection<Permit> getPermitInfo(String permitID) throws SQLException {
        Collection<Permit> permits = new ArrayList<>();
        Connection conn = null;
        PreparedStatement state = null;
        ResultSet results = null;

        try {
            conn = dbService.connectAndReturnConnection();
            String sql = "SELECT * FROM Permits WHERE permitID = ?";
            state = conn.prepareStatement(sql);
            state.setString(1, permitID);

            results = state.executeQuery();

            while (results.next()) {
                Permit newPermit = new Permit(
                        results.getString("permitID"),
                        results.getString("permitType"),
                        results.getString("zoneID"),
                        results.getString("associatedID"),
                        results.getString("carLicenseNum"),
                        results.getString("spaceType"),
                        results.getDate("startDate"),
                        results.getDate("expirationDate"),
                        results.getTime("expirationTime")
                );
                permits.add(newPermit);
            }
        } catch (SQLException e) {
            System.out.println("Error executing getPermitInfo");
            throw e;
        } finally {
            if (conn != null) {
                dbService.close(conn, state, results);
            }
        }
        return permits;
    }

    /**
     * Enters new permit information into the database.
     */
    @Override
    public void enterPermitInfo(Permit permit) throws SQLException {
        Connection conn = null;
        PreparedStatement state = null;
        try {
            conn = dbService.connectAndReturnConnection();
            String sql = "INSERT INTO Permits (permitID, permitType, zoneID, associatedID, carLicenseNum, spaceType, startDate, expirationDate, expirationTime) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            state = conn.prepareStatement(sql);
            state.setString(1, permit.getPermitID());
            state.setString(2, permit.getPermitType());
            state.setString(3, permit.getZoneID());
            state.setString(4, permit.getAssociatedID());
            state.setString(5, permit.getCarLicenseNum());
            state.setString(6, permit.getSpaceType());
            state.setDate(7, permit.getStartDate());
            state.setDate(8, permit.getExpirationDate());
            state.setTime(9, permit.getExpirationTime());

            int id = state.executeUpdate();
            if(id > 0) {
               System.out.println("Successfully created a new permit: " + permit );
            }
            else {
                System.out.println("Failed to insert a new permit.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing enter Permit Information");
            throw e;
        } finally {
            if(conn != null ) {
                dbService.close(conn, state, null);
            }
        }
    }

    /**
     * Updates existing permit information identified by the permit ID.
     * permitID, associatedID, and carLicenseNum are not allowed to be updated.
     */
    @Override
    public void updatePermitInfo(String permitID, String permitType, String zoneID, String spaceType,
    Date startDate, Date expirationDate, Time expirationTime) throws SQLException {
        Connection conn = null;
        PreparedStatement state = null;
        try {
            conn = dbService.connectAndReturnConnection();
            String sql = "UPDATE Permits " +
                    "SET permitType = ?, zoneID = ?," +
                    "spaceType = ?, startDate = ?, expirationDate = ?, expirationTime = ? " +
                    "WHERE permitID = ?";

            state = conn.prepareStatement(sql);
            state.setString(1, permitType);
            state.setString(2, zoneID);
            state.setString(3, spaceType);
            state.setDate(4, startDate);
            state.setDate(5, expirationDate);
            state.setTime(6, expirationTime);
            state.setString(7, permitID);

            // Set the values for the prepared statement
            int rowsUpdated = state.executeUpdate();
            if(rowsUpdated > 0) {
                System.out.println("Permit with id of " + permitID +  " is updated");
            }
            else {
                System.out.println("Permit was not updated");
            }

        } catch (SQLException e) {
            System.out.println("Error executing update Permit Information");
            throw e;
        } finally {
            if(conn != null ) {
                dbService.close(conn, state, null);
            }
        }
    }

    /**
     * Deletes permit information from the database based on the permit ID.
     */
    @Override
    public void deletePermitInfo(String permitID) throws SQLException {
        Connection conn = null;
        PreparedStatement state = null;
        int id;
        try {
            conn = dbService.connectAndReturnConnection();
            String sql = "DELETE FROM Permits WHERE permitID = ?";

            state = conn.prepareStatement(sql);

            // Set the values for the prepared statement
            state.setString(1, permitID);

            id = state.executeUpdate();
            if(id > 0) {
                System.out.println("The permit with id of " + permitID + " is successfully deleted.");
            }
            else {
                System.out.println("The permit with id of " + permitID + " is not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error executing delete Permit Information");
            throw e;
        } finally {
            if(conn != null ) {
                dbService.close(conn, state, null);
            }
        }
    }

    /**
     * Retrieves the number of permits associated with a specific driver, identified by their associated ID.
     */
    @Override
    public int getPermitsNumberForDriver(String associatedID) throws SQLException {
        int permitsNumber = 0;
        Connection conn = null;
        PreparedStatement state = null;
        ResultSet result = null;

        try {
            conn = dbService.connectAndReturnConnection();
            String sql = "SELECT COUNT(DISTINCT permitID) AS permitCount FROM Permits WHERE associatedID = ?";
            state = conn.prepareStatement(sql);
            state.setString(1, associatedID);

            result = state.executeQuery();

            if (result.next()) {
                permitsNumber = result.getInt("permitCount"); // Get the count from the first column
            }
        } catch (SQLException e) {
            System.out.println("Error executing getPermitsNumber");
            throw e;
        } finally {
            if (conn != null) {
                dbService.close(conn, state, result);
            }
        }
        return permitsNumber;
    }

    /**
     * Assigns a permit to a driver and vehicle with the specified details.
     */
    @Override
    public void assignPermitToDriver(String permitID, String permitType, String zoneID, String spaceType,
                                     Date startDate, Date expirationDate, Time expirationTime, Driver driver, Vehicle vehicle) throws SQLException {

        String associatedID = driver.getId();
        String carLicenseNum = null;
        if(vehicle != null) {
            carLicenseNum = vehicle.getLicense();
        }
        Permit newPermit = new Permit(permitID, permitType, zoneID, associatedID, carLicenseNum, spaceType, startDate, expirationDate, expirationTime);
        this.enterPermitInfo(newPermit);
    }

    /**
     * Retrieves the number of vehicles associated with a specific permit ID.
     */
    @Override
    public int getVehicleNumberofPermit(String permitID) throws SQLException {
        int vehicleNumber = 0;
        Connection conn = null;
        PreparedStatement state = null;
        ResultSet result = null;
        try {
            conn = dbService.connectAndReturnConnection();
            String sql = "SELECT COUNT(carLicenseNum) AS vehicleCount FROM Permits WHERE permitID = ?";

            state = conn.prepareStatement(sql);
            state.setString(1, permitID);

            result = state.executeQuery();

            if (result.next()) {
                vehicleNumber = result.getInt("vehicleCount");
            }

        } catch (SQLException e) {
            System.out.println("Error executing getting vehicle number under a permit ");
            throw e;
        } finally {
            if (conn != null) {
                dbService.close(conn, state, result);
            }
        }
        return vehicleNumber;

    }

    /**
     * Removes a vehicle from a permit based on the permit ID and vehicle's license number.
     */
    @Override
    public void removeVehicleFromPermit(String permitID, String carLicenseNum) throws SQLException {
        Connection conn = null;
        PreparedStatement state = null;
        ResultSet results;

        try{
            conn = dbService.connectAndReturnConnection();
            conn.setAutoCommit(false);
            state = conn.prepareStatement("SELECT * FROM Vehicles WHERE carLicenseNumber = ?");
            state.setString(1, carLicenseNum);
            results = state.executeQuery();
            if(results.next()) {
                System.out.println("Vehicle found");
                System.out.println("Continue to delete ");
            }
            else {
                throw new SQLException("Vehicle does not exist");
            }
            String sql = "UPDATE Permits SET carLicenseNum = NULL WHERE permitID = ? AND carLicenseNum = ?";
            state = conn.prepareStatement(sql);
            state.setString(1, permitID);
            state.setString(2, carLicenseNum);
            int id = state.executeUpdate();

            if(id > 0) {
                System.out.println("The vehicle of " + carLicenseNum + " is removed from the permit of " + permitID);
            }
            else {
                System.out.println("Failed to remove the vehicle of "+ carLicenseNum + " from the permit");
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Error executing removing a vehicle from a permit ");
            System.out.println(e.getMessage());
            System.out.println("Transaction is being rolled back");
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("Error: " + rollbackEx.getMessage());
                throw rollbackEx;
            }
        } finally {
            if (conn != null) {
                dbService.close(conn, state, null);
            }
        }
    }

    /**
     * Adds a vehicle to a permit using the vehicle's new license number and the permit ID.
     */
    @Override
    public void addVehicleToPermit(String permitID, String newCarLicenseNum) throws SQLException {
        Connection conn = null;
        PreparedStatement state = null;
        try{
            conn = dbService.connectAndReturnConnection();
            String sql = "UPDATE Permits SET carLicenseNum = ? WHERE permitID = ? AND carLicenseNum IS NULL";
            state = conn.prepareStatement(sql);
            state.setString(1, newCarLicenseNum);
            state.setString(2, permitID);

            int id = state.executeUpdate();

            if(id > 0) {
                System.out.println("The vehicle of " + newCarLicenseNum + " is added to the permit of " + permitID);
            }
            else {
                System.out.println("Failed to add the vehicle of "+ newCarLicenseNum + " to the permit");
            }
        } catch (SQLException e) {
            System.out.println("Error executing adding a vehicle from a permit ");
            throw e;
        } finally {
            if (conn != null) {
                dbService.close(conn, state, null);
            }
        }
    }

    /**
     * Adds a new vehicle to a permit associated with an employee,
     * this method will create a new tuple in database
     */
    @Override
    public void addVehicleToPermitForEmployee(Permit permit, String newCarLicenseNum) throws SQLException {
        permit.setCarLicenseNum(newCarLicenseNum);
        enterPermitInfo(permit);
    }

    /**
     * Retrieves a collection of permits associated with a particular vehicle license number.
     */
    @Override
    public Collection<Permit> getPermitPerCarLicense(String carLicenseNum) throws SQLException {
        Collection<Permit> permits = new ArrayList<>();
        Connection conn = null;
        PreparedStatement state = null;
        ResultSet results = null;
        try {
            conn = dbService.connectAndReturnConnection();
            String sql = "SELECT * FROM Permits WHERE carLicenseNum = ?";
            state = conn.prepareStatement(sql);
            state.setString(1, carLicenseNum);
            results = state.executeQuery();

            while (results.next()) {
                Permit newPermit = new Permit(
                        results.getString("permitID"),
                        results.getString("permitType"),
                        results.getString("zoneID"),
                        results.getString("associatedID"),
                        results.getString("carLicenseNum"),
                        results.getString("spaceType"),
                        results.getDate("startDate"),
                        results.getDate("expirationDate"),
                        results.getTime("expirationTime")
                );
                permits.add(newPermit);
            }
        } catch (SQLException e) {
            System.out.println("Error executing query of getPermitPerCarLicense");
            throw e;
        } finally {
            if (conn != null) {
                dbService.close(conn, state, results);
            }
        }
        return permits;
    }
}
