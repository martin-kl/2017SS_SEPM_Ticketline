package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.PriceCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PriceCategoryRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("generateData")
@Component
public class PriceCategoryDataGenerator {

    private static final int NUMBER_OF_PRICE_CATEGORY_TO_GENERATE = 5;

    @Autowired
    private PriceCategoryRepository priceCategoryRepository;
    private final Faker faker = new Faker();

    @PostConstruct
    private void generatePriceCategories() {
        if (priceCategoryRepository.count() > 0) {
            log.info("price categories already generated");
        } else {
            log.info("generating {} price category entries", NUMBER_OF_PRICE_CATEGORY_TO_GENERATE);
            double modifier = 1;
            for (int i = 0; i < NUMBER_OF_PRICE_CATEGORY_TO_GENERATE; i++) {
                PriceCategory priceCategory = PriceCategory.builder()
                    .name("Category " + String.valueOf('A' + i))
                    .modifier(modifier)
                    .build();
                log.debug("saving price category {}", priceCategory);
                modifier += 0.25;
                priceCategoryRepository.save(priceCategory);
            }
        }
    }
}
