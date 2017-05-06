package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketTransactionRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final TicketTransactionRestClient ticketTransactionRestClient;

    public ReservationServiceImpl(TicketTransactionRestClient customerRestClient) {
        this.ticketTransactionRestClient = customerRestClient;
    }

    @Override
    public List<DetailedTicketTransactionDTO> findReservationsWithStatus(String status)
        throws ExceptionWithDialog {
        return ticketTransactionRestClient.findReservationsWithStatus(status);
    }

    @Override
    public DetailedTicketTransactionDTO findReservationWithID(String id)
        throws ExceptionWithDialog {
        //TODO cast here to uuid? better to search for a string as part of uuid or?
        UUID uuid = UUID.fromString(id);
        System.out.println(
            "uuid from string \"" + id + "\" = \"" + uuid + "\"");
        return ticketTransactionRestClient.findReservationsWithID(uuid);
    }

    @Override
    public List<DetailedTicketTransactionDTO> findReservationsByCustomerAndPerformance(
        String customerName, String performanceName) throws ExceptionWithDialog {
        return ticketTransactionRestClient.findReservationsByCustomerAndPerformance(customerName, performanceName);
    }

}
