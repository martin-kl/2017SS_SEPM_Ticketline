package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReservationsElementController {

    @FXML
    private Label lbCustomerFirstName;
    @FXML
    private Label lbCustomerLastName;
    @FXML
    private Label lbReservationID;


    public void initializeData(ReservationDTO reservationDTO) {
        lbCustomerFirstName.setText(reservationDTO.getCustomerFirstName());
        lbCustomerLastName.setText(reservationDTO.getCustomerLastName());
        lbReservationID.setText(reservationDTO.getId().toString());

        //TODO initialize tickets - sub list from reservation
    }
}
