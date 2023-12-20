package org.parking.service;

import org.parking.model.Permit;
import org.parking.model.Vehicle;
import org.parking.model.Driver;
import java.sql.*;
import java.sql.SQLException;
import java.util.Collection;


public interface PermitsService {

    public Collection<Permit> getPermitInfo(String permitID) throws SQLException;

    public void enterPermitInfo(Permit permit) throws SQLException;

    public void updatePermitInfo(String permitID, String permitType, String zoneID, String spaceType,
                                 Date startDate, Date expirationDate, Time expirationTime) throws SQLException;

    public void deletePermitInfo(String permitID) throws SQLException;

    public int getPermitsNumberForDriver(String associatedID) throws SQLException;

    public void assignPermitToDriver(String permitID, String permitType, String zoneID, String spaceType,
                                     Date startDate, Date expirationDate, Time expirationTime, Driver driver, Vehicle vehicle) throws SQLException;

    public int getVehicleNumberofPermit(String permitID) throws SQLException;

    public void removeVehicleFromPermit(String permitID, String carLicenseNum) throws SQLException;

    public void addVehicleToPermit(String permitID, String newCarLicenseNum) throws SQLException;

    public void addVehicleToPermitForEmployee(Permit permit, String newCarLicenseNum) throws SQLException;

    public Collection<Permit> getPermitPerCarLicense(String carLicenseNum) throws SQLException;

}
