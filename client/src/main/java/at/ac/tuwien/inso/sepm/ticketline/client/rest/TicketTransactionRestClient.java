package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import java.util.List;
import java.util.UUID;

public interface TicketTransactionRestClient {

    /**
     * Find all transactions with status bought or reserved.
     *
     * @param page The request page number
     * @return ordered list of all transactions with status bought or reserved
     */
    List<DetailedTicketTransactionDTO> findTransactionsBoughtReserved(int page) throws ExceptionWithDialog;

    /**
     * Find a single transaction detail entry by id.
     *
     * @param id of the transaction detail entry (can be partial)
     * @return the transaction entry
     */
    List<DetailedTicketTransactionDTO> findTransactionWithID(String id, int page) throws ExceptionWithDialog;

    /**
     * Finds a Transaction/Reservation by the customer Name (First and Last name) and the performance name.
     *
     * @param customerFirstName the customer first name to search for
     * @param customerLastName the customer last name to search for
     * @param performanceName the performance name to search for
     * @param page The requested page number
     * @return list of transactions/details for the customer and the performance name
     */
    List<DetailedTicketTransactionDTO> findTransactionsByCustomerAndPerformance(
        String customerFirstName, String customerLastName, String performanceName, int page)
        throws ExceptionWithDialog;

    DetailedTicketTransactionDTO update(DetailedTicketTransactionDTO dto) throws ExceptionWithDialog;


    /**
     * Opens a the receipt pdf of the giving uuid
     * @param id transaction id
     * @throws ExceptionWithDialog
     */
    void downloadFile(Long id) throws ExceptionWithDialog;
}
