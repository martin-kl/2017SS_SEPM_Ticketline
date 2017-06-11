package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.DetailedTicketTransactionDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.TestDataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.tickettransaction.TicketTransactionMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketTransactionRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class TicketTransactionUpdateEndpointTest extends BaseIntegrationTest {

    private static final String TRANSACTION_ENDPOINT = "/tickettransaction";

    @Autowired
    private TestDataGenerator testDataGenerator;
    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;
    @Autowired
    private TicketTransactionMapper ticketTransactionMapper;

    @Before
    public void beforeTest() {
        testDataGenerator.generateAllData(true);
    }

    @After
    public void afterTest() {
        testDataGenerator.emptyAllRepositories();
    }

    @Test
    public void createTransactionUnauthorized() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(
                DetailedTicketTransactionDTO.builder()
                    .customer(null)
                    .status(TicketStatus.RESERVED)
                    .tickets(new ArrayList<>())
                    .build()
            )
            .when().patch(TRANSACTION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void createTransactionReservedFromStorno() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatusAndOutdated(TicketStatus.STORNO, false, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = ticketTransactionRepository.findFullOne(transactionList.get(0).getId());

        DetailedTicketTransactionDTO dto = ticketTransactionMapper.fromEntity(ticketTransaction);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(
                DetailedTicketTransactionDTO.builder()
                    .customer(null)
                    .status(TicketStatus.RESERVED)
                    .tickets(dto.getTickets())
                    .build()
            )
            .when().patch(TRANSACTION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        DetailedTicketTransactionDTO tt = response.as(DetailedTicketTransactionDTO.class);

        Assert.assertNotNull(tt.getId());
        Assert.assertNotNull(tt.getTickets());
        Assert.assertNotNull(tt.getStatus());
        Assert.assertNull(tt.getCustomer());
        Assert.assertTrue(tt.getTickets().size() == dto.getTickets().size());
        Assert.assertTrue(tt.getStatus() == TicketStatus.RESERVED);
    }

    @Test
    public void createTransactionBoughtFromStorno() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatusAndOutdated(TicketStatus.STORNO, false, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = ticketTransactionRepository.findFullOne(transactionList.get(0).getId());

        DetailedTicketTransactionDTO dto = ticketTransactionMapper.fromEntity(ticketTransaction);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(
                DetailedTicketTransactionDTO.builder()
                    .customer(null)
                    .status(TicketStatus.BOUGHT)
                    .tickets(dto.getTickets())
                    .build()
            )
            .when().patch(TRANSACTION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        DetailedTicketTransactionDTO tt = response.as(DetailedTicketTransactionDTO.class);

        Assert.assertNotNull(tt.getId());
        Assert.assertNotNull(tt.getTickets());
        Assert.assertNotNull(tt.getStatus());
        Assert.assertNull(tt.getCustomer());
        Assert.assertTrue(tt.getTickets().size() == dto.getTickets().size());
        Assert.assertTrue(tt.getStatus() == TicketStatus.BOUGHT);
    }

    @Test
    public void createTransactionStornoFromBought() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatusAndOutdated(TicketStatus.BOUGHT, false, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = ticketTransactionRepository.findFullOne(transactionList.get(0).getId());

        DetailedTicketTransactionDTO dto = ticketTransactionMapper.fromEntity(ticketTransaction);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(
                DetailedTicketTransactionDTO.builder()
                    .id(dto.getId())
                    .customer(null)
                    .status(TicketStatus.STORNO)
                    .tickets(dto.getTickets())
                    .build()
            )
            .when().patch(TRANSACTION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        DetailedTicketTransactionDTO tt = response.as(DetailedTicketTransactionDTO.class);

        Assert.assertNotNull(tt.getId());
        Assert.assertNotNull(tt.getTickets());
        Assert.assertNotNull(tt.getStatus());
        Assert.assertNull(tt.getCustomer());
        Assert.assertTrue(tt.getTickets().size() == dto.getTickets().size());
        Assert.assertTrue(tt.getStatus() == TicketStatus.STORNO);
    }

    @Test
    public void shouldConflictCreatingNewStornoFromBought() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatusAndOutdated(TicketStatus.BOUGHT, false, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = ticketTransactionRepository.findFullOne(transactionList.get(0).getId());

        DetailedTicketTransactionDTO dto = ticketTransactionMapper.fromEntity(ticketTransaction);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(
                DetailedTicketTransactionDTO.builder()
                    .id(null)
                    .customer(null)
                    .status(TicketStatus.STORNO)
                    .tickets(dto.getTickets())
                    .build()
            )
            .when().patch(TRANSACTION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void createTransactionReservedToBought() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatusAndOutdated(TicketStatus.RESERVED, false, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = ticketTransactionRepository.findFullOne(transactionList.get(0).getId());

        DetailedTicketTransactionDTO dto = ticketTransactionMapper.fromEntity(ticketTransaction);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(
                DetailedTicketTransactionDTO.builder()
                    .id(dto.getId())
                    .customer(null)
                    .status(TicketStatus.BOUGHT)
                    .tickets(dto.getTickets())
                    .build()
            )
            .when().patch(TRANSACTION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        DetailedTicketTransactionDTO tt = response.as(DetailedTicketTransactionDTO.class);

        Assert.assertNotNull(tt.getId());
        Assert.assertNotNull(tt.getTickets());
        Assert.assertNotNull(tt.getStatus());
        Assert.assertNull(tt.getCustomer());
        Assert.assertTrue(tt.getTickets().size() == dto.getTickets().size());
        Assert.assertTrue(tt.getStatus() == TicketStatus.BOUGHT);
    }

    @Test
    public void stornoJust1Ticket() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatusAndOutdated(TicketStatus.BOUGHT, false, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = ticketTransactionRepository.findFullOne(transactionList.get(0).getId());

        DetailedTicketTransactionDTO dto = ticketTransactionMapper.fromEntity(ticketTransaction);

        ArrayList<TicketDTO> ticketDTOS = new ArrayList<>();
        ticketDTOS.add(dto.getTickets().get(0));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(
                DetailedTicketTransactionDTO.builder()
                    .id(dto.getId())
                    .customer(null)
                    .status(TicketStatus.STORNO)
                    .tickets(ticketDTOS)
                    .build()
            )
            .when().patch(TRANSACTION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        DetailedTicketTransactionDTO tt = response.as(DetailedTicketTransactionDTO.class);

        Assert.assertNotNull(tt.getId());
        Assert.assertNotNull(tt.getTickets());
        Assert.assertNotNull(tt.getStatus());
        Assert.assertNull(tt.getCustomer());
        Assert.assertTrue(tt.getTickets().size() == 1);
        Assert.assertTrue(tt.getStatus() == TicketStatus.STORNO);
    }

}
