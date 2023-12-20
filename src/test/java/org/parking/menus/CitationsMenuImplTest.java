package org.parking.menus;

import org.parking.model.Citation;
import org.parking.model.Permit;
import org.parking.model.Vehicle;
import org.parking.model.Zone;
import org.parking.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CitationsMenuImplTest {

    private CitationsMenuImpl citationsMenuImpl;
    @Mock
    private CitationsService citationsService;
    @Mock
    private VehicleService vehicleService;
    @Mock
    private PermitsService permitsService;
    @Mock
    private ZoneService zoneService;

    private final Citation mockCitation =  new Citation(0, new Vehicle("test-license", "test-model", "test-color", "", 0), "test-lotName", "test-category", 30.00, "paid", Date.valueOf("2024-01-01"), Time.valueOf("10:10:10"));

    private final Permit mockPermit = new Permit("test-permit", "", "test-zone", "", "", "", Date.valueOf("2023-12-01"), Date.valueOf("2024-01-01"), Time.valueOf("10:10:10"));

    private final Zone mockZone = new Zone("test-zone", "test-lotName");
    private final InputStream systemIn = System.in;


    @BeforeEach
    void init() {
        openMocks( this );
        citationsMenuImpl = new CitationsMenuImpl(citationsService, vehicleService, permitsService, zoneService);
    }

    @AfterEach
    void restoreInput() {
        System.setIn(systemIn);
    }

    @Test
    void callInterface_createCitationNoExistingVehicleFirstLicenseEmpty() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n\ntest-license\ntest-model\ntest-color\ntest-lotName\ntest-category\n30.00\ndue\nDUE\n2024-01-01\n10:10:10\n8\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("test-license")).thenReturn(null);
        citationsMenuImpl.callInterface();
        verify(citationsService, times(1)).createCitation(any(), any());
    }

    @Test
    void callInterface_createCitationNoExistingVehicleInvalidFirstFeeDateAndTime() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\ntest-license\ntest-model\ntest-color\ntest-lotName\ntest-category\nA\n30.00\nDUE\nA\n2024-01-01\nA\n10:10:10\n8\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("test-license")).thenReturn(null);
        citationsMenuImpl.callInterface();
        verify(citationsService, times(1)).createCitation(any(), any());
    }

    @Test
    void callInterface_createCitationSQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\ntest-license\ntest-model\ntest-color\ntest-lotName\ntest-category\n30.00\nPAID\n2024-01-01\n10:10:10\n8\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("test-license")).thenThrow(new SQLException());
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_createCitationExistingVehicleInvalidPermit() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\ntest-license\ntest-model\ntest-color\ntest-citationLotName\ntest-category\n30.00\nPAID\n2024-01-01\n10:10:11\n8\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("test-license")).thenReturn(new Vehicle("test-license", "test-model", "test-color", "test-manf", 2023));
        when(permitsService.getPermitPerCarLicense("test-license")).thenReturn(Collections.singletonList(mockPermit));
        when(zoneService.getZonesById("test-zone")).thenReturn(Collections.singletonList(mockZone));
        citationsMenuImpl.callInterface();
        verify(citationsService, times(1)).createCitation(any(), any());
    }

    @Test
    void callInterface_updateCitation() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\n2\ntest-license\ntest-lotName\ntest-category\n30.00\npaid\nPAID\n2024-01-01\n10:10:10\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getByNumber(2)).thenReturn(mockCitation);
        citationsMenuImpl.callInterface();
        verify(citationsService, times(1)).updateCitation(any());
    }

    @Test
    void callInterface_updateCitationInvalidFirstFeeDateAndTime() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\n2\ntest-license\ntest-lotName\ntest-category\nA\n30.00\nPAID\nA\n2024-01-01\nA\n10:10:10\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getByNumber(2)).thenReturn(mockCitation);
        citationsMenuImpl.callInterface();
        verify(citationsService, times(1)).updateCitation(any());
    }

    @Test
    void callInterface_updateCitationKeepDefaultValues() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\n2\n\n\n\n\n\n\n\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getByNumber(2)).thenReturn(mockCitation);
        citationsMenuImpl.callInterface();
        verify(citationsService, times(1)).updateCitation(any());
    }

    @Test
    void callInterface_updateCitationFirstSQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\n2\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getByNumber(2)).thenThrow(new SQLException());
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateCitationSecondSQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\n2\ntest-license\ntest-lotName\ntest-category\n30.00\nAPPEALED\n2024-01-01\n10:10:10\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getByNumber(2)).thenReturn(mockCitation);
        doThrow(new SQLException()).when(citationsService).updateCitation(any());
        citationsMenuImpl.callInterface();
        verify(citationsService, times(1)).updateCitation(any());
    }

    @Test
    void callInterface_updateCitationNoCitationFound() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\n2\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getByNumber(2)).thenReturn(null);
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateCitationInputMismatch() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nA\n8\n".getBytes());
        System.setIn(in);
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_getAllCitations() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getAll()).thenReturn(Collections.singleton(mockCitation));
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_getAllCitationsNoneFound() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getAll()).thenReturn(null);
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_getAllCitationsSQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getAll()).thenThrow(new SQLException());
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_getByNumber() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("4\n2\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getByNumber(2)).thenReturn(mockCitation);
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_getByNumberNoCitationFound() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("4\n2\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getByNumber(2)).thenReturn(null);
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_getByNumberInputMismatch() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("4\nA\n8\n".getBytes());
        System.setIn(in);
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_getByNumberSQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("4\n2\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.getByNumber(2)).thenThrow(new SQLException());
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_inputMismatchThenExit() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("A\n8\n".getBytes());
        System.setIn(in);
        citationsMenuImpl.callInterface();
    }

    @Test
    void callInterface_deleteByNumberInputMismatch() {
        ByteArrayInputStream in = new ByteArrayInputStream("5\nA\n8\n".getBytes());
        System.setIn(in);
        citationsMenuImpl.callInterface();
    }

    @Test
    void deleteCitationByNumberOption_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
        System.setIn(in);
        Scanner mockScan = new Scanner(in);
        citationsMenuImpl.deleteCitationByNumberOption(mockScan);
        verify(citationsService, times(1)).deleteCitationByNumber(1);
    }

    @Test
    void deleteCitationByNumberOption_inputMismatchErr() {
        ByteArrayInputStream in = new ByteArrayInputStream("A".getBytes());
        System.setIn(in);
        Scanner mockScan = new Scanner(in);
        assertDoesNotThrow(() -> citationsMenuImpl.deleteCitationByNumberOption(mockScan));
    }

    @Test
    void deleteCitationByNumberOption_sqlException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
        System.setIn(in);
        Scanner mockScan = new Scanner(in);
        doThrow(new SQLException()).when(citationsService).deleteCitationByNumber(1);
        assertDoesNotThrow(() -> citationsMenuImpl.deleteCitationByNumberOption(mockScan));
    }

    @Test
    void payCitationByNumberOption_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("6\n1\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.payCitation(1)).thenReturn(true);
        citationsMenuImpl.callInterface();
    }

    @Test
    void payCitationByNumberOption_serviceError() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("6\n1\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.payCitation(1)).thenReturn(false);
        citationsMenuImpl.callInterface();
    }

    @Test
    void payCitationByNumberOption_InputMismatch() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("6\nA\n8\n".getBytes());
        System.setIn(in);
        citationsMenuImpl.callInterface();
    }

    @Test
    void appealCitationByNumberOption_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\n1\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.appealCitation(1)).thenReturn(true);
        citationsMenuImpl.callInterface();
    }

    @Test
    void appealCitationByNumberOption_serviceError() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\n1\n8\n".getBytes());
        System.setIn(in);
        when(citationsService.appealCitation(1)).thenReturn(false);
        citationsMenuImpl.callInterface();
    }

    @Test
    void appealCitationByNumberOption_InputMismatch() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\nA\n8\n".getBytes());
        System.setIn(in);
        citationsMenuImpl.callInterface();
    }


}