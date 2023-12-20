package org.parking.model;

import java.sql.Date;
import java.sql.Time;
import org.parking.model.Driver;
import org.parking.model.ParkingLot;
import org.parking.model.Zone;

public class Permit
{
    private String permitID;
    private String permitType;
    private String zoneID;
    private String associatedID;
    private String carLicenseNum;
    private String spaceType;
    private Date startDate;
    private Date expirationDate;
    private Time expirationTime;


    public Permit(String permitID, String permitType, String zoneID, String associatedID, String carLicenseNum, String spaceType, Date startDate, Date expirationDate, Time expirationTime) {
        this.permitID = permitID;
        this.permitType = permitType;
        this.zoneID = zoneID;
        this.associatedID = associatedID;
        this.carLicenseNum = carLicenseNum;
        this.spaceType = spaceType;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.expirationTime = expirationTime;
    }

    public String getPermitID() {
        return permitID;
    }

    public void setPermitID(String permitID) {
        this.permitID = permitID;
    }

    public String getPermitType() {
        return permitType;
    }

    public void setPermitType(String permitType) {
        this.permitType = permitType;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getAssociatedID() {
        return associatedID;
    }

    public void setAssociatedID(String associatedID) {
        this.associatedID = associatedID;
    }

    public String getCarLicenseNum() {
        return carLicenseNum;
    }

    public void setCarLicenseNum(String carLicenseNum) {
        this.carLicenseNum = carLicenseNum;
    }

    public String getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(String spaceType) {
        this.spaceType = spaceType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Time getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Time expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String toString() {
        return "Permit{" +
                "permitID='" + permitID + '\'' +
                ", permitType='" + permitType + '\'' +
                ", zoneID='" + zoneID + '\'' +
                ", associatedID='" + associatedID + '\'' +
                ", carLicenseNum='" + carLicenseNum + '\'' +
                ", spaceType='" + spaceType + '\'' +
                ", startDate=" + startDate +
                ", expirationDate=" + expirationDate +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
