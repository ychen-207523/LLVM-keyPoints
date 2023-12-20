package org.parking.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.parking.model.Space;

/**
 * Implementation of database operations for Space objects.
 */
public class SpaceServiceImpl implements SpaceService {
    /**
     * Holds the instance of the dbService.
     */
    private final DBService dbService;

    /**
     * Constructor for the Space Service Implementation. Allows the program to
     * perform
     * database operations on Space entries.
     * 
     * @param dbService Database org.parking.service that holds the connection information.
     */
    public SpaceServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }

    public Collection<Space> getAll() {
        Collection<Space> Spaces = new ArrayList<Space>();
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Spaces");
            results = pstate.executeQuery();

            if (!results.next())
                System.out.println("There are no Spaces in the system.");
            else {
                do {
                    int number = results.getInt("number");
                    String type = results.getString("type");
                    boolean status = results.getBoolean("status");
                    String zoneID = results.getString("zoneID");
                    String lotName = results.getString("lotName");

                    Spaces.add(new Space(number, type, status, zoneID, lotName));
                } while (results.next());
            }
        } catch (SQLException e) {
            System.out.println("Error executing getAllSpaces query");
        } finally {
            // If the connection is null it means we didn't allocation any db related
            // objects to close so theres nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, results);

            }
        }

        return Spaces;
    }

    @Override
    public Space getSpace(int number, String zoneID, String lotName) {
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        Space space = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Spaces WHERE number=? AND zoneID=? AND lotName=?");
            pstate.setInt(1, number);
            pstate.setString(2, zoneID);
            pstate.setString(3, lotName);
            results = pstate.executeQuery();

            // If there is a result
            if (results.next()) {
                int resultNumber = results.getInt("number");
                String resultType = results.getString("type");
                boolean resultStatus = results.getBoolean("status");
                String resultZoneID = results.getString("zoneID");
                String resultLotName = results.getString("lotName");
                space = new Space(resultNumber, resultType, resultStatus, resultZoneID, resultLotName);
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
        return space;
    }

    @Override
    public boolean createSpace(Space space) {
        Connection conn = null;
        PreparedStatement pstate = null;
        int rowCount = 0;

        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("INSERT INTO Spaces VALUES (?, ?, ?, ?, ?)");
            pstate.setInt(1, space.getNumber());
            pstate.setString(2, space.getType());
            pstate.setBoolean(3, space.getStatus());
            pstate.setString(4, space.getZoneID());
            pstate.setString(5, space.getLotName());
            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing createSpace query");
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
    public boolean updateSpace(Space originalSpace, Space updatedSpace) {
        Connection conn = null;
        PreparedStatement pstate = null;
        int rowCount = 0;

        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("UPDATE Spaces SET " +
                    "number=?, " +
                    "type=?, " +
                    "status=?, " +
                    "zoneID=?, " +
                    "lotName=? " +
                    "WHERE number=? AND zoneID=? AND lotName=?");
            pstate.setInt(1, updatedSpace.getNumber());
            pstate.setString(2, updatedSpace.getType());
            pstate.setBoolean(3, updatedSpace.getStatus());
            pstate.setString(4, updatedSpace.getZoneID());
            pstate.setString(5, updatedSpace.getLotName());
            pstate.setInt(6, originalSpace.getNumber());
            pstate.setString(7, originalSpace.getZoneID());
            pstate.setString(8, originalSpace.getLotName());
            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing updateSpace query");
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
    public boolean deleteSpace(Space space) {
        Connection conn = null;
        PreparedStatement pstate = null;
        int rowCount = 0;

        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("DELETE FROM Spaces WHERE number=? AND zoneID=? AND lotName=?");
            pstate.setInt(1, space.getNumber());
            pstate.setString(2, space.getZoneID());
            pstate.setString(3, space.getLotName());
            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing deleteSpace query");
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
    public boolean assignTypeOfASpace(String lotName, String zoneID, int number, String type) {
        Connection conn = null;
        PreparedStatement pstate = null;
        int rowCount = 0;

        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("UPDATE Spaces SET type=? WHERE number=? AND zoneID=? AND lotName=?");
            pstate.setString(1, type);
            pstate.setInt(2, number);
            pstate.setString(3, zoneID);
            pstate.setString(4, lotName);
            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing assignTypeOfASpace query");
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