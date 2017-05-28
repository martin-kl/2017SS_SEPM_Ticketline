package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

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
    public List<SimpleNewsDTO> findAll() throws DataAccessException {
        return get(NEWS_URL);
    }

    @Override
    public List<SimpleNewsDTO> findAllUnseen() throws DataAccessException {
        return get(NOT_SEEN_NEWS_URL);
    }

    @Override
    public DetailedNewsDTO findDetailedNews(UUID id) throws DataAccessException {
        try {
            log.debug("Retrieving detailed news entry with id {} from {}", id,
                restClient.getServiceURI(NEWS_URL));
            ResponseEntity<DetailedNewsDTO> detailedNews =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_URL) + "/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedNewsDTO>() {
                    });
            log.debug("Result status was {} with content {}", detailedNews.getStatusCode(),
                detailedNews.getBody());
            return detailedNews.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    private List<SimpleNewsDTO> get(String url) throws DataAccessException {
        try {
            log.debug("Retrieving all news from {}", restClient.getServiceURI(url));
            ResponseEntity<List<SimpleNewsDTO>> news =
                restClient.exchange(
                    restClient.getServiceURI(url),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleNewsDTO>>() {
                    });
            log.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
