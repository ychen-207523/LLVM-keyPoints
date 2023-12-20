package org.parking.menus;

import org.parking.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class InformationProcessMenuTest {

    private InformationProcessMenu infoProcMenuImpl;
    @Mock
    private CUDMenu cudMenu;
    @Mock
    private CitationsService citationsService;
    @Mock
    private SpaceService spaceService;
    @Mock
    private ZoneService zoneService;


    private final InputStream systemIn = System.in;


    @BeforeEach
    void init() {
        openMocks(this);
        infoProcMenuImpl = new InformationProcessMenuImpl(cudMenu, citationsService, spaceService, zoneService);

    }

    @AfterEach
    void restoreSystem() {
        System.setIn(systemIn);
    }

    @Test
    void callInterface_testCUDMenu() {
        String userInput = "1\n5\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        doNothing().when(cudMenu).callInterface();
        infoProcMenuImpl.callInterface();
        verify(cudMenu, times(1)).callInterface();
    }

    @Test
    void callInterface_testInvalidInput() {
        String userInput = "something\n9\n5\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        infoProcMenuImpl.callInterface();
        verify(cudMenu, times(0)).callInterface();
    }

    @Test
    void callInterface_assignZoneToParkingLot_success() {
        String curLot = "testLot", zone = "A", newLot = "newLot";
        String userInput = String.format("2\n%s\n%s\n%s\n5\n", curLot, zone, newLot);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        when(zoneService.assignZoneToParkingLot(eq(curLot), eq(zone), eq(newLot))).thenReturn(true);

        infoProcMenuImpl.callInterface();
        verify(zoneService, times(1)).assignZoneToParkingLot(eq(curLot), eq(zone), eq(newLot));
    }

    @Test
    void callInterface_assignZoneToParkingLot_error() {
        String curLot = "testLot", zone = "A", newLot = "newLot";
        String userInput = String.format("2\n%s\n%s\n%s\n5\n", curLot, zone, newLot);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        when(zoneService.assignZoneToParkingLot(eq(curLot), eq(zone), eq(newLot))).thenReturn(false);

        infoProcMenuImpl.callInterface();
        verify(zoneService, times(1)).assignZoneToParkingLot(eq(curLot), eq(zone), eq(newLot));
    }

    @Test
    void callInterface_assignZoneToParkingLot_invalidNewLot() {
        String curLot = "testLot", zone = "A", newLot = "";
        String userInput = String.format("2\n%s\n%s\n%s\n5\n", curLot, zone, newLot);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        infoProcMenuImpl.callInterface();
        verify(zoneService, times(0)).assignZoneToParkingLot(eq(curLot), eq(zone), anyString());
    }

    @Test
    void callInterface_assignZoneToParkingLot_invalidZone() {
        String curLot = "testLot", zone = "";
        String userInput = String.format("2\n%s\n%s\n5\n", curLot, zone);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        when(zoneService.assignZoneToParkingLot(anyString(), anyString(), anyString())).thenReturn(false);

        infoProcMenuImpl.callInterface();
        verify(zoneService, times(0)).assignZoneToParkingLot(eq(curLot), eq(zone), anyString());
    }

    @Test
    void callInterface_assignZoneToParkingLot_invalidCurrentLot() {
        String curLot = "";
        String userInput = String.format("2\n%s\n5\n", curLot);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        when(zoneService.assignZoneToParkingLot(anyString(), anyString(), anyString())).thenReturn(false);

        infoProcMenuImpl.callInterface();
        verify(zoneService, times(0)).assignZoneToParkingLot(eq(curLot), anyString(), anyString());
    }

    @Test
    void callInterface_assignTypeToSpace_success() {
        int number = 1, type = 1;
        String lotName = "testLotName", zoneID = "A", typeInput = "electric";
        String userInput = String.format("3\n%s\n%s\n%s\n%s\n5\n", lotName, zoneID, number, type);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        when(spaceService.assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(typeInput))).thenReturn(true);
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(1)).assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(typeInput));
    }

    @Test
    void callInterface_assignTypeToSpace_error() {
        int number = 1, type = 1;
        String lotName = "testLotName", zoneID = "A", typeInput = "electric";
        String userInput = String.format("3\n%s\n%s\n%s\n%s\n5\n", lotName, zoneID, number, type);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        when(spaceService.assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(typeInput))).thenReturn(false);
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(1)).assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(typeInput));
    }

    @Test
    void callInterface_assignTypeToSpace_emptyType() {
        int number = 1;
        String lotName = "testLotName", zoneID = "A", typeInput = "regular";
        String userInput = String.format("3\n%s\n%s\n%s\n \n5\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        when(spaceService.assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(typeInput))).thenReturn(true);
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(1)).assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(typeInput));
    }

    @Test
    void callInterface_assignTypeToSpace_defaultType() {
        int number = 1, type = 0;
        String lotName = "testLotName", zoneID = "A", typeInput = "regular";
        String userInput = String.format("3\n%s\n%s\n%s\n%s\n5\n", lotName, zoneID, number, type);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        when(spaceService.assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(typeInput))).thenReturn(true);
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(1)).assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(typeInput));
    }

    @Test
    void callInterface_assignTypeToSpace_unableToParseType() {
        int number = 1;
        String lotName = "testLotName", zoneID = "A", typeInput = "regular";
        String userInput = String.format("3\n%s\n%s\n%s\n%s\n5\n", lotName, zoneID, number, typeInput);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        infoProcMenuImpl.callInterface();
        verify(spaceService, times(0)).assignTypeOfASpace(anyString(), anyString(), anyInt(), anyString());
    }

    @Test
    void callInterface_assignTypeToSpace_invalidSpaceNumber() {
        int number = 0;
        String lotName = "testLotName", zoneID = "A";
        String userInput = String.format("3\n%s\n%s\n%s\n5\n", lotName, zoneID, number);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        infoProcMenuImpl.callInterface();
        verify(spaceService, times(0)).assignTypeOfASpace(anyString(), anyString(), anyInt(), anyString());
    }

    @Test
    void callInterface_assignTypeToSpace_invalidSpaceNumberInput() {
        String lotName = "testLotName", zoneID = "A";
        String userInput = String.format("3\n%s\n%s\n%s\n5\n", lotName, zoneID, "");
        // Empty String
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(0)).assignTypeOfASpace(anyString(), anyString(), anyInt(), anyString());
        // NumberFormatException
        userInput = String.format("3\n%s\n%s\n%s\n5\n", lotName, zoneID, "something");
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(0)).assignTypeOfASpace(anyString(), anyString(), anyInt(), anyString());
    }

    @Test
    void callInterface_assignTypeToSpace_invalidZoneID() {
        String lotName = "testLotName", zoneID = "";
        String userInput = String.format("3\n%s\n%s\n5\n", lotName, zoneID);
        // Empty String
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(0)).assignTypeOfASpace(anyString(), anyString(), anyInt(), anyString());
    }

    @Test
    void callInterface_assignTypeToSpace_invalidLotName() {
        String lotName = "";
        String userInput = String.format("3\n%s\n5\n", lotName);
        // Empty String
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(0)).assignTypeOfASpace(anyString(), anyString(), anyInt(), anyString());
    }

    @Test
    void callInterface_assignTypeToSpace_testAllTypes() {
        int number = 1, type = 1;
        String lotName = "testLotName", zoneID = "A";
        String type1 = "electric";
        String type2 = "handicap";
        String type3 = "compact car";
        String type4 = "regular";
        String userInputTest1 = String.format("3\n%s\n%s\n%s\n%s\n5\n", lotName, zoneID, number, type);
        System.setIn(new ByteArrayInputStream(userInputTest1.getBytes()));
        when(spaceService.assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(type1))).thenReturn(true);
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(1)).assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(type1));

        ++type;
        String userInputTest2 = String.format("3\n%s\n%s\n%s\n%s\n5\n", lotName, zoneID, number, type);
        System.setIn(new ByteArrayInputStream(userInputTest2.getBytes()));
        when(spaceService.assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(type2))).thenReturn(true);
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(1)).assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(type2));

        ++type;
        String userInputTest3 = String.format("3\n%s\n%s\n%s\n%s\n5\n", lotName, zoneID, number, type);
        System.setIn(new ByteArrayInputStream(userInputTest3.getBytes()));
        when(spaceService.assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(type3))).thenReturn(true);
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(1)).assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(type3));

        ++type;
        String userInputTest4 = String.format("3\n%s\n%s\n%s\n%s\n5\n", lotName, zoneID, number, type);
        System.setIn(new ByteArrayInputStream(userInputTest4.getBytes()));
        when(spaceService.assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(type4))).thenReturn(true);
        infoProcMenuImpl.callInterface();
        verify(spaceService, times(1)).assignTypeOfASpace(eq(lotName),eq(zoneID),eq(number),eq(type4));

    }

    @Test
    void callInterface_appealCitation_success() {
        int number = 1;
        String userInput = String.format("4\n%s\n%s\n5\n", number, "y");
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        when(citationsService.appealCitation(eq(number))).thenReturn(true);

        infoProcMenuImpl.callInterface();
        verify(citationsService, times(1)).appealCitation(eq(number));
    }
    @Test
    void callInterface_appealCitation_cancel() {
        int number = 1;
        String userInput = String.format("4\n%s\n%s\n5\n", number, "n");
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        infoProcMenuImpl.callInterface();
        verify(citationsService, times(0)).appealCitation(eq(number));
    }
    @Test
    void callInterface_appealCitation_doesNotExist() {
        int number = 1;
        String userInput = String.format("4\n%s\n%s\n5\n", number, "y");
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        when(citationsService.appealCitation(eq(number))).thenReturn(false);

        infoProcMenuImpl.callInterface();
        verify(citationsService, times(1)).appealCitation(eq(number));
    }
    @Test
    void callInterface_appealCitation_invalidNumber() {
        int number = 0;
        String userInput = String.format("4\n%s\n5\n", number);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        // Number is less than 1.
        infoProcMenuImpl.callInterface();
        verify(citationsService, times(0)).appealCitation(eq(number));

        // NumberFormatException
        userInput = String.format("4\n%s\n5\n", "number");
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        infoProcMenuImpl.callInterface();
        verify(citationsService, times(0)).appealCitation(eq(number));
    }

}
