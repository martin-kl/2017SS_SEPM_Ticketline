package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.GridPane;
import org.controlsfx.glyphfont.FontAwesome;

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
     * This returns a map of form <Row, ColumnCount>, containing the columncount for each row
     *
     * @param detailedPerformanceDTO the object of the performance entry
     * @return a map containing each row's column count
     */
    Map<Integer, Integer> getColumCounts(DetailedPerformanceDTO detailedPerformanceDTO);

    /**
     * This returns the biggest row's column count
     *
     * @param detailedPerformanceDTO the object of the performance entry
     * @return the amount of columns in the biggest row
     */
    Integer getLargestRowColumnCount(DetailedPerformanceDTO detailedPerformanceDTO);

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
