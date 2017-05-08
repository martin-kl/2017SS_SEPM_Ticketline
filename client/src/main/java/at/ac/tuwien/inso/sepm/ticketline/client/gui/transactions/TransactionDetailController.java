package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.client.util.Callable;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.el.LambdaExpression;

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

    private CustomerDTO selectedCustomer;

    public TransactionDetailController(SpringFxmlLoader springFxmlLoader) {
        this.springFxmlLoader = springFxmlLoader;
    }


    public void initData(List<TicketDTO> ticketDTOList, PerformanceDTO performanceDTO) {
        setHeader(performanceDTO.getName());
        setTickets(ticketDTOList);
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/customerSelection.fxml");
        CustomerSelection tdvc = (CustomerSelection) wrapper.getController();
        hbMain.getChildren().add((VBox) wrapper.getLoadedObject());
        tdvc.reloadCustomers();
        tdvc.setOnContinueClicked(o -> {
            //CONTINUE WAS CLICKED
        });
        tdvc.setOnSelectionChange(o -> {
            //o is null if selection is removed
            if (o == null) {
                selectedCustomer = null;
            } else {
                CustomerDTO customer = (CustomerDTO) o;
                selectedCustomer = customer;
            }
        });
    }


    public void initData(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        setHeader(detailedTicketTransactionDTO.getPerformanceName());
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetailsView.fxml");
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
