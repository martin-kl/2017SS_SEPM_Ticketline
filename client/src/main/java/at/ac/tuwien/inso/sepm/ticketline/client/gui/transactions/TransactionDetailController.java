package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import java.time.Instant;
import java.util.UUID;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader.LoadWrapper;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionDetailController {

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

    public TransactionDetailController(SpringFxmlLoader springFxmlLoader) {
        this.springFxmlLoader = springFxmlLoader;
    }

    //TODO this method is just for testing while we dont have a saalplan - should be deleted afterwards
    public void initData(List<? extends TicketDTO> ticketDTOList, String performanceName) {
        setHeader(performanceName);
        setTickets(ticketDTOList);
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/customerSelection.fxml");
        CustomerSelection customerSelection = (CustomerSelection) wrapper.getController();
        hbMain.getChildren().add((VBox) wrapper.getLoadedObject());
        customerSelection.reloadCustomers();
        this.ticketDTOList = ticketDTOList;
    }

    public void changeToDetailView(CustomerDTO selectedCustomer) {
        SpringFxmlLoader.LoadWrapper wrapper2 = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetailsView.fxml");
        TransactionDetailsViewController tdvc = (TransactionDetailsViewController) wrapper2
            .getController();
        if(selectedCustomer == null) {
            //TODO no customer was selected - take a "guest" user or insert null?
        }

        //TODO delete this - just to test - create random performance
        tdvc.initController(selectedCustomer,
            new PerformanceDTO(UUID.randomUUID(), "musterPerformance",
                Instant.now(), Instant.now(), null, null), ticketDTOList);

        //clear list and add relevant items again
        ObservableList<Node> children = hbMain.getChildren();
        children.clear();
        children.add(vbTicketsInclLabel);
        children.add(spSeparator);
        children.add((VBox) wrapper2.getLoadedObject());
    }

    //this is the "normal" method that should be called after the saalplan
    public void initData(List<? extends TicketDTO> ticketDTOList, PerformanceDTO performanceDTO) {
        setHeader(performanceDTO.getName());
        setTickets(ticketDTOList);
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/customerSelection.fxml");
        CustomerSelection customerSelection = (CustomerSelection) wrapper.getController();
        hbMain.getChildren().add((VBox) wrapper.getLoadedObject());
        customerSelection.reloadCustomers();
        this.ticketDTOList = ticketDTOList;
    }


    public void initData(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        setHeader(detailedTicketTransactionDTO.getPerformanceName());
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetailsView.fxml");
        TransactionDetailsViewController tdvc = (TransactionDetailsViewController) wrapper
            .getController();
        tdvc.setDetailedTicketTransactionDTO(detailedTicketTransactionDTO);

        //TODO check if this works so - do we have to clean the children before inserting?
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
