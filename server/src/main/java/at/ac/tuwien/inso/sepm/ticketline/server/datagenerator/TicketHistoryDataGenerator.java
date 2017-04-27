package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketHistoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
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
            buyStornoAndReserveTickets(ticketsToBuyAndStorno, customers);

        }
    }

    private void reserveTickets(List<Ticket> tickets, List<Customer> customers) {
        for (Ticket ticket : tickets) {
            int randomCustomerIndex = (int) (Math.random() * customers.size());
            createTicket(
                ticket,
                customers.get(randomCustomerIndex),
                TicketHistory.Status.RESERVED
            );
        }
    }

    private void reserveAndBuyTickets(List<Ticket> tickets, List<Customer> customers) {
        for (Ticket ticket : tickets) {
            int randomCustomerIndex = (int) (Math.random() * customers.size());
            createTicket(
                ticket,
                customers.get(randomCustomerIndex),
                TicketHistory.Status.RESERVED
            );
            createTicket(
                ticket,
                customers.get(randomCustomerIndex),
                TicketHistory.Status.BOUGHT
            );

        }
    }

    private void buyAndStornoTickets(List<Ticket> tickets, List<Customer> customers) {
        for (Ticket ticket : tickets) {
            int randomCustomerIndex = (int) (Math.random() * customers.size());
            createTicket(
                ticket,
                customers.get(randomCustomerIndex),
                TicketHistory.Status.BOUGHT
            );
            createTicket(
                ticket,
                customers.get(randomCustomerIndex),
                TicketHistory.Status.STORNO
            );
        }
    }

    private void buyStornoAndReserveTickets(List<Ticket> tickets, List<Customer> customers) {
        for (Ticket ticket : tickets) {
            int randomCustomerIndex = (int) (Math.random() * customers.size());
            createTicket(
                ticket,
                customers.get(randomCustomerIndex),
                TicketHistory.Status.BOUGHT
            );
            createTicket(
                ticket,
                customers.get(randomCustomerIndex),
                TicketHistory.Status.STORNO
            );
            // set new customer (or randomly the same person)
            randomCustomerIndex = (int) (Math.random() * customers.size());
            createTicket(
                ticket,
                customers.get(randomCustomerIndex),
                TicketHistory.Status.RESERVED
            );
        }
    }

    private void createTicket(Ticket ticket, Customer customer, TicketHistory.Status status) {
        TicketHistory ticketHistory = TicketHistory.builder()
            .ticket(ticket)
            .customer(customer)
            .status(status)
            .build();
        ticketHistoryRepository.save(ticketHistory);
    }

}
