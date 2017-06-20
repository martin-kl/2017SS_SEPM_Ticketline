package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PaymentProviderOption;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.payment.PaymentRequestDTO;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.hamcrest.core.Is.is;

//
// Test Tokens from here
// https://stripe.com/docs/testing
//

public class PaymentEndpointTest extends BaseIntegrationTest {

    private static final String TRANSACTION_ENDPOINT = "/tickettransaction";

    private static final String TOKEN_SUCCESS ="tok_bypassPending";
    private static final String TOKEN_FAILED ="tok_chargeDeclined";

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
    public void payUnauthorized() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatusAndOutdated(TicketStatus.BOUGHT, false, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = ticketTransactionRepository.findFullOne(transactionList.get(0).getId());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(
                PaymentRequestDTO
                    .builder()
                    .provider(PaymentProviderOption.STRIPE)
                    .source(TOKEN_SUCCESS)
                    .build()
            )
            .when().post(TRANSACTION_ENDPOINT + "/" + ticketTransaction.getId() + "/pay")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void paySuccessfully() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatusAndOutdated(TicketStatus.BOUGHT, false, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = ticketTransactionRepository.findFullOne(transactionList.get(0).getId());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(
                PaymentRequestDTO
                    .builder()
                    .provider(PaymentProviderOption.STRIPE)
                    .source(TOKEN_SUCCESS)
                    .build()
            )
            .when().post(TRANSACTION_ENDPOINT + "/" + ticketTransaction.getId() + "/pay")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        ticketTransaction = ticketTransactionRepository.findFullOne(ticketTransaction.getId());
        Assert.assertNotNull(ticketTransaction.getPaymentIdentifier());
        Assert.assertNotNull(ticketTransaction.getPaymentProviderOption());
    }

    @Test
    public void payFailed() {
        List<TicketTransaction> transactionList = ticketTransactionRepository
            .findByStatusAndOutdated(TicketStatus.BOUGHT, false, new PageRequest(0, 1));
        Assert.assertFalse(transactionList.isEmpty());
        TicketTransaction ticketTransaction = ticketTransactionRepository.findFullOne(transactionList.get(0).getId());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(
                PaymentRequestDTO
                    .builder()
                    .provider(PaymentProviderOption.STRIPE)
                    .source(TOKEN_FAILED)
                    .build()
            )
            .when().post(TRANSACTION_ENDPOINT + "/" + ticketTransaction.getId() + "/pay")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.PRECONDITION_FAILED.value()));
    }
}
