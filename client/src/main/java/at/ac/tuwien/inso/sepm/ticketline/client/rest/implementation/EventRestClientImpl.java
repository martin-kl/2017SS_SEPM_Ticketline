package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.EventArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
public class EventRestClientImpl implements EventRestClient {
    private static final String EVENT_URL = "/event";
    private static final String EVENT_SEARCH_URL = "/event/search";
    private static final String ARTIST_URL = "/artist";
    private static final String LOCATION_URL = "/location/search";

    private final RestClient restClient;

    public EventRestClientImpl(RestClient restClient){
        this.restClient = restClient;
    }

    @Override
    public List<EventDTO> findAll(int page) throws DataAccessException {
        try {
            log.debug("Retrieving all events from {}", restClient.getServiceURI(EVENT_URL));
            ResponseEntity<List<EventDTO>> event =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(restClient.getServiceURI(EVENT_URL))
                        .queryParam("size", 20)
                        .queryParam("page", page)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<EventDTO>>() {
                    });
            log.debug("Result status was {} with content {}", event.getStatusCode(), event.getBody());
            return event.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<EventDTO> search(EventSearchDTO searchParams, int page) throws DataAccessException {
        try {
            log.debug("Searching events with event search dto = {}", searchParams);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<EventSearchDTO> entity = new HttpEntity<>(searchParams, headers);
            ResponseEntity<List<EventDTO>> response =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(restClient.getServiceURI(EVENT_SEARCH_URL))
                        .build().toUri(),
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<EventDTO>>() {
                    });
            log.debug("Result status was {} with content {}", response.getStatusCode(),
                response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed to search for events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<ArtistDTO> searchArtists(String query, int page) throws DataAccessException {
        try {
            log.debug("Searching artists with query {} using url {}", query,
                restClient.getServiceURI(ARTIST_URL) + "?search=" + query);
            ResponseEntity<List<ArtistDTO>> artists =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(
                        URI.create(restClient.getServiceURI(ARTIST_URL) + "/search"))
                        .queryParam("fuzzyArtistName", query)
                        .queryParam("page", page)
                        .queryParam("size", 20)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ArtistDTO>>() {
                    });
            log.debug("Result status was {} with content {}", artists.getStatusCode(),
                artists.getBody());
            return artists.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed to search for artists with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<LocationDTO> searchLocations(LocationDTO searchParams, int page) throws DataAccessException {
        try {
            log.debug("Searching locations with location search dto = {}", searchParams);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LocationDTO> entity = new HttpEntity<>(searchParams, headers);
            ResponseEntity<List<LocationDTO>> response =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(restClient.getServiceURI(LOCATION_URL))
                        .build().toUri(),
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<LocationDTO>>() {
                    });
            log.debug("Result status was {} with content {}", response.getStatusCode(),
                response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed to search for locations with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }


}
