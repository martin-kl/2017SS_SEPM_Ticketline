package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customer")
@Api(value = "customer")
public class CustomerEndpoint {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerEndpoint(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of customer")
    public List<CustomerDTO> findAll() {
        return customerMapper.fromEntity(customerService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one Customer by id")
    public CustomerDTO find(@PathVariable UUID id) {
        return customerMapper.fromEntity(customerService.findOne(id));
    }

    @RequestMapping(value="/search/{query}", method = RequestMethod.GET)
    @ApiOperation(value = "Get searched customers")
    public List<CustomerDTO> search(@PathVariable String query) {
        return customerMapper.fromEntity(customerService.search(query));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Save a customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerMapper.fromDTO(customerDTO);
        customer = customerService.save(customer);
        return customerMapper.fromEntity(customer);
    }
}
