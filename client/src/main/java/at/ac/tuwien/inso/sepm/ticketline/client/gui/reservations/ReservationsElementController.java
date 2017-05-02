package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ReservationsElementController {

    @FXML
    private VBox vbReservationAndTickets;
    @FXML
    private Label lbCustomerFirstName;
    @FXML
    private Label lbCustomerLastName;
    @FXML
    private Label lbReservationID;

    private final SpringFxmlLoader springFxmlLoader;

    public ReservationsElementController(SpringFxmlLoader springFxmlLoader) {
        this.springFxmlLoader = springFxmlLoader;
    }

    public void initializeData(ReservationDTO reservationDTO) {
        lbCustomerFirstName.setText(reservationDTO.getCustomerFirstName());
        lbCustomerLastName.setText(reservationDTO.getCustomerLastName());
        lbReservationID.setText(reservationDTO.getId().toString());

        ObservableList<Node> vbReservationAndTicketsChildren = vbReservationAndTickets.getChildren();
        vbReservationAndTicketsChildren.clear();

        Iterator<TicketDTO> iterator = reservationDTO.getTickets().iterator();
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
            if (iterator.hasNext()) {
                Separator separator = new Separator();
                vbReservationAndTicketsChildren.add(separator);
            }
        }
    }
}
