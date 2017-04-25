package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatTicket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorTicket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TicketMapper {

    /*
    default TicketDTO toDTO(Ticket ticket) {
        if(ticket instanceof SeatTicket)
            return toSeatTicketDTO((SeatTicket) ticket);
        return toSectorTicketDTO((SectorTicket) ticket);
    }

    SeatTicketDTO toSeatTicketDTO();

    TicketDTO fromEntity(Ticket one);
    */
}
