package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import java.net.URI;
import jdk.nashorn.internal.runtime.URIUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.UUID;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class CustomerRestClientImpl implements CustomerRestClient {

    private static final String CUSTOMER_URL = "/customer";

    private final RestClient restClient;

    public CustomerRestClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<CustomerDTO> findAll(int page) throws DataAccessException {
        try {
            log.debug("Retrieving all customers from {}", restClient.getServiceURI(CUSTOMER_URL));
            ResponseEntity<List<CustomerDTO>> customer =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(restClient.getServiceURI(CUSTOMER_URL))
                        .queryParam("page", page)
                        .queryParam("size", 20)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CustomerDTO>>() {
                    });
            log.debug("Result status was {} with content {}", customer.getStatusCode(),
                customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve list of all customer with status code " + e.getStatusCode()
                    .toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<CustomerDTO> search(String query, int page) throws DataAccessException {
        try {
            log.debug("Searching customers with query {} using url {}", query,
                restClient.getServiceURI(CUSTOMER_URL) + "?search=" + query);
            ResponseEntity<List<CustomerDTO>> customer =
                restClient.exchange(
                    //URI.create(baseUrl + "/" + serviceLocation);
                    UriComponentsBuilder.fromUri(
                        URI.create(restClient.getServiceURI(CUSTOMER_URL) + "/search"))
                        .queryParam("query", query)
                        .queryParam("page", page)
                        .queryParam("size", 20)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CustomerDTO>>() {
                    });
            log.debug("Result status was {} with content {}", customer.getStatusCode(),
                customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed to search for customers with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public CustomerDTO findOne(UUID id) throws DataAccessException {
        try {
            log.debug("Retrieving one customer with uuid {} from {}", id,
                restClient.getServiceURI(CUSTOMER_URL));
            ResponseEntity<CustomerDTO> customer =
                restClient.exchange(
                    restClient.getServiceURI(CUSTOMER_URL) + "/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CustomerDTO>() {
                    });
            log.debug("Result status was {} with content {}", customer.getStatusCode(),
                customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve one customer with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public CustomerDTO save(CustomerDTO customer) throws ExceptionWithDialog {
        try {
            log.debug("saving customer with first name {} and last name {} in {}",
                customer.getFirstName(), customer.getLastName(),
                restClient.getServiceURI(CUSTOMER_URL));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CustomerDTO> entity = new HttpEntity<>(customer, headers);
            ResponseEntity<CustomerDTO> customerReturn = restClient.exchange(
                restClient.getServiceURI(CUSTOMER_URL),
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<CustomerDTO>() {
                }
            );
            log.debug("Result status was {} with content {}", customerReturn.getStatusCode(),
                customerReturn.getBody());
            return customerReturn.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("Failed save customer with status code " + e.getStatusCode().toString());
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new ValidationException("customer.error.uniqueEmail");
            } else {
                throw new DataAccessException(
                    "Failed save customer with status code " + e.getStatusCode().toString());
            }
        } catch (RestClientException e) {
            log.error("Failed save customer with with error message " + e.getMessage());
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
