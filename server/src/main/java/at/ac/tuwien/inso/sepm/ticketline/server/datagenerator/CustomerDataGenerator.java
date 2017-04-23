package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("generateData")
@Component
public class CustomerDataGenerator {
    private static final int NUMBER_OF_CUSTOMER_TO_GENERATE = 25;

    @Autowired
    private CustomerRepository customerRepository;
    private final Faker faker = new Faker();

    @PostConstruct
    private void generateCustomers() {
        if (customerRepository.count() > 0) {
            log.info("customers already generated");
        } else {
            log.info("generating {} customer entries", NUMBER_OF_CUSTOMER_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_CUSTOMER_TO_GENERATE; i++) {
                Customer customer = Customer.builder()
                    .name(faker.artist().name())
                    .build();
                log.debug("saving artist {}", customer);
                customerRepository.save(customer);
            }
        }
    }
}
