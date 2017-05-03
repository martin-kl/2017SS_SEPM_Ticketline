package at.ac.tuwien.inso.sepm.ticketline.rest.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PerformanceDTO", description = "A performance DTO for performance entries via rest")
public class PerformanceDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

    @ApiModelProperty(required = true, readOnly = true, name = "The start time of the performance")
    private Instant startTime;

    @ApiModelProperty(required = true, readOnly = true, name = "The end time of the performance")
    private Instant endTime;

    @ApiModelProperty(required = true, readOnly = true, name = "The default price for tickets in this performance")
    private BigDecimal defaultPrice;

    @ApiModelProperty(required = true, readOnly = true, name = "The location of the performance")
    private LocationDTO location;

    @ApiModelProperty(required = true, readOnly = true, name = "The list of tickets available for this performance")
    private List<TicketDTO> tickets;
}
