package org.parking.menus;

import org.parking.model.Driver;
import org.parking.model.Permit;
import org.parking.model.Vehicle;
import org.parking.service.*;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.sql.SQLException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class PermitsMenuImplTest {

    private PermitsMenuImpl permitsMenuImpl;

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
        permitsMenuImpl = new PermitsMenuImpl(permitsService, driversService, vehicleService);

    }

    @AfterEach
    void restoreSystem() {
        System.setIn(systemIn);

    }

    @Test
    void menu_createPermitForStudent() throws SQLException {

        String userInput = "1\nPERMIT_ID\nASSOCIATED_ID\nCAR_LICENSE\nCommuter\nSpecial Event\nV\nAS\nRegular\n2023-01-01\n2023-12-31\n23:59:59\ny\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "S"));
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(1);
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));

        permitsMenuImpl.menu();

        verify(permitsService, times(1)).enterPermitInfo(any(Permit.class));
    }

    @Test
    void menu_createPermitForVisitor() throws SQLException {

        String userInput = "1\n \nPERMIT_ID\nASSOCIATED_ID\nCAR_LICENSE\nCommuter\nS\nV\nRegular\n2033-wrong\n2023-01-01\n2000-12-31\n2024-12-31\n23:59:59\nb\ny\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "V"));
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(0);
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));

        permitsMenuImpl.menu();

        verify(permitsService, times(1)).enterPermitInfo(any(Permit.class));
    }


    @Test
    void menu_createPermitForEmployee() throws SQLException {

        String userInput = "6\n1\nPERMIT_ID\nASSOCIATED_ID\nCAR_LICENSE\nCommuter\nAS\nA\nwrongType\nRegular\n2023-01-01\n2023-12-31\n23:59:59\ny\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(1);
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));

        permitsMenuImpl.menu();

        verify(permitsService, times(1)).enterPermitInfo(any(Permit.class));
    }

    @Test
    void menu_createPermitForEmployee_maxNumberReached() throws SQLException {

        String userInput = "6\n1\nPERMIT_ID\nASSOCIATED_ID\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(3);

        permitsMenuImpl.menu();

        verify(permitsService, times(0)).enterPermitInfo(any(Permit.class));
    }

    @Test
    void menu_createPermitForEmployee_SpecialEvent() throws SQLException {

        String userInput = "1\nPERMIT_ID\nASSOCIATED_ID\nCAR_LICENSE\nwrongType\nSpecial event\nA\nRegular\n2023-01-01\n2023-12-31\n23:59:59\ny\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(permitsService.getPermitsNumberForDriver(anyString())).thenReturn(2);
        when(vehicleService.getByLicense(anyString())).thenReturn(new Vehicle("SBF", "GT-R-Nismo", "Pearl White TriCoat", "Nissan", 2024));

        permitsMenuImpl.menu();

        verify(permitsService, times(1)).enterPermitInfo(any(Permit.class));
    }

    @Test
    void menu_createPermit_SQLException() throws SQLException {

        String userInput = "1\nPERMIT_ID\nASSOCIATED_ID\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(new Driver("123", "Bob", "E"));
        when(permitsService.getPermitsNumberForDriver(anyString())).thenThrow(new SQLException());
        permitsMenuImpl.menu();
    }

    @Test
    void menu_createPermit_noDriver() throws SQLException {

        String userInput = "1\nPERMIT_ID\nASSOCIATED_ID\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        ArrayList<Permit> currentArray = new ArrayList<>();
        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(null);

        permitsMenuImpl.menu();

        verify(permitsService, times(0)).enterPermitInfo(any(Permit.class));
    }



    @Test
    void menu_updatePermitForStudent() throws SQLException {
        String userInput = "2\nVSBF1C\n \nA\nBS\n \n2023-02-01\n2023-12-31\n23:59:59\ny\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        Driver associatedDriver = new Driver("123", "Bob", "S");

        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(associatedDriver);

        permitsMenuImpl.menu();

        verify(permitsService).updatePermitInfo(
                eq("VSBF1C"),
                eq("Commuter"),
                eq("BS"),
                eq("Regular"),
                any(Date.class),
                any(Date.class),
                any(Time.class)
        );
    }

    @Test
    void menu_updatePermitForVisitor() throws SQLException {
        String userInput = "2\nVSBF1C\n \nA\nV\nwrongType\nRegular\n\n2023-wrong\2023-02-01\n2020-12-31\n2023-12-31\n23:59:59\ny\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        Driver associatedDriver = new Driver("123", "Bob", "V");

        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(associatedDriver);

        permitsMenuImpl.menu();

        verify(permitsService).updatePermitInfo(
                eq("VSBF1C"),
                eq("Commuter"),
                eq("V"),
                eq("regular"),
                any(Date.class),
                any(Date.class),
                any(Time.class)
        );
    }

    @Test
    void menu_updatePermitForEmployee() throws SQLException {
        String userInput = "2\nVSBF1C\n \nAS\nV\nA\n \n\n2023-wrong\2023-02-01\n2020-12-31\n2023-12-31\n23:59:59\nw\ny\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);
        Driver associatedDriver = new Driver("123", "Bob", "E");

        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);
        when(driversService.getById(anyString())).thenReturn(associatedDriver);

        permitsMenuImpl.menu();

        verify(permitsService).updatePermitInfo(
                eq("VSBF1C"),
                eq("Commuter"),
                eq("A"),
                eq("Regular"),
                any(Date.class),
                any(Date.class),
                any(Time.class)
        );
    }

    @Test
    void menu_updatePermit_SQLException() throws SQLException {

        String userInput = "2\nVSBF1C\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        when(permitsService.getPermitInfo(anyString())).thenThrow(new SQLException());
        permitsMenuImpl.menu();
    }

    @Test
    void menu_deletePermit() throws SQLException {
        String userInput = "3\nVSBF1C\ny\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);

        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);

        permitsMenuImpl.menu();

        verify(permitsService).deletePermitInfo("VSBF1C");
    }

    @Test
    void menu_deletePermitNoPermitFound() throws SQLException {
        String userInput = "3\nVSBF1C\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        when(permitsService.getPermitInfo(anyString())).thenReturn(new ArrayList<>());
        permitsMenuImpl.menu();
        verify(permitsService, times(0)).deletePermitInfo(anyString());
    }

    @Test
    void menu_deletePermitCancel() throws SQLException {
        String userInput = "3\nVSBF1C\nw\nn\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Permit existingPermit = new Permit("'VSBF1C'", "Commuter", "A", "123", "SBF", "Regular", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"), Time.valueOf("23:59:59"));
        ArrayList<Permit> currentArray = new ArrayList<>();
        currentArray.add(existingPermit);

        when(permitsService.getPermitInfo(anyString())).thenReturn(currentArray);

        permitsMenuImpl.menu();

    }

    @Test
    void menu_deletePermit_SQLException() throws SQLException {
        String userInput = "3\nVSBF1C\n4\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        when(permitsService.getPermitInfo(anyString())).thenThrow(new SQLException());
        permitsMenuImpl.menu();
    }



}