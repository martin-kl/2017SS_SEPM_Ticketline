package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import static org.hamcrest.core.Is.is;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PrincipalRole;
import at.ac.tuwien.inso.sepm.ticketline.rest.principal.PrincipalDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal.Role;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

public class PrincipalEndpointTest extends BaseIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PrincipalRepository principalRepository;
    @Autowired
    private PrincipalService principalService;

    private static final String PRINCIPAL_ENDPOINT = "/principal";

    private final String USERNAME1 = "testuser1";
    private final Principal.Role TESTROLE1 = Role.ADMIN;
    private final String USERONEMAIL = "testuser@test.at";
    private Principal testUserOne;

    private final String USERNAME2 = "testuser_two";
    private final Principal.Role TESTROLE2 = Role.SELLER;
    private final String USERTWOMAIL = "test_second_mail@test.com";
    private Principal testUserTwo;

    @Before
    public void setUp() {
        principalRepository.deleteAll();
        testUserOne = Principal.builder()
            .role(TESTROLE1)
            .username(USERNAME1)
            //.password(passwordEncoder.encode("password"))
            .enabled(true)
            .email(USERONEMAIL)
            .build();
        testUserOne = principalService.save(testUserOne, "password");

        testUserTwo = Principal.builder()
            .role(TESTROLE2)
            .username(USERNAME2)
            //.password("passwordTwo")
            .enabled(false)
            .email(USERTWOMAIL)
            .build();
        testUserTwo = principalService.save(testUserTwo, "passwordTwo");
    }

    @After
    public void cleanUp() {
        principalRepository.deleteAll();
    }

    @Test
    public void allActionsRequireAuthorization() {
        validUserTokenWithPrefix = "foobar";
        Assert.assertThat(requestCustomers().getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
        //Assert.assertThat(requestSpecificCustomer(UUID.randomUUID()).getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
        //Assert.assertThat(saveCustomerRequest(getCustomerDTO(null)).getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void canSaveAndUpdateNewSeller() {
        PrincipalDTO newPrincipal = new PrincipalDTO();
        newPrincipal.setUsername("new_user");
        newPrincipal.setNewPassword("new_password");
        newPrincipal.setPrincipalRole(PrincipalRole.SELLER);
        newPrincipal.setEmail("test@test.at");
        newPrincipal.setLocked(false);

        Response response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(newPrincipal)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().post(PRINCIPAL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        PrincipalDTO savedPrincipal = response.as(PrincipalDTO.class);
        Assert.assertTrue(savedPrincipal.getUsername().equals(newPrincipal.getUsername()));
        Assert.assertTrue(savedPrincipal.getEmail().equals(newPrincipal.getEmail()));
        Assert.assertTrue(savedPrincipal.getPrincipalRole().equals(newPrincipal.getPrincipalRole()));


        savedPrincipal.setEmail("ticketline@test.at");

        Response response1 = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(savedPrincipal)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().post(PRINCIPAL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response1.getStatusCode(), is(HttpStatus.OK.value()));
        PrincipalDTO principalEdit = response1.as(PrincipalDTO.class);
        Assert.assertTrue(principalEdit.getId().equals(savedPrincipal.getId()));
        Assert.assertTrue(principalEdit.getUsername().equals(newPrincipal.getUsername()));
        Assert.assertTrue(principalEdit.getEmail().equals("ticketline@test.at"));
        Assert.assertTrue(principalEdit.getPrincipalRole().equals(newPrincipal.getPrincipalRole()));
    }


    private Response requestCustomers() {
        return RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(PRINCIPAL_ENDPOINT + "?size=10000")
            .then().extract().response();
    }
}
