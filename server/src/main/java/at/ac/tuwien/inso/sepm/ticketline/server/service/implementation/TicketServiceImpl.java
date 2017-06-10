package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
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
import at.ac.tuwien.inso.sepm.ticketline.server.service.PaymentService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private PaymentService paymentService;

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
        return ticketTransactionRepository.findAllValidTransactions(pageable);
    }

    @Override
    public TicketTransaction findTransactionsByID(Long id) {
        Optional<TicketTransaction> transaction = ticketTransactionRepository.findOneById(id);
        if(transaction == null)
            throw new NotFoundException();
        if (!transaction.isPresent()) {
            throw new NotFoundException();
        }
        return transaction.get();
    }

    @Override
    public List<TicketTransaction> findById(String id, Pageable pageable) {
        id = "%" + id + "%";
        List<TicketTransaction> transactions = ticketTransactionRepository.findWithPartialId(id, pageable.getPageSize()*pageable.getPageNumber(), pageable.getPageSize()*pageable.getPageNumber()+pageable.getPageSize());
        if(transactions == null)
            throw new NotFoundException();
        if(transactions.size() == 0) {
            return new ArrayList<>(0);
        }
        return transactions;
        //return null;
    }


    @Override
    public List<TicketTransaction> findTransactionsByCustomerAndLocation(
        String customerFirstName,
        String customerLastName,
        String performance, Pageable pageable) {

        List<TicketTransaction> result = ticketTransactionRepository
            .findByCustomerAndLocation(
                "%" + customerFirstName + "%",
                "%" + customerLastName + "%",
                performance,
                pageable
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
    @Transactional
    public TicketTransaction setTransactionStatus(DetailedTicketTransactionDTO ticketTransaction) {

        // remove duplicates
        HashSet<TicketDTO> hs = new HashSet<>();
        hs.addAll(ticketTransaction.getTickets());
        ticketTransaction.setTickets(new ArrayList<>(hs));

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
            List<TicketTransaction> ttList = ticketTransactionRepository.findTransactionForTicket(t.getId());
            if (ttList.size() > 0 && !isValidTransaction(ttList.get(0), ticketTransaction.getId())) {
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

            tt = ticketTransactionRepository.save(tt);
        } else {
            // update the transaction
            tt = ticketTransactionRepository.findOne(ticketTransaction.getId());

            tt.setOutdated(true);
            tt = ticketTransactionRepository.save(tt);

            // check if the ticket count is valid
            if (tt.getTicketHistories().size() < ticketTransaction.getTickets().size()) {
                throw new BadRequestException();
            }

            // transition BOUGHT to RESERVED not allowed
            if (tt.getStatus() == TicketStatus.BOUGHT && ticketTransaction.getStatus() == TicketStatus.RESERVED) {
                throw new BadRequestException();
            }

            // check for old tickets and keep them in the old status
            List<TicketHistory> keepHistories = new ArrayList<>();
            for (TicketHistory th : tt.getTicketHistories()) {
                boolean found = false;
                for (Ticket ticket : ticketList) {
                    if (th.getTicket().getId().equals(ticket.getId())) {
                        found = true;
                    }
                }
                if (!found) {
                    keepHistories.add(th);
                }
            }

            // there are some old histories, so lets save as a new transaction
            if (!keepHistories.isEmpty()) {
                TicketTransaction keepTT = TicketTransaction.builder()
                    .customer(customer)
                    .status(tt.getStatus())
                    .paymentIdentifier(tt.getPaymentIdentifier())
                    .paymentProviderOption(tt.getPaymentProviderOption())
                    .refunded(tt.isRefunded())
                    .build();
                keepTT = ticketTransactionRepository.save(keepTT);
                for (TicketHistory oldTh : keepHistories) {
                    TicketHistory th = TicketHistory.builder()
                        .ticket(oldTh.getTicket())
                        .ticketTransaction(keepTT)
                        .build();
                    ticketHistoryRepository.save(th);
                }
            }

            // transition RESERVED to STORNO removes the transaction
            if (tt.getStatus() == TicketStatus.RESERVED && ticketTransaction.getStatus() == TicketStatus.STORNO) {
                ticketTransactionRepository.delete(tt);
                tt.setStatus(TicketStatus.STORNO);
                return tt;
            }

            // transition BOUGHT to STORNO opens repays the tickets
            if (tt.getStatus() == TicketStatus.BOUGHT && ticketTransaction.getStatus() == TicketStatus.STORNO) {
                paymentService.refund(tt.getId());
            }

            tt = TicketTransaction.builder()
                .customer(customer)
                .status(ticketTransaction.getStatus())
                .build();

            tt = ticketTransactionRepository.save(tt);
        }

        // some weird error happened
        if (tt == null) {
            log.error("TicketTransaction could not be created or updated. {}", ticketTransaction);
            throw new RuntimeException();
        }

        Set<TicketHistory> histories = new HashSet<>();

        for (Ticket t : ticketList) {
            TicketHistory th = TicketHistory.builder()
                .ticket(t)
                .ticketTransaction(tt)
                .build();
            histories.add(ticketHistoryRepository.save(th));
        }
        tt.setTicketHistories(histories);
        return tt;
    }

    /**
     * checks if a given transaction is comparable and assignable to the given transaction
     *
     * @param toCheck transaction
     * @param compareTransactionId id of the transaction to check
     * @return returns true if the transaction is allowed
     */
    private boolean isValidTransaction(TicketTransaction toCheck, Long compareTransactionId) {
        if (compareTransactionId == null) {
            return toCheck.getStatus() == STORNO;
        } else {
            return toCheck.getId().equals(compareTransactionId);
        }
    }
}
