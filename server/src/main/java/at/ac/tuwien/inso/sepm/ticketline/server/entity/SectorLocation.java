package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="sector")
public class SectorLocation extends Location {

    // TODO
    // must be done like this till we have support for inheritence in the builder annotation
    // https://github.com/rzwitserloot/lombok/pull/1337
    @Builder
    public SectorLocation(
        UUID id,
        String name,
        String street,
        String city,
        String zipCode,
        String country,
        Set<Performance> performances,
        Set<Sector> sectors
    ){
        super(id, name, street, city, zipCode, country, performances);
        this.sectors = sectors;
    }

    @Getter
    @ManyToMany(mappedBy = "location")
    private Set<Sector> sectors;

}
