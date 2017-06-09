package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketTransactionRepository extends JpaRepository<TicketTransaction, Long> {

    /**
     * Returns all TicketTransactions with the given status
     *
     * @param status The status, that the details have to have
     * @return All TicketTransactions with the status
     */
    List<TicketTransaction> findByStatus(TicketStatus status, Pageable pageable);

    /**
     * Returns all ticket transactions with status either status1 oder status2
     *
     * @param status1 The first status the transactions can have
     * @param status2 The second status the transactions can have
     * @param pageable The requested page
     * @return All ticket transactions with status either status1 oder status2
     */
    List<TicketTransaction> findByStatusOrStatusOrderByLastModifiedAtDesc(TicketStatus status1,
        TicketStatus status2, Pageable pageable);

    /**
     * Returns all ticket transactions with status either status1 oder status2
     *
     * @param pageable The requested page
     * @return All ticket transactions with status either status1 oder status2
     */
    @Query("SELECT tt FROM TicketTransaction tt WHERE tt.outdated = FALSE OR status = 'STORNO' ORDER BY tt.lastModifiedAt DESC")
    List<TicketTransaction> findAllValidTransactions(Pageable pageable);

    /**
     * Returns the Transaction with the id of the parameter
     *
     * @param id The id of the transaction
     * @return the Transaction with the id of the parameter
     */
    Optional<TicketTransaction> findOneById(Long id);

    /**
     * Returns the transaction with a (at least partial) match in the id column.
     *
     * @param id The id to search for
     * @param leftLimit The left limit (lower limit) for the result
     * @param rightLimit The right limit (upper limit) for the result
     * @return A list of transactions with the id (or parts of it)
     */
    @Query(value = "SELECT * from ticket_transaction tt WHERE tt.id like ?1 order by tt.id LIMIT ?2,?3", nativeQuery = true)
    List<TicketTransaction> findWithPartialId(String id, int leftLimit, int rightLimit);

    /**
     * Returns a list of Transactions for a customer and a performance
     *
     * @param customerFirstName the customer first name to search for
     * @param customerLastName the customer last name to search for
     * @param performance the name of the performance
     * @param pageable The requested page
     * @return a list of TicketTransactions
     */
    @Query("SELECT tt FROM TicketTransaction tt join tt.customer c join tt.ticketHistories th join th.ticket t join t.performance p WHERE (c.firstName LIKE ?1 AND c.lastName LIKE ?2) AND p.name = ?3 order by tt.id")
    List<TicketTransaction> findByCustomerAndLocation(String customerFirstName,
        String customerLastName, String performance,
        Pageable pageable);

    /**
     * Returns a list (should be always of size 0 or 1) for the tickettransaction of a ticket
     *
     * @param ticketId
     * @return list of ticket transactions
     */
    @Query("SELECT tt FROM TicketTransaction tt JOIN tt.ticketHistories th" +
        " WHERE th.ticket.id = ?1 AND tt.outdated = FALSE ORDER BY th.lastModifiedAt DESC")
    List<TicketTransaction> findTransactionForTicket(UUID ticketId);

}
