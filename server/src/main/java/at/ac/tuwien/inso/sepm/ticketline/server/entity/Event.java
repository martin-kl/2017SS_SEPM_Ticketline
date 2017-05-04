package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
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
@ToString(exclude = {"eventArtists", "performances"})
@Entity
public class Event extends Audited {

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
    private EventCategory category = EventCategory.NO_CATEGORY;

    @Column(columnDefinition = "text")
    private String description;

    @Getter
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private Set<EventArtist> eventArtists;

    @Getter
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private Set<Performance> performances;
}

