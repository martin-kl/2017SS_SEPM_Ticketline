package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, UUID> {
    /**
     * Find a single performance by id
     *
     * @param id the is of the performance
     * @return Optional containing the performance
     */
    @Query("SELECT p FROM Performance p JOIN FETCH p.tickets WHERE p.id = ?1")
    Optional<Performance> findOneById(UUID id);
}
