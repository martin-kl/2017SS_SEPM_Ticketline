package at.ac.tuwien.inso.sepm.ticketline.rest.performance;


import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketWrapperDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "DetailedPerformanceDTO", description = "a detailed DTO for a performance")
public class DetailedPerformanceDTO extends PerformanceDTO {
    @ApiModelProperty(readOnly = true, name = "Wrapped Ticketlist")
    private List<TicketWrapperDTO> ticketWrapperList;
}
