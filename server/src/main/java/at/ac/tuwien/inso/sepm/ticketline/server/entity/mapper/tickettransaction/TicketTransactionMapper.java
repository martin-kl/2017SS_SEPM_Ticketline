package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickettransaction;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@DecoratedWith(TicketTransactionTicketsMapper.class)
public interface TicketTransactionMapper {

    TicketTransaction fromDTO(DetailedTicketTransactionDTO ticketHistoryDTO);

    DetailedTicketTransactionDTO fromEntity(TicketTransaction one);

}