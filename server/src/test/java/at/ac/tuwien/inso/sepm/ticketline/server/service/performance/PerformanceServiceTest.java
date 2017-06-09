package at.ac.tuwien.inso.sepm.ticketline.server.service.performance;


import at.ac.tuwien.inso.sepm.ticketline.server.TestDataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
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
    public void beforeTest() {
        testDataGenerator.generateAllData(true);
    }

    @After
    public void afterTest(){
        testDataGenerator.emptyAllRepositories();
    }

    @Test(expected = NotFoundException.class)
    public void findOneShouldFail() {
        performanceService.findOne(new UUID(0, 0));
    }

    @Test
    public void findAllTicketsToPerformanceIDShouldSucceed(){
        //performance 0
        List<Ticket> tickets = performanceService.findAllTicketsToPerformanceID(
            testDataGenerator
                .getPerformances()
                .get(0)
                .getId()
        );

        //Performance performance = testDataGenerator.getPerformances().get(0);
        List<Ticket> expectedTickets = new LinkedList<>(testDataGenerator.getTicketsToPerf0());
        Assert.assertTrue(tickets.containsAll(expectedTickets) && expectedTickets.containsAll(tickets));


        //performance 1
        List<Ticket> tickets1 = performanceService.findAllTicketsToPerformanceID(
            testDataGenerator
                .getPerformances()
                .get(1)
                .getId()
        );
        //Performance performance1 = testDataGenerator.getPerformances().get(1);
        List<Ticket> expectedTickets1 = new LinkedList<>(testDataGenerator.getTicketsToPerf1());
        Assert.assertTrue(tickets1.containsAll(expectedTickets1) && expectedTickets1.containsAll(tickets1));
    }

}
