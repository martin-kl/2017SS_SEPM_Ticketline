package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class TransactionDetailsViewController {
    @FXML
    private Label reservationNumber;

    @FXML
    private Label customer;

    @FXML
    private Label totalPrice;

    public void setDetailedTicketTransactionDTO(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        reservationNumber.setText(detailedTicketTransactionDTO.getId().toString());
        customer.setText(detailedTicketTransactionDTO.getCustomer().getFirstName() + " " + detailedTicketTransactionDTO.getCustomer().getLastName());
        totalPrice.setText("€" + Helper.getTotalPrice(detailedTicketTransactionDTO.getTickets()).toString());
    }

}
