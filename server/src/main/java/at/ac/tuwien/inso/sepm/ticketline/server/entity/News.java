package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"principalNews"})
@Entity
public class News extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false, name = "published_at")
    private LocalDateTime publishedAt;

    @Column(nullable = false, length = 100)
    @Size(max = 100, min=1)
    private String title;

    @Column(nullable = false, length = 10_000)
    @Size(min=1, max=1_000)
    private String summary;

    @Column(length = 500_0000)
    private byte[] image;


    @Column(nullable = false, length = 10_000)
    @Size(min=1, max=10_000)
    private String text;

    @Getter
    @OneToMany(mappedBy = "news", cascade = CascadeType.REMOVE)
    private Set<PrincipalNews> principalNews = new HashSet<>();
}