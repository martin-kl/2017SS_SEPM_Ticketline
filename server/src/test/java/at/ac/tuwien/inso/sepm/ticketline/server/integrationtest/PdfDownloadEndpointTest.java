package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.TestDataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketTransaction;
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
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.hamcrest.core.Is.is;

public class PdfDownloadEndpointTest extends BaseIntegrationTest {

    private static final String TRANSACTION_ENDPOINT = "/tickettransaction";

    @Autowired
    private TestDataGenerator testDataGenerator;
    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;

    @Before
    public void beforeTest() {
        testDataGenerator.generateAllData(true);
    }

    @After
    public void afterTest() {
        testDataGenerator.emptyAllRepositories();
    }

    @Test
    public void downloadABoughtTransaction() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatus(TicketStatus.BOUGHT, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = transactionList.get(0);

        Response response = RestAssured
            .given()
            .contentType(ContentType.ANY)
            .when().get(TRANSACTION_ENDPOINT + "/" + ticketTransaction.getId() + "/download")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void downloadAReservedTransaction() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatus(TicketStatus.RESERVED, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = transactionList.get(0);

        Response response = RestAssured
            .given()
            .contentType(ContentType.ANY)
            .when().get(TRANSACTION_ENDPOINT + "/" + ticketTransaction.getId() + "/download")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void downloadAStornoTransaction() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatus(TicketStatus.STORNO, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = transactionList.get(0);

        Response response = RestAssured
            .given()
            .contentType(ContentType.ANY)
            .when().get(TRANSACTION_ENDPOINT + "/" + ticketTransaction.getId() + "/download")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void downloadAnInvalidTransaction() {

        Response response = RestAssured
            .given()
            .contentType(ContentType.ANY)
            .when().get(TRANSACTION_ENDPOINT + "/" + 1 + "/download")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

}
