package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Profile("generateData")
@Component
public class SeatLocationDataGenerator {

    private static final int NUMBER_OF_LOCATIONS_TO_GENERATE = 10;

    private static final int MIN_NUMBER_OF_SEATS_TO_GENERATE = 10;
    private static final int MAX_NUMBER_OF_SEATS_TO_GENERATE = 40;
    private static final int MIN_NUMBER_OF_ROWS_TO_GENERATE = 10;
    private static final int MAX_NUMBER_OF_ROWS_TO_GENERATE = 20;

    @Autowired
    private SeatLocationRepository seatLocationRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PriceCategoryRepository priceCategoryRepository;

    // injected so we make sure that price categories are loaded
    @Autowired
    private PriceCategoryDataGenerator priceCategoryDataGenerator;

    private final Faker faker = new Faker();

    @Order(110)
    @PostConstruct
    private void generateSeatLocations() {
        if (seatLocationRepository.count() > 0) {
            log.info("artists already generated");
        } else {
            log.info("generating {} seat locations entries", NUMBER_OF_LOCATIONS_TO_GENERATE);

            List<PriceCategory> priceCategoryList = priceCategoryRepository.findAll();

            log.info("loaded {} price categories", priceCategoryList.size());

            for (int i = 0; i < NUMBER_OF_LOCATIONS_TO_GENERATE; i++) {

                SeatLocation sectorLocation = SeatLocation.builder()
                    .name("Halle " + faker.name().lastName())
                    .city(faker.address().city())
                    .country(faker.address().country())
                    .street(faker.address().streetAddress())
                    .zipCode(faker.address().zipCode())
                    .performances(new HashSet<>())
                    .build();
                log.debug("saving seat location {}", sectorLocation);
                seatLocationRepository.save(sectorLocation);

                int rows = (int) (Math.random() * MAX_NUMBER_OF_ROWS_TO_GENERATE + MIN_NUMBER_OF_ROWS_TO_GENERATE);
                for (int rowNumber = 0; rowNumber < rows; rowNumber++) {
                    int randomPriceCategoryId = (int) (Math.random() * priceCategoryList.size());
                    int seats = (int) (Math.random() * MAX_NUMBER_OF_SEATS_TO_GENERATE + MIN_NUMBER_OF_SEATS_TO_GENERATE);
                    for (int seatNumber = 0; seatNumber < seats; seatNumber++) {
                        Seat seat = Seat.builder()
                            .location(sectorLocation)
                            .priceCategory(priceCategoryList.get(randomPriceCategoryId))
                            .row(rowNumber + 1)
                            .column(seatNumber + 1)
                            .build();
                        log.debug("saving seat {}", seat);
                        seatRepository.save(seat);
                    }
                }

            }
        }
    }

}