package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader.LoadWrapper;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private VBox vbTickets;

    private final SpringFxmlLoader springFxmlLoader;

    public TransactionDetailController(SpringFxmlLoader springFxmlLoader) {
        this.springFxmlLoader = springFxmlLoader;
    }

    public void initData(List<TicketDTO> ticketDTOList, PerformanceDTO performanceDTO) {
        lbDetailHeader.setText(lbDetailHeader.getText() + " ("  + performanceDTO.getName() + ")");
        setTickets(ticketDTOList);
    }

    public void initData(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        lbDetailHeader.setText(lbDetailHeader.getText() + " ("  + detailedTicketTransactionDTO.getPerformanceName() + ")");
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetailView.fxml");
        TransactionDetailsViewController tdvc = (TransactionDetailsViewController) wrapper.getController();
        tdvc.setDetailedTicketTransactionDTO(detailedTicketTransactionDTO);

        //TODO check if this works so - do we have to clean the children before inserting?
        hbMain.getChildren().add((GridPane) wrapper.getLoadedObject());
        setTickets(detailedTicketTransactionDTO.getTickets());
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
