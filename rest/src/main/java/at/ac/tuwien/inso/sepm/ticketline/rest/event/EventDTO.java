package at.ac.tuwien.inso.sepm.ticketline.rest.event;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
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

    //TODO: Category enum?
    /*@ApiModelProperty(required = true, readOnly = true, name = "The category of the event")
    private Category category;*/

    @ApiModelProperty(required = true, readOnly = true, name = "The description of the event")
    private String description;

    /*@ApiModelProperty(required = true, readOnly = true, name = "The artists playing in the event")
    private List<EventArtistDTO> artists;

    @ApiModelProperty(required = true, readOnly = true, name = "The performances for the event")
    private List<PerformanceDTO> performances;*/
}
