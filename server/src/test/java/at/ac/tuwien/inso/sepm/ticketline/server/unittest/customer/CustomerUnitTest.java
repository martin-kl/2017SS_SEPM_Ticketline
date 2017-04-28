package at.ac.tuwien.inso.sepm.ticketline.server.unittest.customer;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import java.time.LocalDate;
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

    private static final String CUSTOMER_EMAIL = "max.muster@muster.com";
    private static final String CUSTOMER_ADDRESS = "Musterstraße 14, 1010 Wien";
    private static final LocalDate CUSTOMER_BIRTHDAY = LocalDate.now();

    @Test
    public void createValidCustomer() {
        Customer customer = new Customer();
        customer.setFirstName(CUSTOMER_FIRST_NAME);
        customer.setLastName(CUSTOMER_LAST_NAME);
        customer.setEmail(CUSTOMER_EMAIL);
        customer.setAddress(CUSTOMER_ADDRESS);
        customer.setBirthday(CUSTOMER_BIRTHDAY);

        customerService.save(customer);
        List<Customer> list = customerService.findAll();
        assertTrue(list.contains(customer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCustomerWithNameTooShort() {
        Customer customer = new Customer();
        customer.setFirstName(CUSTOMER_NAME_INVALID);
        customer.setEmail(CUSTOMER_EMAIL);
        customer.setAddress(CUSTOMER_ADDRESS);
        customer.setBirthday(CUSTOMER_BIRTHDAY);
        customer.setLastName(CUSTOMER_LAST_NAME);

        customerService.save(customer);
    }

    @Test
    public void editCustomer() {
        Customer customer = new Customer();
        customer.setFirstName(CUSTOMER_FIRST_NAME);
        customer.setLastName(CUSTOMER_LAST_NAME);
        customer.setAddress(CUSTOMER_ADDRESS);
        customer.setEmail(CUSTOMER_EMAIL);
        customer.setBirthday(CUSTOMER_BIRTHDAY);
        customer.setLastName(CUSTOMER_LAST_NAME);

        customerService.save(customer);
        UUID custID = customer.getId();
        assertEquals(customer.getFirstName(), CUSTOMER_FIRST_NAME);
        assertEquals(customer.getLastName(), CUSTOMER_LAST_NAME);
        assertEquals(customer.getEmail(), CUSTOMER_EMAIL);
        assertEquals(customer.getBirthday(), CUSTOMER_BIRTHDAY);
        assertEquals(customer.getAddress(), CUSTOMER_ADDRESS);

        //customer is saved, now try to edit the customer
        customer.setFirstName("New");
        customer.setLastName("Name");
        customer.setEmail("max.neu@neu.at");
        customerService.save(customer);

        assertEquals(customer.getFirstName(), "New");
        assertEquals(customer.getLastName(), "Name");
        assertEquals(customer.getAddress(), CUSTOMER_ADDRESS);
        assertEquals(customer.getEmail(), "max.neu@neu.at");
        assertEquals(customer.getBirthday(), CUSTOMER_BIRTHDAY);
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
        customer.setAddress(CUSTOMER_ADDRESS);
        customer.setEmail(CUSTOMER_EMAIL);
        customer.setBirthday(CUSTOMER_BIRTHDAY);
        customer.setLastName(CUSTOMER_LAST_NAME);

        customerService.save(customer);

        List<Customer> listAfterInsert = customerService.findAll();
        assertEquals(listBeforeInsert.size() + 1, listAfterInsert.size());
    }
}
