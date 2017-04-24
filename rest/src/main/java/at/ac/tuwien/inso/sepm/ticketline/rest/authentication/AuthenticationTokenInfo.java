package at.ac.tuwien.inso.sepm.ticketline.rest.authentication;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AuthenticationTokenInfo", description = "Informations about the current authentication token")
public class AuthenticationTokenInfo {

    private String username;

    private List<String> roles;

    private LocalDateTime issuedAt;

    private LocalDateTime notBefore;

    private LocalDateTime expireAt;

    private Duration validityDuration;

    private Duration overlapDuration;

}
