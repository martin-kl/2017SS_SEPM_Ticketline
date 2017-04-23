package at.ac.tuwien.inso.sepm.ticketline.rest.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AuthenticationToken", description = "Data Transfer Objects for AuthenticationTokens")
public class AuthenticationToken {

    @ApiModelProperty(required = true, readOnly = true, name = "Current authentication token")
    private String currentToken;

    @ApiModelProperty(required = true, readOnly = true, name = "Future authentication token")
    private String futureToken;

}
