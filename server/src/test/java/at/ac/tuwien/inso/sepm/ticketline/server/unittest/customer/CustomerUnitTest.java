package at.ac.tuwien.inso.sepm.ticketline.server.unittest.customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.server.unittest.customer.base.BaseCustomerUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class CustomerUnitTest extends BaseCustomerUnitTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerUnitTest.class);

    //private CustomerRepository customerRepository;
    @MockBean
    private CustomerService customerService;


    /**
     * This test tries to insert a valid customer;
     */
    @Test
    public void createValidCustomer() {
        LOGGER.info("insert a new valid user with name = \"Maximilian Muster\"");
        Customer cust = new Customer();
        cust.setName("Maximilian Muster");
        customerService.save(cust);

        List<Customer> list = customerService.findAll();
        for(Customer tempCust : list) {
            LOGGER.error("id="+tempCust.getId()+", name="+tempCust.getName());
            System.out.println("id="+tempCust.getId()+", name="+tempCust.getName());
        }

        assert(list.contains(cust));
    }
/*
    @Test
    public void findAllCustomers() {

    }
 */
}
