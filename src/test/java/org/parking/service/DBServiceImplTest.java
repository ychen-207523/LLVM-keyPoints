package org.parking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;


import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class DBServiceImplTest {

    private DBServiceImpl dbServiceImpl;
    @Mock
    private Connection connection;
    @Mock
    private Statement statement;

    @Mock
    private ResultSet results;


    @BeforeEach
    void init() {
        openMocks( this );
        dbServiceImpl = new DBServiceImpl();
    }

    @Test
    void  connectAndReturnConnection_success() throws SQLException {
        MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class);
        when(DriverManager.getConnection("", "", "")).thenReturn(connection);
        dbServiceImpl.connectAndReturnConnection();
        driverManager.close();
    }

    @Test
    void  connectAndReturnConnection_SQLException() throws SQLException {
        MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class);
        when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenThrow(SQLException.class);

        Assertions.assertThrows(SQLException.class, () -> {
            dbServiceImpl.connectAndReturnConnection();
        });
        driverManager.close();
    }

    @Test
    void close_success() throws SQLException {
        dbServiceImpl.close(connection, statement, results);
    }

    @Test
    void close_ErrorClosingConnection() throws SQLException {
        doThrow(new SQLException("something went wrong closing the connection")).when(connection).close();
        dbServiceImpl.close(connection, null, null);
    }

    @Test
    void close_ErrorClosingStatement() throws SQLException {
        doThrow(new SQLException("something went wrong closing the statement")).when(statement).close();
        dbServiceImpl.close(connection, statement, null);
    }

    @Test
    void close_ErrorClosingResults() throws SQLException {
        doThrow(new SQLException("something went wrong closing the result set")).when(results).close();
        dbServiceImpl.close(connection, statement, results);
    }
}