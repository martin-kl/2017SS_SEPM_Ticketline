package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("generateData")
@Component
public class ArtistDataGenerator {

    private static final int NUMBER_OF_ARTIST_TO_GENERATE = 25;

    @Autowired
    private ArtistRepository artistRepository;
    private final Faker faker = new Faker();

    @PostConstruct
    private void generateArtists() {
        if (artistRepository.count() > 0) {
            log.info("artists already generated");
        } else {
            log.info("generating {} artist entries", NUMBER_OF_ARTIST_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_ARTIST_TO_GENERATE; i++) {
                Artist artist = Artist.builder()
                    .firstname(faker.name().firstName())
                    .lastname(faker.name().lastName())
                    .build();
                log.debug("saving artist {}", artist);
                artistRepository.save(artist);
            }
        }
    }
}
