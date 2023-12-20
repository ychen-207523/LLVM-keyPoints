package org.parking.menus;

import org.parking.model.Vehicle;
import org.parking.service.DBServiceImpl;
import org.parking.service.VehicleService;
import org.parking.service.VehicleServiceImpl;

import java.sql.*;
import java.util.Scanner;
import java.time.Year;
import java.lang.*;


public class VehicleMenuImpl implements VehicleMenu {

    private final VehicleService vehicleService;

    public VehicleMenuImpl(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    /***************************************************************************
     * Globals used by the rest of the class
     ****************************************************************************/
     String[] menuOptions =
            {
                    "Enter",
                    "Update",
                    "Delete"
            };

    /***************************************************************************
     * User interface for Vehicle controls. Allows for CUD operations on Vehicles
     * table.
     ****************************************************************************/
    public void menu(){
        // Create a scanner and initialize user input to 0
        Scanner scan = new Scanner(System.in);

        while (true)
        {
            // Print the user's options to the console and prompts for input
            int optionNum = 1;
            System.out.println("Select an option:");

            for (String option : menuOptions)
            {
                System.out.println(optionNum + ": " + option);
                ++optionNum;
            }
            System.out.println("0: Return to the main menu");



            // User selects a field
            int option = 0;
            boolean validInput = false;
            while(!validInput){
                System.out.print("\nPlease enter your option: ");
                String input = scan.nextLine();
                try{
                    option = Integer.parseInt(input.trim());
                    System.out.println(option);
                    validInput = true;
                } catch (NumberFormatException e){
                    System.out.println("Please enter a choice 1-4 or 0.");
                }
            }

            // Based on input, executes the specified function
            switch (option)
            {
                case 1:
                // Enter Driver information
                // Enter Vehicle information
                    enterVehicleUI(scan);
                    break;

                case 2:
                // Update Driver information
                // Update Vehicle information
                    updateVehicleUI(scan);
                    break;

                case 3:
                // Delete Driver information
                // Delete Vehicle information
                    deleteVehcileUI(scan);
                    break;

                case 0:
                // Return to the main menu
                    System.out.println("Returning to main menu");
                    return;

                default:
                // Other value input
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /***************************************************************************
     * Collects the vehicle information from user, validates the input with RegEx
     * filter and then passes the object to the vehicle org.parking.service to create an instance
     * in the table.
     ****************************************************************************/
    public void enterVehicleUI(Scanner scan) {
        // Initialize new vehicle object
        Vehicle vehicle = null;

        // Collect vehicle attributes from user
        String license = inputLicense(scan, false);
        String manufacturer = inputManufacturer(scan, false);
        String model = inputModel(scan, false);
        String color = inputColor(scan, false);
        int year = inputYear(scan, false);

        // User validates the vehicle information before submitting request
        while (true) {
            // Prints out driver information and scans for confirmation
            vehicle = new Vehicle(license, model, color, manufacturer, year);
            System.out.println("\n##### REVIEW #####");
            System.out.println(vehicle);
            System.out.print("Do you want to create this vehicle? (Y/N):");
            String choice = scan.nextLine();

            switch (choice.toLowerCase().charAt(0)) {
                case 'y':
                // User selects Yes
                    try {
                        // Create the vehicle using the CRUD interface
                        boolean result = vehicleService.create(vehicle);

                        // Output results based on the success of the run.
                        if(result) System.out.println("Successfully registered " + vehicle.getLicense());
                        else System.out.println("Error occurred in registering vehicle. Please try again.");

                    } catch (SQLException e) {
                        // Output SQL Exception
                        System.out.println("SQL Error: " + e.getMessage());
                    }

                    // Return to previous menu
                    return;

                case 'n':
                // User selects no
                    System.out.println("Not Creating Vehicle. Returning to Vehicle Menu...");
                    // Return to previous menu
                    return;

                // User Enters: Anything else
                default:
                    System.out.println("Please enter Y or N");
                    break;

            }
        }
    }

    /***************************************************************************
     * Pulls in a vehicle information based on the license number. Allows the user
     * to update parameters. If they enter nothing, the parameter persists.
     ****************************************************************************/
    private void updateVehicleUI(Scanner scan){
        // Gets the vehicle license from user
        String license = inputLicense(scan, false);
        Vehicle vehicle = null;

        // Find the vehicle in the system. If vehicle doesn't exist or causes SQL Error, Return to vehicle Menu
        System.out.println("Finding Vehicle information for license: " + license);
        try {
            vehicle = vehicleService.getByLicense(license);
            if (vehicle == null) {
                System.out.println("No Vehicle Found with that License.");
                return;
            }
        } catch (SQLException e) {
            // Output SQL Exception
            System.out.println("SQL Error: " + e.getMessage());
            return;
        }

        // User edits the information for the vehicle
        while (true) {
            // Prints out vehicle information
            System.out.println("Vehicle License (Cannot edit): " + vehicle.getLicense());
            System.out.println("1. Vehicle Model: " + vehicle.getModel());
            System.out.println("2. Vehicle Color: " + vehicle.getColor());
            System.out.println("3. Vehicle Manufacturer: " + vehicle.getManufacturer());
            System.out.println("4. Vehicle Year: " + vehicle.getYear());
            System.out.println("5. Commit Changes");
            System.out.println("0. Discard and Exit");

            // User selects a field to edit
            int field = 0;
            boolean validInput = false;
            while(!validInput){
                System.out.print("\nSelect a field to edit or commit/discard changes: ");
                String input = scan.nextLine();
                try{
                    field = Integer.parseInt(input.trim());
                    System.out.println(field);
                    validInput = true;
                } catch (NumberFormatException e){
                    System.out.println("Please enter a choice 1-4 or 0.");
                }
            }

            switch (field) {
                // Update Vehicle Model
                case 1:
                    String model = inputModel(scan, true);
                    if(!model.isEmpty()) vehicle.setModel(model);
                    break;

                // Update Vehicle Color
                case 2:
                    String color = inputColor(scan, true);
                    if(!color.isEmpty()) vehicle.setColor(color);
                    break;

                // Update Vehicle Manufactruer
                case 3:
                    String manufacturer = inputManufacturer(scan, true);
                    if(!manufacturer.isEmpty()) vehicle.setManufacturer(manufacturer);
                    break;

                // Update Vehicle Year
                case 4:
                    int year = inputYear(scan, true);
                    if(!(year==0)) vehicle.setYear(year);
                    break;

                // Save and Quit
                case 5:
                    try {
                        // Create the vehicle using the CRUD interface
                        boolean result = vehicleService.update(vehicle);

                        // Output results based on the success of the run.
                        if(result) System.out.println("Successfully updated " + vehicle.getLicense());
                        else System.out.println("Error occurred in updating vehicle. Please try again.");

                    } catch (java.sql.SQLException e) {
                        // Output SQL Exception
                        System.out.println("SQL Error: " + e.getMessage());
                    }
                    return;

                case 0:
                // Discard Changes
                    System.out.println("Update canceled. Returning to vehicle menu.");
                    return;

                default:
                // User Enters: Anything else
                    System.out.println("Please enter a choice 1-5 or 0.");
                    break;

            }
        }
    }

    /***************************************************************************
     * Pulls in a vehicle information based on the license number. Prompts the user
     * for confirmation before deleting the record from the database.
     ****************************************************************************/
    public void deleteVehcileUI(Scanner scan){
        // Get the Vehicle's license number from the user
        String license = inputLicense(scan, false);
        Vehicle vehicle = null;

        // Find the vehicle in the system. If vehicle doesn't exist or causes SQL Error, Return to vehicle Menu
        System.out.println("Finding vehicle information for license: " + license);
        try {
            vehicle = vehicleService.getByLicense(license);
            if (vehicle == null) {
                System.out.println("No vehicle found with that license number.");
                return;
            }
        }
        catch (java.sql.SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return;
        }

        // User confirms the user should be deleted
        while (true) {
            // Prints out driver information and scans for confirmation
            System.out.println("\n##### REVIEW #####");
            System.out.println(vehicle);
            System.out.print("Do you want to delete this vehicle? (Y/N):");
            String choice = scan.nextLine();

            switch (choice.toLowerCase().charAt(0)) {
                // User Enters: Yes
                case 'y':
                    try {
                        // Delete the vehicle using the CRUD interface
                        boolean result = vehicleService.delete(vehicle.getLicense());

                        // Output results based on the success of the run.
                        if(result) System.out.println("Successfully deleted " + vehicle.getLicense());
                        else System.out.println("Error occurred in deleting vehicle. Please try again.");

                    } catch (java.sql.SQLException e) {
                        // Output SQL Exception
                        System.out.println("SQL Error: " + e.getMessage());
                    }
                    return;

                case 'n':
                    // User selects no
                    System.out.println("Not deleting vehicle. Returning to vehicle menu...");
                    // Return to previous menu
                    return;

                // User Enters: Anything else
                default:
                    System.out.println("Please enter Y or N");
                    break;

            }
        }

    }

    /***************************************************************************
     * Input Validators. For each parameter, validates the input based on a
     * specified pattern. If the persistent flag is true, the user can enter nothing
     * to have the preexisting value remain in place.
     ****************************************************************************/
    private String inputLicense(Scanner scan, boolean persistent){
        boolean validInput = false;
        String license = null;

        // Allows alphanumeric strings or special characters, but the length must be
        // between 1 and 255 characters and all letters must be capitalized.
        String pattern = "^[A-Z0-9\\W_]{1,255}$";

        while (!validInput) {
            System.out.print("Enter Vehicle's License: ");

            license = scan.nextLine();
            if(license.matches(pattern)) validInput=true;
            else if(persistent && license.isEmpty()) validInput=true;
            else System.out.println("Please enter a string between 1 and 255 characters. All letters should be " +
                    "uppercase");
        }

        return license;
    }

    private String inputManufacturer(Scanner scan, boolean persistent){
        boolean validInput = false;
        String manufacturer = null;

        // Allows alphanumeric strings or hyphens, but the length must be
        // between 1 and 255 characters.
        String pattern = "^[A-Za-z0-9\\-]{1,255}$";

        while (!validInput) {
            System.out.print("Enter Vehicle's Manufacturer: ");

            manufacturer = scan.nextLine();
            if(manufacturer.matches(pattern)) validInput=true;
            else if(persistent && manufacturer.isEmpty()) validInput=true;
            else System.out.println("Please enter a string between 1 and 255 characters. Only alphanumeric characters " +
                        "or hyphens are allowed");
        }


        return manufacturer;
    }

    private String inputModel(Scanner scan, boolean persistent){
        boolean validInput = false;
        String model = null;

        // Allows alphanumeric strings or hyphens, but the length must be
        // between 1 and 255 characters.
        String pattern = "^[A-Za-z0-9\\-]{1,255}$";

        while (!validInput) {
            System.out.print("Enter Vehicle's Model: ");

            model = scan.nextLine();
            if(model.matches(pattern)) validInput=true;
            else if(persistent && model.isEmpty()) validInput=true;
            else System.out.println("Please enter a string between 1 and 255 characters. Only alphanumeric characters " +
                    "or hyphens are allowed");
        }

        return model;
    }

    private String inputColor(Scanner scan, boolean persistent){
        boolean validInput = false;
        String color = null;

        // Allows alphabetic strings or hyphens, but the length must be
        // between 1 and 255 characters.
        String pattern = "^[A-Za-z\\-]{1,255}$";

        while (!validInput) {
            System.out.print("Enter Vehicle's Color: ");

            color = scan.nextLine();
            if(color.matches(pattern)) validInput=true;
            else if(persistent && color.isEmpty()) validInput=true;
            else System.out.println("Please enter a string between 1 and 255 characters. Only alphabetic characters " +
                    "or hyphens are allowed");
        }

        return color;
    }

    private int inputYear(Scanner scan, boolean persistent) {
        boolean validInput = false;
        int year = 0;


        while (!validInput) {
            System.out.print("Enter Vehicle's Year: ");

            try{
                // Get input as string and if it is not empty, convert to integer
                String input = scan.nextLine();
                if(!input.isEmpty()) year = Integer.parseInt(input.trim());
                else if(persistent) return year;

            } catch (NumberFormatException e) {
                System.out.println("Please enter a year between 1886 and " + (Year.now().getValue()+1) + "as an integer");
                continue;
            }

            // year must be between 1886 (when the first car was invented) and 1 year from the present
            // as that is the latest car available on the market.
            //TODO Add Assumption for vehicle years
            if(year>=1886 && year<=Year.now().getValue()+1) validInput=true;
            else System.out.println("Please enter a year between 1886 and " + (Year.now().getValue()+1) + "as an integer");
        }

        return year;
    }

}