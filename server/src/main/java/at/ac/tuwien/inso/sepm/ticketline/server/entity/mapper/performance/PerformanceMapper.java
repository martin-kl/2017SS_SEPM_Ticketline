package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location.LocationMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = LocationMapper.class, componentModel = "spring")
public interface PerformanceMapper {
    Performance fromDTO(PerformanceDTO performance);
    List<Performance> fromDTO(List<PerformanceDTO> performances);

    PerformanceDTO fromEntity(Performance performance);
    List<PerformanceDTO> fromEntity(List<Performance> all);
}
