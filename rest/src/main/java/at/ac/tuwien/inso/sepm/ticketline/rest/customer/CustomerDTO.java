package at.ac.tuwien.inso.sepm.ticketline.rest.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@ApiModel(value = "CustomerDTO", description = "A customer DTO for customers via rest")
public class CustomerDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;


    //TODO read only = true here?

    @ApiModelProperty(required = true, readOnly = true, name = "The name of the customer")
    private String name;

}