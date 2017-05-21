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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    public Map<Integer, Integer> getColumCounts(DetailedPerformanceDTO detailedPerformanceDTO){
        Map<Integer,Integer> columnCounts = new HashMap<>();

        List<TicketWrapperDTO> ticketList = detailedPerformanceDTO.getTicketWrapperList();
        // TODO: get max rows and max columns
        for (TicketWrapperDTO ticketWrapper : ticketList) {
            SeatTicketDTO seatTicket = (SeatTicketDTO) ticketWrapper.getTicket();
            SeatDTO seat = seatTicket.getSeat();

            if(columnCounts.containsKey(seat.getRow())){
                // this row is already in columnCounts, check the value and update if this seat has higher column
                if(columnCounts.get(seat.getRow()) < seat.getColumn()){
                    columnCounts.put(seat.getRow(), seat.getColumn());
                }
            }
            else
                columnCounts.put(seat.getRow(), seat.getColumn());
        }
        return columnCounts;
    }

    @Override
    public Integer getLargestRowColumnCount(DetailedPerformanceDTO detailedPerformanceDTO) {
        Map<Integer, Integer> columCounts = getColumCounts(detailedPerformanceDTO);

        int biggestRow = Collections.max(columCounts.entrySet(), Comparator.comparingInt(Entry::getValue)).getKey();
        return columCounts.get(biggestRow);
    }

    @Override
    public SectorTicketDTO getRandomFreeSectorTicket(DetailedPerformanceDTO detailedPerformanceDTO, SectorDTO sectorDTO, List<TicketDTO> chosenTickets) {
        for(TicketWrapperDTO ticketWrapper : detailedPerformanceDTO.getTicketWrapperList()){
            // each TicketWrapper contains (TicketDTO + TicketStatus)
            if(ticketWrapper.getTicket() instanceof SectorTicketDTO) {
                if(((SectorTicketDTO) ticketWrapper.getTicket()).getSector().equals(sectorDTO)){
                    // we want a ticket in this sector
                    if(ticketWrapper.getStatus().equals(TicketStatus.STORNO)) {
                        if(chosenTickets.contains(ticketWrapper.getTicket()))
                            continue;

                        // this one is free
                        //log.debug("Found a free ticket in wrapper: " + ticketWrapper);
                        return (SectorTicketDTO) ticketWrapper.getTicket();
                    }
                }
            }
        }
        //log.debug("No free ticket found, returning null");
        return null;
    }
}
