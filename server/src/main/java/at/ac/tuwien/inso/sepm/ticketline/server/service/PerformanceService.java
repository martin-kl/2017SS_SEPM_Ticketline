package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.TicketWrapper;

import java.util.List;
import java.util.UUID;

public interface PerformanceService {
    /**
     * Find a single Performance entry by id.
     *
     * @param id the is of the performance
     * @return the performance
     */
    Performance findOne(UUID id);

    /**
     * Finds a list of all tickets for a given performanceID.
     *
     * @param performanceID for the to be returned tickets
     * @return list of found ticktets
     */
    List<Ticket> findAllTicketsToPerformanceID(UUID performanceID);

    /**
     * Adds to a given list of Tickets the current status (reserved/bought/storno).
     * The storno status is set, if the ticket is free.
     *
     * @param tickets for which the status should be found out
     * @return a list of the tickets with the current status
     */
    List<TicketWrapper> addStatusToTickets(List<Ticket> tickets);
}
