package org.parking.menus;

import java.util.Scanner;

import org.parking.model.Zone;
import org.parking.service.ParkingLotService;
import org.parking.service.ZoneService;

/**
 * Provides a user interface for performing operations that affect the Zone
 * table.
 */
public class ZoneMenuImpl implements ZoneMenu {
    /**
     * Service that holds parking lot database operations.
     */
    private final ParkingLotService parkingLotService;
    /**
     * Service that holds zone database operations.
     */
    private final ZoneService zoneService;

    /**
     * Array of the user options for interacting with Zones.
     */
    public String[] options = {
            "Enter Zone information", "Update Zone Information", "Delete Zone information"
    };

    /**
     * Constructor for the Zone Menu Implementation. Allows the user to access a UI
     * for Zone
     * create, update and delete functions.
     * 
     * @param zoneService       The zone org.parking.service that holds the database operations
     *                          for zones.
     * @param parkingLotService Service that holds the database operations for
     *                          parking lots.
     */
    public ZoneMenuImpl(ZoneService zoneService, ParkingLotService parkingLotService) {
        this.zoneService = zoneService;
        this.parkingLotService = parkingLotService;
    }

    /**
     * This function will serve as the menu for the Zone interactions.
     */
    @Override
    public void callInterface() {
        Scanner scan = new Scanner(System.in);
        String input;

        // Loop until the user returns to the main menu
        while (true) {
            System.out.println("Select an option:");

            // Option number will increase with the loop.
            // Format is: #: option
            int optionNum = 1;
            for (String option : options) {
                System.out.println(optionNum + ": " + option);
                ++optionNum;
            }
            // Give the user a way to return to the main menu
            System.out.println("0: Return to the main menu");
            input = scan.nextLine().trim();

            switch (input) {
                // Enter Zone information
                case "1":
                    enterZone(scan);
                    break;
                // Update Zone Info
                case "2":
                    updateZone(scan);
                    break;
                // Delete Zone information
                case "3":
                    deleteZone(scan);
                    break;
                // Return to the main menu
                case "0":
                    System.out.println("Returning to main menu");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Allows the user to enter a Zone into the database.
     * 
     * @param scan Scanner that reads input.
     */
    public void enterZone(Scanner scan) {
        String id, address = null, lotName, input;

        System.out.println("What parking lot do you want to add a zone to?");
        lotName = scan.nextLine().trim();
        if (lotName.isEmpty()) {
            System.out.println("The lot name was empty. Returning to menu.");
            return;
        }

        // Check to see if a parking lot exists with that lot name
        if (parkingLotService.getParkingLot(lotName) == null) {
            // If there is a not a parking lot with that name, then ask if the user would
            // like to create one.
            System.out.println("That parking lot does not exist.");
            System.out.println("Would you like to create a parking lot with the name: " + lotName + "? (y/n)");
            input = scan.nextLine().trim().toLowerCase();
            switch (input) {
                case "y":
                    System.out.println("Please enter the address for the new parking lot:");
                    address = scan.nextLine().trim();
                    if (address.isEmpty()) {
                        System.out.println("The address was blank. Returning to menu.");
                        return;
                    }
                    break;
                case "n":
                    System.out.println("Returning to menu.");
                    return;
                default:
                    System.out.println("Invalid option. Returning to menu.");
                    return;
            }
        }

        // Get the ID for the new zone
        System.out.println("What is the id of the Zone that you want to add?");
        id = scan.nextLine().trim().toUpperCase();
        if (!isValidID(id)) {
            System.out.println("Invalid format. The only allowed values are: A,B,C,D,AS,BS,CS,DS,V");
            System.out.println("Returning to menu.");
            return;
        }

        // If the user opted to create a parking lot, then address will not be null.
        if (address != null) {
            if (!zoneService.createLotAndZone(address, lotName, id)) {
                System.out.println("Unable to create the new parking lot and zone.");
            }
            return;
        } else {
            Zone zone = new Zone(id, lotName);

            if (zoneService.createZone(zone)) {
                System.out.println("Zone was successfully created. Returning to Zone menu.");
            } else {
                System.out.println("There was an error creating the Zone. Please try again.");
            }
            return;
        }
    }

    /**
     * Allows the user to update a zone from the database.
     * 
     * @param scan Scanner that reads input.
     */
    public void updateZone(Scanner scan) {
        String id, input, lotName;
        Zone origZone, newZone;

        System.out.println("Which parking lot is the zone in?");
        lotName = scan.nextLine().trim();
        if (!isValidLotName(lotName)) {
            System.out.println("The lot name was empty. Returning to menu.");
            return;
        }

        if (parkingLotService.getParkingLot(lotName) == null) {
            System.out.println("That parking lot does not exist. Returning to the Zone menu.");
            return;
        }

        System.out.println("What is the id of the Zone that you want to update?");
        id = scan.nextLine().trim().toUpperCase();
        if (!isValidID(id)) {
            System.out.println("Invalid format. The only allowed values are: A,B,C,D,AS,BS,CS,DS,V");
            System.out.println("Returning to menu.");
            return;
        }
        origZone = zoneService.getZone(id, lotName);

        if (origZone != null) {
            newZone = new Zone(origZone.getId(), origZone.getLotName());
            while (true) {
                System.out.println("What attibute do you want to update?");
                System.out.printf("1. ID [current ID]: %s\n", newZone.getId());
                System.out.printf("2. Lot Name [current Lot Name]: %s\n", newZone.getLotName());
                System.out.println("C. Cancel");
                System.out.println("S. Submit changes");

                input = scan.nextLine().trim().toLowerCase();

                switch (input) {
                    // Name
                    case "1":
                        System.out.println("What do you want to change the name to?");
                        id = scan.nextLine().trim().toUpperCase();
                        if (isValidID(id))
                            newZone.setId(id);
                        else {
                            System.out.println("Invalid format. The only allowed values are: A,B,C,D,AS,BS,CS,DS,V");
                            System.out.println("Please try again.");
                        }
                        break;
                    // Address
                    case "2":
                        System.out.println("What do you want to change the address to?");
                        lotName = scan.nextLine().trim();
                        if (isValidLotName(lotName))
                            newZone.setLotName(lotName);
                        else
                            System.out.println("Invalid lot name. Please try again");
                        break;
                    // Cancel
                    case "c":
                        System.out.println("No changes were submitted. Returning to Zone menu.");
                        return;
                    // Submit changes
                    case "s":
                        if (zoneService.updateZone(origZone, newZone)) {
                            System.out.println("The Zone was successfully updated.");
                        } else {
                            System.out.println("There was an issue updating the Zone.");
                        }
                        return;

                    default:
                        System.out.println("Invalid option, please try again.");
                        break;
                }
            }
        } else {
            System.out.println("Unable to find a zone with that lot name and ID. Returning to menu.");
        }
    }

    /**
     * Allows the user to delete a zone from the database.
     * 
     * @param scan Scanner that reads input.
     */
    public void deleteZone(Scanner scan) {
        String input;
        String id, lotName;
        Zone zone;

        System.out.println("What parking lot do you want to remove a zone from?");
        lotName = scan.nextLine().trim();
        if (!isValidLotName(lotName)) {
            System.out.println("The lot name was empty. Returning to menu.");
            return;
        }

        if (parkingLotService.getParkingLot(lotName) == null) {
            System.out.println("That parking lot does not exist. Returning to the Zone menu.");
            return;
        }

        System.out.println("What is the name of the Zone that you want to delete?");
        id = scan.nextLine().trim().toUpperCase();
        if (!isValidID(id)) {
            System.out.println("Invalid format. The only allowed values are: A,B,C,D,AS,BS,CS,DS,V");
            System.out.println("Returning to menu.");
            return;
        }
        zone = zoneService.getZone(id, lotName);

        if (zone != null) {
            while (true) {
                System.out.println("Are you sure you want to delete:\n" + id + " from " + lotName + " (y/n)?");
                input = scan.nextLine().toLowerCase().trim();

                if (input.equals("y")) {
                    System.out.println("Deleting " + id + " from " + lotName + ".");
                    zoneService.deleteZone(zone);
                    break;
                } else if (input.equals("n")) {
                    System.out.println("Returning to Zone menu.");
                    break;
                } else {
                    System.out.println("Invalid option. Please insert \"y\" or \"n\"");
                }
            }
        } else {
            System.out.println("There is no zone with that ID and lot name.");
        }
    }

    /**
     * Checks to see if an ID is valid.
     * 
     * @param id The id to check.
     * @return True if the id is valid, false if not.
     */
    private boolean isValidID(String id) {
        return id.length() <= 2 && id.matches("^([A-Da-dVv][Ss]*)$");
    }

    /**
     * Checks to see if the lot name is valid (not empty).
     * 
     * @param lotName The lot name to check.
     * @return True if the lot name is not empty, false if it is empty.
     */
    private boolean isValidLotName(String lotName) {
        return !lotName.isEmpty();
    }
}
