package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "TicketHistoryDTO", description = "default DTO for Ticket History Entity")
public class TicketHistoryDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

    @ApiModelProperty(readOnly = true, name = "Status of the ticket. Can either be bought, reserved or cancelled")
    private TicketStatus status;

}