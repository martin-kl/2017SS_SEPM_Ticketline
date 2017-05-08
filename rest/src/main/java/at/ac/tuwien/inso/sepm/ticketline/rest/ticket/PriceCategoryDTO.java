package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PriceCategoryDTO", description = "default DTO for Pricecategory")
public class PriceCategoryDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;
    @ApiModelProperty(readOnly = true, name = "name of the priceCategory")
    private String name;
    @ApiModelProperty(readOnly = true, name = "priceFactor which modifies the price")
    private double modifier;

}
