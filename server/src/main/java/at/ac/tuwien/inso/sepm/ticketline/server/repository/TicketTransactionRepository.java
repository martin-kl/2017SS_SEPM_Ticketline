package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketTransactionRepository extends JpaRepository<TicketTransaction, UUID> {

    /**
     * Returns all TicketTransactions with the given status
     * @param status The status, that the transactions have to have
     * @return All TicketTransactions with the status
     */
    List<TicketTransaction> findByStatus(TicketStatus status);

    /**
     * Returns only the first 100 TicketTransactions with the given status
     * @param status The status, that the transactions have to have
     * @return The first 100 TicketTransactions with the status
     */
    List<TicketTransaction> findTop100ByStatus(TicketStatus status);


     /**
     * Returns the Transaction with the id of the parameter
     * @param id The id of the transaction
     * @return the Transaction with the id of the parameter
     */
    Optional<TicketTransaction> findOneById(UUID id);

     /**
     * Returns a list of Transactions for a customer and a performance
     *
     * @param customerName the name of the customer who bought/reserved a ticket
     * @param performance the name of the performance
     * @return a list of TicketTransactions
     */
     //TODO custom implementation
    //List<TicketTransaction> findByCustomerAndLocation(String customerName, String performance);
}
