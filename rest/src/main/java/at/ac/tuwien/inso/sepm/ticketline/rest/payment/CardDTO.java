package at.ac.tuwien.inso.sepm.ticketline.rest.payment;

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
@ApiModel(value = "CardDTO", description = "Informations about a credit card")
public class CardDTO {

    @ApiModelProperty(required = true, readOnly = true, name = "credit card number")
    private String number;

    @ApiModelProperty(required = true, readOnly = true, name = "cvc number")
    private String cvc;

    @ApiModelProperty(required = true, readOnly = true, name = "month of expiration")
    private String expirationMonth;

    @ApiModelProperty(required = true, readOnly = true, name = "year of expiration")
    private String expirationYear;

}
