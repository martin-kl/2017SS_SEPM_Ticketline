package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;

public interface ReservationService {
     /**
     * Find all reservations
     *
     * @return ordered list of al reservations
     */
    List<DetailedTicketTransactionDTO> findReservationsWithStatus(String status) throws ExceptionWithDialog;


    /**
     * Find a single reservation entry by id.
     *
     * @param id the is of the reservation entry
     * @return the news entry
     */
//    DetailedTicketTransactionDTO findOne(UUID id) throws ExceptionWithDialog;

    /**
     * saves a new or edited customer
     * @param customer The customer object to save or edit
     * @return the same customer passed into the method with fields updated
     */
//    DetailedTicketTransactionDTO save(DetailedTicketTransactionDTO customer) throws ExceptionWithDialog;
}
