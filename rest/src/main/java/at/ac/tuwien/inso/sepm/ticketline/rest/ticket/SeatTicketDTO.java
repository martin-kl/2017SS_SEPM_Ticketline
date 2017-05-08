package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@ApiModel(value = "SeatTicketDTO", description = "seat DTO for Ticket Entity")
public class SeatTicketDTO extends TicketDTO {

    @ApiModelProperty(readOnly = true, name = "The seat for this ticket")
    private SeatDTO seat;

}
