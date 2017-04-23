package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Event extends Audited {

    public enum Category {
        NO_CATEGORY
    }

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    @Enumerated(value = EnumType.STRING)
    private Category category = Category.NO_CATEGORY;

    @Column(columnDefinition = "text")
    private String description;

    @Getter
    @OneToMany(mappedBy = "event")
    private Set<EventArtist> eventArtists;

    @Getter
    @OneToMany(mappedBy = "event")
    private Set<Performance> performances;
}
