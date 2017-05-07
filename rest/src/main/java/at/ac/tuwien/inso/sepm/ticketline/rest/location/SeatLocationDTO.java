package at.ac.tuwien.inso.sepm.ticketline.rest.location;

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
@ApiModel(value = "SeatLocationDTO", description = "Seat location DTO for Location Entity")
public class SeatLocationDTO extends LocationDTO {
    @ApiModelProperty(readOnly = true, name = "The type of the location")
    private final String type = "Seat";
}
