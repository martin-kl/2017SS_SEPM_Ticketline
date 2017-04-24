package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

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
    private static final String CUSTOMER_NAME = "Maximilian Muster";

    @Test
    public void shouldMapCustomerToCustomerDTO() {
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setName(CUSTOMER_NAME);

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        assertThat(customerDTO).isNotNull();
        assertThat(customerDTO.getId()).isEqualTo(CUSTOMER_ID);
        assertThat(customerDTO.getName()).isEqualTo(CUSTOMER_NAME);
    }
    @Test
    public void shouldMapCustomerDTOtoCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(CUSTOMER_ID);
        customerDTO.setName(CUSTOMER_NAME);

        assertThat(customerMapper).isNotNull();

        Customer customer = customerMapper.customerDTOtoCustomer(customerDTO);
        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(CUSTOMER_ID);
        assertThat(customer.getName()).isEqualTo(CUSTOMER_NAME);
    }

}
