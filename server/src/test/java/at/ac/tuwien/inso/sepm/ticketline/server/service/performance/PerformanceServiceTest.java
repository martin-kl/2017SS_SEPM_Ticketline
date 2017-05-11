package at.ac.tuwien.inso.sepm.ticketline.server.service.performance;


import at.ac.tuwien.inso.sepm.ticketline.server.TestDataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatTicket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerformanceServiceTest {

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private PerformanceService performanceService;

    @Before
    public void setUpTest() {
        testDataGenerator.generateAllData(true);
    }

    @Test(expected = NotFoundException.class)
    public void findOneShouldFail() {
        performanceService.findOne(new UUID(0, 0));
    }

    @Test
    public void findAllTicketsToPerformanceIDShouldSucceed(){
        List<Ticket> tickets = performanceService.findAllTicketsToPerformanceID(
            testDataGenerator
                .getPerformances()
                .get(0)
                .getId()
        );

        Performance performance = testDataGenerator.getPerformances().get(0);
        log.info("alle tickets zu performance " + performance + " : " + performance.getTickets());


        List<SeatTicket> expectedTickets = testDataGenerator.getTickets();
        log.info("performance: " + testDataGenerator.getPerformances().get(0));
        log.info("TL von service: " + tickets);
        log.info("TL expected: " + expectedTickets);
        Assert.assertTrue(tickets.containsAll(expectedTickets) && expectedTickets.containsAll(tickets));
    }

}
