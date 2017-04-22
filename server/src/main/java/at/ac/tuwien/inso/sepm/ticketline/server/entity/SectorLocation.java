package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="sector")
public class SectorLocation extends Location {

    @Getter
    @ManyToMany(mappedBy = "location")
    private Set<Sector> sectors;

}
