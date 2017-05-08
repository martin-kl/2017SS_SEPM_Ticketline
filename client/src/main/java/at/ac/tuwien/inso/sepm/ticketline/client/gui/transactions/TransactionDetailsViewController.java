package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class TransactionDetailsViewController {

    @FXML
    private Label lbTransactionID;
    @FXML
    private Label transactionNumber;
    @FXML
    private Label customer;
    @FXML
    private Label totalPrice;
    @FXML
    private ButtonBar btnBar;

    private Button btnPrintPDF;
    private Button btnBuy;
    private Button btnReserve;
    private Button btnCancelReservation;

    //TODO if we are coming from the Saalplan - then the status is not set or? (now i try all other stati and if none is set, then I assume to come from Saalplan)

    public void setDetailedTicketTransactionDTO(
        DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        ObservableList<Node> buttons = btnBar.getButtons();
        buttons.clear();

        if (detailedTicketTransactionDTO.getStatus() == TicketStatus.RESERVED) {
            //TODO add buttons
            lbTransactionID.setText(
                BundleManager.getBundle().getString("transaction.detail.reservationNumber"));

            btnCancelReservation = new Button(BundleManager.getBundle().getString("transaction.detail.cancelReservation"));
            btnCancelReservation.setOnAction(event -> {
                //TODO cancel reservation
                System.out.println("cancel reservation button has been clicked");
            });
            btnBuy = new Button(BundleManager.getBundle().getString("transaction.detail.buy"));
            btnBuy.setOnAction(event -> {
                //TODO buy selected tickets
                System.out.println("buy tickets button has been clicked");
            });

            buttons.add(btnCancelReservation);
            buttons.add(btnBuy);
        } else if (detailedTicketTransactionDTO.getStatus() == TicketStatus.BOUGHT) {
            lbTransactionID
                .setText(BundleManager.getBundle().getString("transaction.detail.billNumber"));
            //TODO add buttons to cancel the payment and possible others

            btnPrintPDF = new Button(
                BundleManager.getBundle().getString("transaction.detail.printPDF"));
            btnPrintPDF.setOnAction(event -> {
                //TODO cancel reservation
                System.out
                    .println("print pdf button in already bought transaction has been clicked");
            });

            buttons.add(btnPrintPDF);
        } else if(detailedTicketTransactionDTO.getStatus() == TicketStatus.STORNO) {
            //TODO add buttons
            lbTransactionID
                .setText(BundleManager.getBundle().getString("transaction.detail.stornoNumber"));
             btnPrintPDF = new Button(
                BundleManager.getBundle().getString("transaction.detail.printPDF"));
            btnPrintPDF.setOnAction(event -> {
                //TODO cancel reservation
                System.out
                    .println("print pdf button in cancelled transaction has been clicked");
            });

            buttons.add(btnPrintPDF);
        } else {
            //we are coming from the saalplan - status is not yet set
            btnReserve = new Button(BundleManager.getBundle().getString("transaction.detail.reserve"));
            btnReserve.setOnAction(event -> {
                //TODO reserve tickets
                System.out.println("reserve ticket button has been clicked");
            });

            buttons.add(btnReserve);
        }

        transactionNumber.setText(detailedTicketTransactionDTO.getId().toString());
        customer.setText(detailedTicketTransactionDTO.getCustomer().getFirstName() + " "
            + detailedTicketTransactionDTO.getCustomer().getLastName());
        totalPrice.setText(
            "€" + Helper.getTotalPrice(detailedTicketTransactionDTO.getTickets()).toString());
    }
}
