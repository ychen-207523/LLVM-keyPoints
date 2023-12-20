package org.parking.menus;

/**
 * Interface that holds the public function for the information processing menu.
 */
public interface InformationProcessMenu {
    /**
     * Creates a user interface for the user to navigate. The user will have the
     * option to:
     * - create, update or delete entries in the database
     * - assign zones to parking lots
     * - assign types to spaces
     * - appeal citations
     */
    void callInterface();
}
