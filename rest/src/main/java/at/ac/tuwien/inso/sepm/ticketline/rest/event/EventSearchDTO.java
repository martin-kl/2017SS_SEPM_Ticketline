package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PerformanceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@ApiModel(value = "EventSearchDTO", description = "object for search attributes")
public class EventSearchDTO {
    @ApiModelProperty(readOnly = true, name = "eventName")
    private String eventName;

    @ApiModelProperty(readOnly = true, name = "description")
    private String description;

    @ApiModelProperty(readOnly = true, name = "eventCategory")
    private String eventCategory;

    @ApiModelProperty(readOnly = true, name = "performanceType")
    private PerformanceType performanceType;

    @ApiModelProperty(readOnly = true, name = "performanceDuration")
    private Duration performanceDuration;

    @ApiModelProperty(readOnly = true, name = "performanceStartDate")
    private LocalDate performanceStartDate;

    @ApiModelProperty(readOnly = true, name = "performanceEndDate")
    private LocalDate performanceEndDate;

    @ApiModelProperty(readOnly = true, name = "performanceTicketPrice")
    private BigDecimal performanceTicketPrice;

    @ApiModelProperty(readOnly = true, name = "performanceLocationUUID")
    private UUID performanceLocationUUID;

    @ApiModelProperty(readOnly = true, name = "artistUUID")
    private UUID artistUUID;
}
