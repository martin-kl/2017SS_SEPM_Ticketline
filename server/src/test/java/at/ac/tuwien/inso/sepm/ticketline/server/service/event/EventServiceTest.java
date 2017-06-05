package at.ac.tuwien.inso.sepm.ticketline.server.service.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.EventFilterTestDataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventServiceTest {
    @Autowired
    private EventService eventService;
    @Autowired
    private EventFilterTestDataGenerator eventFilterTestDataGenerator;

    @Test
    public void canFindTopTenEvents() {
        eventFilterTestDataGenerator.generateAllData(true);
        Map<Integer, Event> map = eventService.getTopTen(EventCategory.NO_CATEGORY, -1);

        Assert.assertTrue(map.containsKey(5));
        Assert.assertTrue(map.containsKey(2));
    }
}
