package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketTransactionRepository extends JpaRepository<TicketTransaction, UUID> {

    /**
     * Returns all TicketTransactions with the given status
     *
     * @param status The status, that the transactions have to have
     * @return All TicketTransactions with the status
     */
    List<TicketTransaction> findByStatus(TicketStatus status);

    /**
     * Returns only the first 100 TicketTransactions with the given status
     *
     * @param status The status, that the transactions have to have
     * @return The first 100 TicketTransactions with the status
     */
    List<TicketTransaction> findTop100ByStatus(TicketStatus status);

    /**
     * Returns only the first 100 TicketTransactions with status bought or reserved
     *
     * @return The first 100 TicketTransactions with status bought or reserved
     */
    List<TicketTransaction> findTop100ByStatusOrStatusOrderByIdDesc(TicketStatus status1,
        TicketStatus status2);

    /**
     * Returns the Transaction with the id of the parameter
     *
     * @param id The id of the transaction
     * @return the Transaction with the id of the parameter
     */
    Optional<TicketTransaction> findOneById(UUID id);

    /**
     * Returns a list of Transactions for a customer and a performance
     *
     * @param customerFirstName the customer first name to search for
     * @param customerLastName the customer last name to search for
     * @param performance the name of the performance
     * @return a list of TicketTransactions
     */
    @Query("SELECT tt FROM TicketTransaction tt join tt.customer c join tt.ticketHistories th join th.ticket t join t.performance p WHERE (c.firstName LIKE ?1 OR c.lastName LIKE ?2) AND p.name = ?3 order by tt.id")
    List<TicketTransaction> findByCustomerAndLocation(String customerFirstName,
        String customerLastName, String performance);

}
