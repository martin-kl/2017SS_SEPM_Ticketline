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

    //not needed right now
    /*
    @Override
    public List<DetailedTicketTransactionDTO> findTransactionsWithStatus(String status)
        throws ExceptionWithDialog {
        return ticketTransactionRestClient.findTransactionsWithStatus(status);
    }
    */

    @Override
    public List<DetailedTicketTransactionDTO> findTransactionsBoughtReserved()
        throws ExceptionWithDialog {
        return ticketTransactionRestClient.findTransactionsBoughtReserved();
    }

    @Override
    public DetailedTicketTransactionDTO findTransactionWithID(String id)
        throws ExceptionWithDialog {

        //TODO cast here to uuid? better to search for a string as part of uuid or?

        try {
            UUID uuid = UUID.fromString(id);
            /*
            System.out.println(
                "uuid from string \"" + id + "\" = \"" + uuid + "\"");
            */
            return ticketTransactionRestClient.findTransactionWithID(uuid);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("reservation.error.invalidID");
        }
    }

    @Override
    public List<DetailedTicketTransactionDTO> findTransactionsByCustomerAndPerformance(
        String customerFirstName, String customerLastName, String performanceName)
        throws ExceptionWithDialog {
        return ticketTransactionRestClient
            .findTransactionsByCustomerAndPerformance(customerFirstName, customerLastName,
                performanceName);
    }

}
