package at.ac.tuwien.inso.sepm.ticketline.rest.artist;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "EventArtistDTO", description = "A eventartist DTO for eventartist entries")
public class EventArtistDTO {
    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;
}
