package at.ac.tuwien.inso.sepm.ticketline.server;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PaymentProviderOption;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@Getter
@Component
public class TestDataGenerator {
    /**
     * IMPORTANT NOTE: If you want to use data of this object, you should extend
     * the class instead of editing this file.
     * The methods of this class manipulate the lists of the entities and the repositories.
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
    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;
    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private List<Event> events;
    private List<Performance> performances;
    private List<Ticket> ticketsToPerf0;
    private List<Ticket> ticketsToPerf1;
    private List<Location> locations;
    private List<PriceCategory> priceCategories;
    private List<Customer> customers;

    public void generateAllData(boolean deleteAllRepositories){
        events = new LinkedList<>();
        performances = new LinkedList<>();
        ticketsToPerf0 = new LinkedList<>();
        ticketsToPerf1= new LinkedList<>();
        locations = new LinkedList<>();
        priceCategories = new LinkedList<>();
        customers = new LinkedList<>();

        if(deleteAllRepositories){
            emptyAllRepositories();
        }

        generateEvents();
        generatePriceCategory();
        generateLocation();
        generateSeats();
        generatePerformances();
        reloadLocations();
        generateTickets();

        generateCustomer();
        generateTicketHistoryAndTransaction();
        reloadTickets();

        //int sitzanzahlfuerLoc0 = ((SeatLocation) this.locations.get(0)).getSeats().size();
        //long sitzeImRepo = seatRepository.count();
        //assert(sitzanzahlfuerLoc0 == 600 && sitzanzahlfuerLoc0 == sitzeImRepo);
    }

    public void emptyAllRepositories(){
        //pay attention to the order of the deleteAll calls (foreign keys)
        ticketHistoryRepository.deleteAll();
        ticketTransactionRepository.deleteAll();
        customerRepository.deleteAll();
        sectorTicketRepository.deleteAll();
        seatTicketRepository.deleteAll();
        ticketRepository.deleteAll();
        performanceRepository.deleteAll();
        eventRepository.deleteAll();
        seatRepository.deleteAll();
        seatLocationRepository.deleteAll();
        priceCategoryRepository.deleteAll();
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

    private void reloadTickets(){
        List<Ticket> temp0 = new LinkedList<>();
        for (Ticket ticket : ticketsToPerf0){
            temp0.add(ticketRepository.findOne(ticket.getId()));
        }
        this.ticketsToPerf0 = temp0;

        List<Ticket> temp1 = new LinkedList<>();
        for (Ticket ticket : ticketsToPerf1){
            temp1.add(ticketRepository.findOne(ticket.getId()));
        }
        this.ticketsToPerf1 = temp1;
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

    private void generatePerformances(){
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

        //another performance, same location, same event
        Performance performance2 = new Performance(
            null,
            "TestPerformance",
            LocalDateTime.of(2016,11,27,17,15).toInstant(ZoneOffset.UTC),
            LocalDateTime.of(2016,11,27,19,30).toInstant(ZoneOffset.UTC),
            new BigDecimal(123.5),
            this.events.get(0),
            this.locations.get(0),
            null
        );
        performance2 = performanceRepository.save(performance2);
        performances.add(performance2);

    }

    private void generateTickets(){
        SeatLocation seatLocation = (SeatLocation) this.locations.get(0);
        assert(seatLocation != null);
        assert(seatLocation.getSeats() != null);
        for (Seat seat : seatLocation.getSeats()) {
            //generate tickets for performance 0
            SeatTicket seatTicket = SeatTicket.builder()
                .seat(seat)
                .performance(this.performances.get(0))
                .price(this.performances.get(0).getDefaultPrice())
                .build();
            ticketsToPerf0.add(seatTicket);
            //generate tickets for performance 1 (same location, same event)
            SeatTicket seatTicket1 = SeatTicket.builder()
                .seat(seat)
                .performance(this.performances.get(1))
                .price(this.performances.get(1).getDefaultPrice())
                .build();
            ticketsToPerf1.add(seatTicket1);
        }
        ticketRepository.save(ticketsToPerf0);
        ticketRepository.save(ticketsToPerf1);
    }

    private void generateCustomer(){
        LocalDate birthday1 = LocalDate.of(1990, 5, 15);
        Customer c1 = new Customer(
            null,
            "Max",
            "Mustermann",
            "max@mustermann.at",
            "Karlsplatz 13, 1040 Wien",
            birthday1,
            null
        );
        customerRepository.save(c1);
        customers.add(c1);

        LocalDate birthday2 = LocalDate.of(1992, 9, 4);
        Customer c2 = new Customer(
            null,
            "Gabriele",
            "Musterfrau",
            "gabriele@musterfrau.at",
            "Karlsplatz 13, 1040 Wien",
            birthday2,
            null
        );
        customerRepository.save(c2);
        customers.add(c2);
    }

    private void generateTicketHistoryAndTransaction(){
        //the order of saving the ticketTransactions is important
        TicketTransaction ticketTransaction0 = TicketTransaction.builder()
            .customer(customers.get(0))
            .outdated(true)
            .status(TicketStatus.RESERVED)
            .build();
        ticketTransaction0 = ticketTransactionRepository.save(ticketTransaction0);
        // add tickets 0 and 1
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(0))
                .ticketTransaction(ticketTransaction0)
                .build()
        );
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(1))
                .ticketTransaction(ticketTransaction0)
                .build()
        );

        //the order of saving the ticketTransactions is important
        TicketTransaction ticketTransaction1 = TicketTransaction.builder()
            .customer(customers.get(0))
            .outdated(false)
            .status(TicketStatus.BOUGHT)
            .build();
        ticketTransaction1 = ticketTransactionRepository.save(ticketTransaction1);
        // add tickets 0 and 1
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(0))
                .ticketTransaction(ticketTransaction1)
                .build()
        );
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(1))
                .ticketTransaction(ticketTransaction1)
                .build()
        );

        //the order of saving the ticketTransactions is important
        TicketTransaction ticketTransaction2 = TicketTransaction.builder()
            .customer(null)
            .outdated(false)
            .status(TicketStatus.BOUGHT)
            .build();
        ticketTransaction2 = ticketTransactionRepository.save(ticketTransaction2);
        // add tickets 2
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(2))
                .ticketTransaction(ticketTransaction2)
                .build()
        );

        //the order of saving the ticketTransactions is important
        TicketTransaction ticketTransaction3 = TicketTransaction.builder()
            .customer(null)
            .outdated(false)
            .status(TicketStatus.RESERVED)
            .build();
        ticketTransaction3 = ticketTransactionRepository.save(ticketTransaction3);
        // add tickets 3
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(3))
                .ticketTransaction(ticketTransaction3)
                .build()
        );

        //the order of saving the ticketTransactions is important
        TicketTransaction ticketTransaction4 = TicketTransaction.builder()
            .customer(customers.get(1))
            .outdated(true)
            .status(TicketStatus.BOUGHT)
            .build();
        ticketTransaction4 = ticketTransactionRepository.save(ticketTransaction4);
        // add tickets 4 and 5
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(4))
                .ticketTransaction(ticketTransaction4)
                .build()
        );
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(5))
                .ticketTransaction(ticketTransaction4)
                .build()
        );

        //the order of saving the ticketTransactions is important
        TicketTransaction ticketTransaction5 = TicketTransaction.builder()
            .customer(customers.get(1))
            .outdated(false)
            .status(TicketStatus.STORNO)
            .build();
        ticketTransaction5 = ticketTransactionRepository.save(ticketTransaction5);
        // add tickets 4 and 5
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(4))
                .ticketTransaction(ticketTransaction5)
                .build()
        );
        ticketHistoryRepository.save(
            TicketHistory.builder()
                .ticket(ticketsToPerf0.get(5))
                .ticketTransaction(ticketTransaction5)
                .build()
        );

    }
}