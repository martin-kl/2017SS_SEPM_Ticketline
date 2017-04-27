package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRestClient customerRestClient;

    public CustomerServiceImpl(CustomerRestClient customerRestClient) {
        this.customerRestClient = customerRestClient;
    }

    @Override
    public List<CustomerDTO> findAll() throws DataAccessException {
        return customerRestClient.findAll();
    }

    @Override
    public CustomerDTO findOne(UUID id) throws DataAccessException {
        return customerRestClient.findOne(id);
    }

    @Override
    public CustomerDTO save(CustomerDTO customer) throws DataAccessException {
        if(customer.getFirstName().length() < 3) {
            log.error(
                "Error during saving of customer with id {}, first name of customer is not even 3 char long, that is not valid!",
                customer.getId());
            throw new IllegalArgumentException("Error during saving of customer with id {}, first name of customer is not even 3 char long, that is not valid!");
        }else if(customer.getLastName().length() < 3) {
            log.error("Error during saving of customer with id {}, last name of customer is not even 3 char long, that is not valid!", customer.getId());
            throw new IllegalArgumentException("Error during saving of customer with id {}, last name of customer is not even 3 char long, that is not valid!");
        }else {
            customerRestClient.save(customer);
        }
        return customer;
    }
}
