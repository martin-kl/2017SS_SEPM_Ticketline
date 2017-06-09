package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketHistoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Profile("generateData")
@Component
public class TicketHistoryDataGenerator {

    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;

    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // injected so we make sure that tickets are loaded
    @Autowired
    private TicketDataGenerator ticketDataGenerator;
    @Autowired
    private CustomerDataGenerator customerDataGenerator;

    @PostConstruct
    private void generateTicketHistory() {
        if (ticketHistoryRepository.count() > 0) {
            log.info("ticket history already generated");
        } else {
            // get all data
            List<Ticket> tickets = ticketRepository.findAll();
            List<Customer> customers = customerRepository.findAll();

            // reserve a few tickets
            List<Ticket> ticketsToReserve = new ArrayList<>();
            for (int i = 0; i < tickets.size(); i++) {
                if ((i % 7) == 1) {
                    ticketsToReserve.add(tickets.get(i));
                }
            }
            reserveTickets(ticketsToReserve, customers);

            // reserve a few tickets and then buy them
            List<Ticket> ticketsToReserveAndBuy = new ArrayList<>();
            for (int i = 0; i < tickets.size(); i++) {
                if ((i % 7) == 2) {
                    ticketsToReserveAndBuy.add(tickets.get(i));
                }
            }
            reserveAndBuyTickets(ticketsToReserveAndBuy, customers);

            // buy a few tickets and then storno them
            List<Ticket> ticketsToBuyAndStorno = new ArrayList<>();
            for (int i = 0; i < tickets.size(); i++) {
                if ((i % 7) == 3) {
                    ticketsToBuyAndStorno.add(tickets.get(i));
                }
            }
            buyAndStornoTickets(ticketsToBuyAndStorno, customers);

            // buy a few tickets and then storno them
            List<Ticket> ticketsToBuyStornoAndReserve = new ArrayList<>();
            for (int i = 0; i < tickets.size(); i++) {
                if ((i % 7) == 4) {
                    ticketsToBuyStornoAndReserve.add(tickets.get(i));
                }
            }
            //buyStornoAndReserveTickets(ticketsToBuyAndStorno, customers);
            buyStornoAndReserveTickets(ticketsToBuyStornoAndReserve, customers);

        }
    }

    private void reserveTickets(List<Ticket> tickets, List<Customer> customers) {
        int randomTicketCount = (int) (Math.random() * 5 + 1);
        List<Ticket> tmpTicketList = new ArrayList<>();

        for (Ticket ticket : tickets) {

            tmpTicketList.add(ticket);

            if (tmpTicketList.size() == randomTicketCount) {
                int randomCustomerIndex = (int) (Math.random() * customers.size());
                createTicketTransaction(
                    tmpTicketList,
                    customers.get(randomCustomerIndex),
                    TicketStatus.RESERVED
                );

                randomTicketCount = (int) (Math.random() * 5 + 1);
                tmpTicketList = new ArrayList<>();
            }
        }
    }

    private void reserveAndBuyTickets(List<Ticket> tickets, List<Customer> customers) {
        int randomTicketCount = (int) (Math.random() * 5 + 1);
        List<Ticket> tmpTicketList = new ArrayList<>();

        for (Ticket ticket : tickets) {
            tmpTicketList.add(ticket);

            if (tmpTicketList.size() == randomTicketCount) {
                int randomCustomerIndex = (int) (Math.random() * customers.size());
                createTicketTransaction(
                    tmpTicketList,
                    customers.get(randomCustomerIndex),
                    TicketStatus.RESERVED
                );
                createTicketTransaction(
                    tmpTicketList,
                    customers.get(randomCustomerIndex),
                    TicketStatus.BOUGHT,
                    true
                );

                randomTicketCount = (int) (Math.random() * 5 + 1);
                tmpTicketList = new ArrayList<>();
            }
        }
    }

    private void buyAndStornoTickets(List<Ticket> tickets, List<Customer> customers) {
        int randomTicketCount = (int) (Math.random() * 5 + 1);
        List<Ticket> tmpTicketList = new ArrayList<>();

        for (Ticket ticket : tickets) {
            tmpTicketList.add(ticket);

            if (tmpTicketList.size() == randomTicketCount) {
                int randomCustomerIndex = (int) (Math.random() * customers.size());
                createTicketTransaction(
                    tmpTicketList,
                    customers.get(randomCustomerIndex),
                    TicketStatus.BOUGHT
                );
                createTicketTransaction(
                    tmpTicketList,
                    customers.get(randomCustomerIndex),
                    TicketStatus.STORNO,
                    true
                );

                randomTicketCount = (int) (Math.random() * 5 + 1);
                tmpTicketList = new ArrayList<>();
            }
        }
    }

    private void buyStornoAndReserveTickets(List<Ticket> tickets, List<Customer> customers) {
        int randomTicketCount = (int) (Math.random() * 5 + 1);
        List<Ticket> tmpTicketList = new ArrayList<>();

        for (Ticket ticket : tickets) {
            tmpTicketList.add(ticket);

            if (tmpTicketList.size() == randomTicketCount) {
                int randomCustomerIndex = (int) (Math.random() * customers.size());
                createTicketTransaction(
                    tmpTicketList,
                    customers.get(randomCustomerIndex),
                    TicketStatus.BOUGHT
                );
                createTicketTransaction(
                    tmpTicketList,
                    customers.get(randomCustomerIndex),
                    TicketStatus.STORNO
                );
                // set new customer (or randomly the same person)
                randomCustomerIndex = (int) (Math.random() * customers.size());
                createTicketTransaction(
                    tmpTicketList,
                    customers.get(randomCustomerIndex),
                    TicketStatus.RESERVED,
                    true
                );

                randomTicketCount = (int) (Math.random() * 5 + 1);
                tmpTicketList = new ArrayList<>();
            }
        }
    }

    private void createTicketTransaction(List<Ticket> ticketList, Customer customer, TicketStatus status) {
        createTicketTransaction(ticketList, customer, status, false);
    }

    private void createTicketTransaction(List<Ticket> ticketList, Customer customer, TicketStatus status, boolean setOutdate) {
        TicketTransaction ticketTransaction = TicketTransaction.builder()
            .customer(customer)
            .status(status)
            .outdated(!setOutdate)
            .build();
        ticketTransactionRepository.save(ticketTransaction);
        for (Ticket ticket : ticketList) {
            TicketHistory ticketHistory = TicketHistory.builder()
                .ticket(ticket)
                .ticketTransaction(ticketTransaction)
                .build();
            ticketHistoryRepository.save(ticketHistory);
        }
    }
}
