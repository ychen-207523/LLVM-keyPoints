package org.parking.service;

import org.parking.model.Driver;

import java.sql.*;
import java.util.Collection;

/***************************************************************************
 * CRUD operations for the Drivers table. Includes functions to read all,
 * get by id, create, update, and delete drivers.
 * -------------------------------------------------------------------------
 * Each function throws SQLException if an SQL error occurs and prints any
 * other error messages. If a read returns a null value, it means the object
 * does not exist. Create, Update and Delete return boolean values based on
 * the success of their run.
 ****************************************************************************/
public interface DriversService {
    public Collection<Driver> getAll() throws SQLException;
    public Driver getById(String id) throws SQLException;
    public boolean update(Driver driver) throws SQLException;
    public boolean delete(String id) throws SQLException;
    public boolean create(Driver driver) throws SQLException;

}