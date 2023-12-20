package org.parking.menus;

import java.sql.*;
import java.util.Collection;
import java.util.Scanner;

import org.parking.model.*;
import org.parking.service.*;

public class ReportsMenuImpl implements ReportsMenu {

    private static ReportsService reportsService;
    private static CitationsService citationsService;
    private static ParkingLotService parkingLotService;
    private static ZoneService zoneService;

    public ReportsMenuImpl(ReportsService reportsService, CitationsService citationsService, ParkingLotService parkingLotService, ZoneService zoneService) {
        this.citationsService = citationsService;
        this.reportsService = reportsService;
        this.parkingLotService = parkingLotService;
        this.zoneService = zoneService;
    }

    static String[] menuOptions =
            {
                    "Generate Report for all Citations",
                    "Generate a report for the total number of Citations given in all Zones in the lot for a given time range",
                    "Return the list of zones for each lot as tuple pairs (lot, zone).",
                    "Return the number of Vehicles that are currently in violation.",
                    "Return the number of employees having permits for a given parking zone.",
                    "Return permit information given an ID or phone number.",
                    "Return an available space number given a space type in a given parking lot.",

            };

    public void callInterface(){
        Scanner scan = new Scanner(System.in);
        int input = 0;

        while (true)
        {
            int optionNum = 1;
            System.out.println("Select an option:");

            for (String option : menuOptions)
            {
                System.out.println(optionNum + ": " + option);
                ++optionNum;
            }
            System.out.println("0: Return to the main menu");

            System.out.print("\nPlease enter your option: ");

            input = scan.nextInt();
            scan.nextLine();

            switch (input)
            {
                // Generate Report for all Citations
                case 1:
                    citationListMenu();
                    break;

                // Generate Report for citations in a specific lot between two dates
                case 2:
                    citationReportMenu(scan);
                    break;

                // Return the list of zones for each lot as tuple pairs (lot, zone).
                case 3:
                    zoneListMenu();
                    break;

                // Return the number of Vehicles that are currently in violation.
                case 4:
                    countVehiclesViolationMenu();
                    break;

                // Return the number of employees having permits for a given parking zone.
                case 5:
                    countEmployeePermitsPerZoneMenu(scan);
                    break;

                // Return permit information given an ID or phone number.
                case 6:
                    permitReportMenu(scan);
                    break;

                // Return an available space number given a space type in a given parking lot.
                case 7:
                    reportAvailableSpaceInLotMenu(scan);
                    break;

                // Return to the main menu
                case 0:
                    System.out.println("Returning to main menu");
                    return;

                // Other value input
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /***************************************************************************
     * User interface for generating a citation report (all information about
     * each citation).
     ****************************************************************************/
    private void citationListMenu (){
        Collection<Citation> citations = null;

        try {
            citations = citationsService.getAll();

            if (citations == null){
                System.out.println("No Citations Found.");
                return;
            }
        }
        catch (SQLException e){
            System.out.println("Error fetching citations");
            System.out.print(e.getMessage());
            return;
        }

        System.out.printf("------------------------------------------------------------------------------------------%n");
        System.out.printf("                                Citation Information                                      %n");
        System.out.printf("------------------------------------------------------------------------------------------%n");
        // Print column headers
        System.out.printf("%-5s %-12s %-12s %-12s %-25s %-15s %-6s %-12s%n",
                "NUM.", "DATE", "TIME", "STATUS", "LOT", "CATEGORY", "FEE", "LICENSE");

        for (Citation citation : citations){
            System.out.printf("%-5s %-12s %-12s %-12s %-25s %-15s %-6s %-12s%n",
                    citation.getNumber(),
                    citation.getCitationDate(),
                    citation.getCitationTime(),
                    citation.getPaymentStatus(),
                    citation.getLotName(),
                    citation.getCategory(),
                    citation.getFee(),
                    citation.getVehicle().getLicense());
        }
        System.out.println();

    }

    /***************************************************************************
     * User interface for generating a citation report (# of citations within a
     * specific window for a parking lot).
     ****************************************************************************/
    private void citationReportMenu (Scanner scan){
        // Get the parking lot from the user
        ParkingLot parkingLot = null;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter the parking lot name: ");
            String lotName = scan.nextLine();
            //try{
                parkingLot = parkingLotService.getParkingLot(lotName);
                if (parkingLot == null) {
                    System.out.println("No Parking Lot Found with that Name.");
                }
                else {
                    validInput = true;
                }
//            }
//            catch (SQLException e) {
//               // Output SQL Exception
//               System.out.println("SQL Error: " + e.getMessage());
//            }
        }

        // Get the start date for the citation window
        Date startDate = null;
        validInput = false;
        while (!validInput) {
            System.out.print("Start Date (yyyy-MM-dd): ");
            String startDateStr = scan.nextLine();
            try {
                startDate = Date.valueOf(startDateStr);
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        // Get the end date for the citation window. Date must be after start date.
        Date endDate = null;
        validInput = false;
        while (!validInput) {
            System.out.print("Expiration Date (yyyy-MM-dd): ");
            String endDateStr = scan.nextLine();
            try {
                endDate = Date.valueOf(endDateStr);
                validInput = true;
                if(!endDate.after(startDate)) {
                    System.out.println("The expiration date is not after the start date");
                    validInput = false;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        // Generate Report
        try{
            // Initialize the count to 0
            int citationCount = 0;

            // Generate the report
            citationCount = reportsService.generateCitationReport(parkingLot.getName(), startDate, endDate);

            // Print out the count and report information formatted for readability
            System.out.printf("--------------------------------------------------------%n");
            System.out.printf(" Citation Count - "+ parkingLot.getName() +" %n");
            System.out.printf("--------------------------------------------------------%n");
            System.out.printf("%-5s : %-5s %n", "# of Citation(s)", citationCount);
            System.out.println();

        } catch (SQLException e) {
            // Output SQL Exception
            System.out.println("SQL Error: " + e.getMessage());
        }

    }

    /***************************************************************************
     * Return the list of zones for each lot as tuple pairs (lot, zone).
     ****************************************************************************/
    private void zoneListMenu() {
        // TODO Verify if the Zone in the tuple should have all of the zones or just 1.
        // Generate list of zones from each parking lot
        Collection<Zone> zones = null;

        try{
            // Get data from report org.parking.service
            zones = reportsService.generateZoneReport();

            // Print headers for report
            System.out.printf("%n--------------------------------------------------------%n");
            System.out.printf(" Parking Lot Zones%n");
            System.out.printf("--------------------------------------------------------%n");
            if(!zones.isEmpty()) {
                System.out.printf("%-25s %-5s%n", "ParkingLot", "Zone");
            }
            else System.out.println("No Zones Found.");

            // Format report and print to console
            for(Zone zone : zones) {
                System.out.printf("%-25s %-5s%n", zone.getLotName(), zone.getId());
            }

            System.out.println();

        } catch (SQLException e) {
            // Handle SQL Error Message
            System.out.println("SQL Error: " + e.getMessage());
        }

    }

    /***************************************************************************
     * Return the number of Vehicles that are currently in violation.
     ****************************************************************************/
    private void countVehiclesViolationMenu() {
        // Generate Report
        try{
            // Initialize the count to 0
            int violationCount = 0;

            // Generate the report
            violationCount = reportsService.generateViolatedCarNumber();

            // Print out the count and report information formatted for readability
            System.out.printf("--------------------------------------------------------%n");
            System.out.printf("              Current Cars in Violation                 %n");
            System.out.printf("--------------------------------------------------------%n");
            System.out.printf("%-5s : %-5s %n", "# of Violation(s)", violationCount);
            System.out.println();

        } catch (SQLException e) {
            // Output SQL Exception
            System.out.println("SQL Error: " + e.getMessage());
        }

    }

    /***************************************************************************
     * Return the number of employees having permits for a given parking zone.
     ****************************************************************************/
    private void countEmployeePermitsPerZoneMenu(Scanner scan) {
        // Get parking zone id from user
        boolean validInput = false;
        String zoneId = null;
        String pattern = "(?i)[ABCDV][S]?";

        while (!validInput) {
            System.out.print("Enter the zone id: ");
            zoneId = scan.nextLine();

            // Verify if Zone is an allowed type
            if(!zoneId.matches(pattern)){
                System.out.println("Please enter a valid zone: A/B/C/D/V/AS/BS/CS/DS");
                continue;
            }

            // Verify if the zone exists
            Collection<Zone> zones = zoneService.getZonesById(zoneId);
            if (zones.isEmpty()) {
                System.out.println("No Zone Found with that Id.");
            } else {
                validInput = true;
            }
        }

        // Generate Report
        try{
            // Initialize the count to 0
            int employeeCount = 0;

            // Generate the report
            employeeCount = reportsService.generateExployeesofZone(zoneId);

            // Print out the count and report information formatted for readability
            System.out.printf("--------------------------------------------------------%n");
            System.out.printf(" Employee Count - Zone "+ zoneId.toUpperCase() +" %n");
            System.out.printf("--------------------------------------------------------%n");
            System.out.printf("%-5s : %-5s %n", "# of Employee(s)", employeeCount);
            System.out.println();

        } catch (SQLException e) {
            // Output SQL Exception
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    /***************************************************************************
     * Report the permit information given a driver's ID or phone number.
     ****************************************************************************/
    private void permitReportMenu (Scanner scan) {
        // Get the phone number/Univ ID from the user
        boolean validInput = false;
        String id = null;

        // Allows alphanumeric strings, but the length must be
        // between 1 and 255 characters.
        String pattern = "^[A-Za-z0-9]{1,255}$";

        while (!validInput) {
            System.out.print("Enter Driver's ID: ");

            id = scan.nextLine();
            if(id.matches(pattern)) validInput=true;
            else System.out.println("Please enter the driver's university ID or visitor's phone number. Only include " +
                        "alphanumeric characters");
        }

        // Generate Report
        try{
            // Initialize the count to 0
            Collection<Permit> permits = null;

            // Generate the report
            permits = reportsService.getPermitForDriver(id);

            // Print out the count and report information formatted for readability
            System.out.printf("----------------------------------------------------------------------------------------------------------------%n");
            System.out.printf(" Permits for Driver ID:  "+ id +" %n");
            System.out.printf("----------------------------------------------------------------------------------------------------------------%n");
            if(permits.isEmpty()) System.out.printf(" No Permits Found %n");
            else System.out.printf("%-7s %-12s %-5s %-15s %-15s %-15s %-15s %-15s%n",
                    "ID", "TYPE", "ZONE", "CAR LICENSE #", "SPACE TYPE", "START DATE", "EXP. DATE", "EXP. TIME");

            // Print out the information for each permit
            for(Permit permit : permits){
                System.out.printf("%-7s %-12s %-5s %-15s %-15s %-15s %-15s %-15s%n",
                        permit.getPermitID(),
                        permit.getPermitType(),
                        permit.getZoneID(),
                        permit.getCarLicenseNum(),
                        permit.getSpaceType(),
                        permit.getStartDate(),
                        permit.getExpirationDate(),
                        permit.getExpirationTime());
            }
            System.out.println();

        } catch (SQLException e) {
            // Output SQL Exception
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    /***************************************************************************
     * Return an available space number given a space type in a given parking lot.
     ****************************************************************************/
    private void reportAvailableSpaceInLotMenu(Scanner scan) {
        // Get the parking lot name from user
        ParkingLot parkingLot = null;
        boolean validInput = false;
        while (!validInput) {

            System.out.print("Enter the parking lot name: ");
            String lotName = scan.nextLine();

            parkingLot = parkingLotService.getParkingLot(lotName);
            if (parkingLot == null) {
                System.out.println("No Parking Lot Found with that Name.");
            }
            else {
                validInput = true;
            }
        }

        // Get the space type from user
        String spaceType = null;
        validInput = false;
        String pattern = "(?i)(electric|handicap|compact\\s*car|regular)";

        while (!validInput) {
            System.out.print("Enter Space Type: ");

            spaceType = scan.nextLine();
            if(spaceType.matches(pattern)) validInput=true;
            else System.out.println("Please enter the space type (electric, handicap, compact car, regular).");
        }

        // Find an available space number
        try{
            int spaceNumber =reportsService.getAvailableSpaceinParkingLot(parkingLot.getName(), spaceType);

            // Print out the count and report information formatted for readability
            System.out.printf("--------------------------------------------------------%n");
            System.out.printf(" Available "+ spaceType.toUpperCase() + " Space in "+ parkingLot.getName().toUpperCase() +" %n");
            System.out.printf("--------------------------------------------------------%n");

            if(spaceNumber != 0) System.out.printf("%-5s : %-5s %n", "Space #", spaceNumber);
            else System.out.println("No Spaces Available");

            System.out.println();
        } catch (SQLException e) {
            // Output SQL Exception
            System.out.println("SQL Error: " + e.getMessage());
        }

        // Print results to console

    }
}
