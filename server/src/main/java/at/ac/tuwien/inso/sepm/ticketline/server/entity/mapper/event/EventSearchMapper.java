package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.EventSearch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventSearchMapper {
    EventSearch fromDTO(EventSearchDTO eventSearchDTO);
    EventSearchDTO fromEntity(EventSearch eventSearch);
}
