package org.parking.menus;

import org.parking.model.ParkingLot;
import org.parking.model.Zone;
import org.parking.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ZoneMenuImplTest {

    private ZoneMenuImpl zoneMenuImpl;

    @Mock
    private ZoneService zoneService;
    @Mock
    private ParkingLotService parkingLotService;

    private final InputStream systemIn = System.in;

    @BeforeEach
    void init() {
        openMocks(this);
        zoneMenuImpl = new ZoneMenuImpl(zoneService, parkingLotService);
    }

    @AfterEach
    void restoreInput() {
        System.setIn(systemIn);
    }

    @Test
    void callInterface_invalidInput() {
        String input = "something\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        zoneMenuImpl.callInterface();
    }

    @Test
    void callInterface_enterZone_success() {
        String zoneID = "A";
        String lotName = "testLotName";
        Zone zone = new Zone(zoneID, lotName);
        String input = String.format("1\n%s\n%s\n0\n", lotName, zoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(lotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.createZone(any(Zone.class))).thenReturn(true);
        zoneMenuImpl.callInterface();
        verify(zoneService, times(1)).createZone(any(Zone.class));
    }

    @Test
    void callInterface_enterZone_queryFailed() {
        String zoneID = "A";
        String lotName = "testLotName";
        Zone zone = new Zone(zoneID, lotName);
        String input = String.format("1\n%s\n%s\n0\n", lotName, zoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(lotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.createZone(zone)).thenReturn(false);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(lotName);
        verify(zoneService, times(1)).createZone(any(Zone.class));
    }

    @Test
    void callInterface_enterZone_invalidZone() {
        String zoneID = "F";
        String lotName = "testLotName";
        String input = String.format("1\n%s\n%s\n0\n", lotName, zoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(lotName)).thenReturn(mock(ParkingLot.class));
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(lotName);
        verify(zoneService, times(0)).createZone(any(Zone.class));
    }

    @Test
    void callInterface_enterZone_invalidLotName() {
        String zoneID = "A";
        String lotName = "";
        String input = String.format("1\n%s\n0\n", lotName, zoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(0)).getParkingLot(lotName);
        verify(zoneService, times(0)).createZone(any(Zone.class));
    }

    @Test
    void callInterface_enterZone_createLotAndZone_success() {
        String zoneID = "A", lotName = "testLotName", address = "testAddress";
        String input = String.format("1\n%s\ny\n%s\n%s\n0\n", lotName, address, zoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(lotName)).thenReturn(null);
        when(zoneService.createLotAndZone(address, lotName, zoneID)).thenReturn(true);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(lotName);
        verify(zoneService, times(1)).createLotAndZone(address, lotName, zoneID);
    }

    @Test
    void callInterface_enterZone_createLotAndZone_queryFailed() {
        String zoneID = "A", lotName = "testLotName", address = "testAddress";
        String input = String.format("1\n%s\ny\n%s\n%s\n0\n", lotName, address, zoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(lotName)).thenReturn(null);
        when(zoneService.createLotAndZone(address, lotName, zoneID)).thenReturn(false);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(lotName);
        verify(zoneService, times(1)).createLotAndZone(address, lotName, zoneID);
    }

    @Test
    void callInterface_enterZone_createLotAndZone_cancel() {
        String zoneID = "A", lotName = "testLotName", address = "testAddress";
        String input = String.format("1\n%s\nn\n0\n", lotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(lotName)).thenReturn(null);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(lotName);
        verify(zoneService, times(0)).createLotAndZone(address, lotName, zoneID);
    }

    @Test
    void callInterface_enterZone_createLotAndZone_invalidOption() {
        String zoneID = "A", lotName = "testLotName", address = "testAddress";
        String input = String.format("1\n%s\nsomething\n0\n", lotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(lotName)).thenReturn(null);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(lotName);
        verify(zoneService, times(0)).createLotAndZone(address, lotName, zoneID);
    }

    @Test
    void callInterface_enterZone_createLotAndZone_invalidAddress() {
        String zoneID = "A", lotName = "testLotName", address = "";
        String input = String.format("1\n%s\ny\n%s\n0\n", lotName, address);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(lotName)).thenReturn(null);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(lotName);
        verify(zoneService, times(0)).createLotAndZone(address, lotName, zoneID);
    }

    @Test
    void callInterface_updateZone_success() {
        String testZoneID = "A", testLotName = "testLotName";
        String newZoneID = "B", newLotName = "lotLotName";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("2\n%s\n%s\n1\n%s\n2\n%s\ns\n0\n", testLotName, testZoneID, newZoneID, newLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.getZone(eq(testZoneID), eq(testLotName))).thenReturn(testZone);
        when(zoneService.updateZone(eq(testZone), any(Zone.class))).thenReturn(true);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(1)).getZone(testZoneID, testLotName);
        verify(zoneService, times(1)).updateZone(any(Zone.class), any(Zone.class));
    }

    @Test
    void callInterface_updateZone_invalidInputThenSubmit() {
        String testZoneID = "A", testLotName = "testLotName";
        String newZoneID = "invalidZone", newLotName = "";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("2\n%s\n%s\n1\n%s\n2\n%s\nsomething\ns\n0\n", testLotName, testZoneID, newZoneID, newLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.getZone(eq(testZoneID), eq(testLotName))).thenReturn(testZone);
        when(zoneService.updateZone(eq(testZone), any(Zone.class))).thenReturn(true);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(1)).getZone(testZoneID, testLotName);
        verify(zoneService, times(1)).updateZone(any(Zone.class), any(Zone.class));
    }

    @Test
    void callInterface_updateZone_cancel() {
        String testZoneID = "A", testLotName = "testLotName";
        String newZoneID = "B", newLotName = "lotLotName";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("2\n%s\n%s\n1\n%s\n2\n%s\nc\n0\n", testLotName, testZoneID, newZoneID, newLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.getZone(eq(testZoneID), eq(testLotName))).thenReturn(testZone);
        when(zoneService.updateZone(eq(testZone), any(Zone.class))).thenReturn(true);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(1)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).updateZone(any(Zone.class), any(Zone.class));
    }

    @Test
    void callInterface_updateZone_submissionError() {
        String testZoneID = "A", testLotName = "testLotName";
        String newZoneID = "B", newLotName = "lotLotName";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("2\n%s\n%s\n1\n%s\n2\n%s\ns\n0\n", testLotName, testZoneID, newZoneID, newLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.getZone(eq(testZoneID), eq(testLotName))).thenReturn(testZone);
        when(zoneService.updateZone(eq(testZone), any(Zone.class))).thenReturn(false);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(1)).getZone(testZoneID, testLotName);
        verify(zoneService, times(1)).updateZone(any(Zone.class), any(Zone.class));
    }

    @Test
    void callInterface_updateZone_getZoneFailed() {
        String testZoneID = "A", testLotName = "testLotName";
        String input = String.format("2\n%s\n%s\n0\n", testLotName, testZoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.getZone(eq(testZoneID), eq(testLotName))).thenReturn(null);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(1)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).updateZone(any(Zone.class), any(Zone.class));
    }

    @Test
    void callInterface_updateZone_getParkingLotFailed() {
        String testZoneID = "A", testLotName = "testLotName";
        String input = String.format("2\n%s\n0\n", testLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(null);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(0)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).updateZone(any(Zone.class), any(Zone.class));
    }

    @Test
    void callInterface_updateZone_invalidZoneID() {
        String testZoneID = "something", testLotName = "testLotName";
        String input = String.format("2\n%s\n%s\n0\n", testLotName, testZoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(0)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).updateZone(any(Zone.class), any(Zone.class));
    }

    @Test
    void callInterface_updateZone_invalidLotName() {
        String testLotName = "";
        String input = String.format("2\n%s\n0\n", testLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(0)).getParkingLot(anyString());
        verify(zoneService, times(0)).getZone(anyString(), anyString());
        verify(zoneService, times(0)).updateZone(any(Zone.class), any(Zone.class));
    }

    @Test
    void callInterface_deleteZone_success() {
        String testZoneID = "A", testLotName = "testLotName";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("3\n%s\n%s\ny\n0\n", testLotName, testZoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.getZone(eq(testZoneID), eq(testLotName))).thenReturn(testZone);
        when(zoneService.deleteZone(eq(testZone))).thenReturn(true);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(1)).getZone(testZoneID, testLotName);
        verify(zoneService, times(1)).deleteZone(testZone);
    }

    @Test
    void callInterface_deleteZone_cancel() {
        String testZoneID = "A", testLotName = "testLotName";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("3\n%s\n%s\nn\n0\n", testLotName, testZoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.getZone(eq(testZoneID), eq(testLotName))).thenReturn(testZone);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(1)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).deleteZone(testZone);
    }

    @Test
    void callInterface_deleteZone_invalidOptionThenCancel() {
        String testZoneID = "A", testLotName = "testLotName";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("3\n%s\n%s\nsomething\nn\n0\n", testLotName, testZoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.getZone(eq(testZoneID), eq(testLotName))).thenReturn(testZone);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(1)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).deleteZone(testZone);
    }

    @Test
    void callInterface_deleteZone_zoneDoesNotExist() {
        String testZoneID = "A", testLotName = "testLotName";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("3\n%s\n%s\n0\n", testLotName, testZoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        when(zoneService.getZone(eq(testZoneID), eq(testLotName))).thenReturn(null);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(1)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).deleteZone(testZone);
    }

    @Test
    void callInterface_deleteZone_invalidZone() {
        String testZoneID = "", testLotName = "testLotName";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("3\n%s\n%s\n0\n", testLotName, testZoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(mock(ParkingLot.class));
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(0)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).deleteZone(testZone);
    }

    @Test
    void callInterface_deleteZone_parkingLotNotFound() {
        String testZoneID = "A", testLotName = "testLotName";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("3\n%s\n0\n", testLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(testLotName)).thenReturn(null);
        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(testLotName);
        verify(zoneService, times(0)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).deleteZone(testZone);
    }

    @Test
    void callInterface_deleteZone_invalidParkingLot() {
        String testZoneID = "A", testLotName = "";
        Zone testZone = new Zone(testZoneID, testLotName);
        String input = String.format("3\n%s\n0\n", testLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        zoneMenuImpl.callInterface();
        verify(parkingLotService, times(0)).getParkingLot(testLotName);
        verify(zoneService, times(0)).getZone(testZoneID, testLotName);
        verify(zoneService, times(0)).deleteZone(testZone);
    }
}