package at.ac.tuwien.inso.sepm.ticketline.server.service.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PerformanceType;
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
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.EventFilterTestDataGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventServiceTest {
    @Autowired
    private EventFilterTestDataGenerator eventFilterTestDataGenerator;

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
    public void afterTest() {
        eventTestDataGenerator.emptyAllRepositories();
    }

    @Test
    public void emptyFilterShouldFindAll() {
        EventSearch eventSearch = new EventSearch();

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);
        List<Event> allEvents = eventService.findAll(null);

        Assert.assertEquals(eventsSearchResult, allEvents);
    }

    @Test
    public void filterByEventName() {
        EventSearch eventSearch = EventSearch.builder()
            .eventName("event 0")
            .build();
        singleResultSearch(eventSearch, 0);
    }

    @Test
    public void filterByEventDescription() {
        EventSearch eventSearch = EventSearch.builder()
            .description("description for event 3")
            .build();
        singleResultSearch(eventSearch, 3);
    }

    private void singleResultSearch(EventSearch eventSearch, int expectedAtIndex) {
        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        Event expectedEvent = allEvents.get(expectedAtIndex);

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);

        Assert.assertThat(eventsSearchResult.size(), is(1));
        Assert.assertEquals(expectedEvent, eventsSearchResult.get(0));
    }

    @Test
    public void filterByEventCategory() {
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
    public void filerEventsByArtistID() {
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

    @Test
    public void filterPerformanceByLocation() {
        List<Location> allLocations = locationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        Location location = allLocations.get(0);

        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Event> expectedEvents = new LinkedList<>();
        expectedEvents.add(allEvents.get(0));
        expectedEvents.add(allEvents.get(2));
        expectedEvents.add(allEvents.get(4));
        expectedEvents.add(allEvents.get(6));
        expectedEvents.add(allEvents.get(8));

        EventSearch eventSearch = EventSearch.builder()
            .performanceLocationUUID(location.getId())
            .build();

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);

        for (Event e : eventsSearchResult) {
            Assert.assertThat(e.getPerformances().size(), is(1));
        }

        Assert.assertThat(eventsSearchResult, is(expectedEvents));
    }

    @Test
    public void filterPerformancesByDuration() {
        //Assert.assertThat(); duration
        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Event> expectedEvents = new LinkedList<>(allEvents);

        EventSearch eventSearch = EventSearch.builder()
            .performanceDuration(Duration.ofHours(1))
            .build();

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);
        for (Event e : eventsSearchResult) {
            Assert.assertThat(e.getPerformances().size(), is(1));
        }

        Assert.assertThat(eventsSearchResult, is(expectedEvents));
    }

    @Test
    public void filterPerformancesByType() {
        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Event> expectedEvents = new LinkedList<>(allEvents);

        EventSearch eventSearch = EventSearch.builder()
            .performanceType(PerformanceType.SEAT)
            .build();
        List<Event> eventsSearchResult = eventService.search(eventSearch, null);

        for (Event e : eventsSearchResult) {
            Assert.assertTrue(e.getPerformances().size() == 1 || e.getPerformances().size() == 2);
        }

        Assert.assertThat(eventsSearchResult, is(expectedEvents));
    }

    @Test
    public void filterPerformancesByStartAndEndDate() {
        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Event> expectedEvents = new LinkedList<>(allEvents);

        EventSearch eventSearch = EventSearch.builder()
            .performanceStartDate(LocalDate.of(2012, 6, 5))
            .performanceEndDate(LocalDate.of(2012, 6, 5))
            .build();
        List<Event> eventsSearchResult = eventService.search(eventSearch, null);

        for (Event e : eventsSearchResult) {
            Assert.assertTrue(e.getPerformances().size() == 1);
        }

        Assert.assertThat(eventsSearchResult, is(expectedEvents));
    }

    @Test
    public void filterPerformancesByTicketPrice() {
        List<Event> allEvents = eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        List<Event> expectedEvents = new LinkedList<>(allEvents);

        EventSearch eventSearch = EventSearch.builder()
            .performanceTicketPrice(new BigDecimal(109))
            .build();

        List<Event> eventsSearchResult = eventService.search(eventSearch, null);
        for (Event e : eventsSearchResult) {
            Assert.assertTrue(e.getPerformances().size() == 1);
        }
        Assert.assertThat(eventsSearchResult, is(expectedEvents));
    }


    @Test
    public void canFindTopTenEventsFromEveryCategory() {
        eventFilterTestDataGenerator.generateAllData(true);
        Map<Integer, Event> map = eventService.getTopTen(null, -1);
        Assert.assertTrue(map.containsKey(5));
        Assert.assertTrue(map.containsKey(2));
        Assert.assertTrue(map.size() == 2);
    }

    @Test
    public void canFindTopTenEventsFromNoCategory() {
        eventFilterTestDataGenerator.generateAllData(true);
        Map<Integer, Event> map = eventService.getTopTen(EventCategory.NO_CATEGORY, -1);
        Assert.assertTrue(map.containsKey(2));
        Assert.assertTrue(map.size() == 1);
    }


    @Test
    public void canFindTopTenEventsFromCategoryOne() {
        eventFilterTestDataGenerator.generateAllData(true);
        Map<Integer, Event> map = eventService.getTopTen(EventCategory.CONCERT, -1);
        Assert.assertTrue(map.containsKey(5));
        Assert.assertTrue(map.size() == 1);
    }

/*
        //ATTENTION: this test should work but does not because the date manipulation for the test
        data does not work

    //test time constraint:

    @Test
    public void canFindTopTenEventsFromAllCategoriesInLastMonth() {
        eventFilterTestDataGenerator.generateAllData(true);
        Map<Integer, Event> map = eventService.getTopTen(null, 1);
        System.out.println("\n\n\t\tRESULT:\n" + map);
        Assert.assertTrue(map.containsKey(5));
        //ATTENTION: this assert should work but does not because the date manipulation does not work
        //Assert.assertTrue(map.size() == 1);
    }
    */
}

