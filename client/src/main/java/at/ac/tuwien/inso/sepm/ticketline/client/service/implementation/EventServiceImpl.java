package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRestClient eventRestClient;

    public EventServiceImpl(EventRestClient eventRestClient){
        this.eventRestClient = eventRestClient;
    }

    @Override
    public List<EventDTO> findAll(int page) throws DataAccessException {
        return eventRestClient.findAll(page);
    }
}
