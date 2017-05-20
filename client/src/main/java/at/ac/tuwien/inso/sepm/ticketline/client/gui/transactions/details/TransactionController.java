package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.details;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.PerformanceDetailController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;

import java.util.ArrayList;

import java.util.Iterator;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader.LoadWrapper;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.security.krb5.internal.Ticket;

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

    private DetailedTicketTransactionDTO oldTicketTransaction = null;

    public TransactionController(SpringFxmlLoader springFxmlLoader, ReservationService reservationService) {
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
    public void initData(List<? extends TicketDTO> ticketDTOList, DetailedPerformanceDTO detailedPerformanceDTO, PerformanceDetailController performanceDetailController) {
        this.detailedPerformanceDTO = detailedPerformanceDTO;
        //this.performanceDetailController = performanceDetailController;

        this.selectedCustomer = null;
        ticketsSelectable = true;
        setHeader(detailedPerformanceDTO.getName());
        this.ticketDTOList = ticketDTOList;
        selectedTickets.addAll(ticketDTOList); //all selected form the start
        setTickets(ticketDTOList);

        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactions/details/customerSelection.fxml");
        CustomerSelection customerSelection = (CustomerSelection) wrapper.getController();
        hbMain.getChildren().add((VBox) wrapper.getLoadedObject());
        HBox.setHgrow((VBox) wrapper.getLoadedObject(), Priority.ALWAYS);
        customerSelection.initData();
    }

    //TODO there is no performance passed here - so to save this transaction we possible have to load the performance later
    public void initData(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        this.detailedPerformanceDTO = null;
        this.oldTicketTransaction = detailedTicketTransactionDTO;
        //this.performanceDetailController = null;
        ticketsSelectable = false;
        this.selectedCustomer = detailedTicketTransactionDTO.getCustomer();

        setHeader(detailedTicketTransactionDTO.getPerformanceName());

        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactions/details/transactionDetails.fxml");
        TransactionDetailsController tdvc = (TransactionDetailsController) wrapper
            .getController();
        tdvc.setDetailedTicketTransactionDTO(detailedTicketTransactionDTO);
        tdvc.setTransactionController(this);

        //clear list and add relevant items again
        ObservableList<Node> children = hbMain.getChildren();
        children.clear();
        children.add(vbTicketsInclLabel);
        children.add(spSeparator);

        if (detailedTicketTransactionDTO.getStatus() != TicketStatus.BOUGHT) {
            ticketsSelectable = true;
        }
        hbMain.getChildren().add((VBox) wrapper.getLoadedObject());
        HBox.setHgrow((VBox) wrapper.getLoadedObject(), Priority.ALWAYS);
        ticketDTOList = detailedTicketTransactionDTO.getTickets();
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
            LoadWrapper wrapper = springFxmlLoader
                .loadAndWrap("/fxml/transactions/details/ticketElement.fxml");

            ((TicketElementController) wrapper.getController()).initializeData(ticket);
            HBox ticketBox = (HBox) wrapper.getLoadedObject();
            if (ticketsSelectable) {
                if (selectedTickets.contains(ticket)) {
                    ticketBox.setStyle("-fx-background-color: #2196F3");
                }
                ticketBox.setOnMouseClicked((e) -> {
                    if (!selectedTickets.contains(ticket)) {
                        selectedTickets.add(ticket);
                        ticketBox.setStyle("-fx-background-color: #2196F3");
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
        DetailedTicketTransactionDTO dts = new DetailedTicketTransactionDTO(
            oldTicketTransaction != null ? oldTicketTransaction.getId() : null,
            status,
            selectedCustomer,
            selectedTickets,
            "");
        try {
            reservationService.update(dts);
        } catch (ExceptionWithDialog exceptionWithDialog) {
            exceptionWithDialog.showDialog();
        }

    }
}
