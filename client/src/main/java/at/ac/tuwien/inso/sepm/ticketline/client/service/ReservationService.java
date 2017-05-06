package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;
import java.util.UUID;

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
     * @param id the id of the reservation entry
     * @return the Transaction/Reservation entry
     */
    DetailedTicketTransactionDTO findReservationWithID(String id) throws ExceptionWithDialog;

    /**
     * Finds a Transaction/Reservation by the
     * @param customerName the customer name to search for
     * @param performanceName the performance name to search for
     * @return list of reservations/transactions for the customer and the performance name
     */
    List<DetailedTicketTransactionDTO> findReservationsByCustomerAndPerformance(String customerName, String performanceName)
        throws ExceptionWithDialog;

}
