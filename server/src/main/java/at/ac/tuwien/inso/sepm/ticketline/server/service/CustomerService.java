package at.ac.tuwien.inso.sepm.ticketline.server.service;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    /**
     * Find all customers
     *
     * @return ordered list of al customers
     */
    List<Customer> findAll();


    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return the news entry
     */
    Customer findOne(UUID id);

    /**
     * saves a new or edited customer
     * @param customer The customer object to save or edit
     * @return the same customer passed into the method with fields updated
     */
    Customer save(Customer customer);
}
