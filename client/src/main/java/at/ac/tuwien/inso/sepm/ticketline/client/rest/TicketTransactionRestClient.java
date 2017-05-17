package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;
import java.util.UUID;

public interface TicketTransactionRestClient {

    /**
     * Find all details with a given status.
     *
     * @param status The details that are returned have this status
     * @return ordered list of all details
     */
    //not needed now
    //List<DetailedTicketTransactionDTO> findTransactionsWithStatus(String status) throws ExceptionWithDialog;

    /**
     * Find all transactions with status bought or reserved.
     *
     * @return ordered list of all transactions with status bought or reserved
     */
    List<DetailedTicketTransactionDTO> findTransactionsBoughtReserved(int page) throws ExceptionWithDialog;

    /**
     * Find a single transaction detail entry by id.
     *
     * @param id of the transacion detail entry (can be partial)
     * @return the transaction entry
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
