package at.ac.tuwien.inso.sepm.ticketline.rest.news;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString(exclude = {"image"})
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "DetailedNewsDTO", description = "A detailed DTO for news entries via rest")
public class DetailedNewsDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

    @ApiModelProperty(readOnly = true, name = "The date and time when the news was published")
    private LocalDateTime publishedAt;

    @ApiModelProperty(required = true, name = "The title of the news")
    private String title;

    @ApiModelProperty(required = true, name = "The text content of the news")
    private String text;

    @ApiModelProperty(required = true, name = "The text content of the news")
    private String summary;

    @ApiModelProperty(required = true, name = "The news image")
    private byte[] image;


}