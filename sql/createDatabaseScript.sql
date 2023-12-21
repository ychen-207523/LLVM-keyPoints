-- Drop the table if it exists.
DROP DATABASE IF EXISTS Parking;

-- Create database WPMS (Wolf Parking Management System)
CREATE DATABASE Parking;

USE Parking;

-- Create all of the tables for the database.
CREATE TABLE Drivers
(
	id			VARCHAR(255) PRIMARY KEY NOT NULL,
	name		VARCHAR(255) NOT NULL,
	status		VARCHAR(1) NOT NULL
);

CREATE TABLE ParkingLots
(
  name    varchar(255) PRIMARY KEY NOT NULL,
  address varchar(255) NOT NULL
);

CREATE TABLE Zones
(
	id		VARCHAR(255) NOT NULL,
	lotName	VARCHAR(255) NOT NULL,
	FOREIGN KEY (lotName) REFERENCES ParkingLots (name)
		ON UPDATE CASCADE,
	PRIMARY KEY(id, lotName)
);

CREATE TABLE Spaces
(
  number                integer NOT NULL,
  type                  varchar(11) DEFAULT 'regular',
  status                boolean NOT NULL,
  zoneID                varchar(255) NOT NULL,
  lotName               varchar(255) NOT NULL,
  FOREIGN KEY (zoneID)  REFERENCES Zones (id) ON UPDATE CASCADE,
  FOREIGN KEY (lotName) REFERENCES Zones (lotName) ON UPDATE CASCADE,
  PRIMARY KEY (number, zoneID, lotName) 
);


CREATE TABLE Vehicles
(
  carLicenseNumber       varchar(255) PRIMARY KEY NOT NULL,
  model                  varchar(255) NOT NULL,
  color                  varchar(255) NOT NULL,
  manufacturer           varchar(255) NOT NULL,
  year                   integer
);


CREATE TABLE Permits
(
  id                         integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
  permitID                   varchar(10) NOT NULL,
  permitType                 varchar(20) NOT NULL,
  zoneID                     varchar(255) NOT NULL,
  associatedID               varchar(255) NOT NULL,
  carLicenseNum       	 	 varchar(255),
  spaceType                  varchar(11) NOT NULL,
  startDate                  date NOT NULL,
  expirationDate             date NOT NULL,
  expirationTime             time NOT NULL,  
  
  FOREIGN KEY (carLicenseNum)     REFERENCES Vehicles (carLicenseNumber) ON UPDATE CASCADE,
  FOREIGN KEY (associatedID) REFERENCES Drivers (id) ON UPDATE CASCADE,
  FOREIGN KEY (zoneID)       REFERENCES Zones (id) ON UPDATE CASCADE
);

CREATE TABLE Citations
(
  citationNum                  integer PRIMARY KEY AUTO_INCREMENT NOT NULL,
  citationDate                 date NOT NULL,
  citationTime                 time NOT NULL,
  paymentStatus                varchar(8) NOT NULL,
  lotName                      varchar(255) NOT NULL,
  category                     varchar(255) NOT NULL,
  fee                          decimal(4,2) NOT NULL,
  licenseNum                   varchar(255) NOT NULL,

  Foreign KEY (licenseNum)	   REFERENCES vehicles (carLicenseNumber) ON UPDATE CASCADE,
  FOREIGN KEY (lotName)        REFERENCES ParkingLots (name) ON UPDATE CASCADE
);