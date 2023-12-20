package org.parking.service;

import org.parking.model.Citation;
import org.parking.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class CitationsServiceImpl implements CitationsService {
    private final DBService dbService;

    public CitationsServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }

    /** getAll will query the Citations table and return a collection of Citations that correspond to each row
     * found. If no results are found it will return null. If any SQLExceptions are encountered then it will throw a SQLException.
     */
    public Collection<Citation> getAll() throws SQLException {
        Collection<Citation> citations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("select * from Citations, Vehicles where Citations.licenseNum = Vehicles.carLicenseNumber");
            results = pstate.executeQuery();
            while (results.next()) {
                citations.add(new Citation(results.getInt("citationNum"),
                        new Vehicle(results.getString("carLicenseNumber"),
                                results.getString("model"),
                                results.getString("color"),
                                results.getString("manufacturer"),
                                results.getInt("year")
                        ),
                        results.getString("lotName"),
                        results.getString("category"),
                        results.getDouble("fee"),
                        results.getString("paymentStatus"),
                        results.getDate("citationDate"),
                        results.getTime("citationTime")
                ));
            }
            if (citations.isEmpty()) {
                System.out.println("Citations getAll() did not find any results returning null");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error executing getAll query");
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so theres nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, results);

            }
        }

        return citations;
    }

    /** getByNumber will query the Citations table and return a Citations that corresponds to the provided number.
     * If no items are found that match the provided number it will return null.
     * If any SQLExceptions are encountered then it will throw a SQLException.
     */
    public Citation getByNumber(int number) throws SQLException {
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        Citation citation = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("select * from Citations, Vehicles where Citations.licenseNum = Vehicles.carLicenseNumber AND citationNum = ?");
            pstate.setInt(1, number);
            results = pstate.executeQuery();
            if (results.next()) {
                citation = new Citation(results.getInt("citationNum"),
                        new Vehicle(results.getString("carLicenseNumber"),
                                results.getString("model"),
                                results.getString("color"),
                                results.getString("manufacturer"),
                                results.getInt("year")
                        ),
                        results.getString("lotName"),
                        results.getString("category"),
                        results.getDouble("fee"),
                        results.getString("paymentStatus"),
                        results.getDate("citationDate"),
                        results.getTime("citationTime")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error executing getByNumber query");
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, results);

            }
        }
        return citation;
    }

    /** createCitation will create a handle creating a citation and a vehicle if createVehicle is set to true. It will do this in a transaction
     * so that if the citation fails to create we are not left with an unnecessary vehicle.
     */
    public void createCitation(Citation citation, Boolean createVehicle) throws SQLException {
        Connection conn = null;
        PreparedStatement pstate = null;
        try {
            conn = dbService.connectAndReturnConnection();
            conn.setAutoCommit(false);
            if (createVehicle) {
                pstate = conn.prepareStatement("INSERT INTO Vehicles (carLicenseNumber, model, color, manufacturer, year) VALUES (?, ?, ?, ?, ?)");
                pstate.setString(1, citation.getVehicle().getLicense());
                pstate.setString(2, citation.getVehicle().getModel());
                pstate.setString(3, citation.getVehicle().getColor());
                pstate.setString(4, citation.getVehicle().getManufacturer());
                pstate.setInt(5, citation.getVehicle().getYear());
                int vRespCode = pstate.executeUpdate();
                if (vRespCode > 0) {
                    System.out.println("Vehicle created");
                } else {
                    System.out.println("Vehicle was not created");
                }
            }
            pstate = conn.prepareStatement("INSERT INTO Citations (licenseNum, lotName, category, fee, paymentStatus, citationDate, citationTime) VALUES (?,?,?,?,?,?,?)");
            pstate.setString(1, citation.getVehicle().getLicense());
            pstate.setString(2, citation.getLotName());
            pstate.setString(3, citation.getCategory());
            pstate.setDouble(4, citation.getFee());
            pstate.setString(5, citation.getPaymentStatus());
            pstate.setDate(6, citation.getCitationDate());
            pstate.setTime(7, citation.getCitationTime());
            int respCode = pstate.executeUpdate();
            if (respCode > 0) {
                System.out.println("Citation created");
            } else {
                System.out.println("Citation was not created");
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            System.err.println("Transaction is being rolled back");
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("Error: " + rollbackEx.getMessage());
                throw rollbackEx;
            }
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }
    }


    /** updateCitation will use the values from the provided citation in order to update the citation in the database.
     * If any SQLExceptions are encountered then it will throw a SQLException.
     */
    public void updateCitation(Citation citation) throws SQLException {
        Connection conn = null;
        PreparedStatement pstate = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("UPDATE Citations SET licenseNum=?, lotName=?, category=?, fee=?, paymentStatus=?, citationDate=?, citationTime=? WHERE citationNum = ?");
            pstate.setString(1, citation.getVehicle().getLicense());
            pstate.setString(2, citation.getLotName());
            pstate.setString(3, citation.getCategory());
            pstate.setDouble(4, citation.getFee());
            pstate.setString(5, citation.getPaymentStatus());
            pstate.setDate(6, citation.getCitationDate());
            pstate.setTime(7, citation.getCitationTime());
            pstate.setInt(8, citation.getNumber());
            int respCode = pstate.executeUpdate();
            if (respCode > 0) {
                System.out.println("Citation updated");
            } else {
                System.out.println("Citation was not updated");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }
    }

    /** deleteCitationByNumber will delete an entry from the Citations table based on the provided citationNumber
     * If any SQLExceptions are encountered then it will throw a SQLException.
     */
    public void deleteCitationByNumber(int number) throws SQLException {
        Connection conn = null;
        PreparedStatement pstate = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("DELETE FROM Citations WHERE citationNum = ?");
            pstate.setInt(1, number);
            int respCode = pstate.executeUpdate();
            if (respCode > 0) {
                System.out.println("Citation deleted");
            } else {
                System.out.println("Citation was not deleted");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }
    }

    /** appealCitation will find a citation by the provided number and updates its paymentStatus column to "APPEALED"
     * If any SQLExceptions are encountered then it will log the error from the exception. It will return true if any
     * rows were updated by the call.
     */
    @Override
    public boolean appealCitation(int number) {
        Connection conn = null;
        PreparedStatement pstate = null;
        int result = 0;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("UPDATE Citations SET paymentStatus=\"APPEALED\" WHERE citationNum = ? AND paymentStatus=\"DUE\"");
            pstate.setInt(1, number);
            result = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }
        return result != 0;
    }

    /** payCitation will find a citation by the provided number and updates its paymentStatus column to "PAID" if its status is
     * DUE
     * If any SQLExceptions are encountered then it will log the error from the exception. It will return true if any
     * rows were updated by the call.
     */
    public boolean payCitation(int number) {
        Connection conn = null;
        PreparedStatement pstate = null;
        int result = 0;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("UPDATE Citations SET paymentStatus=\"PAID\" WHERE citationNum = ? AND paymentStatus=\"DUE\"");
            pstate.setInt(1, number);
            result = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // If the connection is null it means we didn't allocation any db related objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);

            }
        }
        return result != 0;
    }
}