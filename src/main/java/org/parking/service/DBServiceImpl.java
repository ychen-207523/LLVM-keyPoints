package org.parking.service;

import java.sql.*;

public class DBServiceImpl implements DBService {
    // TODO: Populate these before testing locally
    static final String jdbcURL = "jdbc:mysql://localhost:3306/Parking";
    static final String user = "test";
    static final String password = "test123";

    // connectAndReturnConnection will create a sql connection and return a Connection. It will throw
    // a SQLException if an issue is encountered.
    public Connection connectAndReturnConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, user, password);
            return connection;
        } catch (SQLException e) {
            System.out.println("Error creating connection");
            System.out.println(e.getMessage());
            throw e;
        }
    }

    // close will take a connection, statement, and result set and attempt to close them.
    // If there are any issues calling close on any of these objects it will catch the error
    // and print the stack trace. It will only attempt to close objects that are not null.
    public void close(Connection connection, Statement statement, ResultSet results) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection");
                System.out.println(e.getMessage());
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("Error closing statement");
                System.out.println(e.getMessage());
            }
        }
        if (results != null) {
            try {
                results.close();
            } catch (SQLException e) {
                System.out.println("Error closing results");
                System.out.println(e.getMessage());
            }
        }
    }
}

