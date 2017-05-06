package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.EventArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventArtist;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface EventArtistMapper {
    EventArtistDTO fromEntity(EventArtist eventArtist);

    List<EventArtistDTO> fromEntity(List<EventArtist> all);
}
