
package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import java.awt.print.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    /**
     * Find a single customer entry by id.
     *
     * @param id the is of the customer entry
     * @return Optional containing the customer entry
     */
    Optional<Customer> findOneById(UUID id);


    /**
     * finds all customers and sorts them
     * @param pageable The next requested page
     * @return a (paged) list of all customers
     */
    List<Customer> findAllOrderByLastName(Pageable pageable);
}

