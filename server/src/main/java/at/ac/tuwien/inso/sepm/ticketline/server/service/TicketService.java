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
     * Simply returns a list of all bought and reserved Transactions
     *
     * @return a list of TicketTransactions
     */
    List<TicketTransaction> getAllBoughtReservedTransactions(Pageable pageable);

    /**
     * Returns a list of Transactions for a customer and a performance
     *
     * @param uuid the id of the transaction
     * @return a list of TicketTransactions
     */
    TicketTransaction findTransactionsByID(UUID uuid);

    /**
     * Returns a list of Transactions for a customer and a performance
     *
     * @param customerFirstName the customer first name to search for
     * @param customerLastName the customer last name to search for
     * @param performance the name of the performance
     * @return a list of TicketTransactions
     */
    List<TicketTransaction> findTransactionsByCustomerAndLocation(String customerFirstName,
        String customerLastName, String performance);

    /**
     * trys to update the ticket transaction. if the transaction id is null, then a
     * a transaction will be created (after verification of all tickets), otherwise
     * we try to update the status
     *
     * @param ticketTransaction
     * @return returns the updated transaction
     */
    TicketTransaction setTransactionStatus(DetailedTicketTransactionDTO ticketTransaction);
}
