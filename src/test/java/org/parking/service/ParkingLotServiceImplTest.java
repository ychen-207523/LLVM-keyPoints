
package org.parking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.collections4.CollectionUtils;
import org.parking.model.ParkingLot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParkingLotServiceImplTest {

    private ParkingLotService parkingLotService;

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
        parkingLotService = new ParkingLotServiceImpl(dbService);
    }

    @Test
    void testGetAll_NoConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertEquals(0, parkingLotService.getAll().size());
        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void GetAll_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM ParkingLots")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("name")).thenReturn("testName");
        when(results.getString("address")).thenReturn("testAddress");

        Collection<ParkingLot> parkingLots = parkingLotService.getAll();
        assertEquals(1, parkingLots.size());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void GetAll_empty() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM ParkingLots")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(false);

        assertEquals(0, parkingLotService.getAll().size());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void testGetParkingLot_noConn() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertNull(parkingLotService.getParkingLot("testName"));
        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testGetParkingLot_success() throws SQLException {
        String parkingLotName = "Alpha";
        String parkingLotAddress = "123 Parking Lot Way";
        ParkingLot parkingLot = new ParkingLot(parkingLotName, parkingLotAddress);

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM ParkingLots WHERE name = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("name")).thenReturn("Alpha");
        when(results.getString("address")).thenReturn("123 Parking Lot Way");

        ParkingLot result = parkingLotService.getParkingLot("Alpha");
        assertEquals(parkingLot.getName(), result.getName());
        assertEquals(parkingLot.getAddress(), result.getAddress());

        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void testGetParkingLot_notFound() throws SQLException
    {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM ParkingLots WHERE name = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(false);

        assertNull(parkingLotService.getParkingLot("Bravo"));

        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void testCreateParkingLot_noConn() throws SQLException {
        ParkingLot test = new ParkingLot("testName", "testAddress");
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(parkingLotService.createParkingLot(test));
        verify(dbService, times(0)).close(connection, statement, null);
    }

    @Test
    void testCreateParkingLot_success() throws SQLException {
        String parkingLotName = "testName";
        String parkingLotAddress = "testAddress";
        ParkingLot parkingLot = new ParkingLot(parkingLotName, parkingLotAddress);

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO ParkingLots VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(parkingLotService.createParkingLot(parkingLot));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testCreateParkingLot_duplicate() throws SQLException {
        String parkingLotName = "testName";
        String parkingLotAddress = "testAddress";
        ParkingLot parkingLot = new ParkingLot(parkingLotName, parkingLotAddress);

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO ParkingLots VALUES (?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(parkingLotService.createParkingLot(parkingLot));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testUpdateParkingLot_noConn() throws SQLException {
        ParkingLot test1 = new ParkingLot("testName", "testAddress");
        ParkingLot test2 = new ParkingLot("testName2", "testAddress2");

        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(parkingLotService.updateParkingLot(test1.getName(), test2));

        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testUpdateParkingLot_success() throws SQLException {
        ParkingLot test1 = new ParkingLot("testName", "testAddress");
        ParkingLot test2 = new ParkingLot("testName2", "testAddress2");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE ParkingLots " +
                "SET name=?, address=? " +
                "WHERE name=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        assertTrue(parkingLotService.updateParkingLot(test1.getName(), test2));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testUpdateParkingLot_notFound() throws SQLException {
        ParkingLot test1 = new ParkingLot("testName", "testAddress");
        ParkingLot test2 = new ParkingLot("testName2", "testAddress2");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE ParkingLots " +
                "SET name=?, address=? " +
                "WHERE name=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);
        assertFalse(parkingLotService.updateParkingLot(test1.getName(), test2));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testDeleteParkingLot_noConn() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(parkingLotService.deleteParkingLot("testName"));
        verify(dbService, times(0)).close(connection, statement, null);
    }

    @Test
    void testDeleteParkingLot_success() throws SQLException {
        ParkingLot test = new ParkingLot("testName", "testAddress");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM ParkingLots WHERE name=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(parkingLotService.deleteParkingLot(test.getName()));

        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testDeleteParkingLot_notFound() throws SQLException {
        ParkingLot test = new ParkingLot("testName", "testAddress");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM ParkingLots WHERE name=?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(parkingLotService.deleteParkingLot(test.getName()));

        verify(dbService, times(1)).close(connection, statement, null);
    }
}