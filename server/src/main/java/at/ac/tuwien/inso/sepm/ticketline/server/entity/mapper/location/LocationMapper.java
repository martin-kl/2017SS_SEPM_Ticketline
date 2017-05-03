package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location;

import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SeatLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SectorLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatLocation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorLocation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LocationMapper {

    default LocationDTO fromEntity(Location location) {
        if(location instanceof SeatLocation)
            return fromEntity((SeatLocation) location);
        return fromEntity((SectorLocation) location);
    }

    SeatLocationDTO fromEntity(SeatLocation location);
    SectorLocationDTO fromEntity(SectorLocation location);

}
