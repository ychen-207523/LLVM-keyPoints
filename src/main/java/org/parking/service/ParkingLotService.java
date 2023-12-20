package org.parking.service;

import java.util.Collection;

import org.parking.model.ParkingLot;

/**
 * Interface that contains all of the available operations for the
 * ParkingLotServiceImpl.
 */
public interface ParkingLotService {
    /**
     * Returns all of the parking lots that have been entered into the database.
     *
     * @return A collection of the parking lots
     */
    public Collection<ParkingLot> getAll();

    /**
     * Returns a parking lot from the database that matches the name parameter.
     * 
     * @param name The name of the parking lot to retrieve.
     * @return The parking lot if a result is found,
     *         null if there is an error or no space is found.
     */
    public ParkingLot getParkingLot(String name);

    /**
     * Creates and stores a parking lot on the database.
     * 
     * @param lot The parking lot to create.
     * @return False if there is an error, true if the parking lot is successfully
     *         created.
     */
    public boolean createParkingLot(ParkingLot lot);

    /**
     * Updates a parking lot in the database.
     * 
     * @param name Name of the parking lot to update.
     * @param lot  New values for the existing parking lot.
     * @return True if successful, false if an error occurs.
     */
    public boolean updateParkingLot(String name, ParkingLot lot);

    /**
     * Deletes a parking lot from the database.
     * 
     * @param name Name of the parking lot to delete.
     * @return true if the parking lot was successfully deleted, false if an error
     *         occurs
     */
    public boolean deleteParkingLot(String name);
}
