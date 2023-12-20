package org.parking;

import org.parking.menus.UserInterfaceImpl;
import org.parking.service.*;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Instantiate all services for dependency injection
        DBService dbService = new DBServiceImpl();
        CitationsService citationsService = new CitationsServiceImpl(dbService);
        VehicleService vehicleService = new VehicleServiceImpl(dbService);
        PermitsService permitsService = new PermitsServiceImpl(dbService);
        ZoneService zonesService = new ZoneServiceImpl(dbService);
        ReportsService reportsService = new ReportsServiceImpl(dbService);
        ParkingLotService parkingLotService = new ParkingLotServiceImpl(dbService);
        DriversService driversService = new DriversServiceImpl(dbService);
        SpaceService spaceService = new SpaceServiceImpl(dbService);
        UserInterfaceImpl UserInterfaceImpl = new UserInterfaceImpl(dbService,
                                                                    citationsService,
                                                                    vehicleService,
                                                                    permitsService,
                                                                    zonesService,
                                                                    driversService,
                                                                    reportsService,
                                                                    parkingLotService,
                                                                    spaceService);
        UserInterfaceImpl.callInterface();

    }
}