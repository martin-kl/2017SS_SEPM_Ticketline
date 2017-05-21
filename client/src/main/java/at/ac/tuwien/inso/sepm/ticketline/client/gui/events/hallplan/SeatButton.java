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
        super.setMinSize(25,25);
        this.fontAwesome = fontAwesome;
        this.ticketWrapper = ticketWrapper;
        setGraphic();
    }

    /**
     * @return true if a free seatbutton was clicked, false otherwise
     */
    public boolean onClick(){
         /* free */
        if(ticketWrapper.getStatus() == TicketStatus.STORNO) {
            ticketWrapper.setStatus(TicketStatus.SELECTED);
            setGraphic();
            return true;
        }
        /* not free */
        else if (ticketWrapper.getStatus() == TicketStatus.RESERVED || ticketWrapper.getStatus() == TicketStatus.BOUGHT) {
        }
        /* selected by the current user */
        else if (ticketWrapper.getStatus() == TicketStatus.SELECTED) {
            ticketWrapper.setStatus(TicketStatus.STORNO);
            setGraphic();
            return false;
        }
        return false;
    }

    private void setGraphic(){
        /* free */
        if(ticketWrapper.getStatus() == TicketStatus.STORNO) {
            //super.setStyle("");
            super.setGraphic(null);
            super.setStyle("-fx-background-color: rgba(205,255,215,0.72); "
                + "-fx-border-color: #495c50;"
                + "-fx-border-style: solid;"
                + "-fx-border-radius: 4;"
                + "-fx-border-width: 2;");
        }
        /* not free */
        else if (ticketWrapper.getStatus() == TicketStatus.RESERVED || ticketWrapper.getStatus() == TicketStatus.BOUGHT) {
            //super.setStyle("");
            super.setGraphic(fontAwesome.create(FontAwesome.Glyph.USER));
            super.setStyle("-fx-background-color: rgba(211,224,255,0.72);"
                + "-fx-border-color: #414a5c;"
                + "-fx-border-style: solid;"
                + "-fx-border-radius: 4;"
                + "-fx-border-width: 2;");
        }
        /* selected by the current user */
        else if (ticketWrapper.getStatus() == TicketStatus.SELECTED) {
            //super.setStyle("");
            super.setGraphic(fontAwesome.create(FontAwesome.Glyph.USER));
            super.setStyle("-fx-background-color: rgba(188,0,0,0.97);"
                + "-fx-border-color: #520000;"
                + "-fx-border-style: solid;"
                + "-fx-border-radius: 4;"
                + "-fx-background-radius: 3;"
                + "-fx-background-insets: 2;"
                + "-fx-border-width: 2;");
        }
    }
    public TicketWrapperDTO getTicketWrapper() {
        return ticketWrapper;
    }

    public void setTicketWrapper(
        TicketWrapperDTO ticketWrapper) {
        this.ticketWrapper = ticketWrapper;
    }
}
