package at.ac.tuwien.inso.sepm.ticketline.server;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Perf;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


@Slf4j
@Getter
@Component
public class TestDataGenerator {
    /**
     * IMPORTANT NOTE: If you want to use data of this Class, you should extend
     * the class instead of editing this file.
     * The methods of this class only manipulate the lists of the entities.
     * */

    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private SeatLocationRepository seatLocationRepository;
    @Autowired
    private LocationRepository locationRepository;
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

    private List<Event> events;
    private List<Performance> performances;
    private List<SeatTicket> tickets;
    private List<Location> locations;
    private List<PriceCategory> priceCategories;

    public void generateAllData(boolean deleteAllRepositories){
        events = new LinkedList<>();
        performances = new LinkedList<>();
        tickets = new LinkedList<>();
        locations = new LinkedList<>();
        priceCategories = new LinkedList<>();


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

        generateEvents();
        generatePriceCategory();
        generateLocation();
        generateSeats();
        generatePerformance();
        reloadLocations();
        //reloadPerformances();
        generateTickets();


        //TODO check why this can fail at commit f3d72446ba2d098470fec2cdceee904855aa7278
        //assert(location.getSeats().size() == seatRepository.count());
    }

    private void reloadLocations(){
        List<Location> temp = new LinkedList<>();
        for(Location location : locations){
            temp.add(locationRepository.findOne(location.getId()));
        }
        this.locations = temp;
    }

    private void reloadPerformances(){
        List<Performance> temp = new LinkedList<>();
        for (Performance performance : performances){
            temp.add(performanceRepository.findOne(performance.getId()));
        }
        this.performances = temp;
    }

    private void generateEvents(){
        Event event = new Event(
            null,
            "Event Name",
            EventCategory.NO_CATEGORY,
            "1 tolles Event",
            null,
            null
        );
        eventRepository.save(event);
        events.add(event);
    }

    private void generateLocation(){
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

        seatLocationRepository.save(location);
        this.locations.add(location);
    }

    private void generatePriceCategory(){
        PriceCategory priceCategory = new PriceCategory(
            null,
            "1 toller Name für eine Preiskategorie",
            1.5
        );
        priceCategoryRepository.save(priceCategory);
        this.priceCategories.add(priceCategory);
    }

    private void generateSeats(){
        //2 rows with 3 seats each
        int rows = 2;
        int seats = 3;
        for (int rowNumber = 0; rowNumber < rows; rowNumber++) {
            for (int seatNumber = 0; seatNumber < seats; seatNumber++) {
                Seat seat = Seat.builder()
                    .location((SeatLocation) this.locations.get(0))
                    .priceCategory(this.priceCategories.get(0))
                    .row(rowNumber + 1)
                    .column(seatNumber + 1)
                    .build();
                seatRepository.save(seat);
            }
        }
    }

    private void generatePerformance(){
        Performance performance = new Performance(
            null,
            "TestPerformance",
            LocalDateTime.of(2016,10,27,20,15).toInstant(ZoneOffset.UTC),
            LocalDateTime.of(2016,10,27,23,30).toInstant(ZoneOffset.UTC),
            new BigDecimal(123.5),
            this.events.get(0),
            this.locations.get(0),
            null
        );
        performance = performanceRepository.save(performance);
        performances.add(performance);

        //TODO another performance to the already existing event
    }

    private void generateTickets(){
        SeatLocation seatLocation = (SeatLocation) this.locations.get(0);
        assert(seatLocation != null);
        assert(seatLocation.getSeats() != null);
        for (Seat seat : seatLocation.getSeats()) {
            SeatTicket seatTicket = SeatTicket.builder()
                .seat(seat)
                .performance(this.performances.get(0))
                .price(this.performances.get(0).getDefaultPrice())
                .build();
            tickets.add(seatTicket);
        }
        seatTicketRepository.save(tickets);
    }
}
