package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TicketElementsController {

    @FXML
    private Label lbPlace;
    @FXML
    private Label lbPrice;
    @FXML
    private Label lbBoughtReserved;

    public void initializeData(ReservationDTO reservationDTO) {
        lbPlace.setText(customerDTO.getFirstName());
        lbPrice.setText(customerDTO.getLastName());
        lbBoughtReserved.setText(customerDTO.getLastName());
    }
}
