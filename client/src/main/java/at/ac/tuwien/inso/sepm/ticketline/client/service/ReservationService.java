package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;
import java.util.UUID;

public interface ReservationService {
     /**
     * Find all transactions
     *
     * @return ordered list of al transactions
     */
     //not needed right now
    //List<DetailedTicketTransactionDTO> findTransactionsWithStatus(String status) throws ExceptionWithDialog;

     /**
     * Find all details with status bought or reserved
     *
     * @return ordered list of all details with status bought or reserved
     */
    List<DetailedTicketTransactionDTO> findTransactionsBoughtReserved(int page) throws ExceptionWithDialog;

    /**
     * Find a single reservation entry by id.
     *
     * @param id the id of the reservation entry
     * @return the Transaction/Reservation entry
     */
    DetailedTicketTransactionDTO findTransactionWithID(String id) throws ExceptionWithDialog;

    /**
     * Finds a Transaction/Reservation by the
     * @param customerFirstName the customer first name to search for
     * @param customerLastName the customer last name to search for
     * @param performanceName the performance name to search for
     * @return list of transactions/details for the customer and the performance name
     */
    List<DetailedTicketTransactionDTO> findTransactionsByCustomerAndPerformance(String customerFirstName, String customerLastName, String performanceName, int page)
        throws ExceptionWithDialog;

}
