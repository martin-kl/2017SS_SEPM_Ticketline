package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
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

        EmailValidator ev = new EmailValidator();
        if(! ev.isValid(customer.getEmail(), null)) {
             log.error(
                "Error during saving of customer with id {}, email address is not well formed, email is \"{}\"",
                customer.getId(), customer.getEmail());
            throw new IllegalArgumentException("Error during saving of customer with id {}, email is not well formed");
        }else  if(customer.getFirstName().length() < 1) {
            log.error(
                "Error during saving of customer with id {}, first name of customer is not even 1 char long, that is not valid!",
                customer.getId());
            throw new IllegalArgumentException("Error during saving of customer with id {}, first name of customer is not even 1 char long, that is not valid!");
        }else if(customer.getLastName().length() < 1) {
            log.error(
                "Error during saving of customer with id {}, last name of customer is not even 1 char long, that is not valid!",
                customer.getId());
            throw new IllegalArgumentException(
                "Error during saving of customer with id {}, last name of customer is not even 1 char long, that is not valid!");
        }else if(customer.getAddress().length() < 5) {
            log.error(
                "Error during saving of customer with id {}, address is not even 5 char long, that is not valid!",
                customer.getId());
            throw new IllegalArgumentException(
                "Error during saving of customer with id {}, address is not even 5 char long, that is not valid!");
        }else {
            customerRestClient.save(customer);
        }
        return customer;
    }
}
