package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketHistoryDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatTicket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickethistory.TicketHistoryMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/ticket")
@Api(value = "ticket")
public class TicketEndpoint {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TicketHistoryMapper ticketHistoryMapper;

    @RequestMapping(value = "/reserve", method = RequestMethod.POST)
    // @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Reserves a ticket")
    public TicketHistoryDTO reserveTicket(@ModelAttribute("ticket") TicketDTO ticketDTO, @ModelAttribute("customer")CustomerDTO customerDTO) {
        Ticket ticket = new SeatTicket();// ticketMapper.fromDTO(ticketDTO);
        Customer customer = customerMapper.fromDTO(customerDTO);
        return ticketHistoryMapper.fromEntity(ticketService.reserveTicket(ticket, customer));
    }
}
