package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location.LocationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/location")
@Api(value = "location")
public class LocationEndpoint {
    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationMapper locationMapper;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation(value = "Search for location, which match the given string search criteria")
    public List<LocationDTO> search(@RequestBody LocationDTO locationDTO){
        return locationService
            .search(locationMapper.fromDTO(locationDTO))
            .stream()
            .map(locationMapper::fromEntity)
            .collect(Collectors.toList());
    }
}
