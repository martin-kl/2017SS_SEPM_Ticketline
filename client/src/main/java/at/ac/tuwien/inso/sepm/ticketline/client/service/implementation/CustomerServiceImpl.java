package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ExceptionWithDialog;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import java.time.LocalDate;
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
    public List<CustomerDTO> search(String query, int page) throws DataAccessException {
        if (query == null || query.equals("")) { return customerRestClient.findAll(page); }
        return customerRestClient.search(query, page);
    }

    @Override
    public CustomerDTO findOne(UUID id) throws DataAccessException {
        return customerRestClient.findOne(id);
    }

    @Override
    public CustomerDTO save(CustomerDTO customer) throws ExceptionWithDialog {

        EmailValidator ev = new EmailValidator();
        if(customer.getEmail() == null || customer.getEmail().equals("") || ! ev.isValid(customer.getEmail(), null)) {
             log.error(
                "Error during saving of customer with id {}, email address is not well formed, email is \"{}\"",
                customer.getId(), customer.getEmail());
            throw new ValidationException("customer.error.email");
        }else  if(customer.getFirstName().length() < 1) {
            log.error(
                "Error during saving of customer with id {}, first name of customer is not even 1 char long, that is not valid!",
                customer.getId());
            throw new ValidationException("customer.error.firstName");
        }else if(customer.getLastName().length() < 1) {
            log.error(
                "Error during saving of customer with id {}, last name of customer is not even 1 char long, that is not valid!",
                customer.getId());
            throw new ValidationException("customer.error.lastName");
         }else if ((customer.getFirstName().matches(".*\\d+.*")) || (customer.getLastName().matches(".*\\d+.*"))) {
             log.error(
                "Error during saving of customer with id {}, either first name or last name contains digits",
                customer.getId());
            throw new ValidationException("customer.error.digitsInName");
        }else if(customer.getAddress().length() < 5) {
            log.error(
                "Error during saving of customer with id {}, address is not even 5 char long, that is not valid!",
                customer.getId());
            throw new ValidationException("customer.error.address");
        }else if(customer.getBirthday() == null) {
            throw new ValidationException("customer.error.birthday");
        }else if(customer.getBirthday().isAfter(LocalDate.now())) {
             log.error(
                "Error during saving of customer with id {}, entered birthday {} is in the future!",
                customer.getId(), customer.getBirthday());
            throw new ValidationException("customer.error.birthday");
        }

        return customerRestClient.save(customer);
    }
}
