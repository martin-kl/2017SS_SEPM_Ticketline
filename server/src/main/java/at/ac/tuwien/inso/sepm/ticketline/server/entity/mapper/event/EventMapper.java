package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(uses = PerformanceMapper.class, componentModel = "spring")
public interface EventMapper {
    Event fromDTO(EventDTO eventDTO);
    List<Event> fromDTO(List<EventDTO> eventDTO);

    EventDTO fromEntity(Event event);
    List<EventDTO> fromEntity(List<Event> all);
}
