package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface EventService {

    /**
     * Find all events
     *
     * @param pageable spring pageable object
     * @return list of events
     */
    List<Event> findAll(Pageable pageable);


    /**
     * Find a single event entry by id.
     *
     * @param id the ids of the event entry
     * @return the event entry
     * @throws at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException
     */
    Event findOne(UUID id);
}
