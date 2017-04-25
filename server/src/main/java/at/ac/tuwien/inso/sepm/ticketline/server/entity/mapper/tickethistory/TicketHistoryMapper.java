package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickethistory;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketHistoryDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TicketHistoryMapper {

    TicketHistory fromDTO(TicketHistoryDTO ticketHistoryDTO);

    TicketHistoryDTO fromEntity(TicketHistory one);

}
