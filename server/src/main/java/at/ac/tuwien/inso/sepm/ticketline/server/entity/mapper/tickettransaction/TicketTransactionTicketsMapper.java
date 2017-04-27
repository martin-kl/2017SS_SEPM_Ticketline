package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickettransaction;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import org.mapstruct.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// @Component
public class TicketTransactionTicketsMapper {

    /*
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tickets {
    }

    @Tickets
    public List<TicketDTO> mapHistoryToTickets(Set<TicketHistory> ticketHistories) {
        return ticketHistories
            .stream()
            .map(th -> ticketRepository.findOne(th.getTicket().getId()))
            .map(ticketMapper::toDTO)
            .collect(Collectors.toList());
    }
    */
}
