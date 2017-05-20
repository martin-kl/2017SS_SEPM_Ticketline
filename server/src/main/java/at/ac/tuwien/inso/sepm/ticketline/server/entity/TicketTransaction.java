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
public class TicketTransaction extends Audited {

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

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
