package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"performances"})
@DiscriminatorColumn(columnDefinition = "varchar default 'sector'")
public abstract class Location extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column
    private String name;

    @Column
    private String street;

    @Column
    private String city;

    @Column
    private String zipCode;

    @Column
    private String country;

    @Getter
    @ManyToMany(mappedBy = "location")
    private Set<Performance> performances;

}
