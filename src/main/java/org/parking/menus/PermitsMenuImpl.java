package org.parking.menus;

import org.parking.model.Driver;
import org.parking.model.Permit;
import org.parking.model.Vehicle;
import org.parking.service.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

public class PermitsMenuImpl implements PermitsMenu {

    private final PermitsService permitsService;
    private final DriversService driversService;
    private final VehicleService vehicleService;

    private static final Set<String> VALID_SPACE_TYPES = new HashSet<>(Arrays.asList("electric", "handicap", "compact car", "regular"));

    private static final Set<String> VALID_PERMIT_TYPES = new HashSet<>(Arrays.asList("residential", "commuter", "peak hours", "special event", "park & ride"));

    public PermitsMenuImpl(PermitsService permitsService, DriversService driversService, VehicleService vehicleService) {
        this.permitsService = permitsService;
        this.driversService = driversService;
        this.vehicleService = vehicleService;
    }

    public void menu() {
        int option = 0;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("1. Enter\n2. Update\n3. Delete\n4. Go back\n");
            System.out.println("Please enter your option: ");
            try {
                option = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Your input was not an integer");
                option = 0;
            }
            switch (option) {
                case 1:
                    createPermit(scan);
                    break;
                case 2:
                    updatePermit(scan);
                    break;
                case 3:
                    deletePermit(scan);
                    break;
                case 4:
                    break;
                default:
                    System.out.println(Constants.LogTryInputAgain);
            }
        } while (option != 4);

    }
    /***************************************************************************
     * 1. Create a new permit
     ****************************************************************************/
    public void createPermit(Scanner scan)  {
        try {
            Permit newPermit;
            System.out.println("Please enter new permit information:");
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

            System.out.println("Please enter associated ID:");
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
                // employee can have up to 3 permits, including additional special event and park & ride
                if(permitNum == 3) {
                    System.out.println("The driver has maximum number of permit");
                    return;
                }
                // allow for one more special event or park & ride
                if(permitNum == 2) {
                    reachMax = true;
                    System.out.println(" The driver can only be assigned one more permit for special event or Park & Ride");
                }
                employee = true;
            }
            // Situation student
            else if(driverType.equals("S")) {
                // student can have up to 2 permits, including additional special event and park & ride
                if(permitNum == 2) {
                    System.out.println("The driver has maximum number of permit");
                    return;
                }
                // allow for one more special event or park & ride
                if(permitNum == 1) {
                    reachMax = true;
                    System.out.println(" The driver can only be assigned one more permit for special event or Park & Ride");
                }
                student = true;
            }
            // Situation Visitor
            else {
                // only allowed one permit
                if(permitNum == 1) {
                    System.out.println("The driver has maximum number of permit");
                    return;
                }
                visitor = true;
            }

            System.out.println("Please enter car license number (if any):");
            String carLicenseNum = scan.nextLine().trim();
            Vehicle vehicle;
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
                System.out.println("Invalid space type. Valid types are electric, handicap, compact car and regular");
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
            newPermit = new Permit(permitID, permitType, zoneID, associatedID, carLicenseNum, spaceType, startDate, expirationDate, expirationTime);
            System.out.println(newPermit);
            while (true) {
                System.out.println("Are you sure? (y/n)");
                String input = scan.nextLine().trim().toLowerCase();
                if ("y".equals(input)) {
                    break;
                } else if ("n".equals(input)) {
                    return;
                } else {
                    System.out.println("Invalid input, please enter 'y' or 'n'.");
                }
            }

            permitsService.enterPermitInfo(newPermit);

        } catch (SQLException e) {
            System.out.println(Constants.LogTryAgain);
        }

    }
    /***************************************************************************
     * 2. Update an existing permit
     ****************************************************************************/
    public void updatePermit(Scanner scan) {
        System.out.println("Please enter the permitID to be updated (press Enter to keep the current value): ");
        String permitID = scan.nextLine();
        try {
            // first, need to find if the permit exists or not
            ArrayList<Permit> permits = (ArrayList<Permit>) permitsService.getPermitInfo(permitID);
            if(permits.isEmpty()) {
                System.out.println("No permit found");
                return;
            }
            Permit currentPermit = permits.get(0);
            System.out.println("Please enter permit type [current: " + currentPermit.getPermitType() + " ]:");
            String permitType = scan.nextLine();
            if(permitType.trim().isEmpty()) {
                permitType = currentPermit.getPermitType();
            }
            else {
                while (!VALID_PERMIT_TYPES.contains(permitType.toLowerCase())) {
                    System.out.println("Invalid permit type. Valid types are residential, commuter, peak hours, special event, and Park & Ride");
                    System.out.print("Please enter a valid permit type: ");
                    permitType = scan.nextLine().trim().toLowerCase();
                }
            }
            String associatedID = currentPermit.getAssociatedID();
            Driver driver = driversService.getById(associatedID);
            String driverType = driver.getStatus();

            System.out.print("Please enter zone ID [current: " + currentPermit.getZoneID() + " ]: ");
            String zoneID = scan.nextLine();
            if (zoneID.trim().isEmpty()) {
                zoneID = currentPermit.getZoneID();
            }
            else {
                if ("E".equals(driverType)) {
                    Set<String> validZones = new HashSet<>(Arrays.asList("A", "B", "C", "D"));
                    while (!validZones.contains(zoneID)) {
                        System.out.println("Employees can only be assigned to zones A, B, C, or D.");
                        System.out.print("Please enter a valid zone ID: ");
                        zoneID = scan.nextLine().trim();
                    }
                }
                if ("S".equals(driverType)) {
                    Set<String> validZonesForStudents = new HashSet<>(Arrays.asList("AS", "BS", "CS", "DS"));
                    while (!validZonesForStudents.contains(zoneID)) {
                        System.out.println("Students can only be assigned to zones AS, BS, CS, or DS.");
                        System.out.print("Please enter a valid zone ID: ");
                        zoneID = scan.nextLine().trim();
                    }
                }
                if ("V".equals(driverType)) {
                    while (!"V".equals(zoneID)) {
                        System.out.println("Visitors can only be assigned to zone V.");
                        System.out.print("Please enter a valid zone ID: ");
                        zoneID = scan.nextLine().trim();
                    }
                }

            }

            System.out.print("Please enter space type [current: " + currentPermit.getSpaceType() + " ]: ");
            String spaceType = scan.nextLine();
            if (!spaceType.trim().isEmpty()) {
                spaceType = spaceType.trim().toLowerCase();
                // Check if the new space type is valid
                while (!VALID_SPACE_TYPES.contains(spaceType)) {
                    System.out.println("Invalid space type. Valid types are electric, handicap, compact car, and regular.");
                    System.out.print("Please enter a valid space type [current: " + currentPermit.getSpaceType() + "]: ");
                    spaceType = scan.nextLine().trim().toLowerCase();
                }
            } else {
                spaceType = currentPermit.getSpaceType(); // Retain the current space type if input was empty
            }


            Date startDate = currentPermit.getStartDate();
            boolean validStartDate = false;
            while (!validStartDate) {
                System.out.print("Start Date (yyyy-MM-dd) [current: " + startDate + " ]: ");
                String startDateStr = scan.nextLine();
                if(startDateStr.trim().isEmpty()) {
                    break;
                }
                try {
                    startDate = Date.valueOf(startDateStr);
                    validStartDate = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                }
            }

            Date expirationDate = currentPermit.getExpirationDate();
            boolean validExpirationDate = false;
            while (!validExpirationDate) {
                System.out.print("Expiration Date (yyyy-MM-dd) [current: " + expirationDate + " ]: ");
                String expirationDateStr = scan.nextLine();
                if(expirationDateStr.trim().isEmpty()) {
                    break;
                }
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

            Time expirationTime = currentPermit.getExpirationTime();
            boolean validExpirationTime = false;
            while (!validExpirationTime) {
                System.out.print("Expiration Time (HH:mm:ss) [current " + expirationTime + " ]: ");
                String expirationTimeStr = scan.nextLine();
                if(expirationTimeStr.trim().isEmpty()) {
                    break;
                }
                try {
                    expirationTime = Time.valueOf(expirationTimeStr);
                    validExpirationTime = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid time format. Please use HH:mm:ss.");
                }
            }
            System.out.println("Current permit information:");
            System.out.println("Permit ID: " + permitID);
            System.out.println("Permit Type: " + permitType);
            System.out.println("Zone ID: " + zoneID);
            System.out.println("Space Type: " + spaceType);
            System.out.println("Start Date: " + startDate);
            System.out.println("Expiration Date: " + expirationDate);
            System.out.println("Expiration Time: " + expirationTime);


            while (true) {
                System.out.println("Do you want to save these changes? (y/n)");
                String input = scan.nextLine().trim().toLowerCase();
                if ("y".equals(input)) {
                    permitsService.updatePermitInfo(permitID, permitType, zoneID, spaceType, startDate, expirationDate, expirationTime);
                    System.out.println("Permit updated successfully.");
                    break;
                } else if ("n".equals(input)) {
                    System.out.println("Update canceled.");
                    return;
                } else {
                    System.out.println("Invalid input, please enter 'y' or 'n'.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /***************************************************************************
     * 3. Delete an existing permit
     ****************************************************************************/
    private void deletePermit(Scanner scan) {
        System.out.println("Please enter the permitID to be deleted: ");
        String permitID = scan.nextLine();
        try {
            // first, need to check if the permit exists or not
            ArrayList<Permit> permits = (ArrayList<Permit>) permitsService.getPermitInfo(permitID);
            if(permits.isEmpty()) {
                System.out.println("No permit found");
                return;
            }
            Permit currentPermit = permits.get(0);
            System.out.println("Current permit information:");
            System.out.println("Permit ID: " + currentPermit.getPermitID());
            System.out.println("Permit Type: " + currentPermit.getPermitType());
            System.out.println("Zone ID: " + currentPermit.getZoneID());
            System.out.println("Space Type: " + currentPermit.getSpaceType());
            System.out.println("Start Date: " + currentPermit.getStartDate());
            System.out.println("Expiration Date: " + currentPermit.getExpirationDate());
            System.out.println("Expiration Time: " + currentPermit.getExpirationTime());

            // Ask for confirmation before deletion
            System.out.println("Are you sure you want to delete this permit? (y/n)");
            while (true) {
                String input = scan.nextLine().trim().toLowerCase();
                if ("y".equals(input)) {
                    permitsService.deletePermitInfo(permitID);
                    System.out.println("Permit deleted successfully.");
                    break;
                } else if ("n".equals(input)) {
                    System.out.println("Permit deletion cancelled.");
                    return;
                } else {
                    System.out.println("Invalid input, please enter 'y' or 'n'.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
