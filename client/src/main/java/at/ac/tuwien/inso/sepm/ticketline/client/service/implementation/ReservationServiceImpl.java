package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketTransactionRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final TicketTransactionRestClient ticketTransactionRestClient;

    public ReservationServiceImpl(TicketTransactionRestClient customerRestClient) {
        this.ticketTransactionRestClient = customerRestClient;
    }

    @Override
    public List<DetailedTicketTransactionDTO> findReservationsWithStatus(String status) throws ExceptionWithDialog {
        return ticketTransactionRestClient.findReservationsWithStatus(status);
    }

}
