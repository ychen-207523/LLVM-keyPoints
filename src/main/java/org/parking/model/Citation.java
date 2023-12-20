package org.parking.model;

import java.sql.Date;
import java.sql.Time;
import org.parking.model.ParkingLot;

public class Citation
{
    private int number;

    private Vehicle vehicle;
    private String lotName;
    private String category;
    private Double fee;
    private String paymentStatus;
    private Date citationDate;
    private Time citationTime;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String status) {
        this.paymentStatus = status;
    }

    public Date getCitationDate() {
        return citationDate;
    }

    public void setCitationDate(Date citationDate) {
        this.citationDate = citationDate;
    }

    public Time getCitationTime() {
        return citationTime;
    }

    public void setCitationTime(Time citationTime) {
        this.citationTime = citationTime;
    }

    public Citation(int number, Vehicle vehicle, String lotName, String category, Double fee, String status, Date citationDate, Time citationTime) throws IllegalArgumentException {
        if (number != 0) {
            this.number = number;
        }
        this.vehicle = vehicle;
        this.lotName = lotName;
        this.category = category;
        this.fee = fee;
        this.paymentStatus = status;
        this.citationDate = citationDate;
        this.citationTime = citationTime;
    }
    @Override
    public String toString() {
        // This is just here for demo purposes. We can probably remove it later
        return "Citation#: " + this.number + "\nLicense: " + this.vehicle.getLicense() +  "\nModel: " + this.vehicle.getModel() + "\nColor: " + this.vehicle.getColor() +"\nLot Name: " + this.lotName + "\nCategory: " + this.category + "\nfee: " + this.fee + "\nPayment Status: " + this.paymentStatus + "\nCitation Date: " + this.citationDate + "\nCitation Time: " + this.citationTime + "\n";
    }
}
