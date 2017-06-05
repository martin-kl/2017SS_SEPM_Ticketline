package at.ac.tuwien.inso.sepm.ticketline.server.service.event;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.EventSearch;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventServiceTest {
    @Autowired
    private EventTestDataGenerator eventTestDataGenerator;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Before
    public void beforeTest() {
        eventTestDataGenerator.generateAllData();
    }

    @After
    public void afterTest(){
        eventTestDataGenerator.emptyAllRepositories();
    }

    @Test
    public void emptyFilterShouldFindAll(){
        EventSearch eventSearch = new EventSearch();

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);
        List<Event> allEvents = eventService.findAll(null);

        Assert.assertEquals(eventsSearchResult, allEvents);
    }

    @Test
    public void filterByEventName(){
        EventSearch eventSearch = EventSearch.builder()
            .eventName("event 0")
            .build();
        singleResultSearch(eventSearch, 0);
    }

    @Test
    public void filterByEventDescription(){
        EventSearch eventSearch = EventSearch.builder()
            .description("description for event 3")
            .build();
        singleResultSearch(eventSearch, 3);
    }

    private void singleResultSearch(EventSearch eventSearch, int expectedAtIndex){
        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        Event expectedEvent = allEvents.get(expectedAtIndex);

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);

        Assert.assertThat(eventsSearchResult.size(), is(1));
        Assert.assertEquals(expectedEvent, eventsSearchResult.get(0));
    }

    @Test
    public void filterByEventCategory(){
        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Event> expectedEvents = new LinkedList<>();
        expectedEvents.add(allEvents.get(1));
        expectedEvents.add(allEvents.get(7));

        EventSearch eventSearch = EventSearch.builder()
            .eventCategory("concert")
            .build();

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);

        Assert.assertThat(eventsSearchResult, is(expectedEvents));
    }

    @Test
    public void filerEventsByArtistID(){
        List<Artist> allArtists = artistRepository.findAll(new Sort(Sort.Direction.ASC, "firstname"));
        Artist artist = allArtists.get(0);

        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Event> expectedEvents = new LinkedList<>();
        expectedEvents.add(allEvents.get(0));
        expectedEvents.add(allEvents.get(2));
        expectedEvents.add(allEvents.get(4));
        expectedEvents.add(allEvents.get(6));
        expectedEvents.add(allEvents.get(8));


        EventSearch eventSearch = EventSearch.builder()
            .artistUUID(artist.getId())
            .build();

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);

        Assert.assertThat(eventsSearchResult, is(expectedEvents));
    }

    /* performance filters */

    //@Test
    /*public void filterPerformanceByLocation(){
        List<Location> allLocations = locationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        Location location = allLocations.get(0);

        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Event> expectedEvents = new LinkedList<>();
        expectedEvents.add(allEvents.get(1));
        expectedEvents.add(allEvents.get(7));

        EventSearch eventSearch = EventSearch.builder()
            .performanceLocationUUID(location.getId())
            .build();

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);

        Assert.assertThat(eventsSearchResult, is(expectedEvents));
    }*/


}
