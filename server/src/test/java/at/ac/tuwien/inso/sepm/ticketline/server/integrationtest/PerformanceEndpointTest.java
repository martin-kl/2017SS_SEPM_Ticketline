package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;


import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.DetailedPerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketWrapperDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location.LocationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;

public class PerformanceEndpointTest extends BaseIntegrationTest {
    private static final String PERFORMANCE_ENDPOINT = "/performance";
    private static final String SPECIFIC_PERFORMANCE_PATH = "/{performanceId}";

    private static final UUID TEST_PERFORMANCE_ID = UUID.randomUUID();
    private static final String TEST_PERFORMANCE_NAME = "PerformanceName";
    private static final Instant TEST_START_TIME = Instant.now();
    private static final Instant TEST_END_TIME = Instant.now();
    private static final BigDecimal TEST_DEFAULT_PRICE = new BigDecimal(100);

    private static final UUID TEST_EVENT_UUID = UUID.randomUUID();
    private static final Event TEST_EVENT = Event.builder()
        .id(TEST_EVENT_UUID)
        .name("Event Test")
        .description("Event Test Descr")
        .category(EventCategory.NO_CATEGORY)
        .build();

    private static final UUID TEST_LOCATION_UUID = UUID.randomUUID();
    private static final Location TEST_LOCATION = SeatLocation.builder()
        .id(TEST_LOCATION_UUID)
        .name("Test Location Name")
        .city("Test city")
        .country("Test country")
        .street("Test Street")
        .zipCode("Test Zipcode")
        .build();

    private static final UUID TEST_TICKET_UUID = UUID.randomUUID();
    private static final Set<Ticket> TEST_TICKETS = Collections.singleton(SeatTicket.builder()
        .id(TEST_TICKET_UUID)
        .price(TEST_DEFAULT_PRICE)
        .build());

    @MockBean
    private PerformanceRepository performanceRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private LocationMapper locationMapper;


    @Test
    public void findSpecificPerformanceUnauthorizedAsAnonymous(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(PERFORMANCE_ENDPOINT + SPECIFIC_PERFORMANCE_PATH, "")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificPerformanceAsUser(){

        BDDMockito.
            given(performanceRepository.findOneById(TEST_PERFORMANCE_ID)).
            willReturn(
                Optional.of(
                    Performance.builder()
                        .id(TEST_PERFORMANCE_ID)
                        .name(TEST_PERFORMANCE_NAME)
                        .startTime(TEST_START_TIME)
                        .endTime(TEST_END_TIME)
                        .defaultPrice(TEST_DEFAULT_PRICE)
                        .event(TEST_EVENT)
                        .location(TEST_LOCATION)
                        .tickets(TEST_TICKETS)
                        .build()
                )
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(PERFORMANCE_ENDPOINT + SPECIFIC_PERFORMANCE_PATH, TEST_PERFORMANCE_ID)
            .then().extract().response();

        //Create TicketDTO and TicketWrapperDTO from Entities
        Ticket ticket = TEST_TICKETS.stream().collect(Collectors.toList()).get(0);
        TicketDTO ticketDTO = ticketMapper.toDTO(ticket);

        TicketWrapperDTO ticketWrapperDTO = new TicketWrapperDTO();
        ticketWrapperDTO.setTicket(ticketDTO);
        ticketWrapperDTO.setStatus(TicketStatus.BOUGHT);
        List<TicketWrapperDTO> ticketWrapperDTOList = new LinkedList<>();
        ticketWrapperDTOList.add(ticketWrapperDTO);

        //Create DetailedPerformanceDTO
        DetailedPerformanceDTO detailedPerformanceDTO = new DetailedPerformanceDTO();
        detailedPerformanceDTO.setId(TEST_PERFORMANCE_ID);
        detailedPerformanceDTO.setName(TEST_PERFORMANCE_NAME);
        detailedPerformanceDTO.setStartTime(TEST_START_TIME);
        detailedPerformanceDTO.setEndTime(TEST_END_TIME);
        detailedPerformanceDTO.setDefaultPrice(TEST_DEFAULT_PRICE);
        detailedPerformanceDTO.setLocation(locationMapper.fromEntity(TEST_LOCATION));
        detailedPerformanceDTO.setTicketWrapperList(ticketWrapperDTOList);

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(
            response.as(DetailedPerformanceDTO.class),
            is(DetailedPerformanceDTO.builder()
                .id(TEST_PERFORMANCE_ID)
                .name(TEST_PERFORMANCE_NAME)
                .startTime(TEST_START_TIME)
                .endTime(TEST_END_TIME)
                .defaultPrice(TEST_DEFAULT_PRICE)
                .location(locationMapper.fromEntity(TEST_LOCATION))
                .build()
            ));
    }

}
