package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatTicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorTicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Profile("generateData")
@Component
public class TicketDataGenerator {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SectorTicketRepository sectorTicketRepository;

    @Autowired
    private SeatTicketRepository seatTicketRepository;

    // injected so we make sure that performances are loaded
    @Autowired
    private PerformanceDataGenerator performanceDataGenerator;

    @PostConstruct
    private void generateTickets() {
        if (ticketRepository.count() > 0) {
            log.info("tickets already generated");
        } else {
            List<Performance> performanceList = performanceRepository.findAll();

            for (Performance performance : performanceList) {
                Location location = performance.getLocation();

                if (location instanceof SeatLocation) {
                    SeatLocation seatLocation = (SeatLocation) location;
                    generateTicketsForSeatPerformance(performance, seatLocation);
                } else if (location instanceof SectorLocation) {
                    SectorLocation sectorLocation = (SectorLocation) location;
                    generateTicketsForSectorPerformance(performance, sectorLocation);
                }

            }
        }
    }

    // TODO: put this code in a service
    private void generateTicketsForSectorPerformance(Performance performance, SectorLocation sectorLocation) {
        List<SectorTicket> tickets = new ArrayList<>();
        for (Sector sector : sectorLocation.getSectors()) {
            for (int i = 0; i < sector.getSize(); i++) {
                SectorTicket sectorTicket = SectorTicket.builder()
                    .sector(sector)
                    .performance(performance)
                    .price(performance.getDefaultPrice().multiply(new BigDecimal(sector.getPriceCategory().getModifier())))
                    .build();
                tickets.add(sectorTicket);
            }
        }
        log.info("created {} tickets", tickets.size());
        sectorTicketRepository.save(tickets);
    }

    // TODO: put this code in a service
    private void generateTicketsForSeatPerformance(Performance performance, SeatLocation seatLocation) {
        List<SeatTicket> tickets = new ArrayList<>();
        for (Seat seat : seatLocation.getSeats()) {
            SeatTicket seatTicket = SeatTicket.builder()
                .seat(seat)
                .performance(performance)
                .price(performance.getDefaultPrice().multiply(new BigDecimal(seat.getPriceCategory().getModifier())))
                .build();
            tickets.add(seatTicket);
        }
        log.info("created {} tickets", tickets.size());
        seatTicketRepository.save(tickets);
    }
}
