package org.parking.menus;

import org.parking.model.Driver;
import org.parking.model.Permit;
import org.parking.model.Vehicle;
import org.parking.service.DriversService;
import org.parking.service.PermitsService;
import org.parking.service.VehicleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class MaintainPermitsAndVehicleMenuImplTest {
    private MaintainPermitsAndVehicleMenuImpl maintainPermitsAndVehicleMenu;
    private final Vehicle mockVehicle =  new Vehicle("ABCD123", "testModel", "color","testManuf", 2023);

    @Mock
    private PermitsService permitsService;
    @Mock
    private DriversService driversService;
    @Mock
    private VehicleService vehicleService;

    private final InputStream systemIn = System.in;

    @BeforeEach
    void init() {
        openMocks(this);
        maintainPermitsAndVehicleMenu = new MaintainPermitsAndVehicleMenuImpl(driversService, vehicleService, permitsService);

    }

    @AfterEach
    void restoreSystem() {
        System.setIn(systemIn);
    }

    @Test
    void assignPermit_Employee_newPermit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("8\n1\ntestDriver\ntestLicnese\ntestPermit\nPark & Ride\nAS\nA\ninvalid\nregular\n2023-01-01\n2023-12-31\n23:59:59\ny\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).assignPermitToDriver(any(), any(), any(), any(), any(), any(),any(), any(), any());
    }

    @Test
    void assignPermit_Employee_TwoPemit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\ntestDriver\ntestLicnese\ntestPermit\nCommuter\nPark & Ride\nA\nregular\n2023-01-01\n2023-12-31\n23:59:59\ny\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("TEST", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(2);;
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).assignPermitToDriver(any(), any(), any(), any(), any(), any(),any(), any(), any());
    }

    @Test
    void assignPermit_Employee_ThreePemit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\ntestDriver\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("TEST", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(3);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).assignPermitToDriver(any(), any(), any(), any(), any(), any(),any(), any(), any());
    }

    @Test
    void assignPermit_Student_newPermit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("8\n1\ntestDriver\ntestLicnese\ntestPermit\nPark & Ride\nA\nAS\nregular\n2023-01-01\n2023-12-31\n23:59:59\ny\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "S"));
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).assignPermitToDriver(any(), any(), any(), any(), any(), any(),any(), any(), any());
    }

    @Test
    void assignPermit_Student_OnePermit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("8\n1\ntestDriver\ntestLicnese\ntestPermit\nCommuter\nPark & Ride\nA\nAS\nregular\n2023-01-01\n2023-12-31\n23:59:59\ny\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "S"));
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(1);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).assignPermitToDriver(any(), any(), any(), any(), any(), any(),any(), any(), any());
    }

    @Test
    void assignPermit_Student_TwoPermit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("8\n1\ntestDriver\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "S"));
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(2);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).assignPermitToDriver(any(), any(), any(), any(), any(), any(),any(), any(), any());
    }

    @Test
    void assignPermit_Visitor_newPermit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("8\n1\ntestDriver\ntestLicnese\ntestPermit\nPark & Ride\nA\nV\nregular\n2023-01-01\n2023-12-31\n23:59:59\ny\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "V"));
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).assignPermitToDriver(any(), any(), any(), any(), any(), any(),any(), any(), any());
    }

    @Test
    void assignPermit_Visitor_cancel_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("8\n1\ntestDriver\ntestLicnese\ntestPermit\nPark & Ride\nA\nV\nregular\n2023-01-01\n2023-12-31\n23:59:59\nn\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "V"));
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).assignPermitToDriver(any(), any(), any(), any(), any(), any(),any(), any(), any());
    }

    @Test
    void assignPermit_SQLException_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("8\n1\ntestDriver\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById(anyString())).thenThrow(new SQLException());
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).assignPermitToDriver(any(), any(), any(), any(), any(), any(),any(), any(), any());
    }

    @Test
    void removeVehicleFromPermit_novehicle_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("4\ntestPermit\n0\n".getBytes());
        System.setIn(in);
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).removeVehicleFromPermit(any(), any());

    }

    @Test
    void removeVehicleFromPermit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("4\ntestPermit\ntestLicense\n0\n".getBytes());
        System.setIn(in);
        ArrayList<Permit> currentArray = new ArrayList<>();
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        currentArray.add(existingPermit);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).removeVehicleFromPermit(any(), any());
    }

    @Test
    void removeVehicleFromPermit_SQLException_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("4\ntestPermit\n0\n".getBytes());
        System.setIn(in);
        ArrayList<Permit> currentArray = new ArrayList<>();
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        currentArray.add(existingPermit);
        when(permitsService.getPermitInfo(anyString())).thenThrow(new SQLException());
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).removeVehicleFromPermit(any(), any());
    }


    @Test
    void createPermit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nPERMIT_ID\nASSOCIATED_ID\nCAR_LICENSE\nCommuter\nAS\nA\nRegular\n2023-01-01\n2023-12-31\n23:59:59\ny\n0\n".getBytes());
        System.setIn(in);
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(1);
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).enterPermitInfo(any());
    }

    @Test
    void updatePermit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\nVSBF1C\nCommuter\nAS\n \n\n2023-wrong\2023-02-01\n2020-12-31\n2023-12-31\n23:59:59\ny\n0\n".getBytes());
        System.setIn(in);
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        Driver associatedDriver = new Driver("123", "Bob", "S");

        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(associatedDriver);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).updatePermitInfo(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void updatePermit_SQLException_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\nVSBF1C\nResidential\n0\n".getBytes());
        System.setIn(in);
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);

        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenThrow(new SQLException());
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).updatePermitInfo(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void addVehicleToPermit_noPermit_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\n\testID\n0\n".getBytes());
        System.setIn(in);
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).addVehicleToPermit(any(), any());
        verify(permitsService, times(0)).addVehicleToPermitForEmployee(any(), any());
    }

    @Test
    void addVehicleToPermit_noVehicle_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\n\testID\ntestLicense\n0\n".getBytes());
        System.setIn(in);
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(vehicleService.getByLicense(anyString())).thenReturn(null);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).addVehicleToPermit(any(), any());
        verify(permitsService, times(0)).addVehicleToPermitForEmployee(any(), any());
    }

    @Test
    void addVehicleToPermit_noDriver_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\n\testID\ntestLicense\n0\n".getBytes());
        System.setIn(in);
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(vehicleService.getByLicense(anyString())).thenReturn(mockVehicle);
        when(driversService.getById(anyString())).thenReturn(null);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).addVehicleToPermit(any(), any());
        verify(permitsService, times(0)).addVehicleToPermitForEmployee(any(), any());
    }

    @Test
    void addVehicleToPermit_employee_twoPermits_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\n\testID\ntestLicense\n0\n".getBytes());
        System.setIn(in);
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(vehicleService.getByLicense(anyString())).thenReturn(mockVehicle);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(permitsService.getVehicleNumberofPermit(anyString())).thenReturn(2);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).addVehicleToPermit(any(), any());
        verify(permitsService, times(0)).addVehicleToPermitForEmployee(any(), any());
    }

    @Test
    void addVehicleToPermit_employee_onePermit_oneVehicle_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\n\testID\ntestLicense\n0\n".getBytes());
        System.setIn(in);
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(vehicleService.getByLicense(anyString())).thenReturn(mockVehicle);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(permitsService.getVehicleNumberofPermit(anyString())).thenReturn(1);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).addVehicleToPermit(any(), any());
        verify(permitsService, times(1)).addVehicleToPermitForEmployee(any(), any());
    }

    @Test
    void addVehicleToPermit_employee_onePermit_zeroVehicle_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\n\testID\ntestLicense\n0\n".getBytes());
        System.setIn(in);
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(vehicleService.getByLicense(anyString())).thenReturn(mockVehicle);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(permitsService.getVehicleNumberofPermit(anyString())).thenReturn(0);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).addVehicleToPermit(any(), any());
        verify(permitsService, times(0)).addVehicleToPermitForEmployee(any(), any());
    }

    @Test
    void addVehicleToPermit_student_oneVehicle_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\n\testID\ntestLicense\n0\n".getBytes());
        System.setIn(in);
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(vehicleService.getByLicense(anyString())).thenReturn(mockVehicle);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "S"));
        when(permitsService.getVehicleNumberofPermit(anyString())).thenReturn(1);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(0)).addVehicleToPermit(any(), any());
        verify(permitsService, times(0)).addVehicleToPermitForEmployee(any(), any());
    }

    @Test
    void addVehicleToPermit_student_zeroVehicle_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("5\n\\testID\ntestLicense\n0\n".getBytes());
        System.setIn(in);
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(vehicleService.getByLicense(anyString())).thenReturn(mockVehicle);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "S"));
        when(permitsService.getVehicleNumberofPermit(anyString())).thenReturn(0);
        maintainPermitsAndVehicleMenu.callInterface();
        verify(permitsService, times(1)).addVehicleToPermit(any(), any());
        verify(permitsService, times(0)).addVehicleToPermitForEmployee(any(), any());
    }

    @Test
    void enterVehicleUI_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("6\nABCD123\ntestManuf\ntestModel\ncolor\n2023\nY\n0\n".getBytes());
        when(vehicleService.create(any(Vehicle.class))).thenReturn(true);
        System.setIn(in);
        maintainPermitsAndVehicleMenu.callInterface();
    }

    @Test
    void deleteVehicleUI_test() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("7\nABCD123\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.delete("ABCD123")).thenReturn(true);
        maintainPermitsAndVehicleMenu.callInterface();
    }



}
