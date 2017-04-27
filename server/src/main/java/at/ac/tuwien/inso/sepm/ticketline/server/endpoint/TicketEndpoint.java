package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketHistoryDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickethistory.TicketHistoryMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickettransaction.TicketTransactionMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;

    @Autowired
    private TicketTransactionMapper ticketTransactionMapper;

    @RequestMapping(value = "/reserve", method = RequestMethod.POST)
    // @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Reserves a ticket")
    public TicketHistoryDTO reserveTicket(@ModelAttribute("ticket") TicketDTO ticketDTO, @ModelAttribute("customer")CustomerDTO customerDTO) {
        Ticket ticket = new SeatTicket();// ticketMapper.fromDTO(ticketDTO);
        Customer customer = customerMapper.fromDTO(customerDTO);
        return ticketHistoryMapper.fromEntity(ticketService.reserveTicket(ticket, customer));
    }

    @RequestMapping(value = "/reserve", method = RequestMethod.GET)
    @ApiOperation(value = "Gets a list of Ticket Reservations")
    public List<DetailedTicketTransactionDTO> getAllTransactions(Pageable pageable) {
        return ticketTransactionRepository
            .findAll(pageable)
            .getContent()
            .stream()
            .map(ticketTransactionMapper::fromEntity)
            .collect(Collectors.toList());
    }
}
