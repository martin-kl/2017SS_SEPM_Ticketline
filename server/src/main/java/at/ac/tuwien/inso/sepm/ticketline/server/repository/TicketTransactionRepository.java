package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
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
     *
     * Returns only the first 100 TicketTransactions with the given status
     * @param status The status, that the transactions have to have
     * @return The first 100 TicketTransactions with the status
     */
    List<TicketTransaction> findTop100ByStatus(TicketStatus status);

}
