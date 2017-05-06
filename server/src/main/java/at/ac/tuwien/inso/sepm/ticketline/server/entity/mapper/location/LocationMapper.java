package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location;

import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SeatLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SectorLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatLocation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    default LocationDTO fromEntity(Location location) {
        if(location instanceof SeatLocation)
            return fromEntity((SeatLocation) location);
        return fromEntity((SectorLocation) location);
    }

    default Location fromDTO(LocationDTO locationDTO){
        if(locationDTO instanceof SeatLocationDTO)
            return fromDTO((SeatLocationDTO) locationDTO);
        return fromDTO((SectorLocationDTO) locationDTO);
    }

    SeatLocationDTO fromEntity(SeatLocation location);
    SectorLocationDTO fromEntity(SectorLocation location);

    SeatLocation fromDTO(SeatLocationDTO seatLocation);
    SectorLocation fromDTO(SectorLocationDTO sectorLocation);
}
