package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {

    /**
     * Simply returns a list of Transactions for a given status
     *
     * @param status of the transactions
     * @return a list of TicketTransactions
     */
    List<TicketTransaction> getAllTransactions(String status);

    /**
     * Simply returns a list of all bought and reserved Transactions
     *
     * @return a list of TicketTransactions
     */
    List<TicketTransaction> getAllBoughtReservedTransactions();

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
     * @param customer the name of the customer who bought/reserved a ticket
     * @param performance the name of the performance
     * @return a list of TicketTransactions
     */
    List<TicketTransaction> findTransactionsByCustomerAndLocation(String customer, String performance);
}
