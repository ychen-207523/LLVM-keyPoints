
package org.parking.service;

import org.parking.model.Permit;
import org.parking.service.DBService;
import org.parking.service.PermitsService;
import org.parking.service.PermitsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import org.junit.jupiter.api.Assertions;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermitsServiceImplTest {

    private PermitsService permitService;

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
        permitService = new PermitsServiceImpl(dbService);
    }


    @Test
    void testgetPermitInfo() throws SQLException {

        String permitID = "TTFWX";
        String permitType = "Commuter";
        String zoneID = "B";
        String associatedID = "466399121";
        String carLicenseNum = "GGdel";
        String spaceType = "Regular";
        Date startDate = Date.valueOf("2022-01-01");
        Date expirationDate = Date.valueOf("2025-10-22");
        Time expirationTime = Time.valueOf("20:00:00");


        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Permits WHERE permitID = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("permitID")).thenReturn(permitID);
        when(results.getString("permitType")).thenReturn(permitType);
        when(results.getString("zoneID")).thenReturn(zoneID);
        when(results.getString("associatedID")).thenReturn(associatedID);
        when(results.getString("carLicenseNum")).thenReturn(carLicenseNum);
        when(results.getString("spaceType")).thenReturn(spaceType);
        when(results.getDate("startDate")).thenReturn(startDate);
        when(results.getDate("expirationDate")).thenReturn(expirationDate);
        when(results.getTime("expirationTime")).thenReturn(expirationTime);

        Permit testPermit = new Permit("TTFWX", "Commuter", "B",
                "466399121", "GGdel", "Regular",
                Date.valueOf("2022-01-01"), Date.valueOf("2025-10-22"), Time.valueOf("20:00:00"));


        PermitsServiceImpl permitsService = new PermitsServiceImpl(dbService);
        permitsService.getPermitInfo(testPermit.getPermitID());


    }

    @Test
    void testenterPermitInfo() throws SQLException {

        Permit testPermit = new Permit("TTFWX", "Commuter", "B",
                "466399121", "GGdel", "Regular",
                Date.valueOf("2022-01-01"), Date.valueOf("2025-10-22"), Time.valueOf("20:00:00"));

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement( "INSERT INTO Permits (permitID, permitType, zoneID, associatedID, carLicenseNum, spaceType, startDate, expirationDate, expirationTime) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        PermitsServiceImpl permitsService = new PermitsServiceImpl(dbService);

        permitsService.enterPermitInfo(testPermit);

        // Verify the interactions with the mocks
        verify(dbService).connectAndReturnConnection();
        verify(connection).prepareStatement(anyString());
        verify(statement).executeUpdate();
        verify(dbService).close(connection, statement, null);
    };

    @Test
    void enterPermitInfo_connSqlFailure() throws SQLException {
        Permit testPermit = new Permit("TTFWX", "Commuter", "B",
                "466399121", "GGdel", "Regular",
                Date.valueOf("2022-01-01"), Date.valueOf("2025-10-22"), Time.valueOf("20:00:00"));
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
           permitService.enterPermitInfo(testPermit);
        });
    }

    @Test
    void enterPermitInfo_statementSqlFailureCleanupConnection() throws SQLException {
        Permit testPermit = new Permit("TTFWX", "Commuter", "B",
                "466399121", "GGdel", "Regular",
                Date.valueOf("2022-01-01"), Date.valueOf("2025-10-22"), Time.valueOf("20:00:00"));

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Permits (permitID, permitType, zoneID, associatedID, carLicenseNum, spaceType, startDate, expirationDate, expirationTime) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            permitService.enterPermitInfo(testPermit);
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testupdatePermitInfo() throws SQLException {

        String permitID = "TTFWX";
        String permitType = "Commuter";
        String zoneID = "B";
        String spaceType = "Regular";
        Date startDate = Date.valueOf("2022-01-01");
        Date expirationDate = Date.valueOf("2025-10-22");
        Time expirationTime = Time.valueOf("20:00:00");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Permits " +
                "SET permitType = ?, zoneID = ?," +
                "spaceType = ?, startDate = ?, expirationDate = ?, expirationTime = ? " +
                "WHERE permitID = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        permitService.updatePermitInfo(permitID, permitType, zoneID, spaceType, startDate, expirationDate, expirationTime);
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testupdatePermitInfo_connSqlFailure() throws SQLException {
        String permitID = "TTFWX";
        String permitType = "Commuter";
        String zoneID = "B";
        String spaceType = "Regular";
        Date startDate = Date.valueOf("2022-01-01");
        Date expirationDate = Date.valueOf("2025-10-22");
        Time expirationTime = Time.valueOf("20:00:00");
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            permitService.updatePermitInfo(permitID, permitType, zoneID, spaceType, startDate, expirationDate, expirationTime);
        });
    }

    @Test
    void testupdatePermitInfo_statementSqlFailureCleanupConnection() throws SQLException {
        String permitID = "TTFWX";
        String permitType = "Commuter";
        String zoneID = "B";
        String spaceType = "Regular";
        Date startDate = Date.valueOf("2022-01-01");
        Date expirationDate = Date.valueOf("2025-10-22");
        Time expirationTime = Time.valueOf("20:00:00");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Permits " +
                "SET permitType = ?, zoneID = ?," +
                "spaceType = ?, startDate = ?, expirationDate = ?, expirationTime = ? " +
                "WHERE permitID = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            permitService.updatePermitInfo(permitID, permitType, zoneID, spaceType, startDate, expirationDate, expirationTime);
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testdeletePermitInfo() throws SQLException {
        String permitID = "TTFWX";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Permits WHERE permitID = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        permitService.deletePermitInfo(permitID);
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testdeletePermitInfo_connSqlFailure() throws SQLException {
        String permitID = "TTFWX";
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            permitService.deletePermitInfo(permitID);
        });
    }

    @Test
    void testdeletePermitInfo_statementSqlFailureCleanupConnection() throws SQLException {
        String permitID = "TTFWX";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Permits WHERE permitID = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            permitService.deletePermitInfo(permitID);
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testgetPermitsNumberForDriver() throws SQLException {
        String associatedID = "466399121";

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(DISTINCT permitID) AS permitCount FROM Permits WHERE associatedID = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getInt("permitCount")).thenReturn(1);

        permitService.getPermitsNumberForDriver(associatedID);

    }

    @Test
    void testgetPermitsNumberForDriver_statementSqlFailureCleanupConnection() throws SQLException {
        String associatedID = "466399121";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(DISTINCT permitID) AS permitCount FROM Permits WHERE associatedID = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            permitService.getPermitsNumberForDriver(associatedID);
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testgetVehicleNumberofPermit() throws SQLException {
        String permitID = "TTFWX";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(carLicenseNum) AS vehicleCount FROM Permits WHERE permitID = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true);
        when(results.getInt("vehicleCount")).thenReturn(2);

        permitService.getVehicleNumberofPermit(permitID);
    }

    @Test
    void testgetVehicleNumberofPermit_statementSqlFailureCleanupConnection() throws SQLException {
        String permitID = "TTFWX";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(carLicenseNum) AS vehicleCount FROM Permits WHERE permitID = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            permitService.getVehicleNumberofPermit(permitID);
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testremoveVehicleFromPermit() throws SQLException {
        String permitID = "TTFWX";
        String carLicenseNum = "GGdel";

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Vehicles WHERE carLicenseNumber = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true);
        when(connection.prepareStatement("UPDATE Permits SET carLicenseNum = NULL WHERE permitID = ? AND carLicenseNum = ?")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        permitService.removeVehicleFromPermit(permitID,carLicenseNum);
    }

    @Test
    void testremoveVehicleFromPermit_statementSqlFailureCleanupConnection() throws SQLException {
        String permitID = "TTFWX";
        String carLicenseNum = "GGdel";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        doNothing().when(connection).setAutoCommit(false);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true);
        when(statement.executeUpdate()).thenThrow(new SQLException("Update failed"));

        doNothing().when(connection).rollback();
        permitService.removeVehicleFromPermit(permitID, carLicenseNum);

        // Verify that the resources are cleaned up
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testaddVehicleToPermit() throws SQLException {
        String permitID = "TTFWX";
        String carLicenseNum = "GGdel";

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Permits SET carLicenseNum = ? WHERE permitID = ? AND carLicenseNum IS NULL")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        permitService.addVehicleToPermit(permitID,carLicenseNum);
        verify(dbService, times(1)).close(connection, statement, null);

    }

    @Test
    void testaddVehicleToPermit_statementSqlFailureCleanupConnection() throws SQLException {
        String permitID = "TTFWX";
        String carLicenseNum = "GGdel";
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Permits SET carLicenseNum = ? WHERE permitID = ? AND carLicenseNum IS NULL")).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            permitService.addVehicleToPermit(permitID,carLicenseNum);
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testgetPermitPerCarLicense() throws SQLException {
        String permitID = "TTFWX";
        String permitType = "Commuter";
        String zoneID = "B";
        String associatedID = "466399121";
        String carLicenseNum = "GGdel";
        String spaceType = "Regular";
        Date startDate = Date.valueOf("2022-01-01");
        Date expirationDate = Date.valueOf("2025-10-22");
        Time expirationTime = Time.valueOf("20:00:00");
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Permits WHERE carLicenseNum = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getString("permitID")).thenReturn(permitID);
        when(results.getString("permitType")).thenReturn(permitType);
        when(results.getString("zoneID")).thenReturn(zoneID);
        when(results.getString("associatedID")).thenReturn(associatedID);
        when(results.getString("carLicenseNum")).thenReturn(carLicenseNum);
        when(results.getString("spaceType")).thenReturn(spaceType);
        when(results.getDate("startDate")).thenReturn(startDate);
        when(results.getDate("expirationDate")).thenReturn(expirationDate);
        when(results.getTime("expirationTime")).thenReturn(expirationTime);
        permitService.getPermitPerCarLicense(carLicenseNum);
    }

    @Test
    void testgetPermitPerCarLicense_statementSqlFailureCleanupConnection() throws SQLException {
        String carLicenseNum = "GGdel";

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Permits WHERE carLicenseNum = ?")).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException());
        Assertions.assertThrows(SQLException.class, () -> {
            permitService.getPermitPerCarLicense(carLicenseNum);
        });
        verify(dbService, times(1)).close(connection, statement, null);
    }

}
