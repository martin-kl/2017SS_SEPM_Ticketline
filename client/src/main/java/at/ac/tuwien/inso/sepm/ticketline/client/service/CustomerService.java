package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    /**
     * fuzzy searches for customers
     *
     * @param query the search query
     * @param page the page number to request
     * @return list of customers
     */
    List<CustomerDTO> search(String query, int page) throws ExceptionWithDialog;

    /**
     * Find a single customer entry by id.
     *
     * @param id the is of the customer entry
     * @return the customer entry
     */
    CustomerDTO findOne(UUID id) throws ExceptionWithDialog;

    /**
     * saves a new or edit a customer
     *
     * @param customer The customer object to save or edit
     * @return the same customer passed into the method with fields updated
     */
    CustomerDTO save(CustomerDTO customer) throws ExceptionWithDialog;
}
