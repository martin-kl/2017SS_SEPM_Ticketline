package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.hallplan;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketWrapperDTO;
import java.awt.Color;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

@Slf4j
public class SeatButton extends Button {
    private TicketWrapperDTO ticketWrapper;
    private FontAwesome fontAwesome;

    public SeatButton(FontAwesome fontAwesome, TicketWrapperDTO ticketWrapper){
        this.fontAwesome = fontAwesome;
        this.ticketWrapper = ticketWrapper;
        setGraphic();
    }

    private void setGraphic(){
        /* free */
        if(ticketWrapper.getStatus() == TicketStatus.STORNO) {
            super.setGraphic(fontAwesome.create(FontAwesome.Glyph.CIRCLE_THIN));
        }
        /* not free */
        else if (ticketWrapper.getStatus() == TicketStatus.RESERVED || ticketWrapper.getStatus() == TicketStatus.BOUGHT) {
            super.setGraphic(fontAwesome.create(FontAwesome.Glyph.USER));
        }
        /* selected by the current user */
        else if (ticketWrapper.getStatus() == TicketStatus.SELECTED) {
            super.setGraphic(fontAwesome.create(FontAwesome.Glyph.CIRCLE));
        }
        // TODO: load image

        //super.setGraphic();
    }

    public String getSeatInfo(){
        SeatDTO seat = ((SeatTicketDTO) ticketWrapper.getTicket()).getSeat();

        return "Seat at: row: " + seat.getRow() + ", column: " + seat.getColumn();
    }
}
