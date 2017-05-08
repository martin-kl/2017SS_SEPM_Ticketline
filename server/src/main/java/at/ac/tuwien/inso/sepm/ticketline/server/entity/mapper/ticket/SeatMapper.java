package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SeatMapper {
    Seat fromDTO(SeatDTO seatDTO);
    SeatDTO fromEntity(Seat seat);
}
