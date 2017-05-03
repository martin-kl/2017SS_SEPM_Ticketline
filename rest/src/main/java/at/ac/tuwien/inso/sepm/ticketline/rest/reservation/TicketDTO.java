package at.ac.tuwien.inso.sepm.ticketline.rest.reservation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "TicketDTO", description = "A ticket DTO for a reservation via rest")
public class TicketDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id of the ticket")
    private UUID id;

    @ApiModelProperty(required = true, readOnly = true, name = "The price for the ticket at the time of the reservation")
    private Long price;

    @ApiModelProperty(required = true, readOnly = true, name = "The id of the performance for this ticket")
    private UUID performanceID;

    @ApiModelProperty(required = true, readOnly = true, name = "The id of the location for this ticket")
    private UUID locationID;

    @ApiModelProperty(required = true, readOnly = true, name = "The name of the location of the performance")
    private String locationName;

    @ApiModelProperty(required = true, readOnly = true, name = "The status of this reservations. If the parameter is TRUE, the ticket has been bought, else it has been reserved")
    private Boolean bought;

}
