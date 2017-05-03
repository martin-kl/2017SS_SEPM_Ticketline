package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;

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
     * @param id the is of the reservation entry
     * @return the news entry
     */
//    CustomerDTO findOne(UUID id) throws ExceptionWithDialog;
}
