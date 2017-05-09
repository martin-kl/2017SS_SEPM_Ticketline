package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import java.util.UUID;

public interface PerformanceService {
    /**
     * Find a single performance entry by id.
     *
     * @param id the is of the performance entry
     * @return the detailedPerformance entry
     */
    DetailedPerformanceDTO findOne(UUID id) throws ExceptionWithDialog;

}
