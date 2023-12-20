package org.parking.menus;

import org.parking.model.Driver;
import org.parking.service.DriversService;

import java.sql.SQLException;
import java.util.*;

public class DriverMenuImpl implements DriverMenu{
    /***************************************************************************
     * Globals used by the rest of the class
     ****************************************************************************/
    private static DriversService driversService;

    public DriverMenuImpl(DriversService driverService) {
        this.driversService = driverService;
    }
    static String[] menuOptions =
            {
                    "Enter",
                    "Update",
                    "Delete"
            };

    /***************************************************************************
     * User interface for Drivers controls. Allows for CUD operations on Drivers
     * table.
     ****************************************************************************/
    public void callInterface(){
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



            // User selects a field to edit
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
                // Enter Driver information
                case 1:
                    enterDriverUI(scan);
                    break;

                // Update Driver information
                case 2:
                    updateDriverUI(scan);
                    break;

                // Delete Driver information
                case 3:
                    deleteDriver(scan);
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
     * Collects the driver information from user, validates the input with RegEx
     * filter and then passes the object to the driver org.parking.service to create an instance
     * in the table.
     ****************************************************************************/
    private static void enterDriverUI( Scanner scan) {
        // Initialize new driver object
        Driver driver = null;

        // Collect driver attributes from user
        String id = inputId(scan, false);
        String name = inputName(scan, false);
        String status = inputStatus(scan, false);

        // User validates the vehicle information before submitting request
        while (true) {
            // Prints out driver information and scans for confirmation
            driver = new Driver(id, name, status);
            System.out.println("\n##### REVIEW #####");
            System.out.println(driver);
            System.out.print("Do you want to create this driver? (Y/N):");
            String choice = scan.nextLine();

            switch (choice.toLowerCase().charAt(0)) {
                // User Enters: Yes
                case 'y':

                    try {
                        // create selected driver from database
                        boolean result = driversService.create(driver);

                        // Output results based on the success of the run.
                        if(result) System.out.println("Successfully registered " + driver.getName());
                        else System.out.println("Error occurred in registering vehicle. Please try again.");

                    } catch (SQLException e) {
                        // Output SQL Exception
                        System.out.println("SQL Error: " + e.getMessage());
                    }

                    // Return to previous menu
                    return;

                // User Enters: No
                case 'n':
                    System.out.println("Not Creating Driver. Returning to Driver Menu...");
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
     * Pulls in driver information based on the id number. Allows the user
     * to update parameters. If they enter nothing, the parameter persists.
     ****************************************************************************/
    private static void updateDriverUI(Scanner scan){
        // Get the Driver's ID from the user
        String id = inputId(scan, false);
        Driver driver = null;

        // Find the driver in the system. If driver doesn't exist or causes SQL Error, Return to Driver Menu
        System.out.println("Finding Driver information for id: " + id);
        try {
            driver = driversService.getById(id);
            if (driver == null) {
                System.out.println("No Driver Found with that ID.");
                return;
            }
        } catch (SQLException e) {
            // Output SQL Exception
            System.out.println("SQL Error: " + e.getMessage());
            return;
        }

        // User edits the information for the driver
        while (true) {
            // Prints out driver information and scans for confirmation
            System.out.println("Driver ID (Cannot Edit): " + driver.getId());
            System.out.println("1. Driver Name: " + driver.getName());
            System.out.println("2. Driver Type: " + driver.getStatus());
            System.out.println("3. Commit Changes");
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
                // Update Driver Name
                case 1:
                    String name = inputName(scan, true);
                    if(!name.isEmpty()) driver.setName(name);
                    break;

                // Update Driver Type
                case 2:
                    String status = inputStatus(scan, true);
                    if(!status.isEmpty()) driver.setStatus(status);
                    break;

                // Save and Quit
                case 3:
                    try {
                        // Update the driver using the CRUD interface
                        boolean result = driversService.update(driver);

                        if(result) System.out.println("Successfully updated "+ driver.getName());
                        else System.out.println("Error occurred in updating driver. Please try again.");

                    } catch (java.sql.SQLException e) {
                        // Output SQL Exception
                        System.out.println("SQL Error: " + e.getMessage());
                    }
                    return;

                // Discard Changes
                case 0:
                    System.out.println("Update Canceled. Returning to Driver Menu.");
                    return;

                // User Enters: Anything else
                default:
                    System.out.println("Please enter a choice 1-4 or 0.");
                    break;

            }
        }

    }

    /***************************************************************************
     * Pulls in a driver information based on the id number. Prompts the user
     * for confirmation before deleting the record from the database.
     ****************************************************************************/
    private static void deleteDriver(Scanner scan){
        // Get the Driver's ID from the user
        String id = inputId(scan, false);
        Driver driver = null;

        // Find the driver in the system. If driver doesn't exist or causes SQL Error, Return to Driver Menu
        System.out.println("Finding Driver information for id: " + id);
        try {
            driver = driversService.getById(id);
            if (driver == null) {
                System.out.println("No Driver Found with that ID.");
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
            System.out.println(driver);
            System.out.print("Do you want to delete this driver? (Y/N):");
            String choice = scan.nextLine();

            switch (choice.toLowerCase().charAt(0)) {
                // User Enters: Yes
                case 'y':
                    // Delete selected driver from database
                    try {
                        // Delete the driver using the CRUD interface
                        boolean result = driversService.delete(id);

                        if(result) System.out.println("Successfully deleted " + driver.getName());
                        else System.out.println("Error occurred in deleting vehicle. Please try again.");
                    } catch (java.sql.SQLException e) {
                        // Output SQL Exception
                        System.out.println("SQL Error: " + e.getMessage());
                    }
                    return;

                // User Enters: No
                case 'n':
                    System.out.println("Not Deleting Driver. Returning to Driver Menu...");
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
    private static String inputId(Scanner scan, boolean persistent){
        boolean validInput = false;
        String id = null;

        // Allows alphanumeric strings, but the length must be
        // between 1 and 255 characters.
        String pattern = "^[A-Za-z0-9]{1,255}$";

        while (!validInput) {
            System.out.print("Enter Driver's ID: ");

            id = scan.nextLine();
            if(id.matches(pattern)) validInput=true;
            else if(persistent && id.isEmpty()) validInput=true;
            else System.out.println("Please enter the driver's university ID or visitor's phone number. Only include " +
                        "alphanumeric characters");
        }

        return id;
    }

    private static String inputName(Scanner scan, boolean persistent){
        boolean validInput = false;
        String name = null;

        // Allows alphabetic strings, but the length must be
        // between 1 and 255 characters. Can also include -, space, and '
        // TODO Add assumption about characters in name
        String pattern = "^[A-Za-z -']{1,255}$";

        while (!validInput) {
            System.out.print("Enter Driver's Name: ");

            name = scan.nextLine();
            if(name.matches(pattern)) validInput=true;
            else if(persistent && name.isEmpty()) validInput=true;
            else System.out.println("Input included invalid characters. Please try again.");
        }

        return name;
    }

    private static String inputStatus(Scanner scan, boolean persistent){
        boolean validInput = false;
        String status = null;

        // Allows the characters E, S, or V
        String pattern = "^[ESV]$";

        while (!validInput) {
            System.out.print("Enter Driver's Status: ");

            status = scan.nextLine().toUpperCase();
            if(status.matches(pattern)) validInput=true;
            else if(persistent && status.isEmpty()) validInput=true;
            else System.out.println("Please enter the driver's status as either S (student), E (employee) or V (visitor). " +
                        "Only entering the single character for the associated status is required.");
        }

        return status;
    }

}
