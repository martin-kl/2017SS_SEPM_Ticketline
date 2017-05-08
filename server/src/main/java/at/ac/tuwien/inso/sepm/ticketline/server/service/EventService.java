package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;

import java.util.List;
import java.util.UUID;

public interface EventService {

    /**
     * Find all events
     *
     * @return list of events
     */
    List<Event> findAll();


    /**
     * Find a single event entry by id.
     *
     * @param id the ids of the event entry
     * @return the event entry
     * @throws at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException
     */
    Event findOne(UUID id);
}