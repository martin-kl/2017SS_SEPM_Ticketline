package at.ac.tuwien.inso.sepm.ticketline.server.service;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    /**
     * Find all customers
     *
     * @return list of customers (ordered by lastname)
     * @param pageable The next requested page
     */
    List<Customer> findAll(Pageable pageable);

    /**
     * Find a single customer entry by id.
     *
     * @param id the ids of the customer entry
     * @return the customer entry
     */
    Customer findOne(UUID id);

    /**
     * saves a new or edited customer
     * @param customer The customer object to save or edit
     * @return the same customer passed into the method with fields updated
     */
    Customer save(Customer customer);

    /**
     * fuzzy searches for customers
     * @param query the search query
     * @return list of customers
     */
    List<Customer> search(String query);
}
