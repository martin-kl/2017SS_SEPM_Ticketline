package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"ticketHistories"})
@Entity
@EqualsAndHashCode(exclude={"ticketHistories", "customer"}, callSuper = false)
public class TicketTransaction extends Audited {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_ticket_id")
    @SequenceGenerator(name = "seq_ticket_id", sequenceName = "seq_ticket_id", initialValue = 100)
    @Column(updatable = false)
    private Long id;

    @Column(updatable = false)
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TicketStatus status = TicketStatus.RESERVED;

    @Getter
    @OneToMany(mappedBy = "ticketTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TicketHistory> ticketHistories;

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_tickethistory_customer")
    )
    private Customer customer;

    @Column
    private boolean outdated = false;

}
