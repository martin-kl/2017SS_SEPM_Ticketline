package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;

import java.util.UUID;

public interface PerformanceService {
    /**
     * Find a single Performance entry by id.
     *
     * @param id the is of the performance
     * @return the performance
     */
    Performance findOne(UUID id);
}
