package org.parking.menus;

import org.parking.model.ParkingLot;
import org.parking.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ParkingLotMenuImplTest {

    private ParkingLotMenuImpl parkingLotMenuImpl;

    @Mock
    private ParkingLotService parkingLotService;

    private final InputStream systemIn = System.in;

    @BeforeEach
    void init() {
        openMocks(this);
        parkingLotMenuImpl = new ParkingLotMenuImpl(parkingLotService);
    }

    @AfterEach
    void restoreInput() {
        System.setIn(systemIn);
    }

    @Test
    void callInterface_enterParkingLot_success() {
        String name = "testLotName";
        String address = "testAddress";
        String input = String.format("1\n%s\n%s\n0\n", name, address);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(null);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).createParkingLot(any(ParkingLot.class));
    }

    @Test
    void callInterface_enterParkingLot_error() {
        String name = "testLotName";
        String address = "testAddress";
        String input = String.format("1\n%s\n%s\n0\n", name, address);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(null);
        when(parkingLotService.createParkingLot(any(ParkingLot.class))).thenReturn(false);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).createParkingLot(any(ParkingLot.class));
    }

    @Test
    void callInterface_enterParkingLot_alreadyExists() {
        String name = "testLotName";
        String address = "testAddress";
        String input = String.format("1\n%s\n0\n", name);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(new ParkingLot(name, address));
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(0)).createParkingLot(any(ParkingLot.class));
    }

    @Test
    void callInterface_enterParkingLot_emptyAddress() {
        String name = "testLotName";
        String address = "";
        String input = String.format("1\n%s\n%s\n0\n", name, address);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(null);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(0)).createParkingLot(any(ParkingLot.class));
    }

    @Test
    void callInterface_enterParkingLot_emptyName() {
        String name = "";
        String address = "testAddress";
        String input = String.format("1\n%s\n%s\n0\n", name, address);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(0)).createParkingLot(any(ParkingLot.class));
    }

    @Test
    void callInterface_updateParkingLot_success() {
        String name = "testLotName", newName = "newLotName";
        String address = "testAddress", newAddress = "newAddress";
        ParkingLot parkingLot = new ParkingLot(name, address);
        String input = String.format("2\n%s\n1\n%s\n2\n%s\ns\n0\n", name, newName, newAddress);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(parkingLot);
        when(parkingLotService.updateParkingLot(name, new ParkingLot(newName, newAddress))).thenReturn(true);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).updateParkingLot(anyString(), any(ParkingLot.class));
    }

    @Test
    void callInterface_updateParkingLot_cancel() {
        String name = "testLotName", newName = "newLotName";
        String address = "testAddress", newAddress = "newAddress";
        ParkingLot parkingLot = new ParkingLot(name, address);
        String input = String.format("2\n%s\n1\n%s\n2\n%s\nc\n0\n", name, newName, newAddress);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(parkingLot);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(anyString());
        verify(parkingLotService, times(0)).updateParkingLot(anyString(), any(ParkingLot.class));
    }

    @Test
    void callInterface_updateParkingLot_invalidInputThenSubmit() {
        String name = "testLotName", newName = "";
        String address = "testAddress", newAddress = "";
        ParkingLot parkingLot = new ParkingLot(name, address);
        String input = String.format("2\n%s\n1\n%s\n2\n%s\ns\n0\n", name, newName, newAddress);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(parkingLot);
        when(parkingLotService.updateParkingLot(name, parkingLot)).thenReturn(true);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(anyString());
        verify(parkingLotService, times(1)).updateParkingLot(anyString(), any(ParkingLot.class));
    }

    @Test
    void callInterface_updateParkingLot_queryFailed() {
        String name = "testLotName", newName = "newLotName";
        String address = "testAddress", newAddress = "newAddress";
        ParkingLot parkingLot = new ParkingLot(name, address);
        String input = String.format("2\n%s\n1\n%s\n2\n%s\ns\n0\n", name, newName, newAddress);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(parkingLot);
        when(parkingLotService.updateParkingLot(name, parkingLot)).thenReturn(false);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(anyString());
        verify(parkingLotService, times(1)).updateParkingLot(anyString(), any(ParkingLot.class));
    }

    @Test
    void callInterface_updateParkingLot_invalidLotName() {
        String name = "";
        String input = String.format("2\n%s\n0\n", name);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(0)).getParkingLot(anyString());
        verify(parkingLotService, times(0)).updateParkingLot(anyString(), any(ParkingLot.class));
    }

    @Test
    void callInterface_deleteParkingLot_success() {
        String name = "testLotName";
        String input = String.format("3\n%s\ny\n0\n", name);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(mock(ParkingLot.class));
        when(parkingLotService.deleteParkingLot(name)).thenReturn(true);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).deleteParkingLot(name);
    }

    @Test
    void callInterface_deleteParkingLot_queryFailed() {
        String name = "testLotName";
        String input = String.format("3\n%s\ny\n0\n", name);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(mock(ParkingLot.class));
        when(parkingLotService.deleteParkingLot(name)).thenReturn(false);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).deleteParkingLot(name);
    }

    @Test
    void callInterface_deleteParkingLot_cancel() {
        String name = "testLotName";
        String input = String.format("3\n%s\nn\n0\n", name);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(mock(ParkingLot.class));
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(name);
        verify(parkingLotService, times(0)).deleteParkingLot(name);
    }

    @Test
    void callInterface_deleteParkingLot_invalidOptionThenDelete() {
        String name = "testLotName";
        String input = String.format("3\n%s\nsomething\ny\n0\n", name);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(mock(ParkingLot.class));
        when(parkingLotService.deleteParkingLot(name)).thenReturn(true);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(name);
        verify(parkingLotService, times(1)).deleteParkingLot(name);
    }

    @Test
    void callInterface_deleteParkingLot_doesNotExist() {
        String name = "testLotName";
        String input = String.format("3\n%s\n0\n", name);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(parkingLotService.getParkingLot(name)).thenReturn(null);
        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(1)).getParkingLot(name);
        verify(parkingLotService, times(0)).deleteParkingLot(name);
    }

    @Test
    void callInterface_deleteParkingLot_emptyName() {
        String name = "";
        String input = String.format("3\n%s\nsomething\ny\n0\n", name);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        parkingLotMenuImpl.callInterface();
        verify(parkingLotService, times(0)).getParkingLot(name);
        verify(parkingLotService, times(0)).deleteParkingLot(name);
    }

    @Test
    void callInterface_invalidInput() {
        String input = String.format("something\n0\n");
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        parkingLotMenuImpl.callInterface();
    }
}