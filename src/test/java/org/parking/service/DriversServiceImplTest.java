package org.parking.service;

import org.apache.commons.collections4.CollectionUtils;
import org.parking.model.Driver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriversServiceImplTest {

    private DriversServiceImpl driversService;

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
        driversService = new DriversServiceImpl(dbService);
    }

    @Test
    void getAll_success() throws SQLException {
        Collection<Driver> drivers = Collections.singleton(new Driver("1234567890", "testName", "S"));
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Drivers")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("id")).thenReturn("1234567890");
        when(results.getString("name")).thenReturn("testName");
        when(results.getString("status")).thenReturn("S");

        CollectionUtils.isEqualCollection(drivers, driversService.getAll());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void getAll_noItemsFound() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Drivers")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(false);
        Assertions.assertNull(driversService.getAll());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void getAll_connSqlFailure() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
           driversService.getAll();
       });
    }

    @Test
    void getAll_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Drivers")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            driversService.getAll();
        });
        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void getById_success() throws SQLException {
        Driver driver = new Driver("1234567890", "testName", "S");
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Drivers WHERE id = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true);
        when(results.getString("id")).thenReturn("1");
        when(results.getString("name")).thenReturn("testName");
        when(results.getString("status")).thenReturn("S");
        CollectionUtils.isEqualCollection(Collections.singleton(driver), Collections.singleton(driversService.getById("1")));
        verify(dbService, times(1)).close(connection,statement, results);
    }

    @Test
    void getById_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Drivers WHERE id = ?")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            driversService.getById("1");
        });
        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void create_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Drivers (id, name, status) VALUES (?, ?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        CollectionUtils.isEqualCollection(Collections.singleton(true), Collections.singleton(driversService.create(new Driver("1234567890", "testName", "S"))));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void create_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Drivers (id, name, status) VALUES (?, ?, ?)")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            driversService.create(new Driver("1234567890", "testName", "S"));
        });
        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void update_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Drivers SET name = ?, status = ? WHERE id = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
//        driversService.update(new Driver("1234567890", "testName", "S"));
        CollectionUtils.isEqualCollection(Collections.singleton(true), Collections.singleton(driversService.update(new Driver("1234567890", "testName", "S"))));
        verify(dbService, times(1)).close(connection,statement,null);
    }

    @Test
    void update_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Drivers SET name = ?, status = ? WHERE id = ?")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            driversService.update(new Driver("1234567890", "testName", "S"));
        });
        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void delete_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Drivers WHERE id = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        CollectionUtils.isEqualCollection(Collections.singleton(true), Collections.singleton(driversService.delete("1234567890")));
        verify(dbService, times(1)).close(connection,statement,null);
    }

    @Test
    void delete_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Drivers WHERE id = ?")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            driversService.delete("1234567890");
        });
        verify(dbService, times(1)).close(connection, null, null);
    }
}