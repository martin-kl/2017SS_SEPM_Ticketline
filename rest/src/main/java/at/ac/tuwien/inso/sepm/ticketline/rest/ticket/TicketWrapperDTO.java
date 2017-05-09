package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@ApiModel(value = "TicketWrapperDTO", description = "wrapper for tickets with status information")
public class TicketWrapperDTO {

    @ApiModelProperty(readOnly = true, name = "Wrapped Ticket")
    private TicketDTO ticket;

    @ApiModelProperty(readOnly = true, name = "Status belonging to the Ticket")
    private TicketStatus status;
}
