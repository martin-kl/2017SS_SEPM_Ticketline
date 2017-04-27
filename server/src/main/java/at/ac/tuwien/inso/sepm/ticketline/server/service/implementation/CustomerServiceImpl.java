package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findOne(UUID id) {
        return customerRepository.findOne(id);
    }

    @Override
    public Customer save(Customer customer) {
        if(customer.getFirstName().length() < 3) {
            LOGGER.error("first name of customer has less then 3 characters - that is not valid");
            throw new IllegalArgumentException("first name of customer must have at least 3 characters");
        }
        if(customer.getLastName().length() < 3) {
            LOGGER.error("last name of customer has less then 3 characters - that is not valid");
            throw new IllegalArgumentException(
                "last name of customer must have at least 3 characters");
        }
        return customerRepository.save(customer);
    }
}
