package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    /**
     * Find a single customer entry by id.
     *
     * @param id the is of the customer entry
     * @return Optional containing the customer entry
     */
    Optional<Event> findOneById(UUID id);
}
