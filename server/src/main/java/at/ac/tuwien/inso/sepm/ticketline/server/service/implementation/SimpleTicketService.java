package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketHistoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class SimpleTicketService implements TicketService {

    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public TicketHistory reserveTicket(Ticket ticket, Customer customer) {
        // verify if the ticket can be reserved
        Optional<TicketHistory> presentTicketHistory = ticketHistoryRepository.findLatestTicketHistory(ticket);
        if (presentTicketHistory.isPresent()) {
            TicketHistory ticketHistory = presentTicketHistory.get();
            if (ticketHistory.getStatus().equals(TicketStatus.BOUGHT)) {
                throw new BadRequestException("Ticket already sold");
            }
            if (ticketHistory.getStatus().equals(TicketStatus.RESERVED)) {
                throw new BadRequestException("Ticket already reserved");
            }
        }
        // ticket can be reserved, so lets do it
        TicketHistory ticketHistory = TicketHistory.builder()
            .status(TicketStatus.RESERVED)
            .customer(customer)
            .ticket(ticket)
            .build();
        return ticketHistoryRepository.save(ticketHistory);
    }
}
