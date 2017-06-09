package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.hallplan;

import at.ac.tuwien.inso.sepm.ticketline.client.service.HallplanService;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.List;

@Slf4j
public class SectorButton extends Button {
    private SectorDTO sectorDTO;
    private FontAwesome fontAwesome;
    private SectorStatus currentStatus = SectorStatus.FREE;

    public SectorButton(FontAwesome fontAwesome, SectorDTO sectorDTO, DetailedPerformanceDTO performanceDTO, List<TicketDTO> chosenTickets, HallplanService hallplanService){
        super.setText(sectorDTO.getName());
        super.alignmentProperty().setValue(Pos.CENTER);
        super.setTextAlignment(TextAlignment.CENTER);
        super.wrapTextProperty().setValue(true);
        super.setPrefSize(200,200);
        this.sectorDTO = sectorDTO;
        this.fontAwesome = fontAwesome;
        setSectorStatus(performanceDTO, chosenTickets, hallplanService);
        setGraphic();
    }

    public void setSectorStatus(DetailedPerformanceDTO performanceDTO, List<TicketDTO> chosenTickets, HallplanService hallplanService){
        /* initializing -> check if there are free tickets for this sector */
        if(hallplanService.getRandomFreeSectorTicket(performanceDTO, sectorDTO, chosenTickets) != null){
            //log.debug("Found a free ticket" + sectorDTO.getName());
            currentStatus = SectorStatus.FREE;
            super.setDisable(false);
        } else {
            currentStatus = SectorStatus.NOT_FREE;
            super.setDisable(true);
        }
        setGraphic();
    }
    private void setGraphic() {
        /* free */
        if (currentStatus == SectorStatus.FREE) {
            super.setStyle("-fx-background-color: rgba(205,255,215,0.72); "
                + "-fx-border-color: #495c50;"
                + "-fx-border-style: solid;"
                + "-fx-border-radius: 4;"
                + "-fx-border-width: 2;");
        }
        /* not free */
        else if (currentStatus == SectorStatus.NOT_FREE) {
            super.setStyle("-fx-background-color: rgba(211,224,255,0.72);"
                + "-fx-border-color: #414a5c;"
                + "-fx-border-style: solid;"
                + "-fx-border-radius: 4;"
                + "-fx-border-width: 2;");
        }
        /* selected by the current user */
        /*else if (currentStatus == SectorStatus.SELECTED) {
            super.setStyle("-fx-background-color: rgba(188,0,0,0.97);"
                + "-fx-border-color: #520000;"
                + "-fx-border-style: solid;"
                + "-fx-border-radius: 4;"
                + "-fx-background-radius: 3;"
                + "-fx-background-insets: 2;"
                + "-fx-border-width: 2;");
        }*/
    }
}
