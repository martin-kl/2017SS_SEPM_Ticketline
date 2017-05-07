package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.DetailedPerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/performance")
@Api(value = "performance")
public class PerformanceEndpoint {
    private final PerformanceService performanceService;
    private final DetailedPerformanceMapper detailedPerformanceMapper;

    public PerformanceEndpoint(PerformanceService performanceService,
                               DetailedPerformanceMapper detailedPerformanceMapper){
        this.performanceService = performanceService;
        this.detailedPerformanceMapper = detailedPerformanceMapper;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific performance")
    public PerformanceDTO findOne(@PathVariable UUID id){
        return detailedPerformanceMapper.fromEntity(performanceService.findOne(id));
    }
}
