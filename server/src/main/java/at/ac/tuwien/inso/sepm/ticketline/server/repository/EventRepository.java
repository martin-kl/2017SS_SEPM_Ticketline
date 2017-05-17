package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    /**
     * Find a single event entry by id.
     *
     * @param id the is of the event entry
     * @return Optional containing the event entry
     */
    Optional<Event> findOneById(UUID id);

    List<Event> findAllOrderByLastModifiedAt(Pageable pageable);
}
