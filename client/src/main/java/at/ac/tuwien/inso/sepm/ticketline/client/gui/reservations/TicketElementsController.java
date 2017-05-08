package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorTicketDTO;
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
        if(ticketDTO instanceof SeatTicketDTO) {
            SeatTicketDTO seatTicket = (SeatTicketDTO) ticketDTO;
            lbPlace.setText("Reihe" + seatTicket.getSeat().getRow() + ", Platz " + seatTicket.getSeat().getColumn());
        }else {
            SectorTicketDTO sectorTicket = (SectorTicketDTO) ticketDTO;
            lbPlace.setText("Sector " + sectorTicket.getSector().getName());
        }
        //lbPlace.setText("TODO insert Place here");//ticketDTO.getLocationName());
        lbPrice.setText(ticketDTO.getPrice() + "€");
    }
}
