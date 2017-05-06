package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickettransaction.TicketTransactionMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tickettransaction")
@Api(value = "tickettransaction")
public class TicketTransactionEndpoint {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketTransactionMapper ticketTransactionMapper;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Gets a list of Ticket Reservations")
    public List<DetailedTicketTransactionDTO> getAllTransactions(
        @RequestParam(value = "status") String status) {
        return ticketService
            .getAllTransactions(status)
            .stream()
            .map(ticketTransactionMapper::fromEntity)
            .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Gets a Ticket Transaction by ID")
    public DetailedTicketTransactionDTO findTicketTransactionByID(
        @RequestParam(value = "id")UUID id) {
        TicketTransaction transaction = ticketService.findTransactionsByID(id);
        return ticketTransactionMapper.fromEntity(transaction);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Gets a list of Ticket Reservations")
    public List<DetailedTicketTransactionDTO> findTicketTransaction(
        @RequestParam(value = "customeName") String customerName,
        @RequestParam(value = "performance") String performance) {
        return ticketService
            .findTransactionsByCustomerAndLocation(customerName, performance)
            .stream()
            .map(ticketTransactionMapper::fromEntity)
            .collect(Collectors.toList());
    }

}
