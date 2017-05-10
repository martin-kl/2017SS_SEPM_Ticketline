package at.ac.tuwien.inso.sepm.ticketline.server;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


@Slf4j
@Component
public class TestDataGenerator {
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private SeatLocationRepository seatLocationRepository;
    @Autowired
    private SeatTicketRepository seatTicketRepository;
    @Autowired
    private PriceCategoryRepository priceCategoryRepository;
    @Autowired
    private SeatRepository seatRepository;


    public void generateAllData(boolean deleteAllRepositories){
        //pay attention to the order of the deleteAll calls (foreign keys)
        if(deleteAllRepositories){
            seatTicketRepository.deleteAll();
            performanceRepository.deleteAll();
            eventRepository.deleteAll();
            seatRepository.deleteAll();
            seatLocationRepository.deleteAll();
            priceCategoryRepository.deleteAll();
        }

        Event event = generateEvent();
        PriceCategory priceCategory = generatePriceCategory();
        SeatLocation location = generateLocation();
        location = generateSeats(priceCategory, location);
        assert(location.getSeats() != null);
        Performance performance = generatePerformance(event, location);
        generateTickets(performance, location);
    }

    private Event generateEvent(){
        Event event = new Event(
            null,
            "Event Name",
            EventCategory.NO_CATEGORY,
            "1 tolles Event",
            null,
            null
        );
        return eventRepository.save(event);
    }

    private SeatLocation generateLocation(){
        SeatLocation location = new SeatLocation(
            null,
            "LocationName 1",
            "StreetName 1",
            "CityName 1",
            "ZipCode 1",
            "Country 1",
            null,
            null
        );

        return seatLocationRepository.save(location);
    }

    private PriceCategory generatePriceCategory(){
        PriceCategory priceCategory = new PriceCategory(
            null,
            "1 toller Name für eine Preiskategorie",
            1.5
        );
        return priceCategoryRepository.save(priceCategory);
    }

    private SeatLocation generateSeats(PriceCategory priceCategory, SeatLocation location){
        //15 rows and each 20 seats
        int rows = 15;
        for (int rowNumber = 0; rowNumber < rows; rowNumber++) {
            int seats = 20;
            for (int seatNumber = 0; seatNumber < seats; seatNumber++) {
                Seat seat = Seat.builder()
                    .location(location)
                    .priceCategory(priceCategory)
                    .row(rowNumber + 1)
                    .column(seatNumber + 1)
                    .build();
                seatRepository.save(seat);
            }
        }
        return seatLocationRepository.getOne(location.getId());
    }

    private Performance generatePerformance(Event event, Location location){
        Performance performance = new Performance(
            null,
            "TestPerformance",
            LocalDateTime.of(2016,10,27,20,15).toInstant(ZoneOffset.UTC),
            LocalDateTime.of(2016,10,27,23,30).toInstant(ZoneOffset.UTC),
            new BigDecimal(123.5),
            event,
            location,
            null
        );
        return performanceRepository.save(performance);
    }

    private void generateTickets(Performance performance, SeatLocation seatLocation){
        List<SeatTicket> tickets = new ArrayList<>();
        assert(seatLocation != null);
        assert(seatLocation.getSeats() != null);
        for (Seat seat : seatLocation.getSeats()) {
            SeatTicket seatTicket = SeatTicket.builder()
                .seat(seat)
                .performance(performance)
                .price(performance.getDefaultPrice())
                .build();
            tickets.add(seatTicket);
        }
        seatTicketRepository.save(tickets);
    }
}
