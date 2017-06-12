package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "DetailedTicketTransactionDTO", description = "a detailed DTO for a transaction")
public class DetailedTicketTransactionDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(readOnly = true, name = "Status of the ticket. Can either be bought, reserved or cancelled")
    private TicketStatus status;

    @ApiModelProperty(readOnly = true, name = "Customer of the transaction")
    private CustomerDTO customer;

    @ApiModelProperty(readOnly = true, name = "Tickets for this transaction")
    private List<? extends TicketDTO> tickets;

    @ApiModelProperty(readOnly = true, name = "Performance Name")
    private String performanceName;
}
