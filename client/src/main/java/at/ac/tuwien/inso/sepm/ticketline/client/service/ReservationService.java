package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import java.util.List;
import java.util.UUID;

public interface ReservationService {
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
//    ReservationDTO findOne(UUID id) throws ExceptionWithDialog;

    /**
     * saves a new or edited customer
     * @param customer The customer object to save or edit
     * @return the same customer passed into the method with fields updated
     */
//    CustomerDTO save(CustomerDTO customer) throws ExceptionWithDialog;
}
