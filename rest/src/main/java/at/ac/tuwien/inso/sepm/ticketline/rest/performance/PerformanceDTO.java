package at.ac.tuwien.inso.sepm.ticketline.rest.performance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PerformanceDTO", description = "A performance DTO for performance entries via rest")
public class PerformanceDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;
    //TODO: finish


}
