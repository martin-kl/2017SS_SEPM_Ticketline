package at.ac.tuwien.inso.sepm.ticketline.server.entity;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@EqualsAndHashCode
public class Seat extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column
    private int row;

    @Column
    private int column;

    @Setter
    @Getter
    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_seat_pricecategory")
    )
    private PriceCategory priceCategory;

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_seat_location")
    )
    private SeatLocation location;
}
