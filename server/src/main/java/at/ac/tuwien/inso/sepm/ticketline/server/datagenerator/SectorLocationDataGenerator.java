package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.PriceCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorLocation;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PriceCategoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorLocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorRepository;
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
public class SectorLocationDataGenerator {

    private static final int NUMBER_OF_LOCATIONS_TO_GENERATE = 10;

    private static final int MIN_NUMBER_OF_SECTORS_TO_GENERATE = 1;
    private static final int MAX_NUMBER_OF_SECTORS_TO_GENERATE = 5;

    private static final int MAX_NUMBER_OF_SECTOR_PLACES = 200;
    private static final int MIN_NUMBER_OF_SECTOR_PLACES = 10;

    @Autowired
    private SectorLocationRepository sectorLocationRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private PriceCategoryRepository priceCategoryRepository;

    // injected so we make sure that price categories are loaded
    @Autowired
    private PriceCategoryDataGenerator priceCategoryDataGenerator;

    private final Faker faker = new Faker();

    @PostConstruct
    private void generateSectorLocations() {
        if (sectorLocationRepository.count() > 0) {
            log.info("artists already generated");
        } else {
            log.info("generating {} sector locations entries", NUMBER_OF_LOCATIONS_TO_GENERATE);

            List<PriceCategory> priceCategoryList = priceCategoryRepository.findAll();

            log.info("loaded {} price categories", priceCategoryList.size());

            for (int i = 0; i < NUMBER_OF_LOCATIONS_TO_GENERATE; i++) {

                SectorLocation sectorLocation = SectorLocation.builder()
                    .name("Halle " + faker.name().lastName())
                    .city(faker.address().city())
                    .country(faker.address().country())
                    .street(faker.address().streetAddress())
                    .zipCode(faker.address().zipCode())
                    .performances(new HashSet<>())
                    .build();
                log.debug("saving sector location {}", sectorLocation);
                sectorLocationRepository.save(sectorLocation);

                int max = (int) (Math.random() * MAX_NUMBER_OF_SECTORS_TO_GENERATE + MIN_NUMBER_OF_SECTORS_TO_GENERATE);
                for (int j = 0; j < max; j++) {
                    int randomPriceCategoryId = (int) (Math.random() * priceCategoryList.size());
                    Sector sector = Sector.builder()
                        .location(sectorLocation)
                        .priceCategory(priceCategoryList.get(randomPriceCategoryId))
                        .size((int) (Math.random() * MAX_NUMBER_OF_SECTOR_PLACES + MIN_NUMBER_OF_SECTOR_PLACES))
                        .build();
                    log.debug("saving sector {}", sector);
                    sectorRepository.save(sector);
                }

            }
        }
    }

}
