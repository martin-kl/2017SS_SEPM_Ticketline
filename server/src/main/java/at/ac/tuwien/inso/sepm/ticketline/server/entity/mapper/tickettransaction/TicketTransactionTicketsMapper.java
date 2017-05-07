package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickettransaction;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public abstract class TicketTransactionTicketsMapper implements TicketTransactionMapper {

    @Autowired
    private TicketTransactionMapper delegate;

    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public DetailedTicketTransactionDTO fromEntity(TicketTransaction ticketTransaction) {
        DetailedTicketTransactionDTO dto = delegate.fromEntity( ticketTransaction );

        dto.setTickets(
            ticketTransaction
                .getTicketHistories()
                .stream()
                .map(t -> t.getTicket())
                .map(ticketMapper::toDTO)
                .collect(Collectors.toList())
        );

        if(ticketTransaction.getTicketHistories().size() > 0) {
            for(TicketHistory history : ticketTransaction.getTicketHistories()) {
                dto.setPerformanceName(history.getTicket().getPerformance().getName());
                return dto;
            }
        }
        dto.setPerformanceName("no name found");
        return dto;
    }

}
