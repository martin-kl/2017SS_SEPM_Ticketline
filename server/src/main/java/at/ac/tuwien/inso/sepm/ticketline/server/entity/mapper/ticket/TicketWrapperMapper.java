package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketWrapperDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.TicketWrapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = TicketMapper.class, componentModel = "spring")
public interface TicketWrapperMapper {
    TicketWrapperDTO fromEntity(TicketWrapper ticketWrapper);
    List<TicketWrapperDTO> fromEntity(List<TicketWrapper> ticketWrapperList);
}
