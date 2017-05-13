package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;


import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.hamcrest.core.Is.is;

public class PerformanceEndpointTest extends BaseIntegrationTest {
    private static final String PERFORMANCE_ENDPOINT = "/performance";
    private static final String SPECIFIC_PERFORMANCE_PATH = "/{performanceId}";

    @MockBean
    private PerformanceRepository performanceRepository;


    @Test
    public void findSpecificPerformanceUnauthorizedAsAnonymous(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(PERFORMANCE_ENDPOINT + SPECIFIC_PERFORMANCE_PATH, "123") //TODO pathParams ändern
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

}
