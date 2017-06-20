package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.details;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.hallplan.PerformanceDetailController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.TransactionListController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class TransactionController {

    @Getter
    @FXML
    private VBox vbTransactionDetail;
    @FXML
    private Label lbDetailHeader;
    @FXML
    private HBox hbMain;
    @FXML
    private VBox vbTicketsInclLabel;
    @FXML
    private VBox vbTickets;
    @FXML
    private Separator spSeparator;

    private List<? extends TicketDTO> ticketDTOList;

    private boolean ticketsSelectable = false;

    private final SpringFxmlLoader springFxmlLoader;
    private final ReservationService reservationService;
    private DetailedPerformanceDTO detailedPerformanceDTO;
    //private PerformanceDetailController performanceDetailController;
    private List<TicketDTO> selectedTickets = new ArrayList<>();
    private CustomerDTO selectedCustomer;


    private TransactionListController tlController = null;

    private DetailedTicketTransactionDTO oldTicketTransaction = null;

    public TransactionController(SpringFxmlLoader springFxmlLoader,
        ReservationService reservationService) {
        this.springFxmlLoader = springFxmlLoader;
        this.reservationService = reservationService;
    }


    public void onContinue(CustomerDTO selectedCustomer) {
        SpringFxmlLoader.LoadWrapper wrapper2 = springFxmlLoader
            .loadAndWrap("/fxml/transactions/details/transactionDetails.fxml");
        TransactionDetailsController tdvc = (TransactionDetailsController) wrapper2
            .getController();
        this.selectedCustomer = selectedCustomer;

        //selected tickets can be passed here

        tdvc.initController(selectedCustomer, detailedPerformanceDTO, ticketDTOList);
        tdvc.setTransactionController(this);

        //clear list and add relevant items again
        ObservableList<Node> children = hbMain.getChildren();
        children.clear();
        children.add(vbTicketsInclLabel);
        children.add(spSeparator);
        children.add((VBox) wrapper2.getLoadedObject());
        HBox.setHgrow((VBox) wrapper2.getLoadedObject(), Priority.ALWAYS);
    }

    //this is the "normal" method that is called after the hallplan
    public void initData(List<? extends TicketDTO> ticketDTOList,
        DetailedPerformanceDTO detailedPerformanceDTO,
        PerformanceDetailController performanceDetailController) {
        this.detailedPerformanceDTO = detailedPerformanceDTO;
        //this.performanceDetailController = performanceDetailController;

        this.oldTicketTransaction = null;
        this.selectedCustomer = null;
        ticketsSelectable = true;
        setHeader(detailedPerformanceDTO.getName());
        this.ticketDTOList = ticketDTOList;
        selectedTickets.clear();
        selectedTickets.addAll(ticketDTOList); //all selected form the start
        setTickets(ticketDTOList);

        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactions/details/customerSelection.fxml");
        CustomerSelection customerSelection = (CustomerSelection) wrapper.getController();
        hbMain.getChildren().add((VBox) wrapper.getLoadedObject());
        HBox.setHgrow((VBox) wrapper.getLoadedObject(), Priority.ALWAYS);
        customerSelection.initData();
    }

    public void initData(DetailedTicketTransactionDTO detailedTicketTransactionDTO,
        TransactionListController transactionListController, boolean cancelledReservation) {
        this.detailedPerformanceDTO = null;
        this.oldTicketTransaction = detailedTicketTransactionDTO;
        //this.performanceDetailController = null;
        ticketsSelectable = false;
        this.selectedCustomer = detailedTicketTransactionDTO.getCustomer();
        this.tlController = transactionListController;

        setHeader(detailedTicketTransactionDTO.getPerformanceName());

        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactions/details/transactionDetails.fxml");
        TransactionDetailsController tdvc = (TransactionDetailsController) wrapper
            .getController();
        tdvc.setDetailedTicketTransactionDTO(detailedTicketTransactionDTO, cancelledReservation);
        tdvc.setTransactionController(this);

        //clear list and add relevant items again
        ObservableList<Node> children = hbMain.getChildren();
        children.clear();
        children.add(vbTicketsInclLabel);
        children.add(spSeparator);

        if (detailedTicketTransactionDTO.getStatus() != TicketStatus.BOUGHT
            && detailedTicketTransactionDTO.getStatus() != TicketStatus.STORNO) {
            ticketsSelectable = true;
        }
        hbMain.getChildren().add((VBox) wrapper.getLoadedObject());
        HBox.setHgrow((VBox) wrapper.getLoadedObject(), Priority.ALWAYS);
        ticketDTOList = detailedTicketTransactionDTO.getTickets();
        selectedTickets.clear();
        selectedTickets.addAll(ticketDTOList); //all selected form the start
        setTickets(ticketDTOList);
    }

    private void setHeader(String performanceName) {
        lbDetailHeader.setText(
            BundleManager.getBundle().getString("transaction.detail.header")
                + performanceName);
    }

    private void setTickets(List<? extends TicketDTO> ticketDTOList) {
        ObservableList<Node> vbTicketBoxChildren = vbTickets.getChildren();
        vbTicketBoxChildren.clear();

        Iterator<? extends TicketDTO> iterator = ticketDTOList.iterator();
        while (iterator.hasNext()) {
            TicketDTO ticket = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                .loadAndWrap("/fxml/transactions/details/ticketElement.fxml");

            ((TicketElementController) wrapper.getController()).initializeData(ticket);
            HBox ticketBox = (HBox) wrapper.getLoadedObject();
            if (ticketsSelectable) {
                if (selectedTickets.contains(ticket)) {
                    ticketBox.setStyle("-fx-background-color: #00afff");
                }
                ticketBox.setOnMouseClicked((e) -> {
                    if (!selectedTickets.contains(ticket)) {
                        selectedTickets.add(ticket);
                        ticketBox.setStyle("-fx-background-color: #00afff");
                    } else {
                        selectedTickets.remove(ticket);
                        ticketBox.setStyle("-fx-background-color: inherit");
                    }
                });
            }
            vbTicketBoxChildren.add(ticketBox);
            if (iterator.hasNext()) {
                Separator separator = new Separator();
                vbTicketBoxChildren.add(separator);
            }
        }
    }

    public void updateTransaction(TicketStatus status) {
        if (selectedTickets.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(BundleManager.getBundle().getString("transaction.notickets"));
            alert
                .setHeaderText(BundleManager.getBundle().getString("transaction.notickets.header"));
            alert.showAndWait();
            return;
        }
        DetailedTicketTransactionDTO dts = new DetailedTicketTransactionDTO(
            oldTicketTransaction != null ? oldTicketTransaction.getId() : null,
            status,
            selectedCustomer,
            selectedTickets,
            "");
        try {
            DetailedTicketTransactionDTO dttdto = reservationService.update(dts);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(BundleManager.getBundle().getString("transaction.saved.title"));
            alert.setHeaderText(BundleManager.getBundle().getString("transaction.saved.header"));
            alert.showAndWait();

            boolean cancelledReservation = false;
            if ((oldTicketTransaction != null) &&
                (oldTicketTransaction.getStatus() == TicketStatus.RESERVED) &&
                (status == TicketStatus.STORNO)) {
                cancelledReservation = true;
            }
            initData(dttdto, tlController, cancelledReservation);
            if (tlController != null) {
                tlController.initTransactions();
            }
        } catch (ExceptionWithDialog exceptionWithDialog) {
            exceptionWithDialog.showDialog();
        }
    }

    public void openPDF() {
        try {
            if (oldTicketTransaction == null) {
                throw new Error("not possible to print a not existing transaction");
            }
            reservationService.downloadFile(oldTicketTransaction.getId());
        } catch (ExceptionWithDialog exceptionWithDialog) {
            exceptionWithDialog.showDialog();
        }
    }
}
