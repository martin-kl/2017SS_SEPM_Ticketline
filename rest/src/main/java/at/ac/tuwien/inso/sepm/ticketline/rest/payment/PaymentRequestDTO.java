package at.ac.tuwien.inso.sepm.ticketline.rest.payment;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PaymentProviderOption;
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
@ApiModel(value = "PaymentRequestDTO", description = "used as in put for the payment call")
public class PaymentRequestDTO {
    @ApiModelProperty(required = true, readOnly = true, name = "provider")
    private PaymentProviderOption provider;

    @ApiModelProperty(required = true, readOnly = true, name = "source")
    private String source;
}
