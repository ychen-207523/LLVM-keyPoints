package org.parking.menus;

import java.util.Scanner;

import org.parking.model.Space;
import org.parking.service.SpaceService;

/**
 * Implements the menu for handling interactions with Spaces.
 */
public class SpaceMenuImpl implements SpaceMenu {

    /**
     * The org.parking.service that supports database operations for Spaces.
     */
    private final SpaceService spaceService;

    /**
     * Array of the menu options for spaces.
     */
    public String[] options = {
            "Enter Space information",
            "Update Space Information",
            "Delete Space information"
    };

    /**
     * Constructor for the Space Menu Implementation. Allows the user to create,
     * update and delete spaces.
     * 
     * @param spaceService The org.parking.service that performs the create, update and delete
     *                     database
     *                     interactions.
     */
    public SpaceMenuImpl(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    /**
     * This function will serve as the menu for the Space interactions.
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
                // Enter Space information
                case "1":
                    enterSpace(scan);
                    break;
                // Update Space Info
                case "2":
                    updateSpace(scan);
                    break;
                // Delete Space information
                case "3":
                    deleteSpace(scan);
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
     * Handles the user interaction for creating a new Space.
     * If the user inputs an invalid value at any time, then
     * they will be returned to the menu.
     * Once all of the values have been accepted, the spaceService will be called.
     * If spaceService returns true, then the space was successfully created.
     * 
     * @param scan Scanner that reads input.
     */
    public void enterSpace(Scanner scan) {
        int number, statusIndicator;
        boolean status;
        String type, zoneID, lotName;

        System.out.println("What parking lot do you want to add a space to?");
        lotName = enterSpaceLotName(scan);
        if (lotName == null) {
            System.out.println("Returning to the menu.");
            return;
        }

        System.out.println("What zone do you want to add a space to?");
        zoneID = enterSpaceZoneID(scan);
        if (zoneID == null) {
            System.out.println("Returning to the menu.");
            return;
        }

        number = enterSpaceNumber(scan);
        if (number < 1) {
            System.out.println("Returning to the menu.");
            return;
        }

        type = enterSpaceType(scan);
        if (type == null) {
            System.out.println("Returning to the menu.");
            return;
        }

        statusIndicator = enterSpaceStatus(scan);
        if (statusIndicator == -1) {
            System.out.println("Returning to the menu.");
            return;
        }
        // If the status indicator is 1, then the space is available
        // status will be true
        status = (statusIndicator == 1);

        Space space = spaceService.getSpace(number, zoneID, lotName);

        if (space != null) {
            System.out.println("That space already exists in that parking lot. Returning to menu.");
        } else {
            space = new Space(number, type, status, zoneID, lotName);

            if (spaceService.createSpace(space)) {
                System.out.println("Space was successfully created. Returning to menu.");
            } else {
                System.out.println("There was an error creating the Space. Please try again.");
            }
        }
    }

    /**
     * Handles the user interaction for updating a Space.
     * If the user inputs an invalid value at any time (when
     * retrieving the space), then they will be returned to the menu.
     * If the user inputs a bad value during the update, the old value will be used.
     * Once all of the values have been accepted, the spaceService will be called.
     * If spaceService returns true, then the space was successfully updated.
     * 
     * @param scan Scanner that reads input.
     */
    public void updateSpace(Scanner scan) {
        int number, statusIndicator;
        boolean status;
        String zoneID, type, lotName, temp;

        System.out.println("What parking lot is the space in that you want to update?");
        lotName = enterSpaceLotName(scan);
        if (lotName == null) {
            System.out.println("There was an error while entering the lotName.");
            return;
        }

        System.out.println("What zone is the space in?");
        zoneID = enterSpaceZoneID(scan);
        if (zoneID == null) {
            System.out.println("There was an error while entering the zoneID.");
            return;
        }

        number = enterSpaceNumber(scan);
        if (number < 1) {
            System.out.println("There was an error while entering the number.");
            return;
        }

        Space originalSpace = spaceService.getSpace(number, zoneID, lotName);

        if (originalSpace == null) {
            System.out.println("That space does not exist. Returning to the Space menu.");
        } else {

            // Load the rest of the fields
            type = originalSpace.getType();
            status = originalSpace.getStatus();

            while (true) {
                System.out.println("What attibute do you want to update?");
                System.out.printf("1. Number [Currently: %d]\n", number);
                System.out.printf("2. Status [Currently: %s]\n", status ? "Available" : "In use");
                System.out.printf("3. Type [Currently: %s]\n", type);
                System.out.printf("4. Zone ID [Currently: %s]\n", zoneID);
                System.out.printf("5. Lot Name [Currently: %s]\n", lotName);
                System.out.println("C. Cancel");
                System.out.println("S. Submit changes");

                temp = scan.nextLine().trim().toLowerCase();

                switch (temp) {
                    case "1":
                        number = enterSpaceNumber(scan);
                        if (number < 1) {
                            System.out.println("Invalid number for a space. Please try again.");
                            number = originalSpace.getNumber();
                        }
                        break;
                    case "2":
                        statusIndicator = enterSpaceStatus(scan);
                        if (statusIndicator < 0) {
                            System.out.println("Invalid option. Please try again.");
                        } else {
                            status = statusIndicator == 1;
                        }
                        break;
                    case "3":
                        type = enterSpaceType(scan);
                        if (type == null) {
                            System.out.println("Invalid option. Please try again.");
                            type = originalSpace.getType();
                        }
                        break;
                    case "4":
                        System.out.println("What would you like to change the zoneID to?");
                        zoneID = enterSpaceZoneID(scan);
                        if (zoneID == null) {
                            System.out.println("There was an issue setting the zoneID. Please try again.");
                            zoneID = originalSpace.getZoneID();
                        }
                        break;
                    case "5":
                        System.out.println("What would you like to change the lotName to?");
                        lotName = enterSpaceLotName(scan);
                        if (lotName == null) {
                            System.out.println("There was an issue setting the lotName. Please try again.");
                            lotName = originalSpace.getLotName();
                        }
                        break;
                    // Cancel
                    case "c":
                        System.out.println("No changes were submitted. Returning to Space menu.");
                        return;
                    // Submit changes
                    case "s":
                        Space updatedSpace = new Space(number, type, status, zoneID, lotName);
                        if (spaceService.updateSpace(originalSpace, updatedSpace)) {
                            System.out.println("Space was successfully updated. Returning to Space menu.");
                        } else {
                            System.out.println("There was an error updating the Space. Please try again.");
                        }
                        return;
                    default:
                        System.out.println("Invalid option, please try again.");
                        break;

                }
            }
        }
    }

    /**
     * Handles the user interaction for deleting a Space.
     * If the user inputs an invalid value at any time, then
     * they will be returned to the menu.
     * Once all of the values have been accepted, the spaceService will be called.
     * If spaceService returns true, then the space was successfully deleted.
     * 
     * @param scan Scanner that reads input.
     */
    public void deleteSpace(Scanner scan) {
        int number;
        String zoneID, lotName, temp;

        System.out.println("What parking lot is the space in that you want to delete?");
        lotName = enterSpaceLotName(scan);
        if (lotName == null) {
            System.out.println("There was an error while entering the lotName");
            return;
        }

        System.out.println("What zone is the space in?");
        zoneID = enterSpaceZoneID(scan);
        if (zoneID == null) {
            System.out.println("There was an error while entering the zoneID");
            return;
        }

        number = enterSpaceNumber(scan);
        if (number < 1) {
            System.out.println("There was an error while entering the number");
            return;
        }

        Space space = spaceService.getSpace(number, zoneID, lotName);

        if (space == null) {
            System.out.println("That space does not exist. Returning to the Space menu.");
        } else {
            System.out.println("Are you sure you want to delete this space? Please type \"yes\" to confirm");
            temp = scan.nextLine().trim().toLowerCase();
            if (!temp.equals("yes")) {
                System.out.println("Aborting operation. Returning to Space menu.");
                return;
            }

            if (spaceService.deleteSpace(space)) {
                System.out.println("Space was successfully deleted. Returning to Space menu.");
            } else {
                System.out.println("There was an error deleting the Space. Please try again.");
            }
        }
    }

    /**
     * Handles the user interaction for entering a space's lot name.
     * Accepts any string, expect for an empty line. If the line
     * is empty, then the function will return a null.
     * 
     * @param scan Scanner that reads input.
     * @return A string with the lot name for the space; null if empty.
     */
    private String enterSpaceLotName(Scanner scan) {
        String lotName;
        lotName = scan.nextLine().trim();
        if (lotName.isEmpty()) {
            System.out.println("The lot name was empty.");
            return null;
        }
        return lotName;
    }

    /**
     * Handles the user interaction for entering a space's zoneID.
     * Accepts any string, expect for an empty line. If the line
     * is empty, then the function will return a null.
     * 
     * @param scan Scanner that reads input.
     * @return A string with the zone ID for the space; null if empty.
     */
    private String enterSpaceZoneID(Scanner scan) {
        String zoneID;
        zoneID = scan.nextLine().trim();
        if (zoneID.isEmpty()) {
            System.out.println("The zone ID was empty.");
            return null;
        }
        return zoneID;
    }

    /**
     * Handles the user interaction for setting the space's availability status.
     * The user will be prompted to enter "1" or "2" for available or not.
     * If the user inputs an unexpected value, the function will return a -1.
     *
     * @param scan Scanner that reads input.
     * @return 1 if the space is available, 0 if the space is not available,
     *         -1 if there was an error with the user's input.
     */
    private int enterSpaceStatus(Scanner scan) {
        String temp;
        System.out.println("What is the availability status of the space? (please select 1 or 2):");
        System.out.println("1. Available");
        System.out.println("2. In Use");
        temp = scan.nextLine().trim();
        if (!temp.matches("^[12]{1}$")) {
            System.out.println("Invalid option. Expected 1 or 2.");
        } else {
            if (temp.equals("1"))
                return 1;
            else
                return 0;
        }
        return -1;
    }

    /**
     * Handles the user interaction for entering space's type.
     * The user will input a number (ideally between 1 and 4) for
     * the corresponding space type. If the user's input is empty,
     * then the type will default to "regular". If the input is number,
     * but not one of the expected, the function will default to regular.
     * 
     * If the user input is NOT an integer, then the program will return
     * a null.
     * 
     * @param scan Scanner that reads input.
     * @return the desired type of space; null if there was an error.
     */
    private String enterSpaceType(Scanner scan) {
        String type;
        int num;
        System.out.println("Please select the type of Space (not selecting a choice will default to regular):");
        System.out.println("1. electric");
        System.out.println("2. handicap");
        System.out.println("3. compact car");
        System.out.println("4. regular");
        type = scan.nextLine().trim();
        // If the line is empty, the type will be regular.
        if (type.isEmpty()) {
            type = "regular";
            return type;
        }
        // Else, type to parse the input as an int.
        else {
            try {
                num = Integer.parseInt(type);
                switch (num) {
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
                        System.out.println("Invalid option. Defaulting to regular.");
                        type = "regular";
                }
                return type;
            } catch (NumberFormatException e) {
                System.out.println("The input was not a number.");
                return null;
            }
        }
    }

    /**
     * Handles the user interaction for entering the Space's number.
     * The number must be greater than 0. If the user enters a string or
     * a value less than 1, then the program will return -1 (which
     * respresents an error).
     * 
     * @param scan Scanner that reads input.
     * @return -1 if an error or illegal value. A positive, non-zero number on
     *         success.
     */
    private int enterSpaceNumber(Scanner scan) {
        String temp;
        int number;

        System.out.println("Please enter the number for the space:");
        temp = scan.nextLine().trim();
        try {
            number = Integer.parseInt(temp);
            if (number < 1) {
                System.out.println("Expected a number greater than 0.");
                return -1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Expected a number. Please try again.");
            return -1;
        }
        return number;
    }
}
