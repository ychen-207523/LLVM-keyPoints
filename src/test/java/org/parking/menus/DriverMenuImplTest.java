package org.parking.menus;

import org.parking.model.Driver;
import org.parking.service.*;
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
class DriverMenuImplTest {

    private DriverMenuImpl driversMenuImpl;
    @Mock
    private DriversService driversService;

    private final Driver mockDriver =  new Driver("1234567890", "testName", "S");

    private final InputStream systemIn = System.in;



    @BeforeEach
    void init() {
        openMocks( this );
        driversMenuImpl = new DriverMenuImpl(driversService);
    }

    @AfterEach
    void restoreInput() {
        System.setIn(systemIn);
    }

    @Test
    void callInterface_enterDriverUI_success() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1234567890\ntestName\nS\nY\n0\n".getBytes());
        System.setIn(in);
        when(driversService.create(any(Driver.class))).thenReturn(true);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_enterDriverUI_fail() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1234567890\ntestName\nS\nY\n0\n".getBytes());
        System.setIn(in);
        when(driversService.create(any(Driver.class))).thenReturn(false);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_enterDriverUI_exit() {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1234567890\ntestName\nS\nt\nn\n0\n".getBytes());
        System.setIn(in);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_enterDriverUI_exception() throws SQLException {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1234567890\ntestName\nS\nY\n0\n".getBytes());
        System.setIn(in);
        when(driversService.create(any(Driver.class))).thenThrow(new SQLException());

        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_name_success() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n1\ntestUpdate\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenReturn(true);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_name_noChange() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n1\n\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenReturn(true);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_status_noChange() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n2\n\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenReturn(true);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_name_inputInvalid() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n1\n12389~!@\n\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenReturn(true);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_status_inputInvalid() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n2\nf\n\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenReturn(true);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_id_inputInvalid() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\na~`!\n1234567890\n1\ntestUpdate\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenReturn(true);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_status_success() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n2\nv\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenReturn(true);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_name_failed() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n1\ntestUpdate\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenReturn(false);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_status_failed() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n2\nv\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenReturn(false);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_find_sqlException() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenThrow(new SQLException());
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_find_noDriverFound() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(null);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_update_sqlException() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n2\nv\n3\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.update(any(Driver.class))).thenThrow(new SQLException());
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_canceled() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n0\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_unavailableOption() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\na\n0\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_updateDriverUI_default() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("2\n1234567890\n4\n0\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_deleteDriverUI_success() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("3\n1234567890\nY\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.delete("1234567890")).thenReturn(true);

        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_deleteDriverUI_failed() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("3\n1234567890\nY\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.delete("1234567890")).thenReturn(false);

        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_deleteDriverUI_SQLException() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("3\n1234567890\nY\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);
        when(driversService.delete("1234567890")).thenThrow(new SQLException());

        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_deleteDriverUI_exit() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("3\n1234567890\nN\n0\n".getBytes());
        System.setIn(in);

        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_deleteDriverUI_driverNotFound() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("3\n1234567890\nY\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(null);

        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_deleteDriverUI_find_SQLException() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("3\n1234567890\nY\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenThrow(new SQLException());

        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_deleteDriverUI_find_default() throws SQLException{
        ByteArrayInputStream in = new ByteArrayInputStream("3\n1234567890\n8\nN\n0\n".getBytes());
        System.setIn(in);
        when(driversService.getById("1234567890")).thenReturn(mockDriver);

        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_default() {
        ByteArrayInputStream in = new ByteArrayInputStream("4\n0\n".getBytes());
        System.setIn(in);
        driversMenuImpl.callInterface();
    }

    @Test
    void callInterface_unavailableCharacter() {
        ByteArrayInputStream in = new ByteArrayInputStream("f\n0\n".getBytes());
        System.setIn(in);
        driversMenuImpl.callInterface();
    }




}