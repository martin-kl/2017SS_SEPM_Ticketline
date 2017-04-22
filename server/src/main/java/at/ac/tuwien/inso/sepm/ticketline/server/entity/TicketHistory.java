package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.base.Audited;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Getter
@Entity
public class TicketHistory extends Audited {

    public enum Status {
        RESERVED,
        STORNO,
        BOUGHT
    }

    @Getter
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(updatable = false)
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.RESERVED;

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_tickethistory_ticket")
    )
    private Ticket ticket;

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_tickethistory_customer")
    )
    private Customer customer;

}
