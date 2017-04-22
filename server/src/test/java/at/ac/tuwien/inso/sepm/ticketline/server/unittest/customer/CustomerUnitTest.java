package at.ac.tuwien.inso.sepm.ticketline.server.unittest.customer;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.server.unittest.customer.base.BaseCustomerUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerUnitTest extends BaseCustomerUnitTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerUnitTest.class);

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.service")
    @EnableJpaRepositories(basePackageClasses = CustomerRepository.class)
    public static class CustomerUnitTestContextConfiguration {
    }

    @Autowired
    private CustomerService customerService;

    private static final long CUSTOMER_ID = 1L;
    private static final String CUSTOMER_NAME = "Maximilian Muster";

    /**
     * This test tries to insert a valid customer;
     */
    @Test
    public void createValidCustomer() {
        /*
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        LOGGER.info("insert a new valid user with name = \"Maximilian Muster\"");
        Customer cust = new Customer();
        cust.setName(CUSTOMER_NAME);
        customerRepository.save(cust);
        LOGGER.info("id of customer after saving = "+cust.getId()+", name ="+cust.getName());

        List<Customer> list = customerRepository.findAll();
        LOGGER.info("size of list = "+list.size());

        for(Customer tempCust : list) {
            LOGGER.info("id="+tempCust.getId()+", name="+tempCust.getName());
            System.out.println("id="+tempCust.getId()+", name="+tempCust.getName());
            System.err.println("id="+tempCust.getId()+", name="+tempCust.getName());
        }
*/

        LOGGER.info("");
        LOGGER.info("insert a new valid user with name over customerService Class, name = \"Maximilian Muster\"");
        Customer cust = new Customer();
        cust.setName(CUSTOMER_NAME);
        customerService.save(cust);
        LOGGER.info("id of customer after saving = "+cust.getId()+", name ="+cust.getName());

        List<Customer> list = customerService.findAll();
        LOGGER.info("size of list = "+list.size());

        for(Customer tempCust : list) {
            LOGGER.info("id="+tempCust.getId()+", name="+tempCust.getName());
            System.out.println("id="+tempCust.getId()+", name="+tempCust.getName());
            System.err.println("id="+tempCust.getId()+", name="+tempCust.getName());
        }

        assertTrue(list.contains(cust));
    }
/*
    @Test
    public void findAllCustomers() {

    }
 */
}
