package at.ac.tuwien.inso.sepm.ticketline.server;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PaymentProviderOption;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.PriceCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatLocation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatTicket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketHistory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PriceCategoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatLocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatTicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorTicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketHistoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class EventFilterTestDataGenerator {

    /**
     * IMPORTANT NOTE: If you want to use data of this object, you should extend
     * the class instead of editing this file.
     * The methods of this class manipulate the lists of the entities and the repositories.
     */

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
    @Autowired
    private EventArtistRepository eventArtistRepository;

    private List<Event> events;
    private List<Performance> performances;
    private List<Ticket> ticketsToPerf0;
    private List<Ticket> ticketsToPerf1;
    private List<Ticket> ticketsToPerf2;
    private List<Location> locations;
    private List<PriceCategory> priceCategories;
    private List<Customer> customers;

    public void generateAllData(boolean deleteAllRepositories) {
        events = new ArrayList<>();
        performances = new ArrayList<>();
        ticketsToPerf0 = new ArrayList<>();
        ticketsToPerf1 = new ArrayList<>();
        ticketsToPerf2 = new ArrayList<>();
        locations = new ArrayList<>();
        priceCategories = new ArrayList<>();
        customers = new ArrayList<>();

        if (deleteAllRepositories) {
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
    }

    private void emptyAllRepositories() {
        //pay attention to the order of the deleteAll calls (foreign keys)
        eventArtistRepository.deleteAll();
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

    private void reloadLocations() {
        List<Location> temp = new LinkedList<>();
        for (Location location : locations) {
            temp.add(locationRepository.findOne(location.getId()));
        }
        this.locations = temp;
    }

    private void reloadPerformances() {
        List<Performance> temp = new LinkedList<>();
        for (Performance performance : performances) {
            temp.add(performanceRepository.findOne(performance.getId()));
        }
        this.performances = temp;
    }

    private void reloadTickets() {
        List<Ticket> temp0 = new LinkedList<>();
        for (Ticket ticket : ticketsToPerf0) {
            temp0.add(ticketRepository.findOne(ticket.getId()));
        }
        this.ticketsToPerf0 = temp0;

        List<Ticket> temp1 = new LinkedList<>();
        for (Ticket ticket : ticketsToPerf1) {
            temp1.add(ticketRepository.findOne(ticket.getId()));
        }
        this.ticketsToPerf1 = temp1;
    }

    private void generateEvents() {
        Event event1 = new Event(
            null,
            "Event 1 Name",
            EventCategory.CONCERT,
            "1 tolles Event",
            null,
            null
        );
        eventRepository.save(event1);
        events.add(event1);

        Event event2 = new Event(
            null,
            "Event 2 Name",
            EventCategory.NO_CATEGORY,
            "2 tolles Event",
            null,
            null
        );
        eventRepository.save(event2);
        events.add(event2);
    }

    //generates 1 location
    private void generateLocation() {
        SeatLocation location = new SeatLocation(
            null,
            "LocationName 1 (this is the only location in this test)",
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

    private void generatePriceCategory() {
        PriceCategory priceCategory = new PriceCategory(
            null,
            "this is the only price category in this test",
            1.5
        );
        priceCategoryRepository.save(priceCategory);
        this.priceCategories.add(priceCategory);
    }

    //adds 2 rows with 3 seats to the one location
    private void generateSeats() {
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

    //adds 2 performances for event1 and 1 performance for event2 - all on the same location
    private void generatePerformances() {

        //Performance 1 = 5 days ago - 2 hour performance
        LocalDateTime performance1Start = LocalDateTime.now();
        LocalDateTime performance1End = LocalDateTime.now();
        performance1Start.minusDays(5);
        performance1Start.minusHours(2);
        performance1End.minusDays(5);

        //Performance 2 = 10 days ago - 3 hour performance
        LocalDateTime performance2Start = LocalDateTime.now();
        LocalDateTime performance2End = LocalDateTime.now();
        performance1Start.minusDays(10);
        performance1Start.minusHours(3);
        performance1End.minusDays(10);

        //Performance 3 = 20 days in the future - 4 hour performance
        LocalDateTime performance3Start = LocalDateTime.now();
        LocalDateTime performance3End = LocalDateTime.now();
        performance1Start.plusDays(20);
        performance1Start.minusHours(4);
        performance1End.plusDays(20);

        Performance performance = new Performance(
            null,
            "TestPerformance1Event1",
            performance1Start.toInstant(ZoneOffset.UTC),
            performance1End.toInstant(ZoneOffset.UTC),
            new BigDecimal(123.5),
            this.events.get(0),
            this.locations.get(0),
            null
        );
        performance = performanceRepository.save(performance);
        performances.add(performance);

        //another performance, same location, SAME event
        Performance performance2 = new Performance(
            null,
            "TestPerformance2Event1",
            performance2Start.toInstant(ZoneOffset.UTC),
            performance2End.toInstant(ZoneOffset.UTC),
            new BigDecimal(123.5),
            this.events.get(0),
            this.locations.get(0),
            null
        );
        performance2 = performanceRepository.save(performance2);
        performances.add(performance2);

        //another performance, same location, OTHER event
        Performance performance3 = new Performance(
            null,
            "TestPerformance1Event2",
            performance3Start.toInstant(ZoneOffset.UTC),
            performance3End.toInstant(ZoneOffset.UTC),
            new BigDecimal(123.5),
            this.events.get(1),
            this.locations.get(0),
            null
        );
        performance3 = performanceRepository.save(performance3);
        performances.add(performance3);
    }

    private void generateTickets() {
        SeatLocation seatLocation = (SeatLocation) this.locations.get(0);
        assert (seatLocation != null);
        assert (seatLocation.getSeats() != null);
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

            //generate tickets for performance 2 (same location, other event)
            SeatTicket seatTicket2 = SeatTicket.builder()
                .seat(seat)
                .performance(this.performances.get(2))
                .price(this.performances.get(2).getDefaultPrice())
                .build();
            ticketsToPerf2.add(seatTicket2);
        }
        ticketRepository.save(ticketsToPerf0);
        ticketRepository.save(ticketsToPerf1);
        ticketRepository.save(ticketsToPerf2);
    }

    private void generateCustomer() {
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

    private void generateTicketHistoryAndTransaction() {
        LocalDateTime localDateTimeNow = LocalDateTime.now();

        //the order of saving the ticketTransactions is important
        TicketTransaction ticketTransactionStorno = new TicketTransaction(
            null,
            TicketStatus.STORNO,
            null,
            this.customers.get(0),
            false,
            null,
            PaymentProviderOption.STRIPE,
            false
        );
        ticketTransactionRepository.save(ticketTransactionStorno);

        TicketTransaction ticketTransactionReserved = new TicketTransaction(
            null,
            TicketStatus.RESERVED,
            null,
            this.customers.get(0),
            false,
            null,
            PaymentProviderOption.STRIPE,
            false
        );
        ticketTransactionRepository.save(ticketTransactionReserved);

        TicketTransaction ticketTransactionBought = new TicketTransaction(
            null,
            TicketStatus.BOUGHT,
            null,
            this.customers.get(0),
            false,
            null,
            PaymentProviderOption.STRIPE,
            false
        );
        ticketTransactionRepository.save(ticketTransactionBought);

        TicketTransaction ticketTransactionBoughtAndLaterCancelled = new TicketTransaction(
            null,
            TicketStatus.BOUGHT,
            null,
            this.customers.get(0),
            false,
            null,
            PaymentProviderOption.STRIPE,
            false
        );
        ticketTransactionRepository.save(ticketTransactionBoughtAndLaterCancelled);
/*
        TicketTransaction ticketTransactionStorno2 = new TicketTransaction(
            null,
            TicketStatus.STORNO,
            null,
            this.customers.get(0),
            false
        );
        ticketTransactionRepository.save(ticketTransactionStorno2);
*/

        /**
         * ATTENTION: the manipulation of the dates is NOT WORKING
         * SO we cannot test the date search correctly in the unit test
         *
         * my plan is:
         * - Performance 0:
         *      date of this history = today (without modification of last modified)
         *      ticket 3 is storno after it is bought, tickets 2 and 4 are bought
         *
         * - Performance 1:
         *      date of this history = 20 days ago (with modification of last modified)
         *      tickets 3-5 are bought
         *
         * - Performance 2:
         *      date of this history = 40 days ago (with modification of last modified)
         *      tickets 1 and 5 are bought
         */

        //perf 0 ticket 1 get storno
        createTicketHistory(0, 1, ticketTransactionStorno,
            localDateTimeNow.toInstant(ZoneOffset.UTC));

        //perf 0 tickets 0 - 2 get reserved
        createTicketHistory(0, 0, ticketTransactionReserved,
            localDateTimeNow.toInstant(ZoneOffset.UTC));
        createTicketHistory(0, 1, ticketTransactionReserved,
            localDateTimeNow.toInstant(ZoneOffset.UTC));
        createTicketHistory(0, 2, ticketTransactionReserved,
            localDateTimeNow.toInstant(ZoneOffset.UTC));

        //perf 0 tickets 2, 3 and 4 get bought
        createTicketHistory(0, 2, ticketTransactionBought,
            localDateTimeNow.toInstant(ZoneOffset.UTC));
        createTicketHistory(0, 3, ticketTransactionBoughtAndLaterCancelled,
            localDateTimeNow.toInstant(ZoneOffset.UTC));
        createTicketHistory(0, 4, ticketTransactionBought,
            localDateTimeNow.toInstant(ZoneOffset.UTC));

        //ticket 3 is storno here
        createTicketHistory(0, 3, ticketTransactionStorno,
            localDateTimeNow.toInstant(ZoneOffset.UTC));
        TicketTransaction tt = ticketTransactionRepository.findOne(ticketTransactionBoughtAndLaterCancelled.getId());
        tt.setOutdated(true);
        ticketTransactionRepository.save(tt);





        //inserting ticket history for performance 1
        LocalDateTime performance1 = LocalDateTime.now();
        performance1.minusDays(20); //remove 20 day from today

        //performance 1 tickets 3, 4 and 5 get bought
        createTicketHistory(1, 3, ticketTransactionBought, performance1.toInstant(ZoneOffset.UTC));
        createTicketHistory(1, 4, ticketTransactionBought, performance1.toInstant(ZoneOffset.UTC));
        createTicketHistory(1, 5, ticketTransactionBought, performance1.toInstant(ZoneOffset.UTC));




        LocalDateTime performance2 = LocalDateTime.now();
        performance2.minusDays(20); //remove 40 day from today

        //performance 2 tickets 1 and 6 get bought
        createTicketHistory(2, 1, ticketTransactionBought, performance2.toInstant(ZoneOffset.UTC));
        createTicketHistory(2, 5, ticketTransactionBought, performance2.toInstant(ZoneOffset.UTC));
    }

    private void createTicketHistory(int performance, int ticket, TicketTransaction transaction,
        Instant lastModified) {
        Ticket ticketObject = null;
        switch (performance) {
            case 0:
                ticketObject = ticketsToPerf0.get(ticket);
                break;
            case 1:
                ticketObject = ticketsToPerf1.get(ticket);
                break;
            case 2:
                ticketObject = ticketsToPerf2.get(ticket);
                break;
        }
        TicketHistory ticketHistory = new TicketHistory(
            null,
            ticketObject,
            transaction
        );
        ticketHistory.setLastModifiedAt(lastModified);
        ticketHistory.setCreatedAt(lastModified);
        ticketHistoryRepository.save(ticketHistory);
    }
}