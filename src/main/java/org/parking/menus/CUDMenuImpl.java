package org.parking.menus;

import org.parking.service.Constants;
import org.parking.service.DriversService;
import org.parking.service.ParkingLotService;
import org.parking.service.PermitsService;
import org.parking.service.SpaceService;
import org.parking.service.VehicleService;
import org.parking.service.ZoneService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CUDMenuImpl implements CUDMenu {
    private PermitsMenu permitsMenu;
    private DriverMenu driverMenu;
    private VehicleMenu vehicleMenu;
    private ParkingLotMenu parkingLotMenu;
    private ZoneMenu zoneMenu;
    private SpaceMenu spaceMenu;


    public CUDMenuImpl(PermitsService permitsService,
                       DriversService driversService,
                       VehicleService vehicleService,
                       ParkingLotService parkingLotService,
                       ZoneService zoneService,
                       SpaceService spaceService) {
        this.permitsMenu    = new PermitsMenuImpl(permitsService, driversService, vehicleService);
        this.driverMenu     = new DriverMenuImpl(driversService);
        this.vehicleMenu    = new VehicleMenuImpl(vehicleService);
        this.parkingLotMenu = new ParkingLotMenuImpl(parkingLotService);
        this.zoneMenu       = new ZoneMenuImpl(zoneService, parkingLotService);
        this.spaceMenu      = new SpaceMenuImpl(spaceService);
    }
    public void callInterface(){
        int option = 0;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("1. Drivers\n2. Parking Lots\n3. Zones\n4. Spaces\n5. Permits\n6. Vehicle\n7. Go back\n");
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
                    driverMenu.callInterface();
                    break;
                case 2:
                    parkingLotMenu.callInterface();
                    break;
                case 3:
                    zoneMenu.callInterface();
                    break;
                case 4:
                    spaceMenu.callInterface();
                    break;
                case 5:
                    permitsMenu.menu();
                    break;
                case 6:
                    vehicleMenu.menu();
                    break;
                case 7:
                    break;
                default:
                    System.out.println(Constants.LogTryInputAgain);
            }

        } while (option != 7);

    }

    public void setPermitsMenu(PermitsMenu permitsMenu) {
        this.permitsMenu = permitsMenu;
    }

    public void setDriverMenu(DriverMenu driverMenu) {
        this.driverMenu = driverMenu;
    }

    public void setVehicleMenu(VehicleMenu vehicleMenu) {
        this.vehicleMenu = vehicleMenu;
    }

    public void setParkingLotMenu(ParkingLotMenu parkingLotMenu) {
        this.parkingLotMenu = parkingLotMenu;
    }

    public void setZoneMenu(ZoneMenu zoneMenu) {
        this.zoneMenu = zoneMenu;
    }

    public void setSpaceMenu(SpaceMenu spaceMenu) {
        this.spaceMenu = spaceMenu;
    }
}
