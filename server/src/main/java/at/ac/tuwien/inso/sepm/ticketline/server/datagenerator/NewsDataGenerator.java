package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Profile("generateData")
@Component
public class NewsDataGenerator {

    private static final int NUMBER_OF_NEWS_TO_GENERATE = 25;

    @Autowired
    private NewsRepository newsRepository;
    private final Faker faker = new Faker();

    @PostConstruct
    private void generateNews() {
        if (newsRepository.count() > 0) {
            log.info("news already generated");
        } else {
            log.info("generating {} news entries", NUMBER_OF_NEWS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                News news = News.builder()
                    .title(faker.lorem().characters(25, 100))
                    .text(faker.lorem().paragraph(faker.number().numberBetween(5, 10)))
                    .publishedAt(
                        LocalDateTime.ofInstant(
                            faker.date()
                                .past(365 * 3, TimeUnit.DAYS).
                                toInstant(),
                            ZoneId.systemDefault()
                        ))
                    .build();
                log.debug("saving news {}", news);
                newsRepository.save(news);
            }
        }
    }

}
