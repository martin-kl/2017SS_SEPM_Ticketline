package at.ac.tuwien.inso.sepm.ticketline.server.service;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    /**
     * Find all customers
     *
     * @return list of customers (ordered by lastname)
     */
    List<Customer> findAll();


    /**
     * Find a single customer entry by id.
     *
     * @param id the ids of the customer entry
     * @return the customer entry
     * @throws at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException
     */
    Customer findOne(UUID id);

    /**
     * saves a new or edited customer
     * @param customer The customer object to save or edit
     * @return the same customer passed into the method with fields updated
     * @throws at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException
     */
    Customer save(Customer customer);
}
