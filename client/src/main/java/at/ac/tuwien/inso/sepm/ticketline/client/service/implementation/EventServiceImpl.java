package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.EventArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Override
    public List<EventDTO> search(EventSearchDTO searchParams, int page) throws ExceptionWithDialog {
        if (searchParams == null) { return eventRestClient.findAll(page); }
        return eventRestClient.search(searchParams, page);
    }

    @Override
    public HashMap<Integer, EventDTO> searchTopTen(String category, Integer monthsInPast) throws ExceptionWithDialog{
        return eventRestClient.searchTopTen(category, monthsInPast);
    }

    @Override
    public List<ArtistDTO> searchArtists(String query, int page) throws ExceptionWithDialog {
        if (query == null || query.equals("")) { return eventRestClient.searchArtists(null, page); }
        return eventRestClient.searchArtists(query, page);
    }

    @Override
    public List<LocationDTO> searchLocations(LocationDTO searchParams, int page) throws ExceptionWithDialog {
        return eventRestClient.searchLocations(searchParams, page);
    }
}
