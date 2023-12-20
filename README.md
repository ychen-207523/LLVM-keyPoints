#Parking-Management-System

## Getting Started

- Clone this repo locally.
- Follow Installation steps

### Installation
From within the repos root directory execute the following which will pull dependencies, run unit
tests, and build the jar in the ./target directory.

`mvn clean package`

You can then run the jar via the following command.

`java -jar target/Parking-Management-System-1.0-SNAPSHOT-jar-with-dependencies.jar`

To run unit tests alone you can execute the following.

`mvn test`

<br /><br />

## Current Task
1. Working on deployment of the code
2. Change the UI

## Model

The `model` package contains all the Java classes that represent entities in our database. Each class corresponds to a table in the database and includes fields that map to the columns within the table.

### Citation
The `Citation` class models the details of a parking citation. It stores information about the citation number, the associated vehicle, the parking lot name where the citation was issued, the category of the violation, the fee charged, payment status, and the date and time when the citation was issued.

#### Attributes
- `int number`: Unique identifier for the citation.
- `Vehicle vehicle`: The vehicle associated with the citation.
- `String lotName`: The name of the parking lot where the citation was issued.
- `String category`: The category of the violation.
- `Double fee`: The fee charged for the violation.
- `String paymentStatus`: The payment status of the citation fee.
- `Date citationDate`: The date when the citation was issued.
- `Time citationTime`: The time when the citation was issued.

### Driver
The `Driver` class encapsulates the information of an individual in our database.

#### Attributes
- `String name`: The full name of the driver.
- `String id`: The unique identifier for the driver.
- `String status`: The current status of the driver, only 'E', 'S', 'V' are allowed.

### Vehicle
The `Vehicle` class represents a vehicle's attributes in the system. It holds information regarding the vehicle's license number, model, color, manufacturer, and year of manufacture.

#### Attributes
- `String license`: The vehicle's license plate number, acting as a unique identifier.
- `String model`: The model of the vehicle.
- `String color`: The color of the vehicle.
- `String manufacturer`: The manufacturer of the vehicle.
- `int year`: The year the vehicle was manufactured.

### ParkingLot
The `ParkingLot` class represents a parking lot within the system. It contains the parking lot's name and address.

#### Attributes
- `String name`: The name of the parking lot.
- `String address`: The physical address of the parking lot.

### Zone
The `Zone` class is designed to represent a specific zone within a parking lot. 

#### Attributes
- `String id`: The identifier for the zone.
- `String lotName`: The name of the parking lot to which the zone belongs.

### Space
The `Space` class defines a parking space within a parking lot. It captures the parking space's number, type, occupancy status, zone identifier, and the lot name it belongs to.

#### Attributes
- `int number`: The number assigned to the parking space, serving as a unique identifier within a parking lot zone.
- `String type`: The type of parking space (e.g., handicapped, compact, regular).
- `boolean status`: The occupancy status of the parking space, where `false` indicates occupied and `true` indicates available.
- `String zoneID`: The identifier of the zone within the parking lot in which the parking space is located.
- `String lotName`: The name of the parking lot in which the parking space is located.

### Permit
The `Permit` class represents a parking permit within the system. It includes information about the permit's ID, type, the zone it's valid for, the ID of the individual or vehicle it's associated with, the vehicle's license number, the type of parking space it permits, and the valid dates and times.

#### Attributes
- `String permitID`: The unique identifier for the permit.
- `String permitType`: The type of permit issued.
- `String zoneID`: The identifier of the zone where the permit is valid.
- `String associatedID`: The identifier of the driver associated with the permit.
- `String carLicenseNum`: The license number of the vehicle associated with the permit.
- `String spaceType`: The type of parking space allowed by the permit.
- `Date startDate`: The start date from which the permit is valid.
- `Date expirationDate`: The expiration date of the permit's validity.
- `Time expirationTime`: The expiration time.

<br /><br />

## Service

The implementation of Service classes follows the pattern of defining an `interface` and its concrete implementations. This approach decouples the service's contract from its implementation, allowing for greater flexibility and ease of testing. 

## DBService Interface

The `DBService`outlines the methods required for establishing a connection to the database and for properly closing database resources.

### Methods

- `Connection connectAndReturnConnection() throws SQLException`: Establishes a connection to the database and returns the `Connection` object. It throws an `SQLException` if a connection cannot be established.
- `void close(Connection connection, Statement statement, ResultSet result)`: Closes the database connection and releases the `Statement` and `ResultSet` resources. It ensures that all database connections are closed properly to prevent resource leaks.

## CitationsService

The `CitationsService` declares methods for retrieving, creating, updating, deleting, and appealing citations.

### Methods

- `Collection<Citation> getAll() throws SQLException`: Retrieves a collection of all `Citation` objects from the database.
- `Citation getByNumber(int number) throws SQLException`: Fetches a single `Citation` by its unique number.
- `void createCitation(Citation citation, Boolean createVehicle) throws SQLException`: Creates a new `Citation`, and optionally a new `Vehicle` record if `createVehicle` is `true`.
- `void updateCitation(Citation citation) throws SQLException`: Updates an existing `Citation` in the database.
- `void deleteCitationByNumber(int number) throws SQLException`: Deletes a `Citation` from the database based on its number.
- `boolean appealCitation(int number)`: Submits an appeal for a `Citation` identified by its number. Returns `true` if the appeal is successful.


## DriversService
The `DriversService` includes methods to read all driver entries, retrieve by ID, create new records, update existing ones, and delete drivers from the database. 

### Methods

- `Collection<Driver> getAll() throws SQLException`: Retrieves all `Driver` entities from the database. If there's an issue with the database access, it throws an `SQLException`.

- `Driver getById(String id) throws SQLException`: Obtains a single `Driver` entity using the unique identifier (`id`). If the `Driver` is not found, it returns `null`.  An `SQLException` is thrown in case of SQL errors during the process.

- `boolean create(Driver driver) throws SQLException`: Attempts to create a new `Driver` record in the database. It returns `true` if the operation is successful, `false` otherwise. An `SQLException` is thrown in case of SQL errors during the process.

- `boolean update(Driver driver) throws SQLException`: Updates the details of an existing `Driver` in the database. It returns `true` if the update is successful, `false` otherwise. An `SQLException` is thrown if there's an error during the update.

- `boolean delete(String id) throws SQLException`: Removes a `Driver` entity from the database using the given identifier (`id`). It returns `true` if the deletion is successful, `false` otherwise. An `SQLException` is thrown in case of SQL errors.

## VehicleService
The `VehicleService` defines the methods necessary to retrieve all vehicle records, obtain a vehicle by its license number, and perform create, update, and delete operations on vehicle records.

### Methods

- `Collection<Vehicle> getAll() throws SQLException`: Retrieves all vehicle records from the database. Throws an `SQLException` if a database access error occurs.

- `Vehicle getByLicense(String license) throws SQLException`: Fetches a single vehicle from the database using the vehicle's license number as a search criterion. Returns `null` if the vehicle does not exist. Any SQL errors encountered will result in an `SQLException`.

- `boolean create(Vehicle vehicle) throws SQLException`: Adds a new vehicle record to the database using the information from the provided `Vehicle` object. Returns `true` if the operation is successful, `false` if it fails. Any SQL errors encountered will result in an `SQLException`.

- `boolean update(Vehicle vehicle) throws SQLException`: Updates an existing vehicle's information in the database based on the data in the provided `Vehicle` object. Returns `true` if the update is successful, `false` otherwise. Throws an `SQLException` in the event of SQL errors.

- `boolean delete(String license) throws SQLException`: Deletes a vehicle record from the database using the provided license number. Returns `true` if the deletion is successful, `false` if it fails. An `SQLException` is thrown if SQL errors occur during the operation.


## ParkingLotService
The `ParkingLotService` provides methods for retrieving all parking lot entries, fetching a specific parking lot by name, and performing create, update, and delete operations on parking lot records.

### Methods

- `Collection<ParkingLot> getAll()`: Retrieves all parking lot records from the database. Returns a collection of `ParkingLot` objects.

- `ParkingLot getParkingLot(String name)`: Obtains a single `ParkingLot` object from the database based on the parking lot's name. If the parking lot does not exist, this method may return `null`.

- `boolean createParkingLot(ParkingLot lot)`: Attempts to add a new parking lot record to the database. Returns `true` if the creation is successful, `false` otherwise.

- `boolean updateParkingLot(String name, ParkingLot lot)`: Updates the details of an existing parking lot in the database. The `name` parameter is used to identify the parking lot to be updated. Returns `true` if the update is successful, `false` otherwise.

- `boolean deleteParkingLot(String name)`: Removes a parking lot record from the database using the parking lot's name as an identifier. Returns `true` if the deletion is successful, `false` if it fails.


## ZoneService
The `ZoneService` includes methods for retrieving all zones, fetching specific zones by identifiers, creating new zones, updating existing zones, and deleting zones.

### Methods

- `Collection<Zone> getAll()`: Retrieves all zones from the database.

- `Zone getZone(String id, String lotName)`: Fetches a specific zone using its identifier and the name of the parking lot it belongs to. Return `null` if the zone does not exist.

- `Collection<Zone> getZonesById(String id)`: Retrieves all zones matching the `id` across all parking lots.

- `Collection<Zone> getZonesByLotName(String lotName)`: Retrieves all zones within a specific parking lot.

- `boolean createZone(Zone zone)`: Attempts to create a new zone record in the database. Returns `true` if the operation is successful, `false` otherwise.

- `boolean updateZone(Zone originalZone, Zone updatedZone)`: Updates an existing zone with new information. Uses `originalZone` to identify the zone to be updated and applies the changes from `updatedZone`.

- `boolean deleteZone(Zone zone)`: Removes a zone record from the database. Returns `true` if the deletion is successful, `false` otherwise.

- `boolean assignZoneToParkingLot(String oldLot, String zoneID, String newLot)`: Changes the parking lot association of an existing zone identified by `zoneID` from `oldLot` to `newLot`. Returns `true` if the reassignment is successful, `false` otherwise.

- `boolean createLotAndZone(String address, String lotName, String zoneID)`: Creates a new parking lot with the specified address and lot name, and a new zone with the given zone identifier within this parking lot. Returns `true` if both the lot and zone are successfully created, `false` otherwise.

## PermitsService
The `PermitsService` offers methods for retrieving, entering, updating, and deleting permit information, as well as associating method required for the project.

### Methods

- `Collection<Permit> getPermitInfo(String permitID) throws SQLException`: Retrieves detailed information for a specific permit based on the permit ID. Return an empty collection if there is no permit associated with the `permitID`.

- `void enterPermitInfo(Permit permit) throws SQLException`: Enters new permit information into the database.

- `void updatePermitInfo(String permitID, String permitType, String zoneID, String spaceType, Date startDate, Date expirationDate, Time expirationTime) throws SQLException`: Updates existing permit information identified by the permit ID. `permitID`, `associatedID`, and `carLicenseNum` are not allowed to be updated.

- `void deletePermitInfo(String permitID) throws SQLException`: Deletes permit information from the database based on the permit ID.

- `int getPermitsNumberForDriver(String associatedID) throws SQLException`: Retrieves the number of permits associated with a specific driver, identified by their associated ID.

- `void assignPermitToDriver(String permitID, String permitType, String zoneID, String spaceType, Date startDate, Date expirationDate, Time expirationTime, Driver driver, Vehicle vehicle) throws SQLException`: Assigns a permit to a driver and vehicle with the specified details.

- `int getVehicleNumberofPermit(String permitID) throws SQLException`: Retrieves the number of vehicles associated with a specific permit ID.

- `void removeVehicleFromPermit(String permitID, String carLicenseNum) throws SQLException`: Removes a vehicle from a permit based on the permit ID and vehicle's license number.

- `void addVehicleToPermit(String permitID, String newCarLicenseNum) throws SQLException`: Adds a vehicle to a permit using the vehicle's new license number and the permit ID.

- `void addVehicleToPermitForEmployee(Permit permit, String newCarLicenseNum) throws SQLException`: Adds a new vehicle to a permit associated with an employee, this method will create a new tuple in database

- `Collection<Permit> getPermitPerCarLicense(String carLicenseNum) throws SQLException`: Retrieves a collection of permits associated with a particular vehicle license number.

<br/><br/>

## Menus 

### Menu Class Design
The menu classes in this application adopt the same design pattern as the service classes, defining interfaces and their specific implementations. This strategy separates the operational interfaces from their execution, increasing the system's flexibility and facilitating easier testing.

### User Interface & Home Page
Upon starting the application, users are greeted with a main menu offering several options:

1. Information Processing
2. Maintaining permits and vehicle information for each driver
3. Generating and maintaining citations
4. Reports
5. Exit


### InformationProcessMenu
Users can:

1. Enter, update, or delete information about drivers, parking lots, zones, spaces, vehicles, and permits.
2. Assign zones to a parking lot.
3. Assign types to specific parking spaces.
4. Appeal citations.
5. Return to the previous menu.

### Entity-Specific Menus
Each entity within the parking management system has a dedicated menu for data manipulation:

- **DriverMenu**: Manage driver information, including Create, Update, and Delete.
- **ParkingLotMenu**:Manage parking lot information, including Create, Update, and Delete.
- **ZoneMenu**: Manage zone information, including Create, Update, and Delete.
- **SpaceMenu**: Manage parking space information, including Create, Update, and Delete.
- **VehicleMenu**: Manage vehicle information, including Create, Update, and Delete.
- **PermitMenu**: Manager parking information, including Create, Update, and Delete.

### MaintainPermitsAndVehicleMenu
This menu is for permit and vehicle management, enabling users to:

1. Assign permits to drivers.
2. Enter and update permit information.
3. Remove vehicles from permits.
4. Add vehicles to permits.
5. Manage vehicle inventory within the system.

### CitationsMenu
The CitationsMenu is for handling of citations by providing the means to:

1. Generate new citations.
2. Maintain existing citation records.
3. Retrieve all citations or search by citation number.
4. Delete citations based on their unique number.

### ReportMenu
This menu offers report generation tools to:

1. Compile citation data across various parameters.
2. Report on citation distribution by zone and time range.
3. List zones within each parking lot.
4. Identify vehicles currently in violation.
5. Count employees with permits in a given zone.
6. Retrieve permit details via ID or phone number.
7. Find available spaces by type in a specific parking lot.
