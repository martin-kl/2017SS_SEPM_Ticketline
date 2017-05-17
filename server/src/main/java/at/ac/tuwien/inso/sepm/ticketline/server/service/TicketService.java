package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {

    /**
     * Simply returns a list of Transactions for a given status
     *
     * @param status of the details
     * @return a list of TicketTransactions
     */
    List<TicketTransaction> getAllTransactions(String status, Pageable pageable);

    /**
     * Simply returns a list of all bought and reserved Transactions.
     *
     * @param pageable The requested page
     * @return a list of TicketTransactions
     */
    List<TicketTransaction> getAllBoughtReservedTransactions(Pageable pageable);

    /**
     * Returns the Transaction with the id.
     *
     * @param uuid the id of the transaction
     * @return the transaction with the id
     */
    TicketTransaction findTransactionsByID(UUID uuid);

    /**
     * Returns a (paged) list of transactions with the (partial) id.
     *
     * @param id the id to search for
     * @param pageable The requested page
     * @return A list of transactions with the id (or parts of it)
     */
    List<TicketTransaction> findById(String id, Pageable pageable);

    /**
     * Returns a list of Transactions for a customer and a performance
     *
     * @param customerFirstName the customer first name to search for
     * @param customerLastName the customer last name to search for
     * @param performance the name of the performance
     * @param pageable The page to request
     * @return a list of TicketTransactions
     */
    List<TicketTransaction> findTransactionsByCustomerAndLocation(String customerFirstName,
        String customerLastName, String performance,
        Pageable pageable);

    /**
     * trys to update the ticket transaction. if the transaction id is null, then a
     * a transaction will be created (after verification of all tickets), otherwise
     * we try to update the status
     *
     * @param ticketTransaction The transaction to update / save
     * @return returns the updated transaction
     */
    TicketTransaction setTransactionStatus(DetailedTicketTransactionDTO ticketTransaction);
}
