package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.details;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TransactionDetailsController {

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

    public void setDetailedTicketTransactionDTO(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {

        loadButtonsAccordingToStatus(detailedTicketTransactionDTO.getStatus(), false);

        transactionNumber.setText(detailedTicketTransactionDTO.getId().toString());
        customer.setText(detailedTicketTransactionDTO.getCustomer().getFirstName() + " "
            + detailedTicketTransactionDTO.getCustomer().getLastName());
        totalPrice.setText(
            "€" + Helper.getTotalPrice(detailedTicketTransactionDTO.getTickets()).toString());
    }

    //if we are coming from the saalplan, the initController method is called and this calls the loadButtonsAccordingToStatus with the flag TRUE - so the status is ignored

    public void initController(CustomerDTO customerDTO, PerformanceDTO performanceDTO, List<? extends TicketDTO> ticketDTOList) {
        loadButtonsAccordingToStatus(TicketStatus.BOUGHT, true);

        transactionNumber.setText("/");
        if(customerDTO == null) {
            customer.setText(BundleManager.getBundle().getString("transaction.detail.anonymousCustomer"));
        }else {
            customer.setText(customerDTO.getFirstName() + " "
                + customerDTO.getLastName());
        }
        totalPrice.setText(
            "€" + Helper.getTotalPrice(ticketDTOList).toString());
    }

    TransactionController transactionController;
    public void setTransactionController(TransactionController transactionController) {
        this.transactionController = transactionController;

    }

    private void loadButtonsAccordingToStatus(TicketStatus status, boolean newEntry) {
        ObservableList<Node> buttons = btnBar.getButtons();
        buttons.clear();

        if(newEntry) {
             //we are coming from the saalplan - status is not yet set
            btnReserve = new Button(BundleManager.getBundle().getString("transaction.detail.reserve"));
            btnReserve.setOnAction(event -> {
                log.info("reserve ticket button has been clicked");
                transactionController.updateTransaction(TicketStatus.RESERVED);

            });
            btnBuy = new Button(BundleManager.getBundle().getString("transaction.detail.buy"));
            btnBuy.setOnAction(event -> {
                log.info("buy tickets button has been clicked");
                transactionController.updateTransaction(TicketStatus.BOUGHT);
            });

            buttons.add(btnReserve);
            buttons.add(btnBuy);
            return;
        }

        if (status == TicketStatus.RESERVED) {
            lbTransactionID.setText(
                BundleManager.getBundle().getString("transaction.detail.reservationNumber"));

            btnCancelReservation = new Button(BundleManager.getBundle().getString("transaction.detail.cancelReservation"));
            btnCancelReservation.setOnAction(event -> {
                log.info("cancel reservation button has been clicked");
                transactionController.updateTransaction(TicketStatus.STORNO);
            });
            btnBuy = new Button(BundleManager.getBundle().getString("transaction.detail.buy"));
            btnBuy.setOnAction(event -> {
                log.info("buy tickets button has been clicked");
                transactionController.updateTransaction(TicketStatus.BOUGHT);
            });

            buttons.add(btnCancelReservation);
            buttons.add(btnBuy);
        } else if (status == TicketStatus.BOUGHT) {
            lbTransactionID
                .setText(BundleManager.getBundle().getString("transaction.detail.billNumber"));
            btnCancelReservation = new Button(BundleManager.getBundle().getString("transaction.detail.cancelReservation"));
            btnCancelReservation.setOnAction(event -> {
                log.info("cancel reservation button has been clicked");
                transactionController.updateTransaction(TicketStatus.STORNO);
            });

            btnPrintPDF = new Button(
                BundleManager.getBundle().getString("transaction.detail.printPDF"));
            btnPrintPDF.setOnAction(event -> {
                log.info("print pdf button in already bought transaction has been clicked");
                transactionController.openPDF();
            });

            buttons.add(btnCancelReservation);
            buttons.add(btnPrintPDF);
        } else if(status == TicketStatus.STORNO) {
            lbTransactionID
                .setText(BundleManager.getBundle().getString("transaction.detail.stornoNumber"));
             btnPrintPDF = new Button(
                BundleManager.getBundle().getString("transaction.detail.printPDF"));
            btnPrintPDF.setOnAction(event -> {
                log.info("print pdf button in cancelled transaction has been clicked");
                transactionController.openPDF();
            });

            buttons.add(btnPrintPDF);
        }

    }
}
