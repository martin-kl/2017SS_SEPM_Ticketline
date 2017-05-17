package at.ac.tuwien.inso.sepm.ticketline.client.gui.transactions.details;

import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TicketElementController {

    @FXML
    private Label lbPlace;
    @FXML
    private Label lbPrice;

    public void initializeData(TicketDTO ticketDTO) {
        lbPlace.setText(Helper.getFormattedTicketPlace(ticketDTO));
        lbPrice.setText(ticketDTO.getPrice().toString() + "€");
    }
}
