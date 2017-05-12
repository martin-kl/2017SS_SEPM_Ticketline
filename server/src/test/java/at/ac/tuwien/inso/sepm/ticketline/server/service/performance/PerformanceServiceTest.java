package at.ac.tuwien.inso.sepm.ticketline.server.service.performance;


import at.ac.tuwien.inso.sepm.ticketline.server.TestDataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatTicket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.misc.Perf;

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
    public void setUpTest() {
        testDataGenerator.generateAllData(true);
    }

    @Test(expected = NotFoundException.class)
    public void findOneShouldFail() {
        performanceService.findOne(new UUID(0, 0));
    }

    /*
    @Test
    public void findAllTicketsToPerformanceIDShouldSucceed(){
        //performance 0
        List<Ticket> tickets = performanceService.findAllTicketsToPerformanceID(
            testDataGenerator
                .getPerformances()
                .get(0)
                .getId()
        );

        Performance performance = testDataGenerator.getPerformances().get(0);
        List<SeatTicket> expectedTickets = new LinkedList<>(testDataGenerator.getTickets());
        expectedTickets.removeIf(t -> !t.getPerformance().getId().equals(performance.getId()));
        Assert.assertTrue(tickets.containsAll(expectedTickets) && expectedTickets.containsAll(tickets));


        //performance 1
        List<Ticket> tickets1 = performanceService.findAllTicketsToPerformanceID(
            testDataGenerator
                .getPerformances()
                .get(1)
                .getId()
        );
        Performance performance1 = testDataGenerator.getPerformances().get(1);
        List<SeatTicket> expectedTickets1 = new LinkedList<>(testDataGenerator.getTickets());
        expectedTickets1.removeIf(t -> !t.getPerformance().getId().equals(performance1.getId()));
        Assert.assertTrue(tickets1.containsAll(expectedTickets1) && expectedTickets1.containsAll(tickets1));
    }

    @Test
    public void addStatusToTicketsShouldSucceed(){

    }*/

}
