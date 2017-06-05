package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<EventArtist> eventArtists;

    @Getter
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Performance> performances;

    @Formula("sold_tickets")
    @Transient
    private int sold_tickets_only_available_when_fetched_through_top_ten;

    @Transient
    private int sold_tickets;
}

