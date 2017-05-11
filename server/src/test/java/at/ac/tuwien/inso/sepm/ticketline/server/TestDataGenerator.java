package at.ac.tuwien.inso.sepm.ticketline.server;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


@Slf4j
@Getter
@Component
public class TestDataGenerator {
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private SeatLocationRepository seatLocationRepository;
    @Autowired
    private SectorTicketRepository sectorTicketRepository;
    @Autowired
    private SeatTicketRepository seatTicketRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private PriceCategoryRepository priceCategoryRepository;
    @Autowired
    private SeatRepository seatRepository;

    private List<Performance> performances;
    private List<SeatTicket> tickets;

    public void generateAllData(boolean deleteAllRepositories){
        performances = new LinkedList<>();
        tickets = new LinkedList<>();

        //pay attention to the order of the deleteAll calls (foreign keys)
        if(deleteAllRepositories){
            sectorTicketRepository.deleteAll();
            seatTicketRepository.deleteAll();
            ticketRepository.deleteAll();
            performanceRepository.deleteAll();
            eventRepository.deleteAll();
            seatRepository.deleteAll();
            seatLocationRepository.deleteAll();
            priceCategoryRepository.deleteAll();

        }

        Event event = generateEvent();
        PriceCategory priceCategory = generatePriceCategory();
        SeatLocation location = generateLocation();
        generateSeats(priceCategory, location);

        //reload location to get seats
        location = seatLocationRepository.getOne(location.getId());

        assert(location.getSeats().size() == seatRepository.count());
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

    private void generateSeats(PriceCategory priceCategory, SeatLocation location){
        //2 rows with 3 seats each
        int rows = 2;
        int seats = 3;
        for (int rowNumber = 0; rowNumber < rows; rowNumber++) {
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
        performance = performanceRepository.save(performance);
        return performance;
    }

    private void generateTickets(Performance performance, SeatLocation seatLocation){
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
        performance = performanceRepository.getOne(performance.getId());
        performances.add(performance);
        seatTicketRepository.save(tickets);
    }
}
