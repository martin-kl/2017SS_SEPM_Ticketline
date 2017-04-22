package at.ac.tuwien.inso.sepm.ticketline.server.unittest.customer;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerUnitTest {
    @Autowired
    private CustomerService customerService;

    private static final String CUSTOMER_NAME = "Maximilian Muster";

    @Test
    public void createValidCustomer() {
        Customer cust = new Customer();
        cust.setName(CUSTOMER_NAME);
        customerService.save(cust);
        List<Customer> list = customerService.findAll();
        assertTrue(list.contains(cust));
    }
}
