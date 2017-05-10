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
@ApiModel(value = "SectorTicketDTO", description = "sector DTO for Ticket Entity")
public class SectorTicketDTO extends TicketDTO {
    @ApiModelProperty(name = "Sector to the ticket")
    private SectorDTO sector;
}
