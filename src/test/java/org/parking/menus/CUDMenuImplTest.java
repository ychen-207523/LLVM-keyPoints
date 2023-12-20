package org.parking.menus;

import org.parking.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class CUDMenuImplTest {

    private CUDMenuImpl CUDMenuImpl;

    @Mock
    private PermitsService permitsService;
    @Mock
    private DriversService driversService;
    @Mock
    private VehicleService vehicleService;
    @Mock
    private ParkingLotService parkingLotService;
    @Mock
    private ZoneService zoneService;
    @Mock
    private SpaceService spaceService;

    private final InputStream systemIn = System.in;


    @BeforeEach
    void init() {
        openMocks(this);
        CUDMenuImpl = new CUDMenuImpl(permitsService,driversService,vehicleService,parkingLotService,zoneService,spaceService);
    }

    @AfterEach
    void restoreSystem() {
        System.setIn(systemIn);
    }

    @Test
    void callInterfaceTest_driverMenu() {
        String userInput = "1\n7\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        DriverMenu driverMenuMock = mock(DriverMenu.class);
        CUDMenuImpl.setDriverMenu(driverMenuMock);
        CUDMenuImpl.callInterface();
        verify(driverMenuMock, times(1)).callInterface();
    }

    @Test
    void callInterfaceTest_parkingLotMenu() {
        String userInput = "2\n7\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        ParkingLotMenu parkingLotMenu = mock(ParkingLotMenu.class);
        CUDMenuImpl.setParkingLotMenu(parkingLotMenu);
        CUDMenuImpl.callInterface();
        verify(parkingLotMenu, times(1)).callInterface();
    }

    @Test
    void callInterfaceTest_zoneMenu() {
        String userInput = "3\n7\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        ZoneMenu zoneMenu = mock(ZoneMenu.class);
        CUDMenuImpl.setZoneMenu(zoneMenu);
        CUDMenuImpl.callInterface();
        verify(zoneMenu, times(1)).callInterface();
    }

    @Test
    void callInterfaceTest_spaceMenu() {
        String userInput = "4\n7\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        SpaceMenu spaceMenu = mock(SpaceMenu.class);
        CUDMenuImpl.setSpaceMenu(spaceMenu);
        CUDMenuImpl.callInterface();
        verify(spaceMenu, times(1)).callInterface();
    }

    @Test
    void callInterfaceTest_permitMenu() {
        String userInput = "5\n7\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        PermitsMenu permitsMenu = mock(PermitsMenu.class);
        CUDMenuImpl.setPermitsMenu(permitsMenu);
        CUDMenuImpl.callInterface();
        verify(permitsMenu, times(1)).menu();
    }

    @Test
    void callInterfaceTest_vehicleMenu() {
        String userInput = "6\n7\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        VehicleMenu vehicleMenu = mock(VehicleMenu.class);
        CUDMenuImpl.setVehicleMenu(vehicleMenu);
        CUDMenuImpl.callInterface();
        verify(vehicleMenu, times(1)).menu();
    }
}
