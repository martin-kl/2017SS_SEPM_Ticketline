package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PerformanceMapper {

    Performance fromDTO(PerformanceDTO perforanceDTO);

    PerformanceDTO fromEntity(Performance performance);

    List<PerformanceDTO> fromEntity(List<Performance> all);

}
