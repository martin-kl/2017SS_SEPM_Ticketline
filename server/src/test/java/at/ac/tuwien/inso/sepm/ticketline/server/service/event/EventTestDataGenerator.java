package at.ac.tuwien.inso.sepm.ticketline.server.service.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventArtist;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EventTestDataGenerator {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EventArtistRepository eventArtistRepository;

    public void generateAllData(){
        emptyAllRepositories();
        generateEvents();
        generateArtists();
        generateEventArtists();
        printEvents();
        printEventArtists();
    }

    public void emptyAllRepositories(){
        eventRepository.deleteAll();
        artistRepository.deleteAll();
        eventArtistRepository.deleteAll();
    }

    private void generateEvents(){
        for(int i = 0; i < 9; i++){
            Event event = Event.builder()
                .id(null)
                .name("Test Event " + i)
                .category(EventCategory.values()[i%EventCategory.values().length])
                .description("Test event description for event " + i)
                .build();
            eventRepository.save(event);
        }
    }

    private void generateArtists(){
        for(int i = 0; i < 6; i++){
            Artist artist = Artist.builder()
                .id(null)
                .firstname("Test Firstname " + i)
                .lastname("Test Lastname " + i)
                .build();
            artistRepository.save(artist);
        }
    }

    private void generateEventArtists(){
        List<Event> events = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Artist> artists = artistRepository.findAll(new Sort(Sort.Direction.ASC, "firstname"));

        for(int i = 0; i < events.size(); i++){

            for(int j = (i%2)*3; j < 3 * (i%2 + 1); j++){
                EventArtist eventArtist = EventArtist.builder()
                    .id(null)
                    .artist(artists.get(j))
                    .event(events.get(i))
                    .build();
                eventArtistRepository.save(eventArtist);
            }
        }
    }

    private void printEvents(){
        List<Event> events = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        log.info("printing events: ");
        for(Event e : events){
            log.info(e + "");
        }
    }

    private void printEventArtists(){
        List<EventArtist> eventArtists = eventArtistRepository.findAll();
        log.info("printing eventartists: ");

        for (EventArtist ea : eventArtists){
            log.info(ea + "");
        }
    }

}
