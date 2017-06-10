package at.ac.tuwien.inso.sepm.ticketline.rest.principal;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.PrincipalRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "PrincipalDTO", description = "A principal DTO for users via rest")
public class PrincipalDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

    @ApiModelProperty(required = true, readOnly = true, name = "The username of the principal")
    private String username;

    @ApiModelProperty(required = false, name = "In case that the user wants to set a new password he passes it in here")
    private String newPassword;

    @ApiModelProperty(required = true, readOnly = true, name = "The role of the principal")
    private PrincipalRole principalRole;

    @ApiModelProperty(required = true, readOnly = true, name = "The email of the principal")
    private String email;

    @ApiModelProperty(required = true, readOnly = true, name = "The number of failed login attempts")
    private int failedLoginCount;

    @ApiModelProperty(readOnly = true, name = "The address of the customer")
    private Boolean locked;
}
