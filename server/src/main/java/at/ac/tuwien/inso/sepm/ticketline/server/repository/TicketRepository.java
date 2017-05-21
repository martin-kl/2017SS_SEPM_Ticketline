package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    /**
     * Find a list of tickets to the corresponding performance.
     *
     * @param performanceId to which the tickets will belong
     * @return List of tickets
     */
    List<Ticket> findByPerformanceId(UUID performanceId);
}
