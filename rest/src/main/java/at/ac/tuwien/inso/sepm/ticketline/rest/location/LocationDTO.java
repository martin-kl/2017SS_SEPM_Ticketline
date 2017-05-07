package at.ac.tuwien.inso.sepm.ticketline.rest.location;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "LocationDTO", description = "default DTO for location entries via rest")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @Type(value = SeatLocationDTO.class, name = "Seat"),
    @Type(value = SectorLocationDTO.class, name = "Sector")
})
public abstract class LocationDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

    @ApiModelProperty(readOnly = true, name = "The name of the location")
    private String name;

    @ApiModelProperty(readOnly = true, name = "The street of the location")
    private String street;

    @ApiModelProperty(readOnly = true, name = "The city of the location")
    private String city;

    @ApiModelProperty(readOnly = true, name = "The zipCode of the location")
    private String zipCode;

    @ApiModelProperty(readOnly = true, name = "The country of the location")
    private String country;

}
