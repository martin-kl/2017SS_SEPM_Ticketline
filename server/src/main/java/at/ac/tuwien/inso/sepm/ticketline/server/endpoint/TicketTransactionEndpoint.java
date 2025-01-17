package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PaymentProviderOption;
import at.ac.tuwien.inso.sepm.ticketline.rest.payment.PaymentRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickettransaction.TicketTransactionMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PaymentService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PdfService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import com.lowagie.text.DocumentException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/tickettransaction")
@Api(value = "tickettransaction")
public class TicketTransactionEndpoint {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketTransactionMapper ticketTransactionMapper;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Gets a list of bought and reserved Ticket Reservations")
    public List<DetailedTicketTransactionDTO> getAllReservedAndBoughtTransactions(
        Pageable pageable) {
        return ticketService
            .getAllBoughtReservedTransactions(pageable)
            .stream()
            .map(ticketTransactionMapper::fromEntity)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one Ticket Transaction by ID")
    public DetailedTicketTransactionDTO findTicketTransactionByID(@PathVariable Long id) {
        return ticketTransactionMapper.fromEntity(ticketService.findTransactionsByID(id));
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ApiOperation(value = "Get a list of transactions with the id")
    public List<DetailedTicketTransactionDTO> findTicketTransactionsByID(
        @RequestParam(value = "id") String id, Pageable pageable) {
        return ticketService
            .findById(id, pageable)
            .stream()
            .map(ticketTransactionMapper::fromEntity)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    @ApiOperation(value = "Gets a list of Ticket Reservations for the customer and the performance name")
    public List<DetailedTicketTransactionDTO> findTicketTransaction(
        @RequestParam(value = "firstname") String customerFirstName,
        @RequestParam(value = "lastname") String customerLastName,
        @RequestParam(value = "performance") String performance,
        Pageable pageable
    ) {
        return ticketService
            .findTransactionsByCustomerAndLocation(customerFirstName, customerLastName, performance,
                pageable)
            .stream()
            .map(ticketTransactionMapper::fromEntity)
            .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PATCH)
    @ApiOperation(value = "Updates a single Ticket Transaction")
    public DetailedTicketTransactionDTO patchTicketTransaction(
        @RequestBody DetailedTicketTransactionDTO dto
    ) {
        TicketTransaction tt = ticketService.setTransactionStatus(dto);
        return ticketTransactionMapper.fromEntity(tt);
    }

    @RequestMapping(value = "{transactionid}/download", method = RequestMethod.GET)
    @ApiOperation(value = "Downloads a PDF a transaction")
    public void downloadPdf(
        @PathVariable(name = "transactionid") Long transactionId,
        @RequestParam(value = "lang", required = false) String language,
        HttpServletResponse response
    ) throws IOException, DocumentException, URISyntaxException {
        TicketTransaction ticketTransaction = ticketService.findTransactionsByID(transactionId);
        if (ticketTransaction == null) {
            throw new NotFoundException();
        }
        String pdfFileName = ticketTransaction.getId() + ticketTransaction.getStatus().name() + ".pdf";
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", pdfFileName);
        response.setHeader(headerKey, headerValue);

        if (language != null && !language.equals("de") && !language.equals("en")) {
            throw new BadRequestException("invalid language");
        }

        pdfService.download(response.getOutputStream(), ticketTransaction, language);
    }

    @RequestMapping(value = "{transactionid}/pay", method = RequestMethod.POST)
    @ApiOperation(value = "Paying the transaction")
    public boolean pay(
        @PathVariable(name = "transactionid") Long transactionId,
        @RequestBody PaymentRequestDTO dto
    ) {
        paymentService.pay(
            dto.getProvider(),
            transactionId,
            dto.getSource()
        );
        return true;
    }

}
