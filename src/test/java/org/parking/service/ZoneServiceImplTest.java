
package org.parking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import org.parking.model.Zone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ZoneServiceImplTest {

    private ZoneService zoneService;

    @Mock
    private DBService dbService;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet results;

    @BeforeEach
    void init() {
        zoneService = new ZoneServiceImpl(dbService);
    }

    /* Test GetAll function */
    @Test
    void testGetAll_NoConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertEquals(0, zoneService.getAll().size());
        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testGetAll_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Zones")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("id")).thenReturn("A");
        when(results.getString("lotName")).thenReturn("testLotName");

        Collection<Zone> resultZones = zoneService.getAll();
        assertEquals(1, resultZones.size());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void testGetAll_empty() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Zones")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(false);

        assertEquals(0, zoneService.getAll().size());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    /* Test Get Zone function */
    @Test
    void testGetZone_noConn() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertNull(zoneService.getZone("A", "testLotName"));
        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testGetZone_success() throws SQLException {
        String zoneID = "A";
        String lotName = "testLotName";
        Zone zone = new Zone(zoneID, lotName);

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Zones WHERE id = ? AND lotName = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("id")).thenReturn("A");
        when(results.getString("lotName")).thenReturn("testLotName");
        Zone resultZone = zoneService.getZone(zoneID, lotName);
        assertEquals(zone.getId(), resultZone.getId());
        assertEquals(zone.getLotName(), resultZone.getLotName());

        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void testGetZone_notFound() throws SQLException
    {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Zones WHERE id = ? AND lotName = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(false);
        assertNull(zoneService.getZone("A", "testLotName"));

        verify(dbService, times(1)).close(connection, statement, results);
    }

    /* Test GetZoneById function */
    @Test
    void testGetZoneById_NoConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertEquals(0, zoneService.getZonesById("A").size());
        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testGetZoneById_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Zones WHERE id = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("id")).thenReturn("A");
        when(results.getString("lotName")).thenReturn("testLotName");

        Collection<Zone> resultZones = zoneService.getZonesById("A");
        assertEquals(1, resultZones.size());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void testGetZoneById_empty() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Zones WHERE id = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(false);

        assertEquals(0, zoneService.getZonesById("A").size());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    /* Test GetZoneByLotName function */
    @Test
    void testGetZoneByLotName_NoConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertEquals(0, zoneService.getZonesByLotName("testLotName").size());
        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testGetZoneByLotName_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Zones WHERE lotName = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("id")).thenReturn("A");
        when(results.getString("lotName")).thenReturn("testLotName");

        Collection<Zone> resultZones = zoneService.getZonesByLotName("testLotName");
        assertEquals(1, resultZones.size());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void testGetZoneByLotName_empty() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Zones WHERE lotName = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(false);

        assertEquals(0, zoneService.getZonesByLotName("testLotName").size());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    /* Test Create Zone function */
    @Test
    void testCreateZone_noConn() throws SQLException {
        Zone test = new Zone("A", "testLotName");
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(zoneService.createZone(test));
        verify(dbService, times(0)).close(connection, statement, null);
    }

    @Test
    void testCreateZone_success() throws SQLException {
        String zoneID = "A";
        String zonelotID = "testLotName";
        Zone zone = new Zone(zoneID, zonelotID);

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Zones VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(zoneService.createZone(zone));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testCreateZone_duplicate() throws SQLException {
        String zoneID = "A";
        String zonelotID = "testLotName";
        Zone zone = new Zone(zoneID, zonelotID);

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Zones VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(zoneService.createZone(zone));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    /* Test Update Zone function */
    @Test
    void testUpdateZone_noConn() throws SQLException {
        Zone test1 = new Zone("A", "testLotName");
        Zone test2 = new Zone("A2", "testLotName2");

        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(zoneService.updateZone(test1, test2));

        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testUpdateZone_success() throws SQLException {
        Zone test1 = new Zone("A", "testLotName");
        Zone test2 = new Zone("A2", "testLotName2");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Zones " +
                "SET id=?, lotName=? " +
                "WHERE id=? " +
                "AND lotName=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        assertTrue(zoneService.updateZone(test1, test2));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testUpdateZone_notFound() throws SQLException {
        Zone test1 = new Zone("A", "testLotName");
        Zone test2 = new Zone("A2", "testLotName2");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Zones " +
                "SET id=?, lotName=? " +
                "WHERE id=? " +
                "AND lotName=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);
        assertFalse(zoneService.updateZone(test1, test2));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    /* Test Delete Zone function */
    @Test
    void testDeleteZone_noConn() throws SQLException {
        Zone test = new Zone("A", "testLotName");
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(zoneService.deleteZone(test));
        verify(dbService, times(0)).close(connection, statement, null);
    }

    @Test
    void testDeleteZone_success() throws SQLException {
        Zone test = new Zone("A", "testLotName");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Zones WHERE id=? and lotName=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(zoneService.deleteZone(test));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testDeleteZone_notFound() throws SQLException {
        Zone test = new Zone("A", "testLotName");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Zones WHERE id=? and lotName=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(zoneService.deleteZone(test));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testAssignZoneToParkingLot_NoConnection() throws SQLException {
        String testOldLot = "testLotName";
        String testZoneId = "A";
        String testNewLot = "testNewLotName";
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(zoneService.assignZoneToParkingLot(testOldLot, testZoneId, testNewLot));

        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testAssignZoneToParkingLot_success() throws SQLException {
        String testOldLot = "testLotName";
        String testZoneId = "A";
        String testNewLot = "testNewLotName";

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Zones " +
                "SET lotName=? " +
                "WHERE id=? " +
                "AND lotName=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        assertTrue(zoneService.assignZoneToParkingLot(testOldLot, testZoneId, testNewLot));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testAssignZoneToParkingLot_empty() throws SQLException {
        String testOldLot = "testLotName";
        String testZoneId = "A";
        String testNewLot = "testNewLotName";

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Zones " +
                "SET lotName=? " +
                "WHERE id=? " +
                "AND lotName=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);
        assertFalse(zoneService.assignZoneToParkingLot(testOldLot, testZoneId, testNewLot));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    /* Test createLotAndZone */
    @Test
    void testCreateLotAndZone_NoConnection() throws SQLException {
        String testAddress = "testAddress";
        String testLotName = "testLotName";
        String testZoneID = "testZoneID";
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(zoneService.createLotAndZone(testAddress, testLotName, testZoneID));
        verify(dbService, times(0)).close(connection, statement, null);
    }

    @Test
    void testCreateLotAndZone_RollbackError() throws SQLException {
        String testAddress = "testAddress";
        String testLotName = "testLotName";
        String testZoneID = "testZoneID";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO ParkingLots VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        when(connection.prepareStatement("INSERT INTO Zones VALUES (?, ?)")).thenThrow(new SQLException());
        doThrow(new SQLException()).when(connection).rollback();
        
        assertFalse(zoneService.createLotAndZone(testAddress, testLotName, testZoneID));
        verify(connection, times(1)).rollback();
        verify(dbService, times(1)).close(connection, statement, null);

    }

    @Test
    void testCreateLotAndZone_CreateBoth() throws SQLException {
        String testAddress = "testAddress";
        String testLotName = "testLotName";
        String testZoneID = "testZoneID";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO ParkingLots VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        when(connection.prepareStatement("INSERT INTO Zones VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(zoneService.createLotAndZone(testAddress, testLotName, testZoneID));
        verify(connection, times(1)).commit();
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testCreateLotAndZone_CreateNewZone() throws SQLException {
        String testAddress = "testAddress";
        String testLotName = "testLotName";
        String testZoneID = "testZoneID";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO ParkingLots VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);
        when(connection.prepareStatement("INSERT INTO Zones VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(zoneService.createLotAndZone(testAddress, testLotName, testZoneID));
        verify(connection, times(1)).commit();
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testCreateLotAndZone_empty() throws SQLException {
        String testAddress = "testAddress";
        String testLotName = "testLotName";
        String testZoneID = "testZoneID";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO ParkingLots VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);
        when(connection.prepareStatement("INSERT INTO Zones VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(zoneService.createLotAndZone(testAddress, testLotName, testZoneID));
        verify(connection, times(1)).commit();
        verify(dbService, times(1)).close(connection, statement, null);
    }
}