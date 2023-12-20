package org.parking.menus;

import org.parking.service.CitationsService;
import org.parking.service.Constants;
import org.parking.service.SpaceService;
import org.parking.service.ZoneService;

import java.util.Scanner;

/**
 * Implements the information processing menu for user interaction.
 */
public class InformationProcessMenuImpl implements InformationProcessMenu {

    /**
     * Holds an instance of the Create/Update/Delete menu.
     */
    private final CUDMenu CUDMenuImpl;
    /**
     * Allows for database operations for citations.
     */
    private final CitationsService citationsService;
    /**
     * Allows for database operations for spaces.
     */
    private final SpaceService spaceService;
    /**
     * Allows for database operations for zones.
     */
    private final ZoneService zoneService;

    /**
     * Constructor for the information processing menu implementation. Once created,
     * the user can call on the interface to perform additional operations.
     *
     * @param cudMenu          An instance of the create/update/delete menu.
     * @param citationsService The citations org.parking.service that allows for database
     *                         operations.
     * @param spaceService     The space org.parking.service that allows for database
     *                         operations.
     * @param zoneService      The zone org.parking.service that allows for database operations.
     */
    public InformationProcessMenuImpl(CUDMenu cudMenu, CitationsService citationsService, SpaceService spaceService,
            ZoneService zoneService) {
        this.CUDMenuImpl = cudMenu;
        this.citationsService = citationsService;
        this.spaceService = spaceService;
        this.zoneService = zoneService;
    }

    @Override
    public void callInterface() {
        Scanner scan = new Scanner(System.in);
        String input;
        int option = 0;
        do {
            System.out.println(
                    "1. Enter/update/delete basic information about drivers, parking lots, zones, spaces, vehicle, and permits");
            System.out.println("2. Assign zones to a parking lot");
            System.out.println("3. Assign a type to a given space");
            System.out.println("4. Appeal a citation");
            System.out.println("5. Go back");
            System.out.println("Please enter your option: ");

            input = scan.nextLine().trim();
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Your input was not an integer");
                option = 0;
            }
            switch (option) {
                case 1:
                    CUDMenuImpl.callInterface();
                    break;
                case 2:
                    // Assign zones to a parking lot
                    assignZoneToParkingLot(scan);
                    break;
                case 3:
                    // Assign a type to a given space
                    assignTypeToSpace(scan);
                    break;
                case 4:
                    // Appeal a citation
                    appealCitation(scan);
                    break;
                case 5:
                    break;
                default:
                    System.out.println(Constants.LogTryInputAgain);
            }
        } while (option != 5);

    }

    /**
     * Allows the user to assign a zone to parking lot.
     * The user will be prompted for the zone's lot name and zone ID
     * before being asked what lot they would like to assign that zone to
     * 
     * @param scan Scanner that reads input.
     */
    private void assignZoneToParkingLot(Scanner scan) {
        String zone, oldLot, newLot;

        // Get the zone's current parking lot
        System.out.println("What parking lot is the zone currently in?");
        oldLot = scan.nextLine().trim();
        if (oldLot.isEmpty()) {
            System.out.println("No value was entered. Returning to menu.");
            return;
        }

        // Get the current zone
        System.out.println("What zone do you want to update?");
        zone = scan.nextLine().trim();
        if (zone.isEmpty()) {
            System.out.println("No value was entered. Returning to menu.");
            return;
        }

        // Get the parking lot that the user wants to assign the zone to
        System.out.println("What parking lot do you want to assign this zone to?");
        newLot = scan.nextLine().trim();
        if (newLot.isEmpty()) {
            System.out.println("No value was entered. Returning to menu.");
            return;
        }

        // Attempt to assign the zone to the new parking lot.
        // UPDATE Zones SET lotName=? WHERE id=? AND lotName=?
        if (zoneService.assignZoneToParkingLot(oldLot, zone, newLot)) {
            // At least one row was updated.
            System.out.println("Zone " + zone + " has been assigned to " + newLot);
        } else {
            // No rows were updated. This could be due to:
            // - Either parking lot does not exist
            // - The zone does not exist
            // - That zone already exists in the dest lot
            System.out.println("No zones were updated.");
        }

    }

    /**
     * Assign a type to a space.
     * The user will be prompted for the space's lot name, zone ID and number.
     * If any of the inputs are blank, the program will return to the menu.
     * If the user inputs an invalid number, the program will return to the menu.
     * 
     * @param scan Scanner that reads input.
     */
    private void assignTypeToSpace(Scanner scan) {
        String lotName, zoneID, input, type;
        int number, typeInt;

        // Get the lot name of the space
        System.out.println("What parking lot is the space in?");
        lotName = scan.nextLine().trim();
        if (lotName.isEmpty()) {
            System.out.println("No value was entered. Returning to menu.");
            return;
        }

        // Get the zone ID of the space
        System.out.println("What zone is the space in?");
        zoneID = scan.nextLine().trim();
        if (zoneID.isEmpty()) {
            System.out.println("No value was entered. Returning to menu.");
            return;
        }

        // Get the space's number
        System.out.println("What is the space number?");
        input = scan.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("No value was entered. Returning to menu.");
            return;
        } else {
            // The user did insert a value, let's make sure that it is a number.
            try {
                number = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // If the input was not number, an error will be thrown. Catch it and print a
                // message
                System.out.println("Please enter a number. Returning to menu.");
                return;
            }
            // If the number is less than 1, it is not a valid number. We cannot have a
            // negative or 0 space.
            if (number < 1) {
                System.out.println("Please enter a positive, non-zero number. Returning to menu.");
                return;
            }
        }
        // Ask the user what type they want the space to be
        System.out.println("What type do you want the space to be?");
        System.out.println("Please select the type of Space (not selecting a choice will default to regular):");
        System.out.println("1. electric");
        System.out.println("2. handicap");
        System.out.println("3. compact car");
        System.out.println("4. regular");
        input = scan.nextLine().trim();

        // If they do not select a choice, set the type to regular.
        if (input.isEmpty()) {
            type = "regular";
        } else {
            try {
                // The user gave a value. Check to see if it is a number.
                typeInt = Integer.parseInt(input);
                switch (typeInt) {
                    case 1:
                        type = "electric";
                        break;
                    case 2:
                        type = "handicap";
                        break;
                    case 3:
                        type = "compact car";
                        break;
                    case 4:
                        type = "regular";
                        break;
                    default:
                        // The user input a number, but it wasn't one of the options. Default the type
                        // to regular
                        System.out.println("Invalid number. Defaulting to Regular.");
                        type = "regular";
                }
            } catch (Exception e) {
                // There was an error processing the input. (i.e. it was not a number)
                System.out.println("Unable to parse input. Returning to menu. Please try again with a number.");
                return;
            }
        }

        // Send the values to the spaceService and submit the query.
        // UPDATE Spaces SET type=? WHERE lotName=? AND zoneID=? AND number=?
        if (spaceService.assignTypeOfASpace(lotName, zoneID, number, type)) {
            System.out.println("The space's type was successfully updated.");
        } else {
            System.out.println("No space(s) were updated.");
        }

    }

    /**
     * Sets the status of a citation to APPEALED.
     * Prompts the user for the citation's number.
     * If the user does not enter a positive, non-zero number
     * then the user will be returned to the menu.
     * 
     * @param scan Scanner that reads input.
     */
    private void appealCitation(Scanner scan) {
        String input;
        int citationNumber;
        System.out.println("What citation would you like to appeal? Please enter the Citation Number.");
        input = scan.nextLine().trim();
        try {
            citationNumber = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number. Returning to menu.");
            return;
        }
        if (citationNumber < 1) {
            System.out.println("Please enter a positive, non-zero number");
            return;
        }

        System.out.println("Are you sure you want to appeal this citation? (y/n)");
        input = scan.nextLine().trim().toLowerCase();
        if (input.equals("y")) {
            if (citationsService.appealCitation(citationNumber)) {
                System.out.println("The citation was appealed.");
            } else {
                System.out.println("There was no citation to appeal.");
            }
        } else {
            System.out.println("Aborting the appeal.");
        }
    }
}
