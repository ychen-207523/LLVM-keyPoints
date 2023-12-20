package org.parking.menus;

import org.parking.model.Driver;
import org.parking.model.Vehicle;
import org.parking.service.*;
import org.parking.service.DriversService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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
public class VehicleMenuImplTest {

    private VehicleMenuImpl vehicleMenuImpl;
    @Mock
    private VehicleService vehicleService;

    private final Vehicle mockVehicle =  new Vehicle("ABCD123", "testModel", "color","testManuf", 2023);

    private final InputStream systemIn = System.in;

    @BeforeEach
    void init() {
        openMocks( this );
        vehicleMenuImpl = new VehicleMenuImpl(vehicleService);
    }

    @Test
    void callInterface_enterVehicle_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\nABCD123\ntestManuf\ntestModel\ncolor\n2023\nY\n0\n".getBytes());
        when(vehicleService.create(any(Vehicle.class))).thenReturn(true);
        System.setIn(in);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_enterVehicle_failed() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\nABCD123\ntestManuf\ntestModel\ncolor\n2023\nY\n0\n".getBytes());
        when(vehicleService.create(any(Vehicle.class))).thenReturn(false);
        System.setIn(in);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_enterVehicle_exit() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\nABCD123\ntestManuf\ntestModel\ncolor\n2023\nN\n0\n".getBytes());
        System.setIn(in);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_enterVehicle_invalidInput() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\nABCD123\ntestManuf\ntestModel\ncolor\n2023\n12345\nN\n0\n".getBytes());
        System.setIn(in);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_enterVehicle_SQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\nABCD123\ntestManuf\ntestModel\ncolor\n2023\nY\n0\n".getBytes());
        when(vehicleService.create(any(Vehicle.class))).thenThrow(new SQLException());
        System.setIn(in);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_find_notFound() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(null);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_find_sqlException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenThrow(new SQLException());

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_invalidFieldInput() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\nf\n0\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_failed() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n1\nupdated\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(false);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_SQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n1\nupdated\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenThrow(new SQLException());

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_default() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n6\n0\n0\n".getBytes());
        System.setIn(in);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_license_invliadInput() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nabcd123\nABCD123\n1\nupdated\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenThrow(new SQLException());

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_Model_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n1\nupdated\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_Model_noUpdate() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n1\n\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_Model_invalidInput() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n1\n123~!\n\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_color_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n2\nupdated\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_color_noUpdate() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n2\n\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_color_invalidInput() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n2\n123~!\n\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_manuf_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n3\nupdated\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_manuf_noUpdate() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n3\n\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_manuf_invalidInput() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n3\n123~!\n\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_year_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n4\n2020\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_year_noUpdate() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n4\n\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_update_year_invalidInput() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n4\n123~!\n\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }
    @Test
    void callInterface_update_year_invalidYear() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("2\nABCD123\n4\n20\n\n5\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.update(mockVehicle)).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_delete_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\nABCD123\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.delete("ABCD123")).thenReturn(true);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_delete_find_vehicleNotFound() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\nABCD123\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(null);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_delete_find_SQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\nABCD123\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenThrow(new SQLException());

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_delete_failed() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\nABCD123\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.delete("ABCD123")).thenReturn(false);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_delete_SQLException() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\nABCD123\nY\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);
        when(vehicleService.delete("ABCD123")).thenThrow(new SQLException());

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_delete_exit() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\nABCD123\nN\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_delete_default() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("3\nABCD123\n8\nN\n0\n".getBytes());
        System.setIn(in);
        when(vehicleService.getByLicense("ABCD123")).thenReturn(mockVehicle);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_exit() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
        System.setIn(in);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_default() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("8\n0\n".getBytes());
        System.setIn(in);

        vehicleMenuImpl.menu();
    }

    @Test
    void callInterface_invalidInput() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("f\n0\n".getBytes());
        System.setIn(in);

        vehicleMenuImpl.menu();
    }
}
