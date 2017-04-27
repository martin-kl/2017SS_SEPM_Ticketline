package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickettransaction;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TicketTransactionMapper {

    TicketTransaction fromDTO(DetailedTicketTransactionDTO ticketHistoryDTO);

    DetailedTicketTransactionDTO fromEntity(TicketTransaction one);

}