package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.scene.layout.VBox;<<<<<<< Updated upstream
=======
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomersElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
>>>>>>> Stashed changes
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
<<<<<<< Updated upstream
import at.ac.tuwien.inso.springfx.SpringFxmlLoader.LoadWrapper;
=======

import java.util.ArrayList;
import java.util.Iterator;
>>>>>>> Stashed changes
import java.util.List;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
<<<<<<< Updated upstream
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> Stashed changes

@Slf4j
@Component
public class TransactionDetailController {
    @FXML
    private Label lbDetailHeader;
    @FXML
    private HBox hbMain;
    @FXML
    private VBox vbTickets;

    private final SpringFxmlLoader springFxmlLoader;

    public TransactionDetailController(SpringFxmlLoader springFxmlLoader) {
        this.springFxmlLoader = springFxmlLoader;
    }


    public void initData(List<TicketDTO> ticketDTOList, PerformanceDTO performanceDTO) {
        setHeader(performanceDTO.getName());
        setTickets(ticketDTOList);
    }


    public void initData(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        setHeader(detailedTicketTransactionDTO.getPerformanceName());
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetailView.fxml");
        TransactionDetailsViewController tdvc = (TransactionDetailsViewController) wrapper.getController();
        tdvc.setDetailedTicketTransactionDTO(detailedTicketTransactionDTO);

        //TODO check if this works so - do we have to clean the children before inserting?
        hbMain.getChildren().add((GridPane) wrapper.getLoadedObject());
        setTickets(detailedTicketTransactionDTO.getTickets());
    }

    private void setHeader(String performanceName) {
        lbDetailHeader.setText(
            BundleManager.getBundle().getString("transaction.detail.header")
                + performanceName);
    }

    private void setTickets(List<TicketDTO> ticketDTOList) {
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
