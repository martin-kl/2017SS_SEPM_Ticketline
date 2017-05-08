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
    private UUID id;

    @ApiModelProperty(readOnly = true, name = "Status of the ticket. Can either be bought, reserved or cancelled")
    private TicketStatus status;

    @ApiModelProperty(readOnly = true, name = "Customer of the transaction")
    private CustomerDTO customer;

    @ApiModelProperty(readOnly = true, name = "Reserved Tickets")
    private List<TicketDTO> tickets;

    //TODO hier auch Location mitladen? - ist eigentlich unnötig weil beim suchen kann ich auch einfach einen String nehmen und den der Suche übergeben oder?
}