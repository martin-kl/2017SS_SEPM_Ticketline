package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Artist extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Getter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_artist",
        joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_artist_event")),
        inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_event_artist")))
    private Set<Event> events;

}
