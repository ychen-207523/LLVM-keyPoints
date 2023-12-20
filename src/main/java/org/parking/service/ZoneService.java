package org.parking.service;

import java.util.Collection;

import org.parking.model.Zone;

/**
 * Interface that holds the functions for interacting with database for Zone
 * operations.
 */
public interface ZoneService {
    /**
     * Returns all of the Zones in the database.
     * Zones are grouped by the parking lot they are in and their identifier.
     *
     * @return A collection of all of the Zones in the database.
     */
    Collection<Zone> getAll();

    /**
     * Retrieves a zone from the database that matches the id and lotName
     * parameters.
     * 
     * @param id      The id of the zone.
     * @param lotName The name of the parking lot the zone is located in.
     * @return The zone from the database; null if an error occurs or the zone
     *         does not exist.
     */
    Zone getZone(String id, String lotName);

    /**
     * Retrieves a collection of zones that match the id provided.
     * 
     * @param id The id to search for.
     * @return A collection of all the zones found that match the id.
     */
    Collection<Zone> getZonesById(String id);

    /**
     * Retrieves a collection of zones that match the lot name provided.
     * 
     * @param lotName The name of the parking lot to search for.
     * @return A collection of all the zones found that match the lotName.
     */
    Collection<Zone> getZonesByLotName(String lotName);

    /**
     * Creates and stores a zone in the database.
     * 
     * @param zone The zone to create.
     * @return True if successful; false if the zone already exists or
     *         an error occurs.
     */
    boolean createZone(Zone zone);

    /**
     * Updates a zone that is stored in the database.
     * 
     * @param originalZone The original values of the zone.
     * @param updatedZone  The new values of the zone.
     * @return True if the update was successful. False if an error occurs or the
     *         zone already exists.
     */
    boolean updateZone(Zone originalZone, Zone updatedZone);

    /**
     * Deletes a zone from the database.
     * 
     * @param zone The zone to delete.
     * @return True if the delete is successful; false if an error occurs or the
     *         zone does not exist.
     */
    boolean deleteZone(Zone zone);

    /**
     * Updates the lotName field of a zone.
     * 
     * @param oldLot The current lot name for the zone.
     * @param zoneID The zone's id
     * @param newLot The new lot name for the zone.
     * @return True if the update was successful. False if an error occurs or the
     *         zone already exists.
     */
    boolean assignZoneToParkingLot(String oldLot, String zoneID, String newLot);

    /**
     * Creates both a parking lot and zone in the database.
     * 
     * @param address The address of the new parking lot.
     * @param lotName The name of the parking lot.
     * @param zoneID  The id of the new zone.
     * @return True if the zone was successfully created. False if an error occurs
     *         or the zone if not created.
     */
    boolean createLotAndZone(String address, String lotName, String zoneID);
}
