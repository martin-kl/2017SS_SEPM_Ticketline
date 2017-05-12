package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ConflictException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketHistoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus.STORNO;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;

    @Override
    public List<TicketTransaction> getAllTransactions(String status, Pageable pageable) {
        TicketStatus ticketStatus;
        try {
            ticketStatus = TicketStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            log.error("Bad status for TicketStatus - status is {}", status);
            throw new BadRequestException("Bad status");
        }

        return ticketTransactionRepository.findByStatus(ticketStatus, pageable);
    }

    @Override
    public List<TicketTransaction> getAllBoughtReservedTransactions(Pageable pageable) {
        return ticketTransactionRepository
            .findByStatusOrStatusOrderByIdDesc(TicketStatus.BOUGHT, TicketStatus.RESERVED, pageable);
        //return ticketTransactionRepository.findByStatus(ticketStatus);
    }

    @Override
    public TicketTransaction findTransactionsByID(UUID id) {
        Optional<TicketTransaction> transaction = ticketTransactionRepository.findOneById(id);
        if(transaction == null)
            throw new NotFoundException();
        if (!transaction.isPresent()) {
            throw new NotFoundException();
        }
        return transaction.get();
    }

    @Override
    public List<TicketTransaction> findTransactionsByCustomerAndLocation(
        String customerFirstName,
        String customerLastName,
        String performance) {

        List<TicketTransaction> result = ticketTransactionRepository
            .findByCustomerAndLocation(
                "%" + customerFirstName + "%",
                "%" + customerLastName + "%",
                performance
            );

        //filter all double elements
        List<TicketTransaction> filteredList = new ArrayList<>();
        for(TicketTransaction tt : result) {
            if(filteredList.size() > 0) {
                if (tt.getId() != filteredList.get(filteredList.size() - 1).getId()) {
                    filteredList.add(tt);
                }
            }else {
                filteredList.add(tt);
            }
        }
        return filteredList;
    }

    @Override
    public TicketTransaction setTransactionStatus(DetailedTicketTransactionDTO ticketTransaction) {

        // collect all tickets that should be in the transaction
        List<Ticket> ticketList = ticketTransaction
            .getTickets()
            .stream()
            .map(tt -> ticketRepository.findOne(tt.getId()))
            .collect(Collectors.toList());

        // if there is at least 1 ticket that is in a wrong transaction
        // throw a conflict exception
        // also if the change from status is bad
        for (Ticket t : ticketList) {
            Optional<TicketTransaction> tto = ticketTransactionRepository.findTransactionForTicket(t.getId());
            if (!isInTransaction(tto, ticketTransaction.getId())) {
                throw new ConflictException();
            }
        }

        Customer customer = ticketTransaction.getCustomer() != null
            ? customerRepository.findOne(ticketTransaction.getCustomer().getId())
            : null;

        TicketTransaction tt = null;

        // create a new transaction
        if (ticketTransaction.getId() == null) {

            if (ticketTransaction.getStatus() == STORNO) {
                throw new BadRequestException();
            }

            tt = TicketTransaction.builder()
                .status(ticketTransaction.getStatus())
                .customer(customer)
                .build();

            return ticketTransactionRepository.save(tt);
        } else {
            // update the transaction
            tt = ticketTransactionRepository.findOne(ticketTransaction.getId());

            // check if the ticket count is valid
            if (tt.getTicketHistories().size() != ticketTransaction.getTickets().size()) {
                throw new BadRequestException();
            }

            // transition BOUGHT to RESERVED not allowed
            if (tt.getStatus() == TicketStatus.BOUGHT && ticketTransaction.getStatus() == TicketStatus.RESERVED) {
                throw new BadRequestException();
            }

            // transition RESERVED to STORNO removes the transaction
            if (tt.getStatus() == TicketStatus.BOUGHT && ticketTransaction.getStatus() == TicketStatus.RESERVED) {
                ticketTransactionRepository.delete(tt);
                tt.setStatus(TicketStatus.STORNO);
                return tt;
            }

            tt.setStatus(ticketTransaction.getStatus());
            tt.setCustomer(customer);

            tt = ticketTransactionRepository.save(tt);
        }

        // some weird error happened
        if (tt == null) {
            log.error("TicketTransaction could not be created or updated. {}", ticketTransaction);
            throw new RuntimeException();
        }

        for (Ticket t : ticketList) {
            TicketHistory th = TicketHistory.builder()
                .ticket(t)
                .ticketTransaction(tt)
                .build();
            ticketHistoryRepository.save(th);
        }

        return ticketTransactionRepository.findOne(tt.getId());
    }

    /**
     * checks if a given transaction is comparable and assignable to the given transaction
     *
     * @param toCheck Optional transaction
     * @param compareTransactionId id of the transaction to check
     * @return returns true if the transaction is allowed
     */
    private boolean isInTransaction(Optional<TicketTransaction> toCheck, UUID compareTransactionId) {
        if (compareTransactionId == null) {
            return !toCheck.isPresent() || toCheck.get().getStatus() == STORNO;
        } else {
            if (!toCheck.isPresent()) {
                return false;
            }
            TicketTransaction tt = toCheck.get();
            return tt.getId() == compareTransactionId && tt.getStatus() != TicketStatus.BOUGHT;
        }
    }
}
