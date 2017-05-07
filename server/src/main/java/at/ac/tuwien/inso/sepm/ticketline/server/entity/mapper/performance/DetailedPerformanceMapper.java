package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location.LocationMapper;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(uses = LocationMapper.class, componentModel = "spring")
@DecoratedWith(DetailedPerformanceMapperDecorator.class)
public interface DetailedPerformanceMapper {
    DetailedPerformanceDTO fromEntity(Performance performance);
    Performance fromDTO(DetailedPerformanceDTO detailedPerformanceDTO);
}
