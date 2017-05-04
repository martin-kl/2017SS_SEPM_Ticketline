package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;

import java.util.List;

public interface EventsControllerService {

    /**
     * Find all event entries.
     *
     * @return list of events
     * @throws DataAccessException in case something went wrong
     */
    List<EventDTO> findAll() throws DataAccessException;
}
