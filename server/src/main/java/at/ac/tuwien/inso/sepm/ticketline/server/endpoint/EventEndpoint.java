package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventSearchMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/event")
@Api(value = "event")
public class EventEndpoint {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventSearchMapper eventSearchMapper;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Gets a list of all Events")
    public List<EventDTO> findAll(Pageable pageable) {
        return eventService
            .findAll(pageable)
            .stream()
            .map(eventMapper::fromEntity)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation(value = "Search for events, which match the given search criteria")
    public List<EventDTO> search(@RequestBody EventSearchDTO eventSearchDTO, Pageable pageable) {

        return eventService
            .search(eventSearchMapper.fromDTO(eventSearchDTO), pageable)
            .stream()
            .map(eventMapper::fromEntity)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "topten", method = RequestMethod.GET)
    @ApiOperation(value = "Get Top Ten Events")
    public Map<Integer, EventDTO> topTen(@RequestParam(value = "category") String category,
        @RequestParam(value = "monthsInPast") int monthsInPast) {
        Map<Integer, EventDTO> dtos = new HashMap<>();
        for (Map.Entry<Integer, Event> entry : eventService.getTopTen(category, monthsInPast)
            .entrySet()) {
            dtos.put(entry.getKey(), eventMapper.fromEntity(entry.getValue()));
        }
        return dtos;
    }
}
