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
        return customerMapper.customerToCustomerDTO(customerService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one Customer by id")
    public CustomerDTO find(@PathVariable UUID id) {
        return customerMapper.customerToCustomerDTO(customerService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "create a new customer")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerMapper.customerDTOtoCustomer(customerDTO);
        customer = customerService.save(customer);
        return customerMapper.customerToCustomerDTO(customer);
    }

}