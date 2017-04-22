package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
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
    @ManyToMany(mappedBy = "events")
    private Set<Artist> artists;

    @Getter
    @OneToMany(mappedBy = "event")
    private Set<Performance> performances;
}
