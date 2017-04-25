package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

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
@ApiModel(value = "TicketDTO", description = "default DTO for Ticket Entity")
public class TicketDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

}