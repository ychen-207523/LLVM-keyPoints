package org.parking.service;

import org.parking.model.Vehicle;

import java.sql.*;
import java.util.Collection;

/***************************************************************************
 * CRUD operations for the Vehicles table. Includes functions to read all,
 * get by license, create, update, and delete vehicles.
 * -------------------------------------------------------------------------
 * Each function throws SQLException if an SQL error occurs and prints any
 * other error messages. If a read returns a null value, it means the object
 * does not exist. Create, Update and Delete return boolean values based on
 * the success of their run.
 ****************************************************************************/
public interface VehicleService {
    public Collection<Vehicle> getAll() throws SQLException;
    public Vehicle getByLicense(String id) throws SQLException;
    public boolean update(Vehicle vehicle) throws SQLException;
    public boolean delete(String license) throws SQLException;
    public boolean create(Vehicle vehicle) throws SQLException;

}