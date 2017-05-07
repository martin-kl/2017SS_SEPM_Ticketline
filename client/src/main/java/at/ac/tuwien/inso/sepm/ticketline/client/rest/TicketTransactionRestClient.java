package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;
import java.util.UUID;

public interface TicketTransactionRestClient {

    /**
     * Find all transactions with a given status
     *
     * @param status The transactions that are returned have this status
     * @return ordered list of all transactions
     */
    //not needed now
    //List<DetailedTicketTransactionDTO> findTransactionsWithStatus(String status) throws ExceptionWithDialog;

    /**
     * Find all transactions with status bought or reserved
     *
     * @return ordered list of all transactions with status bought or reserved
     */
    List<DetailedTicketTransactionDTO> findTransactionsBoughtReserved() throws ExceptionWithDialog;

    /**
     * Find a single transactions entry by id.
     *
     * @param uuid the id of the transactions entry
     * @return the transaction entry
     */
    DetailedTicketTransactionDTO findTransactionWithID(UUID uuid) throws ExceptionWithDialog;

    /**
     * Finds a Transaction/Reservation by the
     *
     * @param customerFirstName the customer first name to search for
     * @param customerLastName the customer last name to search for
     * @param performanceName the performance name to search for
     * @return list of reservations/transactions for the customer and the performance name
     */
    List<DetailedTicketTransactionDTO> findTransactionsByCustomerAndPerformance(
        String customerFirstName, String customerLastName, String performanceName)
        throws ExceptionWithDialog;
}
