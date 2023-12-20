package org.parking.menus;

import org.parking.model.*;
import org.parking.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class ReportsMenuImplTest {

    private ReportsMenuImpl reportsMenu;

    @Mock
    private ReportsService reportsService;

    @Mock
    private CitationsService citationsService;

    @Mock
    private ParkingLotService parkingLotService;

    @Mock
    private ZoneService zoneService;

    private final Citation mockCitation =  new Citation(0, new Vehicle("test-license", "test-model", "test-color", "", 0), "test-lotName", "test-category", 30.00, "paid", Date.valueOf("2024-01-01"), Time.valueOf("10:10:10"));
    private final ParkingLot mockLot = new ParkingLot("Test Lot", "123 Address Way");
    private final Zone mockZone = new Zone("test-zone", "test-lotName");
    private final Permit mockPermit = new Permit("test-permit", "", "test-zone", "", "", "", Date.valueOf("2023-12-01"), Date.valueOf("2024-01-01"), Time.valueOf("10:10:10"));

    @BeforeEach
    void init() {
        openMocks(this);
        reportsMenu = new ReportsMenuImpl(reportsService, citationsService, parkingLotService, zoneService);
    }

    @Test
    void callInterface_citationList_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n0\n".getBytes());
        System.setIn(in);
        Collection<Citation> citations = new ArrayList<>();
        citations.add(mockCitation);
        citations.add(mockCitation);

        when(citationsService.getAll()).thenReturn(citations);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_citationList_noCitations() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n0\n".getBytes());
        System.setIn(in);
        when(citationsService.getAll()).thenReturn(null);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_citationList_sqlException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n0\n".getBytes());
        System.setIn(in);
        when(citationsService.getAll()).thenThrow(new SQLException());

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_citationReport_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nLotName\n2020-01-01\n2023-01-01\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.generateCitationReport(mockLot.getName(),Date.valueOf("2020-01-01"),
                Date.valueOf("2023-01-01"))).thenReturn(5);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_citationReport_noLotFound() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nNoneFound\nLotName\n2020-01-01\n2023-01-01\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(parkingLotService.getParkingLot("NoneFound")).thenReturn(null);
        when(reportsService.generateCitationReport(mockLot.getName(),Date.valueOf("2020-01-01"),
                Date.valueOf("2023-01-01"))).thenReturn(5);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_citationReport_wrongStartDate() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nLotName\nasdf\n2020-01-01\n2023-01-01\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.generateCitationReport(mockLot.getName(),Date.valueOf("2020-01-01"),
                Date.valueOf("2023-01-01"))).thenReturn(5);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_citationReport_earlyEndDate() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nLotName\n2020-01-01\n2019-01-01\n2023-01-01\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.generateCitationReport(mockLot.getName(),Date.valueOf("2020-01-01"),
                Date.valueOf("2023-01-01"))).thenReturn(5);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_citationReport_wrongEndDate() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nLotName\n2020-01-01\nasdf\n2023-01-01\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.generateCitationReport(mockLot.getName(),Date.valueOf("2020-01-01"),
                Date.valueOf("2023-01-01"))).thenReturn(5);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_citationReport_sqlException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nLotName\n2020-01-01\n2023-01-01\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.generateCitationReport(mockLot.getName(),Date.valueOf("2020-01-01"),
                Date.valueOf("2023-01-01"))).thenThrow(new SQLException());

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_zoneList_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\n0\n".getBytes());
        System.setIn(in);
        Collection<Zone> zones = new ArrayList<>();
        zones.add(mockZone);
        zones.add(mockZone);

        when(reportsService.generateZoneReport()).thenReturn(zones);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_zoneList_noZones() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\n0\n".getBytes());
        System.setIn(in);


        reportsMenu.callInterface();
    }

    @Test
    void callInterface_zoneList_SQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\n0\n".getBytes());
        System.setIn(in);

        when(reportsService.generateZoneReport()).thenThrow(new SQLException());

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_countVehicleViolation_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("4\n0\n".getBytes());
        System.setIn(in);

        when(reportsService.generateViolatedCarNumber()).thenReturn(5);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_countVehicleViolation_SQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("4\n0\n".getBytes());
        System.setIn(in);

        when(reportsService.generateViolatedCarNumber()).thenThrow(new SQLException());

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_countEmployeePermitsPerZone_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\nA\n0\n".getBytes());
        System.setIn(in);

        Collection<Zone> zones = new ArrayList<>();
        zones.add(mockZone);

        when(zoneService.getZonesById("A")).thenReturn(zones);
        when(reportsService.generateExployeesofZone("A")).thenReturn(5);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_countEmployeePermitsPerZone_invalidZone() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\nasdf!@\nA\n0\n".getBytes());
        System.setIn(in);

        Collection<Zone> zones = new ArrayList<>();
        zones.add(mockZone);

        when(zoneService.getZonesById("A")).thenReturn(zones);
        when(reportsService.generateExployeesofZone("A")).thenReturn(5);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_countEmployeePermitsPerZone_noZoneFound() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\nA\nA\n0\n".getBytes());
        System.setIn(in);

        Collection<Zone> zones = new ArrayList<>();
        zones.add(mockZone);

        when(zoneService.getZonesById("A")).thenReturn(new ArrayList<>()).thenReturn(zones);
        when(reportsService.generateExployeesofZone("A")).thenReturn(5);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_countEmployeePermitsPerZone_SQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\nA\n0\n".getBytes());
        System.setIn(in);

        Collection<Zone> zones = new ArrayList<>();
        zones.add(mockZone);

        when(zoneService.getZonesById("A")).thenReturn(zones);
        when(reportsService.generateExployeesofZone("A")).thenThrow(new SQLException());

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_permitReportMenu_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("6\nDriverID\n0\n".getBytes());
        System.setIn(in);

        Collection<Permit> permits = new ArrayList<>();
        permits.add(mockPermit);
        permits.add(mockPermit);


        when(reportsService.getPermitForDriver("DriverID")).thenReturn(permits);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_permitReportMenu_invalidIdInput() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("6\n123!~!\nDriverID\n0\n".getBytes());
        System.setIn(in);

        Collection<Permit> permits = new ArrayList<>();
        permits.add(mockPermit);
        permits.add(mockPermit);


        when(reportsService.getPermitForDriver("DriverID")).thenReturn(permits);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_permitReportMenu_SQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("6\nDriverID\n0\n".getBytes());
        System.setIn(in);

        Collection<Permit> permits = new ArrayList<>();
        permits.add(mockPermit);
        permits.add(mockPermit);


        when(reportsService.getPermitForDriver("DriverID")).thenThrow(new SQLException());

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_reportAvailableSpaceInLot_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\nLotName\nelectric\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.getAvailableSpaceinParkingLot(mockLot.getName(),"electric")).thenReturn(7);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_reportAvailableSpaceInLot_noLotFound() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\nNone\nLotName\nelectric\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("None")).thenReturn(null);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.getAvailableSpaceinParkingLot(mockLot.getName(),"electric")).thenReturn(7);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_reportAvailableSpaceInLot_invalidSpaceType() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\nLotName\nspot\nelectric\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.getAvailableSpaceinParkingLot(mockLot.getName(),"electric")).thenReturn(7);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_reportAvailableSpaceInLot_parkingSpotCaseSensitivity() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\nLotName\nElectric\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.getAvailableSpaceinParkingLot(mockLot.getName(),"Electric")).thenReturn(7);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_reportAvailableSpaceInLot_noSpacesFound() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\nLotName\nElectric\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.getAvailableSpaceinParkingLot(mockLot.getName(),"Electric")).thenReturn(0);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_reportAvailableSpaceInLot_SQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\nLotName\nElectric\n0\n".getBytes());
        System.setIn(in);
        when(parkingLotService.getParkingLot("LotName")).thenReturn(mockLot);
        when(reportsService.getAvailableSpaceinParkingLot(mockLot.getName(),"Electric")).thenThrow(new SQLException());

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_exit() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
        System.setIn(in);

        reportsMenu.callInterface();
    }

    @Test
    void callInterface_default() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("8\n0\n".getBytes());
        System.setIn(in);

        reportsMenu.callInterface();
    }

}
