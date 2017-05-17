package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.hallplan.PerformanceDetailController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.hallplan.SeatButton;
import at.ac.tuwien.inso.sepm.ticketline.client.service.HallplanService;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.SectorLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SeatTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.SectorTicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketWrapperDTO;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HallplanServiceImpl implements HallplanService {

    @Override
    public GridPane constructHallplan(DetailedPerformanceDTO detailedPerformanceDTO, GridPane gridPane, FontAwesome fontAwesome){
        if(detailedPerformanceDTO.getLocation() instanceof SectorLocationDTO) {
            // WORKING WITH SECTORS
            // building a list of sectors


        } else {
            // WORKING WITH SEATS
            // building a list of seats
            /*gridPane.getChildren().clear();
            gridPane.getRowConstraints().clear();
            gridPane.getColumnConstraints().clear();
            gridPane.setGridLinesVisible(true);
            gridPane.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            gridPane.setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            List<TicketWrapperDTO> ticketList = detailedPerformanceDTO.getTicketWrapperList();
            // TODO: get max rows and max columns
            for(int i = 0, column = 0, row = 0; i < ticketList.size(); i++, column = (i % 4 == 0 ? 0 : column + 1), row += (i % 4 == 0 ? 1 : 0)){
                TicketWrapperDTO ticketWrapper = ticketList.get(i);
                SeatTicketDTO seatTicket = (SeatTicketDTO) ticketWrapper.getTicket();
                TicketStatus ticketStatus = ticketWrapper.getStatus();
                SeatDTO seat = seatTicket.getSeat();

                SeatButton seatButton = new SeatButton(fontAwesome, ticketWrapper);
                seatButton.setOnMouseClicked(e -> {
                    log.debug("clicked on seat: " + seatButton.getSeatInfo());
                });

                final Label label = new Label(seat.getRow() + "," + seat.getColumn());
                log.debug("seat at: " + seat.getRow() + ", " + seat.getColumn());
                gridPane.add(seatButton, seat.getColumn(), seat.getRow());
            }
*/

        }

        return null;
    }



    @Override
    public List<SectorDTO> getSectorsOfPerformance(DetailedPerformanceDTO detailedPerformanceDTO) {
        List<SectorDTO> sectorList = new ArrayList<>();
        for(TicketWrapperDTO ticketWrapper : detailedPerformanceDTO.getTicketWrapperList()){
            // each TicketWrapper contains (TicketDTO + TicketStatus)
            if(ticketWrapper.getTicket() instanceof SectorTicketDTO) {
                SectorDTO currSector = ((SectorTicketDTO) ticketWrapper.getTicket()).getSector();
                if(!sectorList.contains(currSector))
                    sectorList.add(currSector);
            }
        }
        log.debug("Sectors available: " + sectorList.toString());
        return sectorList;
    }

    @Override
    public List<SeatDTO> getSeatsOfPerformance(DetailedPerformanceDTO detailedPerformanceDTO) {
        List<SeatDTO> seatList = new ArrayList<>();
        for(TicketWrapperDTO ticketWrapper : detailedPerformanceDTO.getTicketWrapperList()){
            // each TicketWrapper contains (TicketDTO + TicketStatus)
            if(ticketWrapper.getTicket() instanceof SeatTicketDTO) {
                SeatDTO currSeat = ((SeatTicketDTO) ticketWrapper.getTicket()).getSeat();
                if(!seatList.contains(currSeat))
                    seatList.add(currSeat);
            }
        }
        log.debug("Sectors available: " + seatList.toString());
        return seatList;
    }

    @Override
    public SectorTicketDTO getRandomFreeSectorTicket(DetailedPerformanceDTO detailedPerformanceDTO, SectorDTO sectorDTO, List<TicketDTO> chosenTickets) {
        for(TicketWrapperDTO ticketWrapper : detailedPerformanceDTO.getTicketWrapperList()){
            //log.debug("in ticketwrapper : " + ticketWrapper);
            // each TicketWrapper contains (TicketDTO + TicketStatus)
            if(ticketWrapper.getTicket() instanceof SectorTicketDTO) {
                if(((SectorTicketDTO) ticketWrapper.getTicket()).getSector().equals(sectorDTO)){
                    // we want a ticket in this sector
                    if(ticketWrapper.getStatus().equals(TicketStatus.STORNO)) {
                        if(chosenTickets.contains(ticketWrapper.getTicket()))
                            continue;

                        // this one is free
                        log.debug("Found a free ticket in wrapper: " + ticketWrapper);
                        return (SectorTicketDTO) ticketWrapper.getTicket();
                    }
                }
            }
        }
        log.debug("No free ticket found, returning null");
        return null;
    }
}
