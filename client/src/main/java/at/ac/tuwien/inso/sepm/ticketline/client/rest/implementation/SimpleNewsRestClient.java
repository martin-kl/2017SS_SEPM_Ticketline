package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Component
public class SimpleNewsRestClient implements NewsRestClient {

    private static final String NEWS_URL = "/news";

    private static final String NOT_SEEN_NEWS_URL = "/news/notseen";

    private final RestClient restClient;

    public SimpleNewsRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<SimpleNewsDTO> findAll(int page) throws DataAccessException {
        return get(NEWS_URL, page);
    }

    @Override
    public List<SimpleNewsDTO> findAllUnseen(int page) throws DataAccessException {
        return get(NOT_SEEN_NEWS_URL, page);
    }

    @Override
    public DetailedNewsDTO publish(DetailedNewsDTO news) throws DataAccessException {
        try {
            log.debug("saving news with title {}", news.getTitle());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<DetailedNewsDTO> entity = new HttpEntity<>(news, headers);
            ResponseEntity<DetailedNewsDTO> customerReturn = restClient.exchange(
                restClient.getServiceURI(NEWS_URL),
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<DetailedNewsDTO>() {
                }
            );
            log.debug("Result status was {} with content {}", customerReturn.getStatusCode(),
                customerReturn.getBody());
            return customerReturn.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("Failed save customer with status code " + e.getStatusCode().toString());
            throw new DataAccessException("Failed save customer with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            log.error("Failed save customer with with error message " + e.getMessage());
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    private List<SimpleNewsDTO> get(String url, int page) throws DataAccessException {
        try {
            log.debug("Retrieving all news from {}", restClient.getServiceURI(url));
            ResponseEntity<List<SimpleNewsDTO>> news =
                restClient.exchange(
                    UriComponentsBuilder.fromUri(restClient.getServiceURI(url))
                        .queryParam("page", page)
                        .queryParam("size", 20)
                        .build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleNewsDTO>>() {
                    });
            log.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }

    }

}
