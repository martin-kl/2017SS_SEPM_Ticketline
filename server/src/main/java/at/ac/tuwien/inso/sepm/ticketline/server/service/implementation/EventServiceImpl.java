package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.EventSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> findAll(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        if (page == null) {
            return new ArrayList<>();
        }
        return page.getContent();
    }

    @Override
    public Event findOne(UUID id) {
        return eventRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Event> search(EventSearch eventSearch){
        return null;
    }
}
