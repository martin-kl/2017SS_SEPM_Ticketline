package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidatorContext;
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
        return customerRepository.findAll(new Sort("lastName"));
    }

    @Override
    public Customer findOne(UUID id) {
        return customerRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Customer save(Customer customer) {
        try {
            return customerRepository.save(customer);
        } catch(TransactionSystemException e) {
            //TODO
            //wie bekommen wir hier eine bessere Meldung aus Hibernate raus?
            //intern gibt es da eine bessere Exception wo der genau Fehler herausgeht
            //aber nach außen bekomm ich nur diese
            throw new BadRequestException(e.getMessage());
        }
    }
}
