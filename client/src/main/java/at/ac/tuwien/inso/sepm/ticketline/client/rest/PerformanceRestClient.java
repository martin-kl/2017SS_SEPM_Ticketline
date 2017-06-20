package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import java.util.UUID;

public interface PerformanceRestClient {
    /**
     * Find a single performance entry by id.
     *
     * @param id the is of the performance entry
     * @return the performance entry
     */
    DetailedPerformanceDTO findOne(UUID id) throws DataAccessException;

}
