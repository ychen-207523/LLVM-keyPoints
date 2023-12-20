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
import org.parking.model.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SpaceServiceImplTest {
    private SpaceService spaceService;

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
        spaceService = new SpaceServiceImpl(dbService);
    }

    /* Test GetAll function */
    @Test
    void testGetAll_NoConnection() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertEquals(0, spaceService.getAll().size());
        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testGetAll_success() throws SQLException {
        Space expected = new Space(1, "electric", true, "A", "testLotName");
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Spaces")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getInt("number")).thenReturn(1);
        when(results.getString("type")).thenReturn("electric");
        when(results.getBoolean("status")).thenReturn(true);
        when(results.getString("zoneID")).thenReturn("A");
        when(results.getString("lotName")).thenReturn("testLotName");

        Collection<Space> resultSpaces = spaceService.getAll();
        Space actual = resultSpaces.iterator().next();
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getZoneID(), actual.getZoneID());
        assertEquals(expected.getLotName(), actual.getLotName());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void testGetAll_empty() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Spaces")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(false);

        assertEquals(0, spaceService.getAll().size());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    /* Test Get Space function */
    @Test
    void testGetSpace_noConn() throws SQLException {
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertNull(spaceService.getSpace(1, "A", "testLotName"));
        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testGetSpace_success() throws SQLException {
        Space expectedSpace = new Space(1, "electric", true, "A", "testLotName");
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Spaces WHERE number=? AND zoneID=? AND lotName=?"))
                .thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.getInt("number")).thenReturn(1);
        when(results.getString("type")).thenReturn("electric");
        when(results.getBoolean("status")).thenReturn(true);
        when(results.getString("zoneID")).thenReturn("A");
        when(results.getString("lotName")).thenReturn("testLotName");

        Space actualSpace = spaceService.getSpace(1, "A", "testLotName");
        assertEquals(expectedSpace.getNumber(), actualSpace.getNumber());
        assertEquals(expectedSpace.getType(), actualSpace.getType());
        assertEquals(expectedSpace.getStatus(), actualSpace.getStatus());
        assertEquals(expectedSpace.getZoneID(), actualSpace.getZoneID());
        assertEquals(expectedSpace.getLotName(), actualSpace.getLotName());
        verify(dbService, times(1)).close(connection, statement, results);
    }

    @Test
    void testGetSpace_notFound() throws SQLException
    {
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Spaces WHERE number=? AND zoneID=? AND lotName=?")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(false);

        assertNull(spaceService.getSpace(1, "A", "testLotName"));
        verify(dbService, times(1)).close(connection, statement, results);
    }

    /* Test Create Space function */
    @Test
    void testCreateSpace_noConn() throws SQLException {
        Space test = new Space(1, "electric", true, "A", "testLotName");
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(spaceService.createSpace(test));
        verify(dbService, times(0)).close(connection, statement, null);
    }

    @Test
    void testCreateSpace_success() throws SQLException {
        Space test = new Space(1, "electric", true, "A", "testLotName");
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Spaces VALUES (?, ?, ?, ?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(spaceService.createSpace(test));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testCreateSpace_duplicate() throws SQLException {
        Space test = new Space(1, "electric", true, "A", "testLotName");
        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("INSERT INTO Spaces VALUES (?, ?, ?, ?, ?)")).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(spaceService.createSpace(test));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    /* Test Update Space function */
    @Test
    void testUpdateSpace_noConn() throws SQLException {
        Space test1 = new Space(1, "electric", true, "A", "testLotName");
        Space test2 = new Space(2, "regular", false, "B", "testLotName2");

        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(spaceService.updateSpace(test1, test2));

        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testUpdateSpace_success() throws SQLException {
        Space test1 = new Space(1, "electric", true, "A", "testLotName");
        Space test2 = new Space(2, "regular", false, "B", "testLotName2");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement(
                "UPDATE Spaces SET " +
                        "number=?, " +
                        "type=?, " +
                        "status=?, " +
                        "zoneID=?, " +
                        "lotName=? " +
                        "WHERE number=? AND zoneID=? AND lotName=?"))
                .thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(spaceService.updateSpace(test1, test2));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testUpdateSpace_notFound() throws SQLException {
        Space test1 = new Space(1, "electric", true, "A", "testLotName");
        Space test2 = new Space(2, "regular", false, "B", "testLotName2");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement(
                "UPDATE Spaces SET " +
                        "number=?, " +
                        "type=?, " +
                        "status=?, " +
                        "zoneID=?, " +
                        "lotName=? " +
                        "WHERE number=? AND zoneID=? AND lotName=?"))
                .thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(spaceService.updateSpace(test1, test2));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    /* Test Delete Space function */
    @Test
    void testDeleteSpace_noConn() throws SQLException {
        Space test = new Space(1, "electric", true, "A", "testLotName");
        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(spaceService.deleteSpace(test));

        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testDeleteSpace_success() throws SQLException {
        Space test = new Space(1, "electric", true, "A", "testLotName");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Spaces WHERE number=? AND zoneID=? AND lotName=?"))
                .thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(spaceService.deleteSpace(test));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testDeleteSpace_notFound() throws SQLException {
        Space test = new Space(1, "electric", true, "A", "testLotName");

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("DELETE FROM Spaces WHERE number=? AND zoneID=? AND lotName=?"))
                .thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(spaceService.deleteSpace(test));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testAssignTypeOfASpace_NoConnection() throws SQLException {
        Space test = new Space(1, "electric", true, "A", "testLotName");

        when(dbService.connectAndReturnConnection()).thenThrow(new SQLException());
        assertFalse(spaceService.assignTypeOfASpace(test.getLotName(), test.getZoneID(), test.getNumber(), test.getType()));

        verify(dbService, times(0)).close(connection, statement, results);
    }

    @Test
    void testAssignTypeOfASpace_success() throws SQLException {
        Space test = new Space(1, "electric", true, "A", "testLotName");
        String testType = "regular";

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Spaces SET type=? WHERE number=? AND zoneID=? AND lotName=?"))
                .thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(spaceService.assignTypeOfASpace(test.getLotName(),
                test.getZoneID(),
                test.getNumber(),
                testType));
        verify(dbService, times(1)).close(connection, statement, null);
    }

    @Test
    void testAssignTypeOfASpace_empty() throws SQLException {
        Space test = new Space(1, "electric", true, "A", "testLotName");
        String testType = "regular";

        when(dbService.connectAndReturnConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Spaces SET type=? WHERE number=? AND zoneID=? AND lotName=?"))
                .thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(spaceService.assignTypeOfASpace(test.getLotName(),
                test.getZoneID(),
                test.getNumber(),
                testType));
        verify(dbService, times(1)).close(connection, statement, null);
    }
}