package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location;

import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LocationMapper {

    Location fromDTO(LocationDTO locationDTO);

    LocationDTO fromEntity(Location location);

    List<LocationDTO> fromEntity(List<Location> all);

}
