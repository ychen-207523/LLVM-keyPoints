package org.parking.service;

import org.parking.model.Citation;

import java.sql.SQLException;
import java.util.Collection;

public interface CitationsService {
    Collection<Citation> getAll() throws SQLException;
    Citation getByNumber(int number) throws SQLException;
    void createCitation(Citation citation, Boolean createVehicle) throws SQLException;
    void updateCitation(Citation citation) throws SQLException;
    void deleteCitationByNumber(int number) throws SQLException;
    boolean appealCitation(int number);
    boolean payCitation(int number);
}
