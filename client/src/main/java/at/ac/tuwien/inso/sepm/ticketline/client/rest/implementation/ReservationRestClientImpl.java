package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.ReservationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public class ReservationRestClientImpl implements ReservationRestClient {

    private static final String RESERVATION_URL = "/reservation";

    private final RestClient restClient;

    public ReservationRestClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<ReservationDTO> findAll() throws ExceptionWithDialog {
       try {
            log.debug("Retrieving all reservations from {}", restClient.getServiceURI(RESERVATION_URL));
            ResponseEntity<List<ReservationDTO>> reservations =
                restClient.exchange(
                    restClient.getServiceURI(RESERVATION_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ReservationDTO>>() {
                    });
            log.debug("Result status was {} with content {}", reservations.getStatusCode(), reservations.getBody());
            return reservations.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve list of all reservations with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
