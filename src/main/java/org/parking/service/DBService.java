package org.parking.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBService {
    Connection connectAndReturnConnection() throws SQLException;
    void close(Connection connection, Statement statement, ResultSet result);
}
