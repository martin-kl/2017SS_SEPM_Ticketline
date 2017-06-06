package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
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
     * @param category The category to search for the top ten
     * @param monthsInPast can be negative to mean entire timespan
     * @return A list of top ten events since 'monthInPast# for the given category
     */
    Map<Integer, Event> getTopTen(EventCategory category, int monthsInPast);
}
