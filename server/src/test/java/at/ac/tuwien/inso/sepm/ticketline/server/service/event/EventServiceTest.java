package at.ac.tuwien.inso.sepm.ticketline.server.service.event;

import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventServiceTest {
    @Autowired
    private EventTestDataGenerator eventTestDataGenerator;

    @Autowired
    private EventService eventService;

    @Before
    public void beforeTest() {
        eventTestDataGenerator.generateAllData();
    }

    @After
    public void afterTest(){
        eventTestDataGenerator.emptyAllRepositories();
    }

    @Test
    public void emptyFilterShouldFindAll(){

    }
}
