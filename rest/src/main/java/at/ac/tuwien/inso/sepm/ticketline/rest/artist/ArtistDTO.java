package at.ac.tuwien.inso.sepm.ticketline.rest.artist;

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
@ApiModel(value = "ArtistDTO", description = "artist DTO")
public class ArtistDTO {
    @ApiModelProperty(readOnly = true, name = "uuid of artist")
    private UUID id;

    @ApiModelProperty(readOnly = true, name = "firstname of artist")
    private String firstname;

    @ApiModelProperty(readOnly = true, name = "lastname of artist")
    private String lastname;
}
