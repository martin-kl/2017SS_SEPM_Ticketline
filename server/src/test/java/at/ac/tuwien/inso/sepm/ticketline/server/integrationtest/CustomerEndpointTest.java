package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.datagenerator.CustomerDataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import com.github.javafaker.Faker;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomerEndpointTest extends BaseIntegrationTest {
    @Autowired
    private CustomerRepository customerRepository;


    private static final String CUSTOMER_ENDPOINT = "/customer";
    private static final String SPECIFIC_CUSTOMER_PATH = "/{customerId}";

    private static final String SEARCH_CUTOMER_PATH = "/search";

    private static final String CUSTOMER_1_FIRSTNAME = "Firstname";
    private static final String CUSTOMER_1_LASTNAME = "Lastname";
    private static final String CUSTOMER_1_ADDRESS = "Street 1 APP. 112132-.,,;:s%%46";
    private static final LocalDate CUSTOMER_1_BIRHTDAY = LocalDate.ofYearDay(20, 30);
    private static final String CUSTOMER_1_EMAIL = "mail@mail.com";
    private static final int CREATED_CUSTOMERS = 5;

    private static final Faker faker = new Faker();

    /**
     * This is a template for future integration tests.
     * 1.) @Before is used to fill the repository with some random data that is not specifically addressed in the tests
     *      this is useful to catch bugs that might only appear if there is more than a single entity in the database.
     *      To generate this you can reuse the logic in the generators by exposing their logic in a static method (e.g. 'allActionsRequireAuthorization').
     * 2.) One or more public static helper methods are defined that prevent repetition: e.g. 'getCustomerDTO(UUID uuid)' will
     *      fill in all the fields for a customer dto with constants defined at the top of the file. It is public/static so it can be reused
     *      in other tests.
     * 3.) All methods that make requests are defined in their own method and return the response to prevent repetition.
     * 4.) It is not necessary to create a test for each function. If you use 'findAll' in your 'create' test 'findAll' is considered tested.
     * 5.) If authorization is necessary for one or more routes test each route like was done in 'allActionsRequireAuthorization'
     * 6.) You have to use equals to compare two entities. Do not compare every field manually.
     * 7.) The method names must communicate which methods you are testing in what context: (e.g. 'findingOneForNonExistingLeadsToNotFound')
     * 8.) Use @Before and @After methods to empty repositories.
     */

    @Before
    public void fillWithCustomers() {
        customerRepository.deleteAll();
        for (int i = 0; i < CREATED_CUSTOMERS; i++) {
            customerRepository.save(CustomerDataGenerator.generateCustomer());
        }
    }

    @After
    public void cleanUp() {
        customerRepository.deleteAll();
    }

    @Test
    public void allActionsRequireAuthorization() {
        validUserTokenWithPrefix = "foobar";
        Assert.assertThat(requestCustomers().getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
        Assert.assertThat(requestSpecificCustomer(UUID.randomUUID()).getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
        Assert.assertThat(saveCustomerRequest(getCustomerDTO(null)).getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findingOneForNonExistingLeadsToNotFound() {
        Response response = requestSpecificCustomer(UUID.randomUUID());
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void savingInvalidCustomerLeadsToBadRequestException() {
        CustomerDTO customer = getCustomerDTO(null);
        customer.setLastName("");
        Response response = saveCustomerRequest(customer);
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void findAllIfNonExistReturnsEmptyList() {
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

    @Test
    public void userCanFuzzySearchForCusomers() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Foo");
        customerDTO.setLastName("Bar");
        customerDTO.setAddress("Adress 123, 1010 Wien");
        customerDTO.setEmail("mymail@mail.com");
        customerDTO.setBirthday(LocalDate.ofEpochDay(100));
        saveCustomerRequest(getCustomerDTO(null)); //save another customer
        Response savedResponse = saveCustomerRequest(customerDTO);
        customerDTO = savedResponse.as(CustomerDTO.class);

        Response response = searchCustomer("Bar oo 123 mail.com");
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<CustomerDTO> customers = Arrays.asList(response.as(CustomerDTO[].class));
        assertTrue(customers.contains(customerDTO));
        assertTrue(customers.size() == 1);
    }

    public static CustomerDTO getCustomerDTO(UUID uuid) {
        CustomerDTO customerDTO = new CustomerDTO();
        if (uuid != null) {
            customerDTO.setId(uuid);
        }
        customerDTO.setFirstName(CUSTOMER_1_FIRSTNAME);
        customerDTO.setLastName(CUSTOMER_1_LASTNAME);
        customerDTO.setAddress(CUSTOMER_1_ADDRESS);
        customerDTO.setBirthday(CUSTOMER_1_BIRHTDAY);
        customerDTO.setEmail(faker.internet().emailAddress());
        return customerDTO;
    }


    //REQUEST HELPERS
    //You can move these methods into base integration test should you need them somewhere else


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

    private Response searchCustomer(String query) {
        return RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .param("query", query)
            .when().get(CUSTOMER_ENDPOINT+SEARCH_CUTOMER_PATH)
            .then().extract().response();
    }

    private Response requestCustomers() {
        return RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + "?size=10000")
            .then().extract().response();
    }
}