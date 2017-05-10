package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatLocation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorLocation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;


public class EventEndpointTest extends BaseIntegrationTest {

    @MockBean
    private EventRepository eventRepository;
    @Autowired
    private PerformanceMapper performanceMapper;

    private static final String EVENT_ENDPOINT = "/event";
    private static final UUID TEST_EVENT_ID = UUID.randomUUID();
    private static final String TEST_EVENT_NAME = "Testname";
    private static final String TEST_EVENT_DESCRIPTION = "Testdescription";
    private static final SeatLocation TEST_EVENT_PERFORMANCE_SEAT_LOCATION = SeatLocation.builder()
        .id(UUID.randomUUID())
        .name("TestSeatLocation")
        .street("TestStreet1")
        .city("TestCity1")
        .zipCode("TestZipCode1")
        .country("TestCountry1")
        .build();

    private static final Performance TEST_EVENT_PERFORMANCE_1 = Performance.builder()
        .name("Testperformance1")
        .location(TEST_EVENT_PERFORMANCE_SEAT_LOCATION)
        .defaultPrice(new BigDecimal(Math.random() * 200 + 10))
        .startTime(Instant.now())
        .endTime(Instant.now())
        .build();

    @Test
    public void findAllEventsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllEventsIfNonExistReturnsEmptyList(){
        eventRepository.deleteAll();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        List<EventDTO> events = Arrays.asList(response.as(EventDTO[].class));
        assertEquals(0, events.size());
    }

    @Test
    public void findAllEvents() {
        BDDMockito.
            given(eventRepository.findAll()).
            willReturn(Collections.singletonList(
                Event.builder()
                    .id(TEST_EVENT_ID)
                    .name(TEST_EVENT_NAME)
                    .category(EventCategory.NO_CATEGORY)
                    .description(TEST_EVENT_DESCRIPTION)
                    .performances(Stream.of(TEST_EVENT_PERFORMANCE_1)
                        .collect(Collectors.toSet()))
                    .build()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertTrue("No Event found", response.as(EventDTO[].class).length == 1);

        Assert.assertThat(Arrays.asList(response.as(EventDTO[].class)), is(Collections.singletonList(
            EventDTO.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .category(EventCategory.NO_CATEGORY)
                .description(TEST_EVENT_DESCRIPTION)
                .performances(Stream.of(TEST_EVENT_PERFORMANCE_1)
                        .map(performanceMapper::fromEntity)
                        .collect(Collectors.toList()))
                .build())));

    }
}
