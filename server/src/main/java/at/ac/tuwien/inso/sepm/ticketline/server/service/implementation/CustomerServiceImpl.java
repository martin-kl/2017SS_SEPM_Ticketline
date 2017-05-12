package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.BadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ConflictException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.util.ValidationHelper;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll(Pageable pageable) {
        return customerRepository.findAllOrderByLastName(pageable);
    }

    @Override
    public Customer findOne(UUID id) {
        return customerRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Customer save(Customer customer) {
        try {
            return customerRepository.save(customer);
        } catch (TransactionSystemException e) {
            throw new BadRequestException(ValidationHelper.getErrorMessages(e).toString());
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException();
        }
    }

    @Override
    public List<Customer> search(String query) {
        List<Customer> customers = customerRepository.findAll();
        List<Customer> result = new ArrayList<>();
        String[] searchWords = query.split(" ");
        for (Customer customer : customers) {
            if (eachSearchWordMatchesOneColumn(customer, searchWords)) {
                result.add(customer);
            }
        }
        return result;
    }

    private boolean eachSearchWordMatchesOneColumn(Customer customer, String[] searchWords) {
        for (String searchWord : searchWords) {
            searchWord = searchWord.toLowerCase();
            if (!(customer.getFirstName().toLowerCase().contains(searchWord)
                || customer.getLastName().toLowerCase().contains(searchWord)
                || customer.getEmail().toLowerCase().contains(searchWord)
                || customer.getAddress().toLowerCase().contains(searchWord))) {
                return false;
            }
        }
        return true;
    }

}
