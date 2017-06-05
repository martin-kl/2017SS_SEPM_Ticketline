package at.ac.tuwien.inso.sepm.ticketline.server.service.customer;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    private static final String CUSTOMER_FIRST_NAME = "Maximilian";
    private static final String CUSTOMER_LAST_NAME = "Muster";
    private static final String CUSTOMER_NAME_INVALID = "";

    private static final String CUSTOMER_EMAIL = "max.muster@muster.com";
    private static final String CUSTOMER_EMAIL_INVALID = "max.mustermuster.com";
    private static final String CUSTOMER_ADDRESS = "Musterstraße 14, 1010 Wien";
    private static final LocalDate CUSTOMER_BIRTHDAY = LocalDate.now();
    private Customer unsavedCustomer;

    private static Pageable pageable = new PageRequest(0, 10000);

    @Before
    public void setUpCustomer() {
        customerRepository.deleteAll();
        unsavedCustomer = new Customer();
        unsavedCustomer.setFirstName(CUSTOMER_FIRST_NAME);
        unsavedCustomer.setLastName(CUSTOMER_LAST_NAME);
        unsavedCustomer.setEmail(CUSTOMER_EMAIL);
        unsavedCustomer.setAddress(CUSTOMER_ADDRESS);
        unsavedCustomer.setBirthday(CUSTOMER_BIRTHDAY);
    }

    @Test
    public void canSaveValidCustomer() {
        Customer returnedFromSave = customerService.save(unsavedCustomer);
        List<Customer> list = customerService.findAll(pageable);
        assertTrue(list.contains(unsavedCustomer));
        assertNotNull(unsavedCustomer.getId());
        assertTrue(returnedFromSave == unsavedCustomer);
    }

    @Test(expected = BadRequestException.class)
    public void createCustomerWithFirstNameTooShort() {
        unsavedCustomer.setFirstName(CUSTOMER_NAME_INVALID);
        customerService.save(unsavedCustomer);
    }

    @Test(expected = BadRequestException.class)
    public void createCustomerWithLastNameTooShort() {
        unsavedCustomer.setLastName(CUSTOMER_NAME_INVALID);
        customerService.save(unsavedCustomer);
    }

    @Test(expected = BadRequestException.class)
    public void createCustomerWithInvalidMail() {
        unsavedCustomer.setEmail(CUSTOMER_EMAIL_INVALID);
        customerService.save(unsavedCustomer);
    }

    @Test(expected = NotFoundException.class)
    public void tryToFindNonExistingCustomer() {
        customerService.findOne(new UUID(1, 2));
    }

    @Test
    public void canSaveAndEditCustomerFindAllAndFindOne() {
        List<Customer> listBeforeInsert = customerService.findAll(pageable);

        //save
        unsavedCustomer = customerService.save(unsavedCustomer);
        UUID custID = unsavedCustomer.getId();

        //edit
        Customer editedVersion = customerService.findOne(custID);
        assertEquals(editedVersion, unsavedCustomer);
        editedVersion.setFirstName("New");
        editedVersion.setLastName("Name");
        editedVersion.setEmail("max.neu@neu.at");
        customerService.save(editedVersion);

        //check edits worked
        editedVersion = customerService.findOne(custID);
        assertEquals(editedVersion.getFirstName(), "New");
        assertEquals(editedVersion.getLastName(), "Name");
        assertEquals(editedVersion.getAddress(), CUSTOMER_ADDRESS);
        assertEquals(editedVersion.getEmail(), "max.neu@neu.at");
        assertEquals(editedVersion.getBirthday(), CUSTOMER_BIRTHDAY);
        assertEquals(editedVersion.getId(), custID);

        //check is in list
        List<Customer> listAfterInsert = customerService.findAll(pageable);
        assertTrue(listAfterInsert.contains(editedVersion));
        assertEquals(listBeforeInsert.size()+1, listAfterInsert.size());
    }

    @Test
    public void canFuzzySearchForCustomersEmail() {
        customerRepository.save(unsavedCustomer);
        List<Customer> result = customerService.search("@mus", pageable);
        assertTrue(result.contains(unsavedCustomer));

        result = customerService.search("straße", pageable);
        assertTrue(result.contains(unsavedCustomer));

        result = customerService.search("milian straße 1010", pageable);
        assertTrue(result.contains(unsavedCustomer));

        result = customerService.search("max", pageable);
        assertTrue(result.contains(unsavedCustomer));

        result = customerService.search("@mus Peter", pageable);
        assertFalse(result.contains(unsavedCustomer));
    }


}
