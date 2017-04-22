package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@DiscriminatorColumn(columnDefinition = "varchar default 'sector'")
public abstract class Ticket extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column
    private BigDecimal price;

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_performance_ticket")
    )
    private Performance performance;

    @Getter
    @OneToMany(mappedBy = "ticket")
    private Set<TicketHistory> ticketHistories;

}
