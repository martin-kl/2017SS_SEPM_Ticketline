package at.ac.tuwien.inso.sepm.ticketline.server.service.performance;


import at.ac.tuwien.inso.sepm.ticketline.server.TestDataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerformanceServiceTest {
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private SeatLocationRepository seatLocationRepository;
    @Autowired
    private SeatTicketRepository seatTicketRepository;
    @Autowired
    private PriceCategoryRepository priceCategoryRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private PerformanceService performanceService;

    @Before
    public void setUpTest() {
        testDataGenerator.generateAllData(true);
    }

    @Test
    public void findOneShouldSucceed() {
        //log.info("perf: " + performance);
        //Performance found = performanceService.findOne(performance.getId());
        //assertEquals(performance,found);
    }

    @Test(expected = NotFoundException.class)
    public void findOneShouldFail() {
        performanceService.findOne(new UUID(0, 0));
    }

}
