package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketTransactionRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public class TicketTransactionRestClientImpl implements TicketTransactionRestClient {

    private static final String TRANSACTION_URL = "/tickettransaction";

    private final RestClient restClient;

    public TicketTransactionRestClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    //this method is not supported over rest
    /*
    @Override
    public List<DetailedTicketTransactionDTO> findReservationsWithStatus() throws ExceptionWithDialog {
       try {
            log.debug("Retrieving all ticket transactions from {}", restClient.getServiceURI(
                TRANSACTION_URL));
            ResponseEntity<List<DetailedTicketTransactionDTO>> reservations =
                restClient.exchange(
                    restClient.getServiceURI(TRANSACTION_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DetailedTicketTransactionDTO>>() {
                    });
            log.debug("Result status was {} with content {}", reservations.getStatusCode(), reservations.getBody());
            return reservations.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve list of all ticket transactions with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
    */

    @Override
    public List<DetailedTicketTransactionDTO> findReservationsWithStatus(String status) throws ExceptionWithDialog {
       try {
            log.debug("Retrieving all ticket transactions from {}", restClient.getServiceURI(
                TRANSACTION_URL));

            ResponseEntity<List<DetailedTicketTransactionDTO>> reservations =
                restClient.exchange(
                    restClient.getServiceURI(TRANSACTION_URL)+"?status=" + status,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DetailedTicketTransactionDTO>>() {
                    });
            log.debug("Result status was {} with content {}", reservations.getStatusCode(), reservations.getBody());
            return reservations.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve list of all ticket transactions with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
