package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TransactionDetailController {
    @FXML
    private Label lbDetailHeader;
    @FXML
    private HBox hbMain;
    @FXML
    private VBox vbTickets;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    public TransactionDetailController(
        MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    public void initData(List<TicketDTO> ticketDTOList, PerformanceDTO performanceDTO) {
        //Todo set performance header
        lbDetailHeader.setText(lbDetailHeader.getText() + " ("  + performanceDTO.getName() + ")");
        setTickets(ticketDTOList);
    }

    public void initData(DetailedTicketTransactionDTO detailedTicketTransactionDTO) {
        //Martin: todo set rest - Calvin: Was meinst du??
        lbDetailHeader.setText(lbDetailHeader.getText() + " ("  + detailedTicketTransactionDTO.getPerformanceName() + ")");
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/transactionDetail/transactionDetailsView.fxml");
        TransactionDetailsViewController tdvc = (TransactionDetailsViewController) wrapper.getController();
        tdvc.setDetailedTicketTransactionDTO(detailedTicketTransactionDTO);
        hbMain.getChildren().set(1, (GridPane) wrapper.getLoadedObject());
        setTickets(detailedTicketTransactionDTO.getTickets());
    }

    private Parent loadTransactionDetails() {

    }

    private void setTickets(List<TicketDTO> ticketDTOList) {
        ObservableList<Node> vbTicketBoxChildren = vbTickets.getChildren();
        vbTicketBoxChildren.clear();

        Iterator<TicketDTO> iterator = ticketDTOList.iterator();
        while (iterator.hasNext()) {
            TicketDTO ticket = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                .loadAndWrap("/fxml/transactionDetail/transactionDetail.fxml");

            ((TicketElementController) wrapper.getController()).initializeData(ticket);
            vbTicketBoxChildren.add((HBox) wrapper.getLoadedObject());
        }
    }
}
