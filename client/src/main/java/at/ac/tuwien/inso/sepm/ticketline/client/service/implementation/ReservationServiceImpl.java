package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.ReservationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRestClient reservationRestClient;

    public ReservationServiceImpl(ReservationRestClient customerRestClient) {
        this.reservationRestClient = customerRestClient;
    }

    @Override
    public List<ReservationDTO> findAll() throws ExceptionWithDialog {
        return reservationRestClient.findAll();
    }

}
