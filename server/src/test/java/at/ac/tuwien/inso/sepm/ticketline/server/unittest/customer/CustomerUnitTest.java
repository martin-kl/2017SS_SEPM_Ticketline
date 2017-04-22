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

    private static final String CUSTOMER_NAME_VALID = "Maximilian Muster";
    private static final String CUSTOMER_NAME_INVALID = "Ma";

    @Test
    public void createValidCustomer() {
        Customer customer = new Customer();
        customer.setName(CUSTOMER_NAME_VALID);
        customerService.save(customer);
        List<Customer> list = customerService.findAll();
        assertTrue(list.contains(customer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCustomerWithNameTooShort() {
        Customer customer = new Customer();
        customer.setName(CUSTOMER_NAME_INVALID);
        customerService.save(customer);
    }

    @Test
    public void editCustomer() {
        Customer customer = new Customer();
        customer.setName(CUSTOMER_NAME_VALID);
        customerService.save(customer);
        Long custID = customer.getId();
        assertEquals(customer.getName(), CUSTOMER_NAME_VALID);

        //customer is saved, now try to edit the customer
        customer.setName("New Name");
        customerService.save(customer);
        assertEquals(customer.getName(), "New Name");
        assertEquals(customer.getId(), custID);

        List<Customer> listAfterEdit = customerService.findAll();
        assertTrue(listAfterEdit.contains(customer));
    }

    @Test
    public void testFindAll() {
        List<Customer> listBeforeInsert = customerService.findAll();
        //add a customer
        Customer customer = new Customer();
        customer.setName(CUSTOMER_NAME_VALID);
        customerService.save(customer);

        List<Customer> listAfterInsert = customerService.findAll();
        assertEquals(listBeforeInsert.size() + 1, listAfterInsert.size());
    }
}
