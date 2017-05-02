package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.TicketDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TicketElementsController {

    @FXML
    private Label lbPlace;
    @FXML
    private Label lbPrice;
    @FXML
    private Label lbBoughtReserved;

    public void initializeData(TicketDTO ticketDTO) {
        lbPlace.setText(ticketDTO.getLocationName());
        lbPrice.setText(ticketDTO.getPrice().toString());
        lbBoughtReserved.setText(
            ticketDTO.getBought() ? BundleManager.getBundle().getString("reservation.ticket.bought")
                : BundleManager.getBundle().getString("reservation.ticket.reserved"));
    }
}
