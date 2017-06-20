package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.PerformanceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public class PerformanceRestClientImpl implements PerformanceRestClient {
    private static final String PERFORMANCE_URL = "/performance";

    private final RestClient restClient;

    public PerformanceRestClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public DetailedPerformanceDTO findOne(UUID id) throws DataAccessException {
        try {
            log.debug("Retrieving one performance with uuid {} from {}", id, restClient.getServiceURI(PERFORMANCE_URL));
            ResponseEntity<DetailedPerformanceDTO> performance =
                restClient.exchange(
                    restClient.getServiceURI(PERFORMANCE_URL)+"/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedPerformanceDTO>() {
                    });
            log.debug("Result status was {} with content {}", performance.getStatusCode(), performance.getBody());
            return performance.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve one performance with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
