package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.datagenerator.CustomerDataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CustomerEndpointTest extends BaseIntegrationTest {
    @Autowired
    private CustomerRepository customerRepository;


    private static final String CUSTOMER_ENDPOINT = "/customer";
    private static final String SPECIFIC_CUSTOMER_PATH = "/{customerId}";

    private static final String CUSTOMER_1_FIRSTNAME = "Firstname";
    private static final String CUSTOMER_1_LASTNAME = "Lastname";
    private static final String CUSTOMER_1_ADDRESS = "Street 1 APP. 112132-.,,;:s%%46";
    private static final LocalDate CUSTOMER_1_BIRHTDAY = LocalDate.ofYearDay(20, 30);
    private static final String CUSTOMER_1_EMAIL = "mail@mail.com";
    private static final int CREATED_CUSTOMERS = 5;

    @Before
    public void fillWithCustomers() {
        for (int i = 0; i < CREATED_CUSTOMERS; i++) {
            customerRepository.save(CustomerDataGenerator.generateCustomer());
        }
    }

    @Test
    public void allActionsRequireAuthorization() {
        validUserTokenWithPrefix = "foobar";
        Assert.assertThat(requestCustomers().getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
        Assert.assertThat(requestSpecificCustomer(UUID.randomUUID()).getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
        Assert.assertThat(saveCustomerRequest(getCustomerDTO(null)).getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findingNonExistingLeadsToNotFound() {
        Response response = requestSpecificCustomer(UUID.randomUUID());
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void userCanFindAllCustomersIfNonExistAndGetEmptyList() {
        customerRepository.deleteAll();
        Response response = requestCustomers();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<CustomerDTO> customers = Arrays.asList(response.as(CustomerDTO[].class));
        assertEquals(0, customers.size());
    }


    @Test
    public void userCanEditAndFindSpecificCustomers() {
        Response insertedCustomerResponse = saveCustomerRequest(getCustomerDTO(null));
        Assert.assertThat(insertedCustomerResponse.getStatusCode(), is(HttpStatus.OK.value()));
        CustomerDTO insertedCustomer = insertedCustomerResponse.as(CustomerDTO.class);
        insertedCustomer.setLastName("editingCustomerAsUserTest");
        Response editedCustomerResponse = saveCustomerRequest(insertedCustomer);
        Assert.assertThat(editedCustomerResponse.getStatusCode(), is(HttpStatus.OK.value()));
        Response getCustomersResponse = requestSpecificCustomer(insertedCustomer.getId());
        CustomerDTO retrievedCustomer = getCustomersResponse.as(CustomerDTO.class);
        assertEquals(retrievedCustomer, insertedCustomer);
    }

    @Test
    public void userCanCreateAndFindAllCustomersInAlphabeticOrder() {
        CustomerDTO firstInAlphabet = getCustomerDTO(null);
        firstInAlphabet.setLastName("AAAAAAAAA");
        firstInAlphabet = saveCustomerRequest(firstInAlphabet).as(CustomerDTO.class);
        CustomerDTO lastInAlphabet = getCustomerDTO(null);
        lastInAlphabet.setLastName("ZZZZZZZZ");
        lastInAlphabet = saveCustomerRequest(lastInAlphabet).as(CustomerDTO.class);
        Response response = requestCustomers();
        List<CustomerDTO> customers = Arrays.asList(response.as(CustomerDTO[].class));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        assertEquals(CREATED_CUSTOMERS+2, customers.size());
        assertTrue(customers.contains(firstInAlphabet));
        assertTrue(customers.contains(lastInAlphabet));
        assertTrue(customers.get(0).equals(firstInAlphabet));
        assertTrue(customers.get(customers.size()-1).equals(lastInAlphabet));
    }



    //HELPERS
    //You can move these methods into base integration test should you need them somewhere else

    private CustomerDTO getCustomerDTO(UUID uuid) {
        CustomerDTO customerDTO = new CustomerDTO();
        if (uuid != null) {
            customerDTO.setId(uuid);
        }
        customerDTO.setFirstName(CUSTOMER_1_FIRSTNAME);
        customerDTO.setLastName(CUSTOMER_1_LASTNAME);
        customerDTO.setAddress(CUSTOMER_1_ADDRESS);
        customerDTO.setBirthday(CUSTOMER_1_BIRHTDAY);
        customerDTO.setEmail(CUSTOMER_1_EMAIL);
        return customerDTO;
    }

    private Response saveCustomerRequest(CustomerDTO customerDTO) {
        return RestAssured
            .given()
            .body(customerDTO)
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().post(CUSTOMER_ENDPOINT)
            .then().extract().response();
    }

    private Response requestSpecificCustomer(UUID uuid) {
        return RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + SPECIFIC_CUSTOMER_PATH, uuid)
            .then().extract().response();
    }

    private Response requestCustomers() {
        return RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT)
            .then().extract().response();
    }


}
