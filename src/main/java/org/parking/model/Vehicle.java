package org.parking.model;

import org.parking.model.Permit;

/***********************************
 * Vehicle Attributes:
 * - carLicenseNumber       varchar(255) PRIMARY KEY NOT NULL,
 * - org.parking.model                  varchar(255) NOT NULL,
 * - color                  varchar(255) NOT NULL,
 * - manufacturer           varchar(255) NOT NULL,
 * - year                   integer
 ***********************************/

public class Vehicle
{
    private String license;
    private String model;
    private String color;
    private String manufacturer;
    private int year;

    public String getLicense() {
        return license;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getYear() {
        return year;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Vehicle(String license, String model, String color, String manufacturer, int year) throws IllegalArgumentException {
        this.license = license;
        this.model = model;
        this.color = color;
        this.manufacturer = manufacturer;
        this.year = year;

    }

    @Override
    public String toString() {
        // Seralize driver object to string
        return "License: " + this.license + "\nModel: " + this.model + "\nColor: " + this.color + "\nManufacturer: "
                + this.manufacturer + "\nYear: " + this.year + "\n";
    }

}
