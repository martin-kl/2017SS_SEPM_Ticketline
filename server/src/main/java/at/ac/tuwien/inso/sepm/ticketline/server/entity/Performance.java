package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Proxy;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"tickets"})
@Entity
public class Performance extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false, length = 10_000)
    @Length(min = 1)
    private String name;

    @Column
    private Instant startTime;

    @Column
    private Instant endTime;

    @Column
    private BigDecimal defaultPrice;

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_performance_event")
    )
    private Event event;

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_performance_location")
    )
    private Location location;

    @Getter
    @ManyToMany(mappedBy = "performance")
    private Set<Ticket> tickets;
}
