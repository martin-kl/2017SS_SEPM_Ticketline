package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;
import java.util.UUID;

public interface TicketTransactionRestClient {
     /**
     * Find all reservations
     *
     * @return ordered list of all reservations
     */
     //this method is not supported over rest
    //List<DetailedTicketTransactionDTO> findReservationsWithStatus() throws ExceptionWithDialog;

     /**
     * Find all reservations with a given status
     *
      * @param status The reservations that are returned have this status
     * @return ordered list of all reservations
     */
    List<DetailedTicketTransactionDTO> findReservationsWithStatus(String status) throws ExceptionWithDialog;


    /**
     * Find a single reservation entry by id.
     *
     * @param uuid the id of the reservation entry
     * @return the Transaction/Reservation entry
     */
    DetailedTicketTransactionDTO findReservationsWithID(UUID uuid) throws ExceptionWithDialog;

    /**
     * Finds a Transaction/Reservation by the
     * @param customerName the customer name to search for
     * @param performanceName the performance name to search for
     * @return list of reservations/transactions for the customer and the performance name
     */
    List<DetailedTicketTransactionDTO> findReservationsByCustomerAndPerformance(String customerName, String performanceName)
        throws ExceptionWithDialog;

}
