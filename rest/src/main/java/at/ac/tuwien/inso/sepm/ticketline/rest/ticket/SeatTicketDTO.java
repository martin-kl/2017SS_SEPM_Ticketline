package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "SeatTicketDTO", description = "seat DTO for Ticket Entity")
@EqualsAndHashCode(callSuper = true)
public class SeatTicketDTO extends TicketDTO {
    @ApiModelProperty(name = "Seat to the ticket")
    private SeatDTO seat;
}
