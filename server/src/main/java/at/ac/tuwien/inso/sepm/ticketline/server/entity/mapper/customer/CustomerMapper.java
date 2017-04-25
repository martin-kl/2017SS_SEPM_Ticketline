package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerMapper {

    Customer fromDTO(CustomerDTO customerDTO);

    CustomerDTO fromEntity(Customer one);

}