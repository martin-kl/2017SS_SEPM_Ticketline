package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Profile("generateData")
@Component
public class PerformanceDataGenerator {

    private static final int NUMBER_OF_PERFORMANCES_FOR_EVENT_TO_GENERATE = 2;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private LocationRepository locationRepository;

    // injected so we make sure that events are loaded
    @Autowired
    private EventDataGenerator eventDataGenerator;

    // injected so we make sure that locations are loaded
    @Autowired
    private SeatLocationDataGenerator seatLocationDataGenerator;
    @Autowired
    private SectorLocationDataGenerator sectorLocationDataGenerator;

    private final Faker faker = new Faker();

    private final String[] performanceNames = {"Spielberg - Red Bull Ring",
        "Stadthalle Wiener - Auftritt 1",
        "Stadthalle Wiener - Auftritt 2",
        "Tournee 2017 - Olympiahalle",
        "Tournee 2017 - Stadthalle",
        "Freiluftauftritt Krieau",
        "Freiluftkonzert Schladming"};

    @PostConstruct
    private void generatePerformances() {
        if (performanceRepository.count() > 0) {
            log.info("performances already generated");
        } else {
            List<Event> eventList = eventRepository.findAll();
            log.info("generating {} performances entries", eventList.size() * NUMBER_OF_PERFORMANCES_FOR_EVENT_TO_GENERATE);

            List<Location> locationList = locationRepository.findAll();
            log.info("loaded {} locations", locationList.size());

            for (Event event : eventList) {

                for (int i = 0; i < NUMBER_OF_PERFORMANCES_FOR_EVENT_TO_GENERATE; i++) {

                    int randomLocationId = (int) (Math.random() * locationList.size());
                    Instant startTime = faker.date()
                        .future(365 * 3, TimeUnit.DAYS)
                        .toInstant();

                    int randomPerformanceName = (int) (Math.random() * performanceNames.length);

                    Performance performance = Performance.builder()
                        .name(performanceNames[randomPerformanceName])
                        //.name(faker.name().lastName())
                        .event(event)
                        .location(locationList.get(randomLocationId))
                        .defaultPrice(new BigDecimal(Math.random() * 200 + 10))
                        .startTime(startTime)
                        .endTime(startTime.plus(4, ChronoUnit.HOURS))
                        .build();

                    log.debug("saving performance {}", performance);
                    performanceRepository.save(performance);
                }
            }
        }
    }
}
