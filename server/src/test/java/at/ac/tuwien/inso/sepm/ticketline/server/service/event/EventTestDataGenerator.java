package at.ac.tuwien.inso.sepm.ticketline.server.service.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
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

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    public void generateAllData(){
        emptyAllRepositories();
        generateEvents();
        generateArtists();
        generateEventArtists();
        generateLocations();
        generatePerfomances();
        printLocations();
        printEvents();
        printEventArtists();
        printPerformances();
    }

    public void emptyAllRepositories(){
        eventRepository.deleteAll();
        artistRepository.deleteAll();
        eventArtistRepository.deleteAll();
        locationRepository.deleteAll();
        performanceRepository.deleteAll();
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


    private void generateLocations(){
        for(int i = 0; i < 6; i++){
            Location location;
            if(i % 2 == 0){
                location = SeatLocation.builder()
                    .id(null)
                    .name("Test Location " + i + ": Seat")
                    .street("Street " + i)
                    .city("City " + i)
                    .zipCode("" + i*1000)
                    .country("Country " + i)
                    .build();
            } else {
                location = SectorLocation.builder()
                    .id(null)
                    .name("Test Location " + i + ": Sector")
                    .street("Street " + i)
                    .city("City " + i)
                    .zipCode("" + i*1000)
                    .country("Country " + i)
                    .build();
            }
            locationRepository.save(location);
        }
    }

    private void generatePerfomances(){
        List<Event> events = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Location> locations = locationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));

        int locationCounter = 0;
        for(Event e : events){
            for(int i = 0; i < 3; i++){
                Performance performance = Performance.builder()
                    .id(null)
                    .name("Perf " + i + " to event " + e.getName())
                    .startTime(LocalDateTime.of(2012,6,5+(i*5),15,00).toInstant(ZoneOffset.UTC))
                    .endTime(LocalDateTime.of(2012,6,5+(i*5),16+i,00).toInstant(ZoneOffset.UTC))
                    .defaultPrice(new BigDecimal((i+1) * 100))
                    .event(e)
                    .location(locations.get(locationCounter))
                    .build();

                locationCounter = (locationCounter + 1) % 6;
                performanceRepository.save(performance);
            }
        }
    }

    private void printEvents(){
        List<Event> events = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        log.info("printing events:");
        for(Event e : events){
            log.info(e + "");
        }
    }

    private void printEventArtists(){
        List<EventArtist> eventArtists = eventArtistRepository.findAll();
        log.info("printing eventartists:");
        for (EventArtist ea : eventArtists){
            log.info(ea + "");
        }
    }

    private void printLocations(){
        List<Location> locations = locationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        log.info("printing locations:");
        for(Location l : locations){
            log.info(l + "");
        }
    }

    private void printPerformances(){
        List<Performance> performances = performanceRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        log.info("printing performances:");
        for(Performance p : performances){
            log.info(p + "");
        }
    }

}
