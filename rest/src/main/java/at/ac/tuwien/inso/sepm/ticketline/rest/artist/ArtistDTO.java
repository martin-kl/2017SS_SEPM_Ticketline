package at.ac.tuwien.inso.sepm.ticketline.rest.artist;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ArtistDTO", description = "An artist DTO - contains only attribute name, which is intended for searching")
public class ArtistDTO {
    @ApiModelProperty(readOnly = true, name = "first and last name in a single string")
    private String name;
}
