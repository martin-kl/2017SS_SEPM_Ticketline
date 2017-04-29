package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {

    /**
     * Simply returns a list of Transactions for a given status
     *
     * @param status of the transactions
     * @param pageable a spring pageable
     * @return a list of TicketTransactions
     */
    List<TicketTransaction> getAllTransactions(String status, Pageable pageable);
}
