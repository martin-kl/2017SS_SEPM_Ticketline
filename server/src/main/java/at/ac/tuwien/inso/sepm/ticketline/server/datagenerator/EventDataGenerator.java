package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
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

    //ATTENTION: eventNames and eventDescriptions have to have the same number of entries
    //and for every entry in eventNames one event is created
    private final String[] eventNames = {"Andreas Gabalier-Heimspiel",
        "David Guetta - Unity Tour",
        "PLANET ERDE II - 2017",
        "Die Toten Hosen-Bolzplatz Tour",
        "The Rolling Stones - 2017"
    };
    private final String[] eventDescriptions = {"Die einzigen MEGA OPEN AIRS von Andreas Gabalier 2017 in ÖSTERREICH.",
        "Neben Superstar DAVID GUETTA kommen auch OLIVER HELDENS und JONAS BLUE.",
        "Die schönsten Bilder der neuen BBC-Erfolgsserie Planet Erde II.",
        "Die Toten Hosen treten nach ihrem Auftritt beim \"Rock in Vienna 2017\" noch bei zwei weiteren Gelegenheiten im Spätsommer in Wiesen und Innsbruck an.",
        "Die Tour wird „STONES – NO FILTER“ heißen und bringt Mick Jagger, Keith Richards, Charlie Watts und Ronnie Wood zurück auf Tour."
    };
    private final EventCategory[] eventCategories = {EventCategory.CONCERT,
        EventCategory.CONCERT,
        EventCategory.THEATER,
        EventCategory.CONCERT,
        EventCategory.CONCERT
    };

    @PostConstruct
    private void generateEvents() {
        if (eventRepository.count() > 0) {
            log.info("events already generated");
        } else {
            log.info("generating {} event entries", eventNames.length);

            List<Artist> artistList = artistRepository.findAll();

            log.info("loaded {} artists", artistList.size());

            for (int i = 0; i < eventNames.length; i++) {

                Event event = Event.builder()
                    //.category(getRandomCategory())
                    .category(eventCategories[i])
                    .name(eventNames[i])
                    .description(eventDescriptions[i])
                    //.description(faker.lorem().characters(25, 100))
                    //.name(faker.name().lastName())
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

    private EventCategory getRandomCategory() {
        double randomVal = Math.random();
        if(randomVal < 0.2)
            return EventCategory.NO_CATEGORY;
        else if(randomVal < 0.35)
            return EventCategory.COMEDY;
        else if(randomVal < 0.5)
            return EventCategory.CONCERT;
        else if(randomVal < 0.67)
            return EventCategory.OPERA;
        else if(randomVal < 0.84)
            return EventCategory.SPORTS;
        else
            return EventCategory.THEATER;
    }
}
