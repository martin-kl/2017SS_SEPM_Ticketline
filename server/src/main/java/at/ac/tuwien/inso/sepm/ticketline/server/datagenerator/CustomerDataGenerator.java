package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import com.github.javafaker.Faker;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
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

            //generate birthday "by hand" cause faker can`t do it
            Random random = new Random();
            for (int i = 0; i < NUMBER_OF_CUSTOMER_TO_GENERATE; i++) {
                long ms = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
                LocalDate birthday = Instant.ofEpochMilli(ms).atZone(ZoneId.systemDefault()).toLocalDate();

                Customer customer = Customer.builder()
                    .firstName(faker.artist().name())
                    .lastName(faker.artist().name())
                    .address(faker.address().fullAddress())
                    .email(faker.internet().emailAddress())
                    .birthday(birthday)
                    .build();
                log.debug("saving artist {}", customer);
                customerRepository.save(customer);
            }
        }
    }
}
