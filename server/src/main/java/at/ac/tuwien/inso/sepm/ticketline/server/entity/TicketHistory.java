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
@EqualsAndHashCode(exclude = {"ticketTransaction", "ticket"}, callSuper = false)
public class TicketHistory extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_tickethistory_ticket")
    )
    private Ticket ticket;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_tickettransaction_tickethistory")
    )
    private TicketTransaction ticketTransaction;

}
