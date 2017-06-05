package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
     *
     * @param category
     * @param startSearchFrom
     * @return
     */

    /*@Query(value = "SELECT e.name, COUNT(t.id) as sold_tickets " +
            "FROM event e, ticket t, performance p " +
            "WHERE " +
            "t.performance_id = p.id AND " +
            "p.event_id = e.id AND " +
            "EXISTS (SELECT * FROM " +
            "ticket_history th, ticket_transaction tt " +
            "WHERE " +
            "t.id = th.ticket_id AND " +
            "tt.id = th.ticket_transaction_id AND " +
            "tt.status = 'bought' " +
            "ORDER BY th.last_modified_at DESC " +
            "LIMIT 1) " +
            "GROUP BY e.id " +
        "ORDER BY sold_tickets DESC LIMIT 10", nativeQuery = true)*/

    /*@Query(value = "SELECT new map(COUNT(t.id) as sold_tickets) " +
    "FROM event e, ticket t, performance p " +
        "WHERE " +
    "t.performance_id = p.id AND " +
    "p.event_id = e.id AND " +
        "(e.category = ?1 OR ?1 = '') " +
    "EXISTS (SELECT * FROM " +
        "ticket_history th, ticket_transaction tt " +
            "WHERE " +
                "t.id = th.ticket_id AND " +
                "tt.id = th.ticket_transaction_id AND " +
                "tt.status = 'bought' AND " +
                "last_modified_at > ?2 " +
                "ORDER BY th.last_modified_at DESC " +
                "LIMIT 1) " +
    "GROUP BY e.id " +
    "ORDER BY sold_tickets DESC " +
    "LIMIT 10")*/

    //@Query(value="SELECT new Map(count(t.id)) as sold_tickets, e) from Event e ")
    //@Query(value="select new map( max(bodyWeight) as max, min(bodyWeight) as min, count(*) as n )")


    //@Query(value = "SELECT e, COUNT(t.id) as sold_tickets " +
            //"FROM Event JOIN Performance p JOIN Ticket t JOIN TicketHistory th JOIN TicketTransaction tt" +
            //"WHERE ")

    @Query(value = "SELECT e.*, COUNT(t.id) as sold_tickets " +
        "FROM event e, ticket t, performance p " +
        "WHERE " +
        "t.performance_id = p.id AND " +
        "p.event_id = e.id AND " +
        "(e.category = ?1 OR ?1 = '') " +
        "AND EXISTS (SELECT * FROM " +
        "ticket_history th, ticket_transaction tt " +
        "WHERE " +
        "t.id = th.ticket_id AND " +
        "tt.id = th.ticket_transaction_id AND " +
        "tt.status = 'bought' AND " +
        "th.last_modified_at > ?2 " +
        "ORDER BY th.last_modified_at DESC " +
        "LIMIT 1) " +
        "GROUP BY e.id " +
        "ORDER BY sold_tickets DESC " +
        "LIMIT 10; ", nativeQuery = true)
    List<Event> getTopTen(String category, Date startSearchFrom);

}
