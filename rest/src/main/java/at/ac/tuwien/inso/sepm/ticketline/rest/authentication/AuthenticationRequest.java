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
@ApiModel(value = "AuthenticationRequest", description = "Data Transfer Objects for Authentication Requests via REST")
public class AuthenticationRequest {

    @ApiModelProperty(required = true, name = "The unique name of the user", example = "admin")
    private String username;

    @ApiModelProperty(required = true, name = "The password of the user", example = "password")
    private String password;

}
