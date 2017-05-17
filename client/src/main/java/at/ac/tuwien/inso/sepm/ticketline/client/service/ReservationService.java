package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;

import java.util.List;

public interface ReservationService {

    /**
     * Find all transactions with status bought or reserved.
     *
     * @param page The requested page number
     * @return ordered list of all details with status bought or reserved
     */
    List<DetailedTicketTransactionDTO> findTransactionsBoughtReserved(int page) throws ExceptionWithDialog;

    /**
     * Find a single transaction entry by id (which can be partial).
     *
     * @param id  The id of the transaction entry
     * @param page The requested page number
     * @return the Transaction/Reservation entry
     */
    List<DetailedTicketTransactionDTO> findTransactionWithID(String id, int page) throws ExceptionWithDialog;

    /**
     * Finds a Transaction/Reservation by the customer Name (First and Last name) and the performance name.
     *
     * @param customerFirstName the customer first name to search for
     * @param customerLastName the customer last name to search for
     * @param performanceName the performance name to search for
     * @param page The requested page number
     * @return list of transactions/details for the customer and the performance name
     */
    List<DetailedTicketTransactionDTO> findTransactionsByCustomerAndPerformance(
        String customerFirstName, String customerLastName, String performanceName, int page)
        throws ExceptionWithDialog;
}
