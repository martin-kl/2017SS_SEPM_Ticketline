package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketWrapperDTO;
import java.util.List;

/**
 * Created by Alex on 09.05.2017.
 */
public interface HallplanService {
    /**
     * Extract the list of Sectors from the detailed performance entry
     *
     * @param detailedPerformanceDTO the object of the performance entry
     * @return the list of sectors entry
     */
    List<SectorDTO> getSectorsOfPerformance(DetailedPerformanceDTO detailedPerformanceDTO);

    /**
     * Extract the list of Seats from the detailed performance entry
     *
     * @param detailedPerformanceDTO the object of the performance entry
     * @return the list of seats entry
     */
    List<SeatDTO> getSeatsOfPerformance(DetailedPerformanceDTO detailedPerformanceDTO);

    /**
     * Get a random free sector ticket of this performance and sector
     *
     * @param detailedPerformanceDTO the object of the performance entry
     * @param sectorDTO the object of the chosen sector entry
     * @param chosenTickets a list of already chosen tickets
     * @return the SectorTicketDTO or null if there are no free Tickets
     */
    SectorTicketDTO getRandomFreeSectorTicket(DetailedPerformanceDTO detailedPerformanceDTO, SectorDTO sectorDTO, List<TicketDTO> chosenTickets);
}
