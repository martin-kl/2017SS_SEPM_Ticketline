package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import java.util.List;
import java.util.UUID;

public interface ReservationRestClient {
     /**
     * Find all reservations
     *
     * @return ordered list of al reservations
     */
    List<ReservationDTO> findAll() throws ExceptionWithDialog;


    /**
     * Find a single reservation entry by id.
     *
     * @param id the is of the reservation entry
     * @return the news entry
     */
//    CustomerDTO findOne(UUID id) throws ExceptionWithDialog;
}
