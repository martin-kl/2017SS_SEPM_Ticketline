package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    Artist fromDTO(ArtistDTO artistDTO);
    ArtistDTO fromEntity(Artist artist);
}
