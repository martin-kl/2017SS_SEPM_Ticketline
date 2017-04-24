package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventArtist;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Profile("generateData")
@Component
public class EventDataGenerator {

    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 25;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EventArtistRepository eventArtistRepository;

    // injected so we make sure that artists are loaded
    @Autowired
    private ArtistDataGenerator artistDataGenerator;

    private final Faker faker = new Faker();

    @PostConstruct
    private void generateEvents() {
        if (eventRepository.count() > 0) {
            log.info("events already generated");
        } else {
            log.info("generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);

            List<Artist> artistList = artistRepository.findAll();

            log.info("loaded {} artists", artistList.size());

            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {

                Event event = Event.builder()
                    .category(Event.Category.NO_CATEGORY)
                    .description(faker.lorem().characters(25, 100))
                    .name("Event " + faker.name().lastName())
                    .build();
                log.debug("saving event {}", event);
                eventRepository.save(event);

                for (int j = 0; j < 3; j++) {
                    EventArtist eventArtist = EventArtist.builder()
                        .artist(artistList.get((int) (Math.random() * artistList.size())))
                        .event(event)
                        .build();
                    log.debug("saving event artist {}", eventArtist);
                    eventArtistRepository.save(eventArtist);
                }
            }
        }
    }

}