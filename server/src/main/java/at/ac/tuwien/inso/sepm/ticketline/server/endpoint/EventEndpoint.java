package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/event")
@Api(value = "event")
public class EventEndpoint {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Gets a list of all Events")
    public List<EventDTO> findAll(Pageable pageable) {
        return eventService
            .findAll(pageable)
            .stream()
            .map(eventMapper::fromEntity)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Search for events, which match the given search criteria")
    public List<EventDTO> search(Pageable pageable,
                                 @RequestParam(value = "eventName", required = false) String eventName,
                                 @RequestParam(value = "description", required = false) String description,
                                 @RequestParam(value = "performanceType", required = false) String performanceType,
                                 @RequestParam(value = "performanceDuration", required = false) Duration performanceDuration,
                                 @RequestParam(value = "performanceDate", required = false) LocalDate performanceDate,
                                 @RequestParam(value = "performanceDateTime", required = false) LocalDateTime performanceDateTime,
                                 @RequestParam(value = "performanceTicketPrice", required = false) BigDecimal performanceTicketPrice,
                                 @RequestParam(value = "performanceLocation", required = false) UUID locationUUID,
                                 @RequestParam(value = "artist", required = false) UUID artistUUID
                                 ){
        /*return eventService
            .findAll(pageable)
            .stream()
            .map(eventMapper::fromEntity)
            .collect(Collectors.toList());*/
        return null;
    }

}
