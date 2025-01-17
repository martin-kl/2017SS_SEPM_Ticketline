package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@ApiModel(value = "SectorDTO", description = "default DTO for Sector Entity")
public class SectorDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

    @ApiModelProperty(readOnly = true, name = "size of the sector")
    private int size;

    @ApiModelProperty(readOnly = true, name = "name of the sector")
    private String name;

    @ApiModelProperty(readOnly = true, name = "priceCategory for a sector")
    private PriceCategoryDTO priceCategory;
}
