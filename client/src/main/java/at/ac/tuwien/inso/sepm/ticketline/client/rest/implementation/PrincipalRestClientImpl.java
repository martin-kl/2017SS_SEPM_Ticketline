package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.interceptor.PrincipalRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class PrincipalRestClientImpl implements PrincipalRestClient {
    private static final String PRINCIPAL_URL = "/principal";

    private final RestClient restClient;

    public PrincipalRestClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<PrincipalDTO> findAll(int page) throws DataAccessException {
        return search("", null, page);
    }

    @Override
    public List<PrincipalDTO> search(String query, Boolean locked, int page) throws DataAccessException{
        try {
            log.debug("Searching principals with query {} using url {}", query,
                restClient.getServiceURI(PRINCIPAL_URL) + "?search=" + query);
            ResponseEntity<List<PrincipalDTO>> customer =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(
                        URI.create(restClient.getServiceURI(PRINCIPAL_URL) + "/search"))
                        .queryParam("query", query)
                        .queryParam("page", page)
                        .queryParam("size", 20)
                        .queryParam("locked", locked)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PrincipalDTO>>() {
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
    public PrincipalDTO save(PrincipalDTO principalDTO) throws ValidationException, DataAccessException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PrincipalDTO> entity = new HttpEntity<>(principalDTO, headers);
            ResponseEntity<PrincipalDTO> customerReturn = restClient.exchange(
                restClient.getServiceURI(PRINCIPAL_URL),
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<PrincipalDTO>() {
                }
            );
            log.debug("Result status was {} with content {}", customerReturn.getStatusCode(),
                customerReturn.getBody());
            return customerReturn.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("Failed save principal with status code " + e.getStatusCode().toString());
            if (e.getStatusCode() == HttpStatus.CONFLICT) { //username used
                throw new ValidationException("principal.username.unique");
            } else if (e.getStatusCode() == HttpStatus.GONE) {//email used
                throw new ValidationException("principal.email.unique");
            } else if (e.getStatusCode() == HttpStatus.LENGTH_REQUIRED) { //admin cannot set own type to seller
                throw new ValidationException("principal.admin.illegal.type.change");
            } else {
                throw new DataAccessException(
                    "Failed save customer with status code " + e.getStatusCode().toString());
            }
        } catch (RestClientException e) {
            log.error("Failed save customer with with error message " + e.getMessage());
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public PrincipalDTO setLocked(UUID principalId, Boolean locked) throws DataAccessException, ValidationException {
        try {
            ResponseEntity<PrincipalDTO> principal =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(
                        URI.create(restClient.getServiceURI(PRINCIPAL_URL) + "/setLocked"))
                        .queryParam("id", principalId)
                        .queryParam("locked", locked)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PrincipalDTO>() {
                    });
            return principal.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.PRECONDITION_FAILED) { //cannot lock own account
                throw new ValidationException("principal.illegal.account.lock");
            } else {
                throw new DataAccessException(
                    "Failed to lock user with status code " + e.getStatusCode().toString());
            }
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }    }
}




