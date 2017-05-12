package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.hallplan.PerformanceDetailController;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
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

@Slf4j
@Component
public class TransactionDetailController {

    @Getter
    @FXML
    private BorderPane bpDetailMainPane;
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

    private final SpringFxmlLoader springFxmlLoader;
    private DetailedPerformanceDTO detailedPerformanceDTO;
    private PerformanceDetailController performanceDetailController;

    public TransactionDetailController(SpringFxmlLoader springFxmlLoader) {
        this.springFxmlLoader = springFxmlLoader;
    }


    public void changeToDetailView(CustomerDTO selectedCustomer) {
        SpringFxmlLoader.LoadWrapper wrapper2 = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetailsView.fxml");
        TransactionDetailsViewController tdvc = (TransactionDetailsViewController) wrapper2
            .getController();
        if (selectedCustomer == null) {
            //TODO no customer was selected - take a "guest" user or insert null? currently null is passed
        }

        tdvc.initController(selectedCustomer, detailedPerformanceDTO, ticketDTOList);

        //clear list and add relevant items again
        ObservableList<Node> children = hbMain.getChildren();
        children.clear();
        children.add(vbTicketsInclLabel);
        children.add(spSeparator);
        children.add((VBox) wrapper2.getLoadedObject());
    }

    //this is the "normal" method that is called after the hallplan
    public void initData(List<? extends TicketDTO> ticketDTOList,
        DetailedPerformanceDTO detailedPerformanceDTO,
        PerformanceDetailController performanceDetailController) {
        this.detailedPerformanceDTO = detailedPerformanceDTO;
        this.performanceDetailController = performanceDetailController;

        setHeader(detailedPerformanceDTO.getName());
        setTickets(ticketDTOList);

        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/customerSelection.fxml");
        CustomerSelection customerSelection = (CustomerSelection) wrapper.getController();
        hbMain.getChildren().add((VBox) wrapper.getLoadedObject());
        customerSelection.reloadCustomers();
        this.ticketDTOList = ticketDTOList;
    }

    //TODO there is no performance passed here - so to save this transaction we possible have to load the performance later
    public void initData(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        this.detailedPerformanceDTO = null;
        this.performanceDetailController = null;

        setHeader(detailedTicketTransactionDTO.getPerformanceName());

        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetailsView.fxml");
        TransactionDetailsViewController tdvc = (TransactionDetailsViewController) wrapper
            .getController();
        tdvc.setDetailedTicketTransactionDTO(detailedTicketTransactionDTO);

        //clear list and add relevant items again
        ObservableList<Node> children = hbMain.getChildren();
        children.clear();
        children.add(vbTicketsInclLabel);
        children.add(spSeparator);

        hbMain.getChildren().add((VBox) wrapper.getLoadedObject());
        setTickets(detailedTicketTransactionDTO.getTickets());

        this.ticketDTOList = detailedTicketTransactionDTO.getTickets();
    }

    private void setHeader(String performanceName) {
        lbDetailHeader.setText(
            BundleManager.getBundle().getString("transaction.detail.header")
                + performanceName);
    }

    private void setTickets(List<? extends TicketDTO> ticketDTOList) {

        //TODO add support to select multiple tickets if it is a reservation
        ObservableList<Node> vbTicketBoxChildren = vbTickets.getChildren();
        vbTicketBoxChildren.clear();

        for (TicketDTO ticket : ticketDTOList) {
            LoadWrapper wrapper = springFxmlLoader
                .loadAndWrap("/fxml/transactionDetail/ticketElement.fxml");

            ((TicketElementController) wrapper.getController()).initializeData(ticket);
            vbTicketBoxChildren.add((HBox) wrapper.getLoadedObject());
        }
    }
}
