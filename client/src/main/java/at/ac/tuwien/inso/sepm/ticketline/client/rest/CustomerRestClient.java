package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerRestClient {
     /**
     * Find all customers
     *
     * @return ordered list of al customers
     */
    List<CustomerDTO> findAll() throws DataAccessException;


    /**
     * fuzzy seraches for customers
     * @param query the serach query
     * @return list of customers
     */
    List<CustomerDTO> search(String query) throws DataAccessException;


    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return the news entry
     */
    CustomerDTO findOne(UUID id) throws DataAccessException;

    /**
     * saves a new or edited customer
     * @param customer The customer object to save or edit
     * @return the same customer passed into the method with fields updated
     */
    CustomerDTO save(CustomerDTO customer) throws ExceptionWithDialog;
}
