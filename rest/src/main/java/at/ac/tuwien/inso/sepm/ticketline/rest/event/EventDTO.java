package at.ac.tuwien.inso.sepm.ticketline.rest.event;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "EventDTO", description = "A event DTO for events entries via rest")
public class EventDTO {


    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

    @ApiModelProperty(required = true, readOnly = true, name = "The name of the event")
    private String name;


    @ApiModelProperty(required = true, readOnly = true, name = "The category of the event")
    private EventCategory category;

    @ApiModelProperty(required = true, readOnly = true, name = "The description of the event")
    private String description;

    /*@ApiModelProperty(required = true, readOnly = true, name = "The artists playing in the event")
    private List<EventArtistDTO> artists;*/

    @ApiModelProperty(required = true, readOnly = true, name = "The performances for the event")
    private List<PerformanceDTO> performances;
}
