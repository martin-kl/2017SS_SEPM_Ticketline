package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketHistoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.AssertTrue;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

public class TicketTransactionEndpointTest extends BaseIntegrationTest {

    private static final String TRANSACTION_ENDPOINT = "/tickettransaction";

    private static final UUID TEST_TRANSACTION_ID = UUID.randomUUID();
    private static final Customer TEST_CUSTOMER = Customer.builder()
        .id(UUID.randomUUID())
        .firstName("TestuserFN")
        .lastName("TestuserLN")
        .email("mail@mail.com")
        .address("First Street 12, 2019 City")
        .birthday(LocalDate.now())
        .build();

    private static final Performance TEST_PERFORMANCE = Performance.builder()
        .name("Testperformance")
        .build();

    private static final Ticket TEST_TICKET = SeatTicket.builder()
        .id(UUID.randomUUID())
        .price(new BigDecimal(100))
        .performance(TEST_PERFORMANCE)
        .build();

    private static final TicketHistory TEST_TICKET_HISTORY = TicketHistory.builder()
        .id(UUID.randomUUID())
        .ticket(TEST_TICKET)
        .build();

    @MockBean
    private TicketTransactionRepository ticketTransactionRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;
    @Autowired
    private PerformanceRepository performanceRepository;

    @Test
    public void findAllTransactionsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TRANSACTION_ENDPOINT + "?status=RESERVED")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findTransactionByValidId() {
        BDDMockito
            .given(ticketTransactionRepository.findOneById(TEST_TRANSACTION_ID))
            .willReturn(Optional.of(TicketTransaction.builder()
                .id(TEST_TRANSACTION_ID)
                .status(TicketStatus.RESERVED)
                .customer(TEST_CUSTOMER)
                .ticketHistories(Collections.singleton(
                    TEST_TICKET_HISTORY
                ))
                .build()
            ));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            //.when().get(TRANSACTION_ENDPOINT + "?id=" + TEST_TRANSACTION_ID)
            .when().get(TRANSACTION_ENDPOINT + "/" + TEST_TRANSACTION_ID)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        DetailedTicketTransactionDTO item = response.as(DetailedTicketTransactionDTO.class);

        Assert.assertTrue(item.getCustomer() != null);
        Assert.assertTrue(item.getId() != null);
        Assert.assertTrue(item.getStatus() == TicketStatus.RESERVED);
        Assert.assertFalse(item.getTickets().isEmpty());
    }

    @Test
    public void findTransactionByInvalidId() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TRANSACTION_ENDPOINT + "/" + UUID.randomUUID())
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void findAllBoughtReservedAuthorized() {
        List<TicketTransaction> ttList = new ArrayList<>(2);
        for(int i = 0; i < 5; i++) {
            Set<TicketHistory> ttSet = new HashSet<>(1);
            ttSet.add(TEST_TICKET_HISTORY);
            TicketTransaction tt = new TicketTransaction(UUID.randomUUID(), TicketStatus.BOUGHT, ttSet, TEST_CUSTOMER);
            ttList.add(tt);
        }
        BDDMockito
            .given(ticketTransactionRepository.findByStatusOrStatusOrderByLastModifiedAtDesc(
                eq(TicketStatus.BOUGHT), eq(TicketStatus.RESERVED), any(Pageable.class)))
            .willReturn(ttList);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TRANSACTION_ENDPOINT + "?page=0&size=10")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        //System.out.println(response.getBody());
        List<DetailedTicketTransactionDTO> responseList = Arrays.asList(response.as(DetailedTicketTransactionDTO[].class));
        Assert.assertEquals(5, responseList.size());
    }

}
