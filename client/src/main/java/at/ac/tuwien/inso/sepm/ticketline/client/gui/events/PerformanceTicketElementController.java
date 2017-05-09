package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorTicketDTO;
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

    @FXML
    private Label lblPrice;

    public void initializeData(TicketDTO ticketDTO) {
        if(ticketDTO instanceof SeatTicketDTO){
            SeatTicketDTO seatTicket = (SeatTicketDTO) ticketDTO;
            lblPlace.setText(seatTicket.getSeat().getRow() + ", " + seatTicket.getSeat().getColumn());
            lblPrice.setText("" + seatTicket.getPrice());
        }
        else {
            SectorTicketDTO sectorTicket = (SectorTicketDTO) ticketDTO;
            lblPlace.setText(sectorTicket.getSector().getName());
            lblPrice.setText("" + sectorTicket.getPrice());
        }

    }
}
