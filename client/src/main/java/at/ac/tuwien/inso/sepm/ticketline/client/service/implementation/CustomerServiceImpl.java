package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        if(customer.getName().length() < 3) {
            //TODO show error message and stay in window

        }else {
            customerRestClient.save(customer);
        }
        return customer;
    }
}
