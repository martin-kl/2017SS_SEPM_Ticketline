package at.ac.tuwien.inso.sepm.ticketline.rest.news;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ApiModel(value = "DetailedNewsDTO", description = "A simple DTO for news entries via rest")
public class SimpleNewsDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private UUID id;

    @ApiModelProperty(required = true, readOnly = true, name = "The date and time when the news was published")
    private LocalDateTime publishedAt;

    @ApiModelProperty(required = true, readOnly = true, name = "The title of the news")
    private String title;

    @ApiModelProperty(required = true, readOnly = true, name = "The summary of the news")
    private String summary;

    @Override
    public String toString() {
        return "SimpleNewsDTO{" +
            "id=" + id +
            ", publishedAt=" + publishedAt +
            ", title='" + title + '\'' +
            ", summary='" + summary + '\'' +
            '}';
    }

    public static NewsDTOBuilder builder() {
        return new NewsDTOBuilder();
    }

    public static final class NewsDTOBuilder {

        private UUID id;
        private LocalDateTime publishedAt;
        private String title;
        private String summary;

        public NewsDTOBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public NewsDTOBuilder publishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public NewsDTOBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NewsDTOBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public SimpleNewsDTO build() {
            SimpleNewsDTO newsDTO = new SimpleNewsDTO();
            newsDTO.setId(id);
            newsDTO.setPublishedAt(publishedAt);
            newsDTO.setTitle(title);
            newsDTO.setSummary(summary);
            return newsDTO;
        }
    }
}