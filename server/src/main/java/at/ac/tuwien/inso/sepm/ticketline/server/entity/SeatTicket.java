package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
public class SeatTicket extends Ticket {

    // TODO
    // must be done like this till we have support for inheritence in the builder annotation
    // https://github.com/rzwitserloot/lombok/pull/1337
    @Builder
    public SeatTicket(
        UUID id,
        BigDecimal price,
        Performance performance,
        Set<TicketHistory> ticketHistories,
        Seat seat
    ) {
        super(id, price, performance, ticketHistories);
        this.seat = seat;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fk_ticket_seat")
    )
    private Seat seat;

}
