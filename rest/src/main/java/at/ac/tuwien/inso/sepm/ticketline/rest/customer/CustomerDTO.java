package at.ac.tuwien.inso.sepm.ticketline.rest.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "CustomerDTO", description = "A customer DTO for customers via rest")
public class CustomerDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

    @ApiModelProperty(required = true, readOnly = true, name = "The first name of the customer")
    private String firstName;

    @ApiModelProperty(required = true, readOnly = true, name = "The last name of the customer")
    private String lastName;

    @ApiModelProperty(required = true, readOnly = true, name = "The email of the customer")
    private String email;

    @ApiModelProperty(required = true, readOnly = true, name = "The address of the customer")
    private String address;

    @ApiModelProperty(required = true, readOnly = true, name = "The birthday of the customer")
    private LocalDate birthday;
}