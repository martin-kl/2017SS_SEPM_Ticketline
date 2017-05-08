package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Slf4j
@Component
public class EventRestClientImpl implements EventRestClient {
    private static final String EVENT_URL = "/event";

    private final RestClient restClient;

    public EventRestClientImpl(RestClient restClient){
        this.restClient = restClient;
    }

    @Override
    public List<EventDTO> findAll() throws DataAccessException {
        try {
            log.debug("Retrieving all events from {}", restClient.getServiceURI(EVENT_URL));
            ResponseEntity<List<EventDTO>> event =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL),
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

}
