package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    //TODO is this method even sinful, are we searching for 1 customer by id?!?
    /**
     * Find a single customer entry by id.
     *
     * @param id the is of the customer entry
     * @return Optional containing the customer
     */
    Optional<Customer> findOneById(Long id);
}
