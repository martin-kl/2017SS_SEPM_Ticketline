package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class TransactionDetailsViewController {

    @FXML
    private Label lbTransactionID;
    @FXML
    private Label reservationNumber;
    @FXML
    private Label customer;
    @FXML
    private Label totalPrice;

    public void setDetailedTicketTransactionDTO(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        if(detailedTicketTransactionDTO.getStatus() == TicketStatus.RESERVED) {
            lbTransactionID.setText(BundleManager.getBundle().getString("transaction.detail.reservationNumber"));
        }else if(detailedTicketTransactionDTO.getStatus() == TicketStatus.BOUGHT) {
            lbTransactionID.setText(BundleManager.getBundle().getString("transaction.detail.billNumber"));
        }else {
            lbTransactionID.setText(BundleManager.getBundle().getString("transaction.detail.stornoNumber"));
        }
        reservationNumber.setText(detailedTicketTransactionDTO.getId().toString());
        customer.setText(detailedTicketTransactionDTO.getCustomer().getFirstName() + " " + detailedTicketTransactionDTO.getCustomer().getLastName());
        totalPrice.setText("€" + Helper.getTotalPrice(detailedTicketTransactionDTO.getTickets()).toString());
    }

}
