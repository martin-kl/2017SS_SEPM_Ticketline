package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;

public interface TicketService {

    /**
     * reserves a ticket for a given customer, if possible
     *
     * @param ticket to reserve
     * @param customer who will reserve the item
     * @return the TicketHistory
     */
    TicketHistory reserveTicket(Ticket ticket, Customer customer);
}
