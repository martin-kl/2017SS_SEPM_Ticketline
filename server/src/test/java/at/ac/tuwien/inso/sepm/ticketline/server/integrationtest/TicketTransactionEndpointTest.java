package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickettransaction.TicketTransactionMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.core.Is.is;

public class TicketTransactionEndpointTest extends BaseIntegrationTest {

    private static final String TRANSACTION_ENDPOINT = "/tickettransaction";

    private static final UUID TEST_TRANSACTION_ID = UUID.randomUUID();
    private static final Customer TEST_CUSTOMER = Customer.builder()
        .id(UUID.randomUUID())
        .firstName("TestuserFN")
        .lastName("TestuserLN")
        .build();

    private static final Ticket TEST_TICKET = SeatTicket.builder()
        .id(UUID.randomUUID())
        .price(new BigDecimal(100))
        .build();

    private static final TicketHistory TEST_TICKET_HISTORY = TicketHistory.builder()
        .id(UUID.randomUUID())
        .ticket(TEST_TICKET)
        .build();

    @MockBean
    private TicketTransactionRepository ticketTransactionRepository;

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
    public void findAllReservedTransactions() {
        BDDMockito
            .given(ticketTransactionRepository.findByStatus(TicketStatus.RESERVED))
            .willReturn(Collections.singletonList(
                TicketTransaction
                    .builder()
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
            .when().get(TRANSACTION_ENDPOINT + "?status=RESERVED")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertTrue("No Transaction Found",
            response.as(DetailedTicketTransactionDTO[].class).length == 1);
        DetailedTicketTransactionDTO[] list = response.as(DetailedTicketTransactionDTO[].class);
        for (DetailedTicketTransactionDTO item : list) {
            Assert.assertTrue(item.getCustomer() != null);
            Assert.assertTrue(item.getId() != null);
            Assert.assertTrue(item.getStatus() == TicketStatus.RESERVED);
            Assert.assertFalse(item.getTickets().isEmpty());
        }
    }

    @Test
    public void findAllBoughtTransactions() {
        BDDMockito
            .given(ticketTransactionRepository.findByStatus(TicketStatus.BOUGHT))
            .willReturn(Collections.singletonList(
                TicketTransaction
                    .builder()
                    .id(TEST_TRANSACTION_ID)
                    .status(TicketStatus.BOUGHT)
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
            .when().get(TRANSACTION_ENDPOINT + "?status=BOUGHT")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertTrue("No Transaction Found",
            response.as(DetailedTicketTransactionDTO[].class).length == 1);
        DetailedTicketTransactionDTO[] list = response.as(DetailedTicketTransactionDTO[].class);
        for (DetailedTicketTransactionDTO item : list) {
            Assert.assertTrue(item.getCustomer() != null);
            Assert.assertTrue(item.getId() != null);
            Assert.assertTrue(item.getStatus() == TicketStatus.BOUGHT);
            Assert.assertFalse(item.getTickets().isEmpty());
        }
    }

    @Test
    public void findAllStornoTransactions() {
        BDDMockito
            .given(ticketTransactionRepository.findByStatus(TicketStatus.STORNO))
            .willReturn(Collections.singletonList(
                TicketTransaction
                    .builder()
                    .id(TEST_TRANSACTION_ID)
                    .status(TicketStatus.STORNO)
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
            .when().get(TRANSACTION_ENDPOINT + "?status=STORNO")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertTrue("No Transaction Found",
            response.as(DetailedTicketTransactionDTO[].class).length == 1);
        DetailedTicketTransactionDTO[] list = response.as(DetailedTicketTransactionDTO[].class);
        for (DetailedTicketTransactionDTO item : list) {
            Assert.assertTrue(item.getCustomer() != null);
            Assert.assertTrue(item.getId() != null);
            Assert.assertTrue(item.getStatus() == TicketStatus.STORNO);
            Assert.assertFalse(item.getTickets().isEmpty());
        }
    }

    @Test
    public void findAllInvalidStatusTransactions() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TRANSACTION_ENDPOINT + "?status=INVALID")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    //TODO this test is NOT working - i`ve no idea how to fix the problem

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
        //TODO bei response.as auch auf array? - weil eig. liefert die Methode ja nur 1 Transaction retour
        Assert.assertTrue("No Transaction Found",
            response.as(DetailedTicketTransactionDTO[].class).length == 1);

        DetailedTicketTransactionDTO item = response.as(DetailedTicketTransactionDTO.class);
        Assert.assertTrue(item.getCustomer() != null);
        Assert.assertTrue(item.getId() != null);
        Assert.assertTrue(item.getStatus() == TicketStatus.RESERVED);
        Assert.assertFalse(item.getTickets().isEmpty());
    }
}
