package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class CustomerMapperTestContextConfiguration {

    }

    @Autowired
    //@SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private CustomerMapper customerMapper;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final String CUSTOMER_FIRST_NAME = "Maximilian";
    private static final String CUSTOMER_LAST_NAME = "Muster";

    private static final String CUSTOMER_EMAIL = "max.muster@muster.com";
    private static final String CUSTOMER_ADDRESS = "Musterstraße 14, 1010 Wien";
    private static final LocalDate CUSTOMER_BIRTHDAY = LocalDate.now();


    @Test
    public void shouldMapCustomerToCustomerDTO() {
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setFirstName(CUSTOMER_FIRST_NAME);
        customer.setLastName(CUSTOMER_LAST_NAME);
        customer.setEmail(CUSTOMER_EMAIL);
        customer.setAddress(CUSTOMER_ADDRESS);
        customer.setBirthday(CUSTOMER_BIRTHDAY);


        CustomerDTO customerDTO = customerMapper.fromEntity(customer);
        assertThat(customerDTO).isNotNull();
        assertEquals(customerDTO.getId(), CUSTOMER_ID);
        assertEquals(customerDTO.getFirstName(), CUSTOMER_FIRST_NAME);
        assertEquals(customerDTO.getLastName(), CUSTOMER_LAST_NAME);
        assertEquals(customerDTO.getEmail(), CUSTOMER_EMAIL);
        assertEquals(customerDTO.getAddress(), CUSTOMER_ADDRESS);
        assertEquals(customerDTO.getBirthday(), CUSTOMER_BIRTHDAY);
    }

    @Test
    public void shouldMapCustomerDTOtoCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(CUSTOMER_ID);
        customerDTO.setFirstName(CUSTOMER_FIRST_NAME);
        customerDTO.setLastName(CUSTOMER_LAST_NAME);
        customerDTO.setBirthday(CUSTOMER_BIRTHDAY);
        customerDTO.setAddress(CUSTOMER_ADDRESS);
        customerDTO.setEmail(CUSTOMER_EMAIL);

        assertThat(customerMapper).isNotNull();

        Customer customer = customerMapper.fromDTO(customerDTO);
        assertThat(customer).isNotNull();
        assertEquals(customer.getId(), CUSTOMER_ID);
        assertEquals(customer.getFirstName(), CUSTOMER_FIRST_NAME);
        assertEquals(customer.getLastName(), CUSTOMER_LAST_NAME);
        assertEquals(customer.getAddress(), CUSTOMER_ADDRESS);
        assertEquals(customer.getEmail(), CUSTOMER_EMAIL);
        assertEquals(customer.getBirthday(), CUSTOMER_BIRTHDAY);
    }

}
