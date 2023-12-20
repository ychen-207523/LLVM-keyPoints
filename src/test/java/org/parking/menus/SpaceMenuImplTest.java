package org.parking.menus;

import org.parking.model.Space;
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

class SpaceMenuImplTest {

    private SpaceMenuImpl spaceMenuImpl;

    @Mock
    private SpaceService spaceService;

    private final InputStream systemIn = System.in;


    @BeforeEach
    void init() {
        openMocks(this);
        spaceMenuImpl = new SpaceMenuImpl(spaceService);
    }

    @AfterEach
    void restoreInput() {
        System.setIn(systemIn);
    }

    @Test
    void callInterface_invalidInput() {
        String input = "something\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        spaceMenuImpl.callInterface();
    }


    @Test
    void callInterface_enterSpace_success() {
        int number = 1, typeInput = 1, statusInput = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        when(spaceService.createSpace(any(Space.class))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_queryFailed() {
        int number = 1, typeInput = 1, statusInput = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        when(spaceService.createSpace(any(Space.class))).thenReturn(false);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_spaceAlreadyExists() {
        int number = 1, typeInput = 1, statusInput = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(mock(Space.class));
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(0)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_statusInUse() {
        int number = 1, typeInput = 1, statusInput = 2;
        boolean status = false;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));


        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        when(spaceService.createSpace(any(Space.class))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_invalidStatusInput() {
        int number = 1, typeInput = 1, statusInput = 11;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(0)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_handicapType() {
        int number = 1, typeInput = 2, statusInput = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "handicap";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        when(spaceService.createSpace(any(Space.class))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_compactCarType() {
        int number = 1, typeInput = 3, statusInput = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "compact car";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        when(spaceService.createSpace(any(Space.class))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_regularType() {
        int number = 1, typeInput = 4, statusInput = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "regular";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        when(spaceService.createSpace(any(Space.class))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_emptyInput() {
        int number = 1, statusInput = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "regular", typeInput = "";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        when(spaceService.createSpace(any(Space.class))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_defaultType() {
        int number = 1, typeInput = 10, statusInput = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "regular";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, typeInput, statusInput);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        when(spaceService.createSpace(any(Space.class))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_invalidTypeInput() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "regular";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("1\n%s\n%s\n%s\n%s\n0\n", lotName, zoneID, number, type);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(0)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_invalidNumberValue() {
        int number = 0;
        String zoneID = "A", lotName = "testLotName";
        String input = String.format("1\n%s\n%s\n%s\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(0)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_invalidNumberInput() {
        String number = "invalid";
        String zoneID = "A", lotName = "testLotName";
        String input = String.format("1\n%s\n%s\n%s\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(anyInt(), anyString(), anyString());
        verify(spaceService, times(0)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_invalidZoneInput() {
        String zoneID = "", lotName = "testLotName";
        String input = String.format("1\n%s\n%s\n0\n", lotName, zoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(anyInt(), anyString(), anyString());
        verify(spaceService, times(0)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_enterSpace_invalidParkingLotInput() {
        String lotName = "";
        String input = String.format("1\n%s\n0\n", lotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(anyInt(), anyString(), anyString());
        verify(spaceService, times(0)).createSpace(any(Space.class));
    }

    @Test
    void callInterface_updateSpace_success() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("2\n%s\n%s\n%s\ns\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(test);
        when(spaceService.updateSpace(eq(test), any(Space.class))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).updateSpace(eq(test), any(Space.class));
    }

    @Test
    void callInterface_updateSpace_queryFailed() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("2\n%s\n%s\n%s\ns\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(test);
        when(spaceService.updateSpace(eq(test), any(Space.class))).thenReturn(false);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).updateSpace(eq(test), any(Space.class));
    }

    @Test
    void callInterface_updateSpace_cancel() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("2\n%s\n%s\n%s\nc\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(test);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(0)).updateSpace(eq(test), any(Space.class));
    }

    @Test
    void callInterface_updateSpace_invalidInputThenCancel() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("2\n%s\n%s\n%s\nsomething\nc\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(test);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(0)).updateSpace(eq(test), any(Space.class));
    }

    @Test
    void callInterface_updateSpace_doesNotExist() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("2\n%s\n%s\n%s\ns\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(0)).updateSpace(eq(test), any(Space.class));
    }

    @Test
    void callInterface_updateSpace_updateAllFieldsThenSubmit() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String retrieveSpace = String.format("%s\n%s\n%s\n", lotName, zoneID, number);
        String updateNumber = "1\n2\n";
        String updateStatus = "2\n2\n";
        String updateType = "3\n2\n";
        String updateZoneID = "4\nB\n";
        String updateLotName = "5\nnewLotName\n";
        String input = String.format("2\n%s%s%s%s%s%ss\n0\n", retrieveSpace, updateNumber,
                updateStatus,
                updateType,
                updateZoneID,
                updateLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(test);
        when(spaceService.updateSpace(eq(test), any(Space.class))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).updateSpace(eq(test), any(Space.class));
    }

    @Test
    void callInterface_updateSpace_updateAllFieldsWithInvalidThenSubmit() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String retrieveSpace = String.format("%s\n%s\n%s\n", lotName, zoneID, number);
        String updateNumber = "1\n\n";
        String updateStatus = "2\n\n";
        String updateType = "3\ninvalid\n";
        String updateZoneID = "4\n\n";
        String updateLotName = "5\n\n";
        String input = String.format("2\n%s%s%s%s%s%ss\n0\n", retrieveSpace, updateNumber,
                updateStatus,
                updateType,
                updateZoneID,
                updateLotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(test);
        when(spaceService.updateSpace(eq(test), eq(test))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).updateSpace(eq(test), any(Space.class));
    }

    @Test
    void callInterface_updateSpace_invalidNumber() {
        int number = 0;
        String zoneID = "A", lotName = "testLotName";
        String retrieveSpace = String.format("%s\n%s\n%s\n", lotName, zoneID, number);
        String input = String.format("2\n%s0\n", retrieveSpace);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(anyInt(), anyString(), anyString());
        verify(spaceService, times(0)).updateSpace(any(Space.class), any(Space.class));
    }

    @Test
    void callInterface_updateSpace_invalidZoneID() {
        String zoneID = "", lotName = "testLotName";
        String retrieveSpace = String.format("%s\n%s\n", lotName, zoneID);
        String input = String.format("2\n%s0\n", retrieveSpace);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(anyInt(), anyString(), anyString());
        verify(spaceService, times(0)).updateSpace(any(Space.class), any(Space.class));
    }

    @Test
    void callInterface_updateSpace_invalidParkingLot() {
        String lotName = "";
        String retrieveSpace = String.format("%s\n", lotName);
        String input = String.format("2\n%s0\n", retrieveSpace);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(anyInt(), anyString(), anyString());
        verify(spaceService, times(0)).updateSpace(any(Space.class), any(Space.class));
    }


    @Test
    void callInterface_deleteSpace_success() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("3\n%s\n%s\n%s\nyes\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(test);
        when(spaceService.deleteSpace(eq(test))).thenReturn(true);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).deleteSpace(eq(test));
    }

    @Test
    void callInterface_deleteSpace_queryFailed() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("3\n%s\n%s\n%s\nyes\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(test);
        when(spaceService.deleteSpace(eq(test))).thenReturn(false);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(1)).deleteSpace(eq(test));
    }

    @Test
    void callInterface_deleteSpace_abort() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("3\n%s\n%s\n%s\nno\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(test);
        when(spaceService.deleteSpace(eq(test))).thenReturn(false);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(0)).deleteSpace(any(Space.class));
    }

    @Test
    void callInterface_deleteSpace_doesNotExist() {
        int number = 1;
        boolean status = true;
        String zoneID = "A", lotName = "testLotName", type = "electric";
        Space test = new Space(number, type, status, zoneID, lotName);
        String input = String.format("3\n%s\n%s\n%s\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(spaceService.getSpace(number, zoneID, lotName)).thenReturn(null);
        spaceMenuImpl.callInterface();
        verify(spaceService, times(1)).getSpace(eq(number), eq(zoneID), eq(lotName));
        verify(spaceService, times(0)).deleteSpace(any(Space.class));
    }

    @Test
    void callInterface_deleteSpace_invalidNumber() {
        int number = 0;
        String zoneID = "A", lotName = "testLotName";
        String input = String.format("3\n%s\n%s\n%s\n0\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(anyInt(), anyString(), anyString());
        verify(spaceService, times(0)).deleteSpace(any(Space.class));
    }

    @Test
    void callInterface_deleteSpace_invalidZoneID() {
        String zoneID = "", lotName = "testLotName";
        String input = String.format("3\n%s\n%s\n0\n", lotName, zoneID);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(anyInt(), anyString(), anyString());
        verify(spaceService, times(0)).deleteSpace(any(Space.class));
    }

    @Test
    void callInterface_deleteSpace_invalidParkingLot() {
        String lotName = "";
        String input = String.format("3\n%s\n0\n", lotName);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        spaceMenuImpl.callInterface();
        verify(spaceService, times(0)).getSpace(anyInt(), anyString(), anyString());
        verify(spaceService, times(0)).deleteSpace(any(Space.class));
    }
}