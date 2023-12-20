package org.parking.menus;

import org.parking.model.Driver;
import org.parking.model.Permit;
import org.parking.model.Vehicle;
import org.parking.service.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

public class MaintainPermitsAndVehicleMenuImpl implements MaintainPermitsAndVehicleMenu {
    private final DriversService driversService;
    private final VehicleService vehicleService;
    private final PermitsService permitsService;
    private final PermitsMenuImpl permitsMenu;
    private final VehicleMenuImpl vehicleMenu;

    private static final Set<String> VALID_SPACE_TYPES = new HashSet<>(Arrays.asList("electric", "handicap", "compact car", "regular"));
    private static final Set<String> VALID_PERMIT_TYPES = new HashSet<>(Arrays.asList("residential", "commuter", "peak hours", "special event", "park & ride"));


    public MaintainPermitsAndVehicleMenuImpl(DriversService driversService, VehicleService vehicleService, PermitsService permitsService) {
        this.driversService = driversService;
        this.permitsService = permitsService;
        this.vehicleService = vehicleService;
        this.permitsMenu = new PermitsMenuImpl(permitsService, driversService, vehicleService);
        this.vehicleMenu = new VehicleMenuImpl(vehicleService);

    }


    public void callInterface() {
        int option;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("1. Assign a permit to a driver");
            System.out.println("2. Enter permit information");
            System.out.println("3. Update permit information");
            System.out.println("4. Remove a vehicle from a permit");
            System.out.println("5. Add a vehicle to a permit");
            System.out.println("6. Add a vehicle");
            System.out.println("7. Delete a vehicle");
            System.out.println("0. Go back");
            System.out.print("Please enter your option: ");
            try {
                option = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Your input was not an integer");
                option = 9;
            }
            switch (option) {
                case 0:
                    break;
                case 1:
                    assignPermit(scan);
                    break;
                case 2:
                    // This part is the same as the creating permits in permit menu
                    permitsMenu.createPermit(scan);
                    break;
                case 3:
                    // This part is the same as the updating permits in permit menu
                    permitsMenu.updatePermit(scan);
                    break;
                case 4:
                    removeVehicleFromPermit(scan);
                    break;
                case 5:
                    addVehicleToPermit(scan);
                    break;
                case 6:
                    // This part is the same as the creating vehicles in vehicle menu
                    vehicleMenu.enterVehicleUI(scan);
                    break;
                case 7:
                    // This part is the same as the deleting vehicle in vehicle menu
                    vehicleMenu.deleteVehcileUI(scan);
                    break;
                default:
                    System.out.println(Constants.LogTryInputAgain);
            }
        } while(option != 0);
    }
    /***************************************************************************
     * 1. Assign permit to driver
     *  This will create a new permit
     ****************************************************************************/
    private void assignPermit(Scanner scan) {
        try {
            System.out.println("Please the associated ID of the driver: ");
            String associatedID = scan.nextLine();
            Driver driver;
            driver = driversService.getById(associatedID);
            if(driver == null) {
                System.out.println(" No driver found.");
                return;
            }
            String driverType = driver.getStatus();
            int permitNum = permitsService.getPermitsNumberForDriver(associatedID);
            boolean reachMax = false;
            boolean employee = false, student = false, visitor = false;
            // Situation: employee
            if(driverType.equals("E")) {
                // Employees can have up to 3 permits, including special events and park & ride
                if(permitNum == 3) {
                    System.out.println("The driver has maximum number of permit");
                    return;
                }
                // one more of special events and park & ride allowed
                if(permitNum == 2) {
                    reachMax = true;
                    System.out.println(" The driver can only be assigned one more permit for special event or Park & Ride");
                }
                employee = true;
            }
            // Situation student
            else if(driverType.equals("S")) {
                // Employees can have up to 2 permits, including special events and park & ride
                if(permitNum == 2) {
                    System.out.println("The driver has maximum number of permit");
                    return;
                }
                // one more of special events and park & ride allowed
                if(permitNum == 1) {
                    reachMax = true;
                    System.out.println(" The driver can only be assigned one more permit for special event or Park & Ride");
                }
                student = true;
            }
            // Situation Visitor
            else {
                if(permitNum == 1) {
                    System.out.println("The driver has maximum number of permit");
                    return;
                }
                visitor = true;
            }

            System.out.println("Please enter car license number (if any):");
            String carLicenseNum = scan.nextLine().trim();
            Vehicle vehicle = null;
            if(!carLicenseNum.isEmpty()) {
                vehicle = vehicleService.getByLicense(carLicenseNum);
                if(vehicle == null) {
                    System.out.println(" No vehicle found. ");
                    return;
                }
            }
            else {
                carLicenseNum = null;
            }

            System.out.println("Please enter permit ID:");
            String permitID = scan.nextLine();

            while (permitID.trim().isEmpty()) {
                System.out.println("Permit ID cannot be an empty string. Please enter a valid permit ID:");
                permitID = scan.nextLine();
            }

            ArrayList<Permit> existingPermit = (ArrayList<Permit>) permitsService.getPermitInfo(permitID);
            if(!existingPermit.isEmpty()) {
                System.out.println("This permit already exists");
                return;
            }

            System.out.print("Please enter permit type: ");
            String permitType = scan.nextLine().trim();

            // If 'reachMax' is true, validate the 'permitType' to be either "Special event" or "Park & Ride"
            if (reachMax) {
                while (!"special event".equalsIgnoreCase(permitType) && !"park & ride".equalsIgnoreCase(permitType)) {
                    System.out.println("Permit type can only be 'Special event' or 'Park & Ride'.");
                    System.out.print("Please enter a valid permit type: ");
                    permitType = scan.nextLine().trim();
                }
            }
            else {
                while (!VALID_PERMIT_TYPES.contains(permitType.toLowerCase())) {
                    System.out.println("Invalid permit type. Valid types are residential, commuter, peak hours, special event, and Park & Ride");
                    System.out.print("Please enter a valid permit type: ");
                    permitType = scan.nextLine().trim().toLowerCase();
                }
            }

            System.out.println("Please enter zone ID:");
            String zoneID = scan.nextLine().trim();
            if (employee) {
                Set<String> validZones = new HashSet<>(Arrays.asList("A", "B", "C", "D"));
                while (!validZones.contains(zoneID)) {
                    System.out.println("Employees can only be assigned to zones A, B, C, or D.");
                    System.out.print("Please enter a valid zone ID: ");
                    zoneID = scan.nextLine().trim();
                }
            }
            if (student) {
                Set<String> validZonesForStudents = new HashSet<>(Arrays.asList("AS", "BS", "CS", "DS"));
                while (!validZonesForStudents.contains(zoneID)) {
                    System.out.println("Students can only be assigned to zones AS, BS, CS, or DS.");
                    System.out.print("Please enter a valid zone ID: ");
                    zoneID = scan.nextLine().trim();
                }
            }
            if (visitor) {
                while (!"V".equals(zoneID)) {
                    System.out.println("Visitors can only be assigned to zone V.");
                    System.out.print("Please enter a valid zone ID: ");
                    zoneID = scan.nextLine().trim();
                }
            }

            System.out.println("Please enter space type (electric, handicap, compact car, regular):");
            String spaceType = scan.nextLine().trim().toLowerCase();

            while (!VALID_SPACE_TYPES.contains(spaceType)) {
                System.out.println("Invalid space type. Valid types are electric, handicap, compact car, and regular");
                System.out.print("Please enter a valid space type: ");
                spaceType = scan.nextLine().trim().toLowerCase();
            }

            Date startDate = null;
            boolean validStartDate = false;
            while (!validStartDate) {
                System.out.print("Start Date (yyyy-MM-dd): ");
                String startDateStr = scan.nextLine();
                try {
                    startDate = Date.valueOf(startDateStr);
                    validStartDate = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                }
            }

            Date expirationDate = null;
            boolean validExpirationDate = false;
            while (!validExpirationDate) {
                System.out.print("Expiration Date (yyyy-MM-dd): ");
                String expirationDateStr = scan.nextLine();
                try {
                    expirationDate = Date.valueOf(expirationDateStr);
                    validExpirationDate = true;
                    if(!expirationDate.after(startDate)) {
                        System.out.println("The expiration date is not after the start date");
                        validExpirationDate = false;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                }
            }

            Time expirationTime = null;
            boolean validExpirationTime = false;
            while (!validExpirationTime) {
                System.out.print("Expiration Time (HH:mm:ss): ");
                String expirationTimeStr = scan.nextLine();
                try {
                    expirationTime = Time.valueOf(expirationTimeStr);
                    validExpirationTime = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid time format. Please use HH:mm:ss.");
                }
            }
            while (true) {
                System.out.println("Are you sure? (y/n)");
                String input = scan.nextLine().trim().toLowerCase();
                if ("y".equals(input)) {
                    break;
                } else if ("n".equals(input)) {
                    System.out.println("Cancelling...");
                    return;
                } else {
                    System.out.println("Invalid input, please enter 'y' or 'n'.");
                }
            }
            permitsService.assignPermitToDriver(permitID, permitType, zoneID, spaceType,
                    startDate, expirationDate, expirationTime, driver, vehicle);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /***************************************************************************
     * 4. Remove Vehicle from permit
     *  This function does not delete permit, instead, it set carLicenseNum to null
     ****************************************************************************/
    private void removeVehicleFromPermit(Scanner scan) {
        try {
            System.out.println("Please enter the permitID: ");
            String permitID = scan.nextLine();
            ArrayList<Permit> permits = (ArrayList<Permit>) permitsService.getPermitInfo(permitID);
            if(permits.isEmpty()) {
                System.out.println("No permit found.");
                return;
            }

            System.out.println("Please enter the vehicle license: ");
            String carLicenseNum = scan.nextLine();

            permitsService.removeVehicleFromPermit(permitID, carLicenseNum);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /***************************************************************************
     * 5. add Vehicle to permit
     *  First, it will check the number of vehicle under the permit
     *  Employee can have up to 2 vehicles under the same permit
     *  Student and Visitor can have only one vehicle under the same permit
     ****************************************************************************/
    private void addVehicleToPermit(Scanner scan) {
        try {
            System.out.println("Please enter the permitID: ");
            String permitID = scan.nextLine();
            ArrayList<Permit> permits = (ArrayList<Permit>) permitsService.getPermitInfo(permitID);
            if(permits.isEmpty()) {
                System.out.println("No permit found.");
                return;
            }

            System.out.println("Please enter the vehicle license: ");
            String carLicenseNum = scan.nextLine();
            Vehicle vehicle = vehicleService.getByLicense(carLicenseNum);
            if(vehicle == null) {
                System.out.println("No vehicle found.");
                return;
            }

            String assoicatedID = permits.get(0).getAssociatedID();

            Driver driver = driversService.getById(assoicatedID);

            if(driver == null) {
                System.out.println("No driver associated with this permit. ");
                return;
            }

            String type = driver.getStatus();
            int vehicleCount = permitsService.getVehicleNumberofPermit(permitID);
            // situation: Employee
            if(type.equals("E")) {
                if (vehicleCount == 2) {
                    System.out.println("The permit has two vehicles registered. No more vehicle allowed");
                    return;
                }
                // situation: one permit, one vehicle, can add a new permit tuple
                if(vehicleCount == 1 && permits.size() == 1) {
                    permitsService.addVehicleToPermitForEmployee(permits.get(0), carLicenseNum);
                }
                // where vehicleCount is 0, permits number is 1 or 2. Or vehicle count is 1, permits number is 2
                // the vehicle will be added to a permit with carLicenseNum of null
                if(vehicleCount < permits.size()) {
                    permitsService.addVehicleToPermit(permitID, carLicenseNum);
                }
            }
            // situation: Student
            if(type.equals("S") || type.equals("V")) {
                if(vehicleCount == 1) {
                    System.out.println("The permit has one vehicle registered. No more vehicle allowed");
                    return;
                }
                if(vehicleCount < permits.size()) {
                    permitsService.addVehicleToPermit(permitID, carLicenseNum);
                }
            }
            System.out.println("Vehicle with license of " + carLicenseNum + " has been added to permit " + permitID);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
