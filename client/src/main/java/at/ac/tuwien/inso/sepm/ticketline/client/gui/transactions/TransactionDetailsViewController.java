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

    public void setDetailedTicketTransactionDTO(
        DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        ObservableList<Node> buttons = btnBar.getButtons();
        buttons.clear();
        if (detailedTicketTransactionDTO.getStatus() == TicketStatus.RESERVED) {
            lbTransactionID.setText(
                BundleManager.getBundle().getString("transaction.detail.reservationNumber"));

            btnReserve = new Button(
                BundleManager.getBundle().getString("transaction.detail.cancelReservation"));
            btnReserve.setOnAction(event -> {
                //TODO cancel reservation
                System.out.println("cancel reservation button has been clicked");
            });
            buttons.add(btnReserve);
        } else if (detailedTicketTransactionDTO.getStatus() == TicketStatus.BOUGHT) {
            lbTransactionID
                .setText(BundleManager.getBundle().getString("transaction.detail.billNumber"));
            //TODO add buttons to cancel the payment

            btnPrintPDF = new Button(
                BundleManager.getBundle().getString("transaction.detail.printPDF"));
            btnPrintPDF.setOnAction(event -> {
                //TODO cancel reservation
                System.out
                    .println("print pdf button in already bought transaction has been clicked");
            });
            buttons.add(btnPrintPDF);
        } else {
            lbTransactionID
                .setText(BundleManager.getBundle().getString("transaction.detail.stornoNumber"));
        }

        /*
            <Button fx:id="btnReserve" layoutX="61.0" layoutY="10.0" mnemonicParsing="false" onAction="#handleReserveButton" text="%transaction.detail.reserve" />
          <Button fx:id="btnBuy" mnemonicParsing="false" onAction="#handleBuyButton" text="%transaction.detail.buy" />
         */

        transactionNumber.setText(detailedTicketTransactionDTO.getId().toString());
        customer.setText(detailedTicketTransactionDTO.getCustomer().getFirstName() + " "
            + detailedTicketTransactionDTO.getCustomer().getLastName());
        totalPrice.setText(
            "€" + Helper.getTotalPrice(detailedTicketTransactionDTO.getTickets()).toString());
    }

    public void handleBuyButton(ActionEvent actionEvent) {
    }

    public void handlePrintButton(ActionEvent actionEvent) {
    }

    public void handleCancelButton(ActionEvent actionEvent) {
    }
}
