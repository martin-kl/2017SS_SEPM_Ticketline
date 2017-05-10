package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SectorMapper {
    Sector fromDTO(SectorDTO sectorDTO);
    SectorDTO fromEntity(Sector sector);
}
