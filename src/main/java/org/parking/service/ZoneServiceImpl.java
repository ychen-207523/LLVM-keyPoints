package org.parking.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.parking.model.Zone;

/**
 * Implementation of the operations for creating, updating and deleting zones
 * from the database.
 */
public class ZoneServiceImpl implements ZoneService {
    /**
     * Database org.parking.service that holds the connection information.
     */
    private final DBService dbService;

    /**
     * Constructor for the Zone Service Implementation. Allows the program to call
     * on database operations for the Zone table.
     * 
     * @param dbService Database org.parking.service that holds the connection information.
     */
    public ZoneServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Collection<Zone> getAll() {
        Collection<Zone> zones = new ArrayList<Zone>();
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Zones");
            results = pstate.executeQuery();

            if (!results.next())
                System.out.println("There are no zones in the system.");
            else {
                do {
                    String id = results.getString("id");
                    String lotName = results.getString("lotName");

                    zones.add(new Zone(id, lotName));
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
        return zones;
    }

    @Override
    public Zone getZone(String id, String lotName) {
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        Zone zone = null;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Zones WHERE id = ? AND lotName = ?");
            pstate.setString(1, id);
            pstate.setString(2, lotName);
            results = pstate.executeQuery();

            // If there are no results
            if (!results.next()) {
                // then do nothing. Leave parkingLot null.
            } else {
                String resultId = results.getString("id");
                String resultLotName = results.getString("lotName");
                zone = new Zone(resultId, resultLotName);
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
        return zone;
    }

    @Override
    public Collection<Zone> getZonesById(String id) {
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        Zone zone;
        ArrayList<Zone> zones = new ArrayList<Zone>();
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Zones WHERE id = ?");
            pstate.setString(1, id);
            results = pstate.executeQuery();

            // If there are no results
            if (!results.next()) {
                // then do nothing. Leave parkingLot null.
            } else {
                do {
                    String resultId = results.getString("id");
                    String resultLotName = results.getString("lotName");
                    zone = new Zone(resultId, resultLotName);
                    zones.add(zone);
                } while (results.next());
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
        return zones;
    }

    @Override
    public Collection<Zone> getZonesByLotName(String lotName) {
        Connection conn = null;
        PreparedStatement pstate = null;
        ResultSet results = null;
        Zone zone;
        Collection<Zone> zones = new ArrayList<Zone>();
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("SELECT * FROM Zones WHERE lotName = ?");
            pstate.setString(1, lotName);
            results = pstate.executeQuery();

            // If there are no results
            if (!results.next()) {
                // then do nothing. Leave parkingLot null.
            } else {
                do {
                    String resultId = results.getString("id");
                    String resultLotName = results.getString("lotName");
                    zone = new Zone(resultId, resultLotName);
                    zones.add(zone);
                } while (results.next());
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
        return zones;
    }

    @Override
    public boolean createZone(Zone zone) {
        Connection conn = null;
        PreparedStatement pstate = null;
        int rowCount = 0;
        try {
            conn = dbService.connectAndReturnConnection();
            pstate = conn.prepareStatement("INSERT INTO Zones VALUES (?, ?)");
            pstate.setString(1, zone.getId());
            pstate.setString(2, zone.getLotName());
            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing createZone query");
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
    public boolean updateZone(Zone originalZone, Zone updatedZone) {
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
            pstate = conn.prepareStatement("UPDATE Zones " +
                    "SET id=?, lotName=? " +
                    "WHERE id=? " +
                    "AND lotName=?");
            pstate.setString(1, updatedZone.getId());
            pstate.setString(2, updatedZone.getLotName());
            pstate.setString(3, originalZone.getId());
            pstate.setString(4, originalZone.getLotName());
            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing updateZone query");
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
    public boolean deleteZone(Zone zone) {
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
            pstate = conn.prepareStatement("DELETE FROM Zones WHERE id=? and lotName=?");
            pstate.setString(1, zone.getId());
            pstate.setString(2, zone.getLotName());

            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing deleteZone query");
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
    public boolean assignZoneToParkingLot(String oldLot, String zoneID, String newLot) {
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
            pstate = conn.prepareStatement("UPDATE Zones " +
                    "SET lotName=? " +
                    "WHERE id=? " +
                    "AND lotName=?");
            pstate.setString(1, newLot);
            pstate.setString(2, zoneID);
            pstate.setString(3, oldLot);
            rowCount = pstate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing assignZoneToParkingLotQuery query");
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
    public boolean createLotAndZone(String address, String lotName, String zoneID) {
        Connection conn = null;
        PreparedStatement pstate = null;
        boolean status = false;
        try {
            // Try to create the new parking lot
            conn = dbService.connectAndReturnConnection();
            conn.setAutoCommit(false);
            pstate = conn.prepareStatement("INSERT INTO ParkingLots VALUES (?, ?)");
            pstate.setString(1, lotName);
            pstate.setString(2, address);
            int vRespCode = pstate.executeUpdate();
            if (vRespCode > 0) {
                System.out.println("Parking lot created.");
            } else {
                System.out.println("Parking lot was not created.");
            }
            // Try to insert the new zone
            pstate = conn.prepareStatement("INSERT INTO Zones VALUES (?, ?)");
            pstate.setString(1, zoneID);
            pstate.setString(2, lotName);
            int respCode = pstate.executeUpdate();
            if (respCode > 0) {
                System.out.println("Zone was created.");
                status = true;
            } else {
                System.out.println("Zone was not created.");
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            System.err.println("Transaction is being rolled back.");
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("Error: " + rollbackEx.getMessage());
            }
        } finally {
            // If the connection is null it means we didn't allocation any db related
            // objects to close so there's nothing
            // to clean up.
            if (conn != null) {
                dbService.close(conn, pstate, null);
            }
        }
        return status;
    }
}
