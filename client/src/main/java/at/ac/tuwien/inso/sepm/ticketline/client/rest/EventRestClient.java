package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;

import java.util.List;

public interface EventRestClient {
    /**
     * Find all event entries.
     *
     * @return list of events
     * @throws DataAccessException in case something went wrong
     */
    List<EventDTO> findAll(int page) throws DataAccessException;
}
