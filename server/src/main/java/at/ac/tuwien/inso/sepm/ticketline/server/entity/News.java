package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class News {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_news_id")
    @SequenceGenerator(name = "seq_news_id", sequenceName = "seq_news_id")
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, name = "published_at")
    private LocalDateTime publishedAt;

    @Getter
    @Setter
    @Column(nullable = false)
    @Size(max = 100)
    private String title;

    @Getter
    @Setter
    @Column(nullable = false, length = 10_000)
    private String text;

    @Getter
    @OneToMany(mappedBy = "news")
    private Set<PrincipalNews> principalNews;

    public static NewsBuilder builder() {
        return new NewsBuilder();
    }

    @Override
    public String toString() {
        return "News{" +
            "id=" + id +
            ", publishedAt=" + publishedAt +
            ", title='" + title + '\'' +
            ", text='" + text + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (id != null ? !id.equals(news.id) : news.id != null) return false;
        if (publishedAt != null ? !publishedAt.equals(news.publishedAt) : news.publishedAt != null) return false;
        if (title != null ? !title.equals(news.title) : news.title != null) return false;
        return text != null ? text.equals(news.text) : news.text == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    public static final class NewsBuilder {
        private Long id;
        private LocalDateTime publishedAt;
        private String title;
        private String text;

        private NewsBuilder() {
        }

        public NewsBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NewsBuilder publishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public NewsBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NewsBuilder text(String text) {
            this.text = text;
            return this;
        }

        public News build() {
            News news = new News();
            news.setId(id);
            news.setPublishedAt(publishedAt);
            news.setTitle(title);
            news.setText(text);
            return news;
        }
    }
}