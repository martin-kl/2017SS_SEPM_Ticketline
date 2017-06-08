package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrincipalRepository extends JpaRepository<Principal, UUID>,
    QueryDslPredicateExecutor<Principal> {

    /**
     * Finds a Principal by username
     *
     * @param username as a search term
     * @return an optional principal user
     */
    Optional<Principal> findByUsername(String username);

    /**
     * Increments the failedLoginCount and sets the enabled flag for the principal with the given ID
     *
     * @param id The principal ID to increment the failed login count
     * @param enabled The new enabled status for this principal
     * @return The updated principal entry
     */
    //Modifying and Transactional are needed for spring to show it:
        // modifying: let Spring-data know that the query is not a query used to select values, but to update values
        // transactional: The transactional annotation itself defines the scope of a single database transaction.
            // The database transaction happens inside the scope of a persistence context
    @Modifying
    @Transactional
    @Query(value = "UPDATE Principal p SET p.failedLoginCount = p.failedLoginCount + 1, p.enabled = ?2 WHERE p.id = ?1")
    void incrementFailedLoginCount(UUID id, boolean enabled);

    /**
     * Resets the failedLoginCount for the principal with the given ID
     *
     * @param id The principal ID to reset the failed login count
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE Principal p SET p.failedLoginCount = 0 WHERE p.id = ?1")
    void resetFailedLoginCount(UUID id);

    /**
     * Enable/Disable the principal with the given id and set failedLoginCount to 0
     *
     * @param id The principal ID to unlock
     * @param enabled The enable status for the principal
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE Principal p SET p.failedLoginCount = 0, p.enabled = ?2 WHERE p.id = ?1")
    void updateEnabledFlag(UUID id, boolean enabled);
}
