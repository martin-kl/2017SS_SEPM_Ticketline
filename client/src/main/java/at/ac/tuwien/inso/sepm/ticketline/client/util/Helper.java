package at.ac.tuwien.inso.sepm.ticketline.client.util;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Calvin on 08.05.17.
 */
public class Helper {
    public static BigDecimal getTotalPrice(List<TicketDTO> tickets) {
        BigDecimal result = new BigDecimal(0);
        for (TicketDTO ticket : tickets) {
            result.add(ticket.getPrice());
        }
        return result;
    }
}
