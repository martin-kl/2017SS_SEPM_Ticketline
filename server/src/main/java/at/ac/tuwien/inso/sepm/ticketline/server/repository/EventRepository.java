package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.TopTenEventWrapper;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>, QueryDslPredicateExecutor<Event> {
    /**
     * Find a single event entry by id.
     *
     * @param id the is of the event entry
     * @return Optional containing the event entry
     */
    Optional<Event> findOneById(UUID id);

    List<Event> findAllOrderByLastModifiedAt(Pageable pageable);

    /**
     * gets top ten events since 'startingSearchFrom'.
     * @param category The category to search for the top ten
     * @param startingSearchFrom starting from this day the query gets the top ten events
     * @param pageable A pageable object to limit the result to the first 10 entries
     * @return A list of top ten "TopTenEventWrapper" since 'startingSearchFrom' for the given category
     */
    @Query(value=
    "SELECT new at.ac.tuwien.inso.sepm.ticketline.server.service.util.TopTenEventWrapper(e, count(e.id)) " +
        "FROM Event e join e.performances p join p.tickets t join t.ticketHistories th join th.ticketTransaction tt "
        + "WHERE tt.outdated = false AND (e.category = ?1 OR ?1 IS NULL)"
        + "AND th.lastModifiedAt > ?2 "
        + "AND tt.status = 'BOUGHT'"
        + "GROUP BY e.id "
        + "ORDER BY count(e.id) DESC")
    List<TopTenEventWrapper> getTopTen(EventCategory category, Instant startingSearchFrom, Pageable pageable);
}
