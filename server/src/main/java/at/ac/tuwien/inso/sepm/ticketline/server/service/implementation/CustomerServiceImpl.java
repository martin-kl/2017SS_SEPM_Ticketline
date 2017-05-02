package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.ValidationHelper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll(new Sort("lastName"));
    }

    @Override
    public Customer findOne(UUID id) {
        return customerRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Customer save(Customer customer) {
        try {
            //TODO validate customer here too?
            return customerRepository.save(customer);
        } catch(TransactionSystemException e) {
            throw new BadRequestException(ValidationHelper.getErrorMessages(e).toString());
        }
    }
}
