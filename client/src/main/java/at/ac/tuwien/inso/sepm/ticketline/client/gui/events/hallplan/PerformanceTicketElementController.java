package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.hallplan;

import at.ac.tuwien.inso.sepm.ticketline.client.util.Helper;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PerformanceTicketElementController {
    @FXML
    private Label lblPlace;

    public void initializeData(TicketDTO ticketDTO) {
        lblPlace.setText(Helper.getFormattedTicketPlace(ticketDTO));
    }
}
