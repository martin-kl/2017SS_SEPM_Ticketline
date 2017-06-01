package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;

import java.util.List;

public interface EventService {

    /**
     * Find all event entries.
     *
     * @return list of events
     * @throws DataAccessException in case something went wrong
     */
    List<EventDTO> findAll(int page) throws DataAccessException;


    /**
     * fuzzy searches for events
     *
     * @param query the search query
     * @param page the page number to request
     * @return list of customers
     */
    //List<EventDTO> search(String query, int page) throws ExceptionWithDialog;
}
