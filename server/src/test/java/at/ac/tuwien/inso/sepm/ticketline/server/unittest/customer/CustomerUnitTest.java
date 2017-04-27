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
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerUnitTest {
    @Autowired
    private CustomerService customerService;

    private static final String CUSTOMER_FIRST_NAME = "Maximilian";
    private static final String CUSTOMER_LAST_NAME = "Muster";
    private static final String CUSTOMER_NAME_INVALID = "Ma";

    @Test
    public void createValidCustomer() {
        Customer customer = new Customer();
        customer.setFirstName(CUSTOMER_FIRST_NAME);
        customer.setLastName(CUSTOMER_LAST_NAME);
        customerService.save(customer);
        List<Customer> list = customerService.findAll();
        assertTrue(list.contains(customer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCustomerWithNameTooShort() {
        Customer customer = new Customer();
        customer.setFirstName(CUSTOMER_NAME_INVALID);
        customer.setLastName(CUSTOMER_LAST_NAME);
        customerService.save(customer);
    }

    @Test
    public void editCustomer() {
        Customer customer = new Customer();
        customer.setFirstName(CUSTOMER_FIRST_NAME);
        customer.setLastName(CUSTOMER_LAST_NAME);

        customerService.save(customer);
        UUID custID = customer.getId();
        assertEquals(customer.getFirstName(), CUSTOMER_FIRST_NAME);
        assertEquals(customer.getLastName(), CUSTOMER_LAST_NAME);

        //customer is saved, now try to edit the customer
        customer.setFirstName("New");
        customer.setLastName("Name");
        customerService.save(customer);

        assertEquals(customer.getFirstName(), "New");
        assertEquals(customer.getLastName(), "Name");
        assertEquals(customer.getId(), custID);

        List<Customer> listAfterEdit = customerService.findAll();
        assertTrue(listAfterEdit.contains(customer));
    }

    @Test
    public void testFindAll() {
        List<Customer> listBeforeInsert = customerService.findAll();
        //add a customer
        Customer customer = new Customer();
        customer.setFirstName(CUSTOMER_FIRST_NAME);
        customer.setLastName(CUSTOMER_LAST_NAME);
        customerService.save(customer);

        List<Customer> listAfterInsert = customerService.findAll();
        assertEquals(listBeforeInsert.size() + 1, listAfterInsert.size());
    }
}
