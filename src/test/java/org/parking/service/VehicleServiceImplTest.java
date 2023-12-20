package org.parking.service;

import org.apache.commons.collections4.CollectionUtils;
import org.parking.model.Vehicle;
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
class VehicleServiceImplTest {

    private VehicleServiceImpl vehiclesService;

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
        vehiclesService = new VehicleServiceImpl(dbService);
    }

    @Test
    void getAll_success() throws SQLException {
        Collection<Vehicle> vehicles = Collections.singleton(new Vehicle("ABCD123", "testModel", "color","testManuf", 2023));
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Vehicles")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("carLicenseNumber")).thenReturn("ABCD123");
        when(results.getString("model")).thenReturn("testModel");
        when(results.getString("color")).thenReturn("color");
        when(results.getString("manufacturer")).thenReturn("testManuf");
        when(results.getInt("year")).thenReturn(2020);

        CollectionUtils.isEqualCollection(vehicles, vehiclesService.getAll());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void getAll_noItemsFound() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Vehicles")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(false);
        Assertions.assertNull(vehiclesService.getAll());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void getAll_connSqlFailure() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            vehiclesService.getAll();
        });
    }

    @Test
    void getAll_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Vehicles")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            vehiclesService.getAll();
        });
        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void getById_success() throws SQLException {
        Vehicle vehicle = new Vehicle("ABCD123", "testModel", "color","testManuf", 2023);
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Vehicles WHERE carLicenseNumber = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true);
        when(results.getString("carLicenseNumber")).thenReturn("ABCD123");
        when(results.getString("model")).thenReturn("testModel");
        when(results.getString("color")).thenReturn("color");
        when(results.getString("manufacturer")).thenReturn("testManuf");
        when(results.getInt("year")).thenReturn(2020);
        CollectionUtils.isEqualCollection(Collections.singleton(vehicle), Collections.singleton(vehiclesService.getByLicense("ABCD123")));
        verify(dbService, times(1)).close(connection,statement, results);
    }

    @Test
    void getById_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Vehicles WHERE carLicenseNumber = ?")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            vehiclesService.getByLicense("ABCD123");
        });
        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void create_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Vehicles (carLicenseNumber, model, color, manufacturer, year) VALUES (?, ?, ?, ?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        CollectionUtils.isEqualCollection(Collections.singleton(true), Collections.singleton(vehiclesService.create(new Vehicle("ABCD123", "testModel", "color","testManuf", 2023))));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void create_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Vehicles (carLicenseNumber, model, color, manufacturer, year) VALUES (?, ?, ?, ?, ?)")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            vehiclesService.create(new Vehicle("ABCD123", "testModel", "color","testManuf", 2023));
        });
        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void update_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Vehicles SET model = ?, color = ?, manufacturer = ?, year = ? WHERE carLicenseNumber = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        CollectionUtils.isEqualCollection(Collections.singleton(true), Collections.singleton(vehiclesService.update(new Vehicle("ABCD123", "testModel", "color","testManuf", 2023))));
        verify(dbService, times(1)).close(connection,statement,null);
    }

    @Test
    void update_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Vehicles SET model = ?, color = ?, manufacturer = ?, year = ? WHERE carLicenseNumber = ?")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            vehiclesService.update(new Vehicle("ABCD123", "testModel", "color","testManuf", 2023));
        });
        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void delete_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Vehicles WHERE carLicenseNumber = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        CollectionUtils.isEqualCollection(Collections.singleton(true), Collections.singleton(vehiclesService.delete("ABCD123")));
        verify(dbService, times(1)).close(connection,statement,null);
    }

    @Test
    void delete_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Vehicles WHERE carLicenseNumber = ?")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            vehiclesService.delete("ABCD123");
        });
        verify(dbService, times(1)).close(connection, null, null);
    }
}