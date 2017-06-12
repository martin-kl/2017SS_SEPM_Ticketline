package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketTransactionRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.DesktopApi;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;

import org.apache.commons.io.FileUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class TicketTransactionRestClientImpl implements TicketTransactionRestClient {

    private static final String TRANSACTION_URL = "/tickettransaction";

    private final RestClient restClient;

    public TicketTransactionRestClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public List<DetailedTicketTransactionDTO> findTransactionsBoughtReserved(int page)
        throws ExceptionWithDialog {
        try {
            log.debug("Retrieving all ticket details (bought and reserved) from {}",
                restClient.getServiceURI(TRANSACTION_URL));
            ResponseEntity<List<DetailedTicketTransactionDTO>> reservations =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(restClient.getServiceURI(TRANSACTION_URL))
                        .queryParam("size", 20)
                        .queryParam("page", page)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DetailedTicketTransactionDTO>>() {
                    });
            log.debug("Result status was {} with content {}", reservations.getStatusCode(),
                reservations.getBody());
            return reservations.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve list of all ticket details (bought and reserved) with status "
                    + "code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<DetailedTicketTransactionDTO> findTransactionWithID(String id, int page)
        throws ExceptionWithDialog {
        try {
            log.debug("Retrieving a ticket details from {} with partial id {}",
                restClient.getServiceURI(
                    TRANSACTION_URL), id);

            ResponseEntity<List<DetailedTicketTransactionDTO>> reservation =
                restClient.exchange(
                    UriComponentsBuilder
                        .fromUri(restClient.getServiceURI(TRANSACTION_URL + "/find"))
                        .queryParam("id", id)
                        .queryParam("page", page)
                        .queryParam("size", 20)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DetailedTicketTransactionDTO>>() {
                    });
            log.debug("Result status was {} with content {}", reservation.getStatusCode(),
                reservation.getBody());
            return reservation.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve the ticket transaction with id " + id + " with status code " + e
                    .getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<DetailedTicketTransactionDTO> findTransactionsByCustomerAndPerformance(
        String customerFirstName, String customerLastName, String performanceName, int page)
        throws ExceptionWithDialog {
        try {
            log.debug(
                "Retrieving ticket details from {} for customerFirstName {}, customerLastName {} and performanceName {}",
                restClient.getServiceURI(
                    TRANSACTION_URL), customerFirstName, customerLastName, performanceName);

            ResponseEntity<List<DetailedTicketTransactionDTO>> reservations =
                restClient.exchange(
                    UriComponentsBuilder
                        .fromUri(restClient.getServiceURI(TRANSACTION_URL + "/filter"))
                        .queryParam("firstname", customerFirstName)
                        .queryParam("lastname", customerLastName)
                        .queryParam("performance", performanceName)
                        .queryParam("page", page)
                        .queryParam("size", 20)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DetailedTicketTransactionDTO>>() {
                    });
            log.debug("Result status was {} with content {}", reservations.getStatusCode(),
                reservations.getBody());
            return reservations.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve list of ticket details for customer and performance with status code "
                    + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public DetailedTicketTransactionDTO update(DetailedTicketTransactionDTO dto)
        throws ExceptionWithDialog {
        try {
            log.debug("Updating detailed ticket transaction dto = {}", dto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<DetailedTicketTransactionDTO> entity = new HttpEntity<>(dto, headers);
            ResponseEntity<DetailedTicketTransactionDTO> response =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(restClient.getServiceURI(TRANSACTION_URL))
                        .build().toUri(),
                    HttpMethod.PATCH,
                    entity,
                    new ParameterizedTypeReference<DetailedTicketTransactionDTO>() {
                    });
            log.debug("Result status was {} with content {}", response.getStatusCode(),
                response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed to update detailed ticket transaction dto " + dto);
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void downloadFile(Long id) throws ExceptionWithDialog {
        try {
            String language = BundleManager.getBundle().getLocale().getLanguage();
            URI uri = restClient.getServiceURI(TRANSACTION_URL + "/" + id + "/download?lang=" + language);
            File destination = new File("files/" + id.toString() + ".pdf");
            FileUtils.copyURLToFile(uri.toURL(), destination);
            DesktopApi.open(destination);
        } catch (Exception e) {
            throw new DataAccessException(
                "Failed to download transaction receipt with id " + id);
        }
    }
}
