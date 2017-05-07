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
     * Finds a Transaction by the Customer name and the performance name.
     * @param customerName the customer name to search for
     * @param performanceName the performance name to search for
     * @return list of transactions for the customer and the performance name
     */
    List<DetailedTicketTransactionDTO> findTransactionsByCustomerAndPerformance(String customerName, String performanceName)
        throws ExceptionWithDialog;
}
