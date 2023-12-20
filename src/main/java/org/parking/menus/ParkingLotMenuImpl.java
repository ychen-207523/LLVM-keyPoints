package org.parking.menus;

import java.util.Scanner;

import org.parking.model.ParkingLot;
import org.parking.service.ParkingLotService;

/**
 * Implementation of the parking lot menu. This class implements the functions
 * needed for the parking lot menu.
 */
public class ParkingLotMenuImpl implements ParkingLotMenu {

    /**
     * The parkingLotService to use for database interactions.
     */
    private final ParkingLotService parkingLotService;

    /**
     * Constructor for the parkingLotService.
     * Developers can call on this object to access the menu for parking lot
     * operations.
     * 
     * @param parkingLotService Service that handles interactions with the database
     *                          for parking lot objects.
     */
    public ParkingLotMenuImpl(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    /**
     * An array that holds the options for this menu.
     */
    public String[] options = {
            "Enter Parking lot information",
            "Update Parking Lot Information",
            "Delete Parking lot information"
    };

    /**
     * This function will serve as the menu for the parking lot interactions.
     */
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
                // Enter Parking lot information
                case "1":
                    enterParkingLot(scan);
                    break;
                // Update Parking Lot Info
                case "2":
                    updateParkingLot(scan);
                    break;
                // Delete Parking lot information
                case "3":
                    deleteParkingLot(scan);
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
     * Handles the user interaction for entering a parking lot.
     * 
     * @param scan Scanner that reads in user input.
     */
    public void enterParkingLot(Scanner scan) {
        String name;
        String address;

        System.out.println("What is the name of the parking lot that you want to enter?");
        name = scan.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("The name was empty. Returning to menu.");
            return;
        }

        ParkingLot lot = parkingLotService.getParkingLot(name);

        if (lot != null) {
            System.out.println("A parking lot already exists with that name. Returning to the parking lot menu.");
            return;
        } else {
            System.out.println("Please enter the address of the parking lot:");
            address = scan.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("The address was empty. Returning to menu.");
                return;
            }

            System.out.println("Creating parking lot with:");
            System.out.println("Name: " + name);
            System.out.println("Address: " + address);

            lot = new ParkingLot(name, address);
            if (parkingLotService.createParkingLot(lot)) {
                System.out.println("Parking lot was successfully created.");
            } else {
                System.out.println("There was an error creating the parking lot.");
            }

            return;
        }
    }

    /**
     * Handles the user interaction for updating a parking lot.
     * 
     * @param scan Scanner that reads in user input.
     */
    public void updateParkingLot(Scanner scan) {
        String name, input;

        System.out.println("What is the name of the parking lot that you want to update?");
        name = scan.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("The name was empty. Returning to menu.");
            return;
        }

        ParkingLot lot = parkingLotService.getParkingLot(name);

        if (lot != null) {
            while (true) {
                System.out.println("What attibute do you want to update?");
                System.out.printf("1. Name [Currently: %s]\n", lot.getName());
                System.out.printf("2. Address [Currently: %s]\n", lot.getAddress());
                System.out.println("C. Cancel");
                System.out.println("S. Submit changes");

                input = scan.nextLine().trim().toLowerCase();

                switch (input) {
                    // Name
                    case "1":
                        System.out.println("The current name is:" + lot.getName());
                        System.out.println("What do you want to change the name to?");
                        input = scan.nextLine().trim();
                        if (input.isEmpty()) {
                            System.out.println("The name was empty, please try again.");
                            continue;
                        } else {
                            lot.setName(input);
                        }
                        break;
                    // Address
                    case "2":
                        System.out.println("The current address is:" + lot.getAddress());
                        System.out.println("What do you want to change the address to?");
                        input = scan.nextLine().trim();
                        if (input.isEmpty()) {
                            System.out.println("The address was empty, please try again.");
                            continue;
                        } else {
                            lot.setAddress(input);
                        }
                        break;
                    // Cancel
                    case "c":
                        System.out.println("No changes were submitted. Returning to parking lot menu.");
                        return;
                    // Submit changes
                    case "s":
                        if (parkingLotService.updateParkingLot(name, lot)) {
                            System.out.println("The parking lot was successfully updated.");
                        } else {
                            System.out.println("There was an issue updating the parking lot.");
                        }
                        return;

                    default:
                        System.out.println("Invalid option, please try again.");
                        break;
                }

            }
        } else {
            System.out.println("There is not a parking lot in the database with the name: " + name);
        }

        return;
    }

    /**
     * Handles the user interaction for deleting a parking lot.
     * 
     * @param scan Scanner that reads in user input.
     */
    public void deleteParkingLot(Scanner scan) {
        String input;
        String name;

        System.out.println("What is the name of the parking lot that you want to delete?");
        name = scan.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("The name was empty. Returning to menu.");
            return;
        }

        ParkingLot lot = parkingLotService.getParkingLot(name);

        if (lot != null) {
            while (true) {
                System.out.println("Are you sure you want to delete:\n" + name + " (y/n)");
                input = scan.nextLine().toLowerCase().trim();

                if (input.equals("y")) {
                    if (parkingLotService.deleteParkingLot(name)) {
                        System.out.println("The parking lot was successfully deleted.");
                    } else {
                        System.out.println("There was an issue deleting the parking lot.");
                    }
                    break;
                } else if (input.equals("n")) {
                    System.out.println("Returning to parking lot menu.");
                    break;
                } else {
                    System.out.println("Invalid option. Please insert \"y\" or \"n\"");
                }
            }
        } else {
            System.out.println("Parking lot: " + name + " does not exist.");
        }
        return;
    }
}
