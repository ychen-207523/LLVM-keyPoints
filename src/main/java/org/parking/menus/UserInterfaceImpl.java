package org.parking.menus;


import org.parking.service.*;

import java.util.Scanner;

public class UserInterfaceImpl implements UserInterface {
    private final CitationsMenu citationsMenuImpl;
    private final MaintainPermitsAndVehicleMenu maintainPermitsAndVehicleMenuImpl;
    private final ReportsMenu reportsMenuImpl;
    private final InformationProcessMenu informationProcessMenuImpl;

    public UserInterfaceImpl(DBService dbService,
                             CitationsService citationsService,
                             VehicleService vehicleService,
                             PermitsService permitsService,
                             ZoneService zoneService,
                             DriversService driversService,
                             ReportsService reportsService,
                             ParkingLotService parkingLotService,
                             SpaceService spaceService) {
        citationsMenuImpl = new CitationsMenuImpl(citationsService, vehicleService, permitsService, zoneService);
        maintainPermitsAndVehicleMenuImpl = new MaintainPermitsAndVehicleMenuImpl(driversService, vehicleService, permitsService);
        reportsMenuImpl = new ReportsMenuImpl(reportsService, citationsService, parkingLotService, zoneService);
        informationProcessMenuImpl = new InformationProcessMenuImpl(
                new CUDMenuImpl(permitsService,
                        driversService,
                        vehicleService,
                        parkingLotService,
                        zoneService,
                        spaceService),
                citationsService, spaceService, zoneService);
    }

    public void callInterface() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome to Wolf Parking Management System\n\n");
        int option = 0;
        String input;
        do {
            HomePage.menu();
            System.out.print("\nPlease enter your option:");

            input = scan.nextLine();
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }

            switch (option) {
                case 1:
                    informationProcessMenuImpl.callInterface();
                    break;
                case 2:
                    maintainPermitsAndVehicleMenuImpl.callInterface();
                    // call Maintaining permits and vehicle information function
                    break;
                case 3:
                    // call Generating and maintaining citations function
                    citationsMenuImpl.callInterface();
                    break;
                case 4:
                    // call Reports function
                    // TODO: This needs to be constructed instead of statically called for dependency injection
                    reportsMenuImpl.callInterface();
                case 5:
                    System.out.println("Thank you for using Wolf Parking Management System.");
                    System.out.println("See you next time.");
                    break;
                default:
                    System.out.println("Invalid input, please enter again.");
            }

        } while (option != 5);


    }
}
