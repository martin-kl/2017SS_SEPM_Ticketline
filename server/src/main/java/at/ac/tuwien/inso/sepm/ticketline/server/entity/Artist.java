package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"eventArtists"})
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
    @OneToMany(mappedBy = "artist", fetch = FetchType.EAGER)
    private Set<EventArtist> eventArtists;

}
