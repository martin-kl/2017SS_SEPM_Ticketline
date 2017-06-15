package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketTransactionRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final TicketTransactionRestClient ticketTransactionRestClient;

    public ReservationServiceImpl(TicketTransactionRestClient ticketTransactionRestClient) {
        this.ticketTransactionRestClient = ticketTransactionRestClient;
    }

    @Override
    public List<DetailedTicketTransactionDTO> findTransactionsBoughtReserved(int page)
        throws ExceptionWithDialog {
        return ticketTransactionRestClient.findTransactionsBoughtReserved(page);
    }

    @Override
    public List<DetailedTicketTransactionDTO> findTransactionWithID(String id, int page) throws ExceptionWithDialog {
        return ticketTransactionRestClient.findTransactionWithID(id, page);
    }

    @Override
    public List<DetailedTicketTransactionDTO> findTransactionsByCustomerAndPerformance(
        String customerFirstName, String customerLastName, String performanceName, int page)
        throws ExceptionWithDialog {
        return ticketTransactionRestClient
            .findTransactionsByCustomerAndPerformance(customerFirstName, customerLastName,
                performanceName, page);
    }

    @Override
    public DetailedTicketTransactionDTO update(DetailedTicketTransactionDTO dto) throws ExceptionWithDialog {
        return ticketTransactionRestClient.update(dto);
    }

    @Override
    public void downloadFile(Long id) throws ExceptionWithDialog {
        ticketTransactionRestClient.downloadFile(id);
    }

}
