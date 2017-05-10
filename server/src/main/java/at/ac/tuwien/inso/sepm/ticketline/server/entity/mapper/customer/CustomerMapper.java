package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer fromDTO(CustomerDTO customerDTO);

    CustomerDTO fromEntity(Customer customer);

    List<CustomerDTO> fromEntity(List<Customer> all);
}
