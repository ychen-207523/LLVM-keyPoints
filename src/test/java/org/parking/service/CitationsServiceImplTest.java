package org.parking.service;

import org.apache.commons.collections4.CollectionUtils;
import org.parking.model.Citation;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitationsServiceImplTest {

    private CitationsServiceImpl citationsService;

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
        citationsService = new CitationsServiceImpl(dbService);
    }

    @Test
    void getAll_success() throws SQLException {
        Collection<Citation> citations = Collections.singleton(new Citation(1, new Vehicle("testLicense", "testModel", "testColor", "testManf", 1234), "testLotName", "testCategory", 1.50, "testStatus", new Date(1698067480), new Time(1698067480)));
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("select * from Citations, Vehicles where Citations.licenseNum = Vehicles.carLicenseNumber")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getInt("citationNum")).thenReturn(1);
        when(results.getString("carLicenseNumber")).thenReturn("testLicense");
        when(results.getString("model")).thenReturn("testModel");
        when(results.getString("color")).thenReturn("testColor");
        when(results.getString("manufacturer")).thenReturn("testManf");
        when(results.getInt("year")).thenReturn(1234);
        when(results.getString("lotName")).thenReturn("testLotName");
        when(results.getString("category")).thenReturn("testCategory");
        when(results.getDouble("fee")).thenReturn(1.50);
        when(results.getString("paymentStatus")).thenReturn("testStatus");
        when(results.getDate("citationDate")).thenReturn(new Date(1698067480));
        when(results.getTime("citationTime")).thenReturn(new Time(1698067480));

        CollectionUtils.isEqualCollection(citations, citationsService.getAll());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void getAll_noItemsFound() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("select * from Citations, Vehicles where Citations.licenseNum = Vehicles.carLicenseNumber")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(false);
        Assertions.assertNull(citationsService.getAll());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void getAll_connSqlFailure() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
           citationsService.getAll();
       });
    }

    @Test
    void getAll_statementSqlFailureCleanupConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("select * from Citations, Vehicles where Citations.licenseNum = Vehicles.carLicenseNumber")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            citationsService.getAll();
        });
        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void getByNumber_success() throws SQLException {
        Citation citation = new Citation(1, new Vehicle("testLicense", "testModel", "testColor", "testManf", 1234), "testLotName", "testCategory", 1.50, "testStatus", new Date(1698067480), new Time(1698067480));
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("select * from Citations, Vehicles where Citations.licenseNum = Vehicles.carLicenseNumber AND citationNum = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true);
        when(results.getInt("citationNum")).thenReturn(1);
        when(results.getString("carLicenseNumber")).thenReturn("testLicense");
        when(results.getString("model")).thenReturn("testModel");
        when(results.getString("color")).thenReturn("testColor");
        when(results.getString("manufacturer")).thenReturn("testManf");
        when(results.getInt("year")).thenReturn(1234);
        when(results.getString("lotName")).thenReturn("testLotName");
        when(results.getString("category")).thenReturn("testCategory");
        when(results.getDouble("fee")).thenReturn(1.50);
        when(results.getString("paymentStatus")).thenReturn("testStatus");
        when(results.getDate("citationDate")).thenReturn(new Date(1698067480));
        when(results.getTime("citationTime")).thenReturn(new Time(1698067480));
        CollectionUtils.isEqualCollection(Collections.singleton(citation), Collections.singleton(citationsService.getByNumber(1)));
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void createCitation_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Vehicles (carLicenseNumber, model, color, manufacturer, year) VALUES (?, ?, ?, ?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        when(connection.prepareStatement("INSERT INTO Citations (licenseNum, lotName, category, fee, paymentStatus, citationDate, citationTime) VALUES (?,?,?,?,?,?,?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        citationsService.createCitation(new Citation(1, new Vehicle("testLicense", "testModel", "testColor", "testManf", 1234), "testLotName", "testCategory", 1.50, "testStatus", new Date(1698067480), new Time(1698067480)), true);
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void createCitation_vehicleCreatedErrCreatedCitation() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Vehicles (carLicenseNumber, model, color, manufacturer, year) VALUES (?, ?, ?, ?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        when(connection.prepareStatement("INSERT INTO Citations (licenseNum, lotName, category, fee, paymentStatus, citationDate, citationTime) VALUES (?,?,?,?,?,?,?)")).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            citationsService.createCitation(new Citation(1, new Vehicle("testLicense", "testModel", "testColor", "testManf", 1234), "testLotName", "testCategory", 1.50, "testStatus", new Date(1698067480), new Time(1698067480)), true);
        });
        verify(connection, times(1)).rollback();
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void createCitation_vehicleCreatedErrCreatedCitationRollbackFailed() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Vehicles (carLicenseNumber, model, color, manufacturer, year) VALUES (?, ?, ?, ?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        when(connection.prepareStatement("INSERT INTO Citations (licenseNum, lotName, category, fee, paymentStatus, citationDate, citationTime) VALUES (?,?,?,?,?,?,?)")).thenThrow(new SQLException());
        doThrow(new SQLException()).when(connection).rollback();
        Assertions.assertThrows(SQLException.class, () -> {
            citationsService.createCitation(new Citation(1, new Vehicle("testLicense", "testModel", "testColor", "testManf", 1234), "testLotName", "testCategory", 1.50, "testStatus", new Date(1698067480), new Time(1698067480)), true);
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void updateCitation_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Citations SET licenseNum=?, lotName=?, category=?, fee=?, paymentStatus=?, citationDate=?, citationTime=? WHERE citationNum = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        citationsService.updateCitation(new Citation(1, new Vehicle("testLicense", "testModel", "testColor", "testManf", 1234), "testLotName", "testCategory", 1.50, "testStatus", new Date(1698067480), new Time(1698067480)));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void updateCitation_errUpdatingCitation() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Citations SET licenseNum=?, lotName=?, category=?, fee=?, paymentStatus=?, citationDate=?, citationTime=? WHERE citationNum = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            citationsService.updateCitation(new Citation(1, new Vehicle("testLicense", "testModel", "testColor", "testManf", 1234), "testLotName", "testCategory", 1.50, "testStatus", new Date(1698067480), new Time(1698067480)));
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void deleteCitation_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Citations WHERE citationNum = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        citationsService.deleteCitationByNumber(1);
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void deleteCitation_errDeletingCitation() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Citations WHERE citationNum = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            citationsService.deleteCitationByNumber(1);
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }


    @Test
    void appealCitation_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Citations SET paymentStatus=\"APPEALED\" WHERE citationNum = ? AND paymentStatus=\"DUE\"")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        assertTrue(citationsService.appealCitation(1));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void appealCitation_error() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(SQLException.class);
        assertFalse(citationsService.appealCitation(1));
        verify(dbService, times(0)).close(connection, statement, null);
    }

    @Test
    void payCitation_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Citations SET paymentStatus=\"PAID\" WHERE citationNum = ? AND paymentStatus=\"DUE\"")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        assertTrue(citationsService.payCitation(1));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void payCitation_error() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(SQLException.class);
        assertFalse(citationsService.payCitation(1));
        verify(dbService, times(0)).close(connection, statement, null);
    }
}