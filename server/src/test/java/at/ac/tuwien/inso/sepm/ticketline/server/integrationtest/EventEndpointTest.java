package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatLocation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorLocation;
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
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.hamcrest.core.Is.is;


public class EventEndpointTest extends BaseIntegrationTest {

    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private PerformanceRepository performanceRepository;
    @MockBean
    private EventArtistRepository eventArtistRepository;

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
    private static final SectorLocation TEST_EVENT_PERFORMANCE_SECTOR_LOCATION = SectorLocation.builder()
        .id(UUID.randomUUID())
        .name("TestSectorLocation")
        .street("TestStreet2")
        .city("TestCity2")
        .zipCode("TestZipCode2")
        .country("TestCountry2")
        .build();

    private static final Performance TEST_EVENT_PERFORMANCE_1 = Performance.builder()
        .name("Testperformance1")
        .location(TEST_EVENT_PERFORMANCE_SEAT_LOCATION)
        .defaultPrice(new BigDecimal(Math.random() * 200 + 10))
        .startTime(Instant.now())
        .endTime(Instant.now())
        .build();

    private static final Performance TEST_EVENT_PERFORMANCE_2 = Performance.builder()
        .name("Testperformance2")
        .location(TEST_EVENT_PERFORMANCE_SECTOR_LOCATION)
        .defaultPrice(new BigDecimal(Math.random() * 200 + 10))
        .startTime(Instant.now())
        .endTime(Instant.now())
        .build();

    //TODO: implement complete test
    @Test
    public void findAllEventsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }
 /*
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
                    .performances(Collections.singleton(
                        TEST_EVENT_PERFORMANCE_1
                    ))
                    .build()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        System.out.println("Event list: " + response.as(EventDTO[].class).length);
        Assert.assertTrue("No Event found", response.as(EventDTO[].class).length == 1);


        Assert.assertThat(Arrays.asList(response.as(EventDTO[].class)), is(Collections.singletonList(
            EventDTO.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .category(EventCategory.NO_CATEGORY)
                .description(TEST_EVENT_DESCRIPTION)
                .performances(Collections.singleton(
                    TEST_EVENT_PERFORMANCE_1
                ))
                .build())));

    }
 */
}
