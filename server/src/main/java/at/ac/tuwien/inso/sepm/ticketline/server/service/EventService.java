package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.EventSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
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

    /**
     * Find events which match the attributes specified in eventSearch
     *
     * @param eventSearch contains the attributes
     * @param pageable paging object
     * @return list of events including the performances
     * */
    List<Event> search(EventSearch eventSearch, Pageable pageable);

    /**
     * gets top ten events since 'monthInPast'.
     * @param category can be an empty string to mean all categories
     * @param monthsInPast can be negative to mean entire timespan
     * @return
     */
    Map<Integer, Event> getTopTen(String category, int monthsInPast);
}
