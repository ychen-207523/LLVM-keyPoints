package org.parking.service;

import java.util.Collection;

import org.parking.model.Space;

/**
 * Interface for database operations relating to Spaces.
 */
public interface SpaceService {
    /**
     * Returns all of the Spaces in the database.
     * Spaces are grouped by the parking lot they are in,
     * the zone they are in and their identifier.
     *
     * @return A collection of all of the Spaces in the database.
     */
    public Collection<Space> getAll();

    /**
     * Retrieves a space that has a primary key that matches the number,
     * zoneID and lotName from the database.
     * 
     * @param number  The number of the space.
     * @param zoneID  The zoneID the space is in.
     * @param lotName The name of the parking lot the space is in.
     * @return The space from the database, null if it does not exist.
     */
    Space getSpace(int number, String zoneID, String lotName);

    /**
     * Creates and stores a space in the database.
     * 
     * @param space The space to be created.
     * @return True if the space was created successfully, false if
     *         it already exists or an error occurs.
     */
    boolean createSpace(Space space);

    /**
     * Updates a space in the database.
     * 
     * @param originalSpace The original space to look for.
     * @param updatedSpace  The values to the set the space to.
     * @return True if successful, false if the space does not
     *         exist or an error occurs.
     */
    boolean updateSpace(Space originalSpace, Space updatedSpace);

    /**
     * Deletes a space from the database.
     * 
     * @param space The space to be deleted
     * @return True if the space was deleted successfully; false if an error
     *         occurred or the space does not exist.
     */
    boolean deleteSpace(Space space);

    /**
     * Updates the type of a space.
     * 
     * @param lotName The name of the parking lot where the space is located.
     * @param zoneID  The zone ID where the space is.
     * @param number  The number of the space.
     * @param type    The type the space is being set to.
     * @return True if successful, false if the space does not exist or an error
     *         occurs.
     */
    boolean assignTypeOfASpace(String lotName, String zoneID, int number, String type);
}
