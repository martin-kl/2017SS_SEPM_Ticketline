package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.hamcrest.core.Is.is;


public class EventEndpointTest extends BaseIntegrationTest {
    private static final String EVENT_ENDPOINT = "/event";
    private static final UUID TEST_EVENT_ID = UUID.randomUUID();
    private static final String TEST_EVENT_NAME = "Testname";
    private static final String TEST_EVENT_DESCRIPTION = "Testdescription";

    @MockBean
    private EventRepository eventRepository;

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

   /* @Test
    public void findAllEvents() {
        BDDMockito.
            given(eventRepository.findAll()).
            willReturn(Collections.singletonList(
                Event.builder()
                    .id(TEST_EVENT_ID)
                    .name(TEST_EVENT_NAME)
                    .category(EventCategory.NO_CATEGORY)
                    .description(TEST_EVENT_DESCRIPTION)
                    .build()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(EventDTO[].class)), is(Collections.singletonList(
            EventDTO.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .category(EventCategory.NO_CATEGORY)
                .description(TEST_EVENT_DESCRIPTION)
                .build())));
    }*/

}
