package at.ac.tuwien.inso.sepm.ticketline.client.util;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;

import java.math.BigDecimal;
import java.util.List;

public class Helper {

    public static BigDecimal getTotalPrice(List<? extends TicketDTO> tickets) {
        BigDecimal result = new BigDecimal(0);
        for (TicketDTO ticket : tickets) {
            result = result.add(ticket.getPrice());
        }
        return result;
    }

    public static String getFormattedTicketPlace(TicketDTO ticketDTO) {
        if (ticketDTO instanceof SeatTicketDTO) {
            SeatTicketDTO seatTicket = (SeatTicketDTO) ticketDTO;
            return BundleManager.getBundle().getString("row") + " " +
                    seatTicket.getSeat().getRow() + ", " +
                    BundleManager.getBundle().getString("seat") + " " + seatTicket
                    .getSeat().getColumn();
        } else {
            SectorTicketDTO sectorTicket = (SectorTicketDTO) ticketDTO;
            return BundleManager.getBundle().getString("sector") + " " +
                sectorTicket.getSector().getName();
        }
    }
}
