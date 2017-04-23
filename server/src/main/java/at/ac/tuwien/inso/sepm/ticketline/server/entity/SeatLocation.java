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
@DiscriminatorValue(value="seat")
public class SeatLocation extends Location {

    // TODO
    // must be done like this till we have support for inheritence in the builder annotation
    // https://github.com/rzwitserloot/lombok/pull/1337
    @Builder
    public SeatLocation(
        UUID id,
        String name,
        String street,
        String city,
        String zipCode,
        String country,
        Set<Performance> performances,
        Set<Seat> seats
    ){
        super(id, name, street, city, zipCode, country, performances);
        this.seats = seats;
    }

    @Getter
    @ManyToMany(mappedBy = "location")
    private Set<Seat> seats;

}
