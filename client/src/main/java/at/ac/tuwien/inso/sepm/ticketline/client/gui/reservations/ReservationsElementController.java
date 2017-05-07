package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class ReservationsElementController {

    @FXML
    private VBox vbReservationAndTickets;
    @FXML
    private HBox hbReservation;
    @FXML
    private Label lbCustomerFirstName;
    @FXML
    private Label lbCustomerLastName;
    @FXML
    private Label lbReservationID;
    @FXML
    private Label lbPerformanceName;
    @FXML
    private Label lbBoughtReserved;

    private final SpringFxmlLoader springFxmlLoader;

    public ReservationsElementController(SpringFxmlLoader springFxmlLoader) {
        this.springFxmlLoader = springFxmlLoader;
    }

    public void initializeData(DetailedTicketTransactionDTO ticketTransactionDTO) {
        lbCustomerFirstName.setText(ticketTransactionDTO.getCustomer().getFirstName());
        lbCustomerLastName.setText(ticketTransactionDTO.getCustomer().getLastName());
        lbReservationID.setText(ticketTransactionDTO.getId().toString());
        lbPerformanceName.setText(ticketTransactionDTO.getPerformanceName());

        TicketStatus status = ticketTransactionDTO.getStatus();
        lbBoughtReserved.setText(
            status == TicketStatus.BOUGHT ? BundleManager.getBundle()
                .getString("reservation.ticket.bought")
                : BundleManager.getBundle().getString("reservation.ticket.reserved"));

        ObservableList<Node> vbReservationAndTicketsChildren = vbReservationAndTickets
            .getChildren();

        //copy HBox away, clear VBox and add HBox again
        HBox hbReservationTemp = hbReservation;
        vbReservationAndTicketsChildren.clear();
        vbReservationAndTicketsChildren.add(hbReservationTemp);

        Iterator<TicketDTO> iterator = ticketTransactionDTO.getTickets().iterator();

        while (iterator.hasNext()) {
            TicketDTO ticket = iterator.next();
            SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                .loadAndWrap("/fxml/reservations/ticketsElement.fxml");

            ((TicketElementsController) wrapper.getController()).initializeData(ticket);
            HBox reservationBox = (HBox) wrapper.getLoadedObject();

            /*
                customerBox.setOnMouseClicked((e) -> {
                    handleCustomerEdit(customer);
                });
           */
            vbReservationAndTicketsChildren.add(reservationBox);
        }
    }
}
