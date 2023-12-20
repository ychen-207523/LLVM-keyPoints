package org.parking.service;

import org.parking.model.*;
import org.parking.model.ParkingLot;
import org.parking.model.Permit;
import org.parking.service.*;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class ReportsServiceImplTest {

    private ReportsServiceImpl reportsService;

    @Mock
    private DBService dbService;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet results;

    @BeforeEach
    void init() { reportsService = new ReportsServiceImpl(dbService); }

    @Test
    void generateCitationReport_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(*) FROM Citations WHERE lotName = ? AND citationDate " +
                "BETWEEN ? AND ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true);
        when(results.getInt(1)).thenReturn(1);

        Assertions.assertEquals(reportsService.generateCitationReport("Lot Name",
                new Date(2023,01,01), new Date(2023, 12,01)),1);

        verify(dbService, times(1)).close(connection, statement, results);

    }

    @Test
    void generateCitationReport_sqlException() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(*) FROM Citations WHERE lotName = ? AND citationDate " +
                "BETWEEN ? AND ?")).thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            reportsService.generateCitationReport("Lot Name", new Date(2023,01,01),
                    new Date(2023, 12,01));
        });

        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void generateZoneReport_success() throws SQLException {
        Collection<Zone> zones = Collections.singleton(new Zone("A","TestLot"));

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT lotName, id FROM Zones ORDER BY lotName")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("id")).thenReturn("A");
        when(results.getString("lotName")).thenReturn("TestLot");

        CollectionUtils.isEqualCollection(zones,reportsService.generateZoneReport());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void generateZoneReport_sqlException() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT lotName, id FROM Zones ORDER BY lotName"))
                .thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            reportsService.generateZoneReport();
        });

        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void generateViolatedCarNumber_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT count(DISTINCT licenseNum) FROM Citations WHERE " +
                "paymentStatus = 'DUE'")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true);
        when(results.getInt(1)).thenReturn(1);

        Assertions.assertEquals(reportsService.generateViolatedCarNumber(),1);

        verify(dbService, times(1)).close(connection, statement, results);

    }

    @Test
    void generateViolatedCarNumber_sqlException() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT count(DISTINCT licenseNum) FROM Citations WHERE " +
                "paymentStatus = 'DUE'")).thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            reportsService.generateViolatedCarNumber();
        });

        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void generateExployeesofZone_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(*) \n" +
                "FROM  (SELECT * FROM Drivers WHERE Drivers.status = 'E') Employees \n" +
                "JOIN (SELECT * FROM Permits WHERE Permits.zoneID = ?) zonePermits \n" +
                "WHERE Employees.id = zonePermits.associatedID;")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true);
        when(results.getInt(1)).thenReturn(1);

        Assertions.assertEquals(reportsService.generateExployeesofZone("A"),1);

        verify(dbService, times(1)).close(connection, statement, results);

    }

    @Test
    void generateExployeesofZone_sqlException() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(*) \n" +
                "FROM  (SELECT * FROM Drivers WHERE Drivers.status = 'E') Employees \n" +
                "JOIN (SELECT * FROM Permits WHERE Permits.zoneID = ?) zonePermits \n" +
                "WHERE Employees.id = zonePermits.associatedID;")).thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            reportsService.generateExployeesofZone("A");
        });

        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void getPermitForDriver_success() throws SQLException {
        Collection<Permit> permits = Collections.singleton(new Permit("testPermit", "",
                "testZone", "", "", "", Date.valueOf("2023-12-01"),
                Date.valueOf("2024-01-01"), Time.valueOf("10:10:10")));

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Permits WHERE associatedID = ?;")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("permitID")).thenReturn("testPermit");
        when(results.getString("permitType")).thenReturn("");
        when(results.getString("zoneID")).thenReturn("testZone");
        when(results.getString("associatedID")).thenReturn("");
        when(results.getString("carLicenseNum")).thenReturn("");
        when(results.getString("spaceType")).thenReturn("");
        when(results.getDate("startDate")).thenReturn(Date.valueOf("2023-12-01"));
        when(results.getDate("expirationDate")).thenReturn(Date.valueOf("2024-01-01"));
        when(results.getTime("expirationTime")).thenReturn(Time.valueOf("10:10:10"));


        CollectionUtils.isEqualCollection(permits,reportsService.getPermitForDriver("testPermit"));
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void getPermitForDriver_sqlException() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Permits WHERE associatedID = ?;"))
                .thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            reportsService.getPermitForDriver("TestID");
        });

        verify(dbService, times(1)).close(connection, null, null);
    }

    @Test
    void getAvailableSpaceinParkingLot_success() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT number  FROM Spaces WHERE type =? AND status = '1' " +
                "AND lotName =?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true);
        when(results.getInt(1)).thenReturn(1);

        Assertions.assertEquals(reportsService.getAvailableSpaceinParkingLot("TestLot", "Regular"),1);

        verify(dbService, times(1)).close(connection, statement, results);

    }

    @Test
    void getAvailableSpaceinParkingLot_sqlException() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT number  FROM Spaces WHERE type =? AND status = '1' " +
                "AND lotName =?")).thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            reportsService.getAvailableSpaceinParkingLot("TestLot", "Regular");
        });

        verify(dbService, times(1)).close(connection, null, null);
    }


    // private Citation
}
