package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "ReservationDTO", description = "A reservation DTO for reservations via rest")
public class ReservationDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id of the reservation")
    private UUID id;

    @ApiModelProperty(required = true, readOnly = true, name = "The first name of the customer")
    private String customerFirstName;

    @ApiModelProperty(required = true, readOnly = true, name = "The last name of the customer")
    private String customerLastName;

    //@ApiModelProperty(required = true, readOnly = true, name = "The set of tickets in this reservation");
    //annotations are not allowed here ??
    private Set<TicketDTO> tickets;
}
