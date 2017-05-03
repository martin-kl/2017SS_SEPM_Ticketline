package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class TicketElementsController {

    @FXML
    private Label lbPlace;
    @FXML
    private Label lbPrice;

    public void initializeData(TicketDTO ticketDTO) {
        lbPlace.setText("TODO");//ticketDTO.getLocationName());
        lbPrice.setText(ticketDTO.getPrice().toString());
    }
}
