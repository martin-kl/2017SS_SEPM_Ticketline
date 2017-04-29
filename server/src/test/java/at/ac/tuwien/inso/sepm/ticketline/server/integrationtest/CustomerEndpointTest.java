package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.core.Is.is;

public class CustomerEndpointTest extends BaseIntegrationTest{
    @MockBean
    private CustomerRepository customerRepository;

    private static final String CUSTOMER_ENDPOINT = "/customer";
    private static final String SPECIFIC_NEWS_PATH = "/{customerId}";

    @Test
    public void findAllNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(CUSTOMER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));

    }
    @Test
    public void findAllNewsAsUser() {
        UUID uuid = new UUID(1, 2);
        BDDMockito.
            given(customerRepository.findAll(new Sort("lastname"))).
            willReturn(Collections.singletonList(
                Customer.builder()
                    .id(uuid)
                    .firstName("Name")
                    .lastName("Lastname")
                    .address("balbalbalba")
                    .birthday(LocalDate.MAX)
                    .email("mail@mail.com")
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(uuid)
                .firstName("Name")
                .lastName("Lastname")
                .address("balbalbalba")
                .birthday(LocalDate.MAX)
                .email("mail@mail.com")
                .build())));
    }


}
