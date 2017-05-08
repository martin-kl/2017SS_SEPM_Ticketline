package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
     /**
     * Find all customers
     *
     * @return ordered list of al customers
     */
    List<CustomerDTO> findAll() throws ExceptionWithDialog;


    /**
     * Find a single news entry by id.
     *
     * @param id the is of the customer entry
     * @return the customer entry
     */
    CustomerDTO findOne(UUID id) throws ExceptionWithDialog;

    /**
     * saves a new or edit a customer
     * @param customer The customer object to save or edit
     * @return the same customer passed into the method with fields updated
     */
    CustomerDTO save(CustomerDTO customer) throws ExceptionWithDialog;
}