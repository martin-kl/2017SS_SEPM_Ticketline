package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.datatransfer.FlavorEvent;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"ticketHistories"})
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
    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER)
    private Set<TicketHistory> ticketHistories;

}
